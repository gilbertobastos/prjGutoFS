package com.gutofs;

import java.io.File;
import com.gutofs.GutoFS;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here

        GutoFS gutofs = null;

        gutofs = new GutoFS(
                "gutofs.harddisk",
                1);

        gutofs.adicionarArquivo("texto1.txt");
        System.out.println(gutofs.getQtdBlocosLivres());
        System.out.println(gutofs.getQtdBlocosOcupados());
        
    }
}
