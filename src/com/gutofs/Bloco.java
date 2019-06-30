package com.gutofs;

/**
 * Ckasse que irá representar um bloco do sistema de arquivos GutoFS.
 * 
 * @author guto
 */
public class Bloco {
    
    /** 
     * Variável que irá armazenar para qual posição do disco de armazenamento
     * do GutoFS esse bloco está referenciando. 
     */
    public final int deslocamentoDisco;
    
    /** Se o bloco está em uso ou não... */
    private boolean emUso;
    
    /**
     * 
     * @param deslocamentoDisco Disco Posição do disco de armazenamento que esse bloco irá
     *                          referenciar.
     */
    public Bloco(int deslocamentoDisco)
    {
       this.deslocamentoDisco = deslocamentoDisco;
       this.emUso = false;
    }
    
    /**
     * 
     * @return Se o bloco está sendo usado.
     */
    public boolean getEmUso() { return this.emUso; } 
    
    /**
     * 
     * @param emUso Se o bloco está sendo usado.
     */
    public void setEmUso(boolean emUso) { this.emUso = emUso; }
}
