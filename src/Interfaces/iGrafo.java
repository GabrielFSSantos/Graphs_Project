package Interfaces;

import GBase.Aresta;
import GBase.Vertice;

public interface iGrafo {

    public int quantVertices();

    public int quantArestas();

    public Aresta getAresta(Vertice origem, Vertice destino);

    public boolean veriConexo(iRepresentacao grafoRepresentacao);

    public boolean veriCompleto(iRepresentacao grafoRepresentacao);

    public void printInfoGrafo();
}
