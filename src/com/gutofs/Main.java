package com.gutofs;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
	// write your code here

        GutoFS gutofs;

        gutofs = new GutoFS(
                "gutofs.harddisk",
                1);

        gutofs.adicionarArquivo("texto1.txt");
    }
}
