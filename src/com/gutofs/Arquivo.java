package com.gutofs;

import java.util.LinkedList;

/**
 *  Classe que representa o arquivo no sistema de arquivos GutoFS.
 *
 *  A classe, que será majoritariamente operada por outras classes, irá servir mais como um container de informações
 *  do quê efetivamente algum objeto que executará operações.
 * 
 * @author guto
 */
public class Arquivo {

    /**
     * Nome do arquivo (apenas o nome, sem nenhum caminho).
     */
    private String nomeDoArquivo;

    /**
     * Tamanho do arquivo em bytes.
     */
    private long tamanhoEmBytes;

    /**  
     * Blocos alocados para este arquivo.
     *
     * A variável abaixo irá armazenar os blocos de dados que estão alocados para este arquivo utilizando uma LinkedList,
     * onde a mesma irá guardar os blocos que irão apontar para as posições do "disco rígido" do
     * GutoFS que deverão ser lidas.
     */
    private final LinkedList<Bloco>  listaBlocosDeDados;

    /**
     * Construtor simples...
     */
    public Arquivo()
    {
        listaBlocosDeDados = new LinkedList<>();
    }

    /**
     *
     * @param nomeDoArquivo Nome do arquivo.
     */
    public void setNomeDoArquivo(String nomeDoArquivo) {this.nomeDoArquivo = nomeDoArquivo;}

    /**
     *
     * @return Nome do arquivo.
     */
    public String getNomeDoArquivo() {return nomeDoArquivo;}

    /**
     *
     * @param tamanhoEmBytes Tamanho do arquivo em bytes.
     */
    public void setTamanhoEmBytes(long tamanhoEmBytes) {this.tamanhoEmBytes = tamanhoEmBytes;}

    /**
     *
     * @return tamanhoEmBytes Tamanho do arquivo em bytes.
     */
    public long getTamanhoEmBytes() {return tamanhoEmBytes;}

    /**
     * Método que tem o objecto de adicionar à lista de referências de bloco deste arquivo um bloco que tenha sido
     * alocado à este arquivo.
     *
     * @param bloco Bloco.
     */
    public void adicionarBlocoALista(Bloco bloco) {listaBlocosDeDados.add(bloco);}

    /**
     * Método que retorna o número do bloco alocado na posição passada por parâmetro da lista de blocos deste arquivo.
     *
     * @param pos Posição da lista de blocos deste arquivo.
     * @return bloco Bloco.
     */
    public Bloco getBlocoAlocado(int pos) {return listaBlocosDeDados.get(pos);}

    /**
     * Método que tem o objectivo de retornar a quantidade de blocos alocados à este arquivo.
     *
     * @return Qtd. de blocos alocados à este arquivo.
     */
    public int getQtdBlocosAlocados() { return listaBlocosDeDados.size(); }
}
