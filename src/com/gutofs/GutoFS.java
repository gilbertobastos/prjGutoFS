package com.gutofs;

import java.io.*;
import java.util.LinkedList;

/**
 * 
 * @author guto
 */
public class GutoFS {

    /**
     *  "Disco de armazenamento", que basicamente é uma referência aponta para um "arquivo externo" em que os arquivos
     *  serão gravados (como um "Disco Rígido"?)...
     */
    private final File discoArmazenamento;

    /**
     * Espaço disponível no disco de armazenamento em bytes.
     */
    private Long espacoDispDiscoArmazenameto;
    
    /**
     * Lista de blocos do GutoFS.
     */
    private Bloco[] listaBlocos;

    /**
     * Lista de arquivos que estão armazenados no grande sistema de arquivos GutoFS.
     */
    private final LinkedList<Arquivo> listaArquivos;

    /**
     * Tamanho do bloco no sistema de arquivos GutoFs em bytes.
     *
     * O "disco de armazenamento" será divididos em blocos, onde cada um desses blocos irá possuir o tamanho especificado
     * na variável abaixo. Os arquivos, por fim, terão seu conteúdo armazenados nesses diversos blocos.
     */
    private final int tamanhoBlocoEmBytes;
    
    /**
     *
     *
     *
     * @param nomeArquivoDiscoArmazenamento Nome do arquivo que será o disco de armazenamento deste sistema de arquivos.
     * @param tamanhoBlocoEmBytes Tamanho do bloco (em bytes).
     * @throws java.io.IOException
     */
    public GutoFS(String nomeArquivoDiscoArmazenamento, int tamanhoBlocoEmBytes) throws IOException
    {
        this.listaArquivos = new LinkedList<>();
        this.discoArmazenamento = new File(nomeArquivoDiscoArmazenamento);
        this.tamanhoBlocoEmBytes = tamanhoBlocoEmBytes;
        
        /* Verificando se o arquivo existe e se há permissões para gravação. */
        if (!this.discoArmazenamento.exists() || !this.discoArmazenamento.canRead() ||
            !this.discoArmazenamento.canWrite())
            throw new IOException("Não foi possível abrir o arquivo para gravação.");
        
        this.espacoDispDiscoArmazenameto = this.discoArmazenamento.length();
        
        /* Agora dividindo o arquivo em blocos. */
        int qtdBlocosTotal = (int) (this.discoArmazenamento.length() / tamanhoBlocoEmBytes);
        this.listaBlocos = new Bloco[qtdBlocosTotal];
        
        for (int i = 0; i < qtdBlocosTotal; i++)
            this.listaBlocos[i] = new Bloco(i * this.tamanhoBlocoEmBytes);
    }

    /**
     *
     * @return Qtd. de blocos que o sistema de arquivos possui.
     */
    private int getQtdBlocosTotal() {return this.listaBlocos.length; }

    /**
     *
     * @return Qtd. de blocos ocupados.
     */
    private int getQtdBlocosOcupados() 
    {
        int qtdBlocosOcupados = 0;
        
        for (Bloco bloco : this.listaBlocos) {
            if (bloco.getEmUso()) qtdBlocosOcupados++;
        }
        
        return qtdBlocosOcupados;
    }

    /**
     *
     * @return Qtd. de blocos livres.
     */
    private int getQtdBlocosLivres() 
    {
        return this.listaBlocos.length - this.getQtdBlocosOcupados();
    }

    /**
     *
     * @return Espaço disponível no sistema de arquivos em bytes.
     */
    public Long getEspacoLivreEmBytes()
    {
        return this.espacoDispDiscoArmazenameto;
    }

    /**
     * Método que retorna um bloco livre para ser utilizado na alocação de um
     * arquivo no disco de armazenamento.
     * 
     * @return bloco Bloco.
     */
    private Bloco obterUmBlocoLivre()
    {
        for (Bloco bloco : listaBlocos)
        {
            if (!bloco.getEmUso())
                return bloco;
        }
        return null;
    }
    
    /**
     * Método que procura por um arquivo na lista de arquivos do GutoFS.
     * 
     * @param nomeDoArquivo Nome do arquivo no GutoFS.
     * @return Referência para o arquivo (caso não seja localizado nenhum
     * arquivo com tal nome, o método irá retornar NULL).
     */
    private Arquivo localizarArquivo(String nomeDoArquivo)
    {
        Arquivo arquivoGutoFS = null;
        
        /* Localizando o arquivo a ser removido... */
        for (int i = 0; i < this.listaArquivos.size(); i++)
        {
            if (this.listaArquivos.get(i).getNomeDoArquivo().equals(nomeDoArquivo))
            {
                arquivoGutoFS = this.listaArquivos.get(i);
                break;
            }
        }
        
        return arquivoGutoFS;
    }
    
