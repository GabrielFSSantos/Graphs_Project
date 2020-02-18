package Interfaces;

import GBase.Aresta;
import GBase.Vertice;
import java.io.File;
import java.util.ArrayList;

public interface iRepresentacao {

    //_________________\\
    // >>> VERTICE <<< \\
    //―――――――――――――――――\\
    public void addVertice(Vertice vertice);

    public void removeVertice(Vertice vertice);

    public ArrayList<Vertice> getArrayVertice();

    public int getQuantVertices();

    //________________\\
    // >>> ARESTA <<< \\
    //――――――――――――――――\\
    public void addAresta(Aresta aresta);

    public void removeAresta(Aresta aresta);

    public ArrayList<Aresta> getArrayAresta();

    public int getQuantArestas();

    public Aresta getAresta(Vertice origem, Vertice destino);

    public boolean getDigrafo();

    //_______________\\
    // >>> GRAFO <<< \\
    //―――――――――――――――\\
    public void FileRead(File arq);

    public void setArrayArestas(ArrayList<Aresta> arrayArestas);

    public void printGrafo();

    public Object clone();
}
