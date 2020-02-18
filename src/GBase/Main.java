package GBase;

import Representações.EnumRepresentacao;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, Exception {

        //File arq = new File("Caminho");
        Grafo grafo = new Grafo(EnumRepresentacao.MatrizIncidenciaRep);
        grafo.printInfoGrafo();

    }
}