    /**
     *  Método que adiciona um arquivo externo ao disco de armazenamento do GutoFS.
     *  O arquivo externo deve estar na pasta "AreaDeTroca".
     *
     * @param nomeDoArquivo Nome do arquivo (incluindo o caminho do mesmo).
     *
     */
    public void adicionarArquivo(String nomeDoArquivo) throws FileNotFoundException, OutOfMemoryError, IOException
    {
        /* Verificando se há espaço suficiente para gravar o arquivo no sistema de arquivos. */
        File arquivoExterno = new File(nomeDoArquivo);
        if (arquivoExterno.length() > this.espacoDispDiscoArmazenameto)
            throw new OutOfMemoryError("Não há espaço para salvar o arquivo.");
        
        /* Por fim, "gravando o arquivo", criando uma estância da classe
         * Arquivo, populando as informações necessárias para a mesma 
         * e adicionando ela na lista de arquivos do GutoFS. */
        Arquivo arquivoGutoFS = new Arquivo();
        arquivoGutoFS.setNomeDoArquivo(nomeDoArquivo);
        arquivoGutoFS.setTamanhoEmBytes(arquivoExterno.length());
        this.listaArquivos.add(arquivoGutoFS);
        
        /* Atualizando o indicador de espaço disponível do GutoFS. */
        this.espacoDispDiscoArmazenameto -= arquivoExterno.length();
        
        /* Abrindo o disco de armazenamento e o arquivo a ser adicionado no
         * disco para gravação e leitura respectivamente.
         */
        RandomAccessFile reader = new RandomAccessFile(arquivoExterno, "rw");
        RandomAccessFile writer = new RandomAccessFile(this.discoArmazenamento, "rw");
       
        byte[] byteBuffer = new byte[this.tamanhoBlocoEmBytes];
        int qtdBytesLidos = 0;
        Bloco blocoAlocado;
        
        /* O laço abaixo será responsável por alocar os blocos do sistema de arquivos
         * para esse arquivo.
         */
        for (;;)
        {
            /* Lendo os conteúdos do arquivo externo (apenas uma fatia) 
             * e inserindo os mesmos
             * na variável byteBuffer, onde todo seu conteúdo será
             * despejado no disco de armazenamento.
             */
            qtdBytesLidos = reader.read(byteBuffer);

            if (qtdBytesLidos > 0)
            {
                /* Alocando um bloco para o arquivo... */
                blocoAlocado = obterUmBlocoLivre();
                blocoAlocado.setEmUso(true);
                arquivoGutoFS.adicionarBlocoALista(blocoAlocado);
               
                /* Gravando as informações do arquivo nesse bloco no disco de armazenamento. */
                writer.seek(blocoAlocado.deslocamentoDisco);
                writer.write(byteBuffer);
            }
            else
                break;
        }
        
        writer.close();
        reader.close();
    }

    /**
     *
     * @param nomeDoArquivo Nome do arquivo a ser removido.
     */
    public void removerArquivo(String nomeDoArquivo) throws FileNotFoundException
    {
        /* Localizando o arquivo a ser removido... */
        Arquivo arquivoGutoFS = localizarArquivo(nomeDoArquivo);
       
        /* Caso o arquivo exista, prosseguindo com a remoção do mesmo
         * liberando os blocos anteriormente alocados. 
         */
        if (arquivoGutoFS == null) throw new FileNotFoundException();
        
        listaArquivos.remove(arquivoGutoFS);
        
        for (int i = 0; i < arquivoGutoFS.getQtdBlocosAlocados(); i++)
            arquivoGutoFS.getBlocoAlocado(i).setEmUso(false);
        
        /* Atualizando o indicador de espaço disponível... */
        this.espacoDispDiscoArmazenameto += arquivoGutoFS.getTamanhoEmBytes();
    }
    
    /**
     * Método que tem o objetivo de salvar um arquivo que está no GutoFS para
     * o sistema de arquivos do sistema operacional.
     * 
     * @param nomeDoArquivo Nome do arquivo no GutoFS a ser gravado externamente.
     * @param caminhoExterno Onde o arquivo deverá ser criado (incluir o nome do arquivo!)
     */
    public void gravarArquivoExternamente(String nomeDoArquivo, String caminhoExterno)
    {
        
    }
}
