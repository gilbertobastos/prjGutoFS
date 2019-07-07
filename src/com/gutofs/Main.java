package com.gutofs;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here

        GutoFS gutofs;

        gutofs = new GutoFS(
                "gutofs.harddisk",
                4);

        gutofs.adicionarArquivo("letra-musica-1.txt");
        gutofs.adicionarArquivo("letra-musica-2.txt");
        
        gutofs.gravarArquivoExternamente("letra-musica-2.txt", "suffocation-human-waiste.txt");
        gutofs.gravarArquivoExternamente("letra-musica-1.txt", "btm-lithium-overdose.txt");
    }
}
