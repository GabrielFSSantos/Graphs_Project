package GBase;

public class Aresta {

    private int id;
    private double peso;
    private Vertice origem;
    private Vertice destino;

    public Aresta(int id, Vertice origem, Vertice destino) {

        this.id = id;
        this.origem = origem;
        this.destino = destino;
    }

    public Aresta(int id, Vertice origem, Vertice destino, double peso) {

        this(id, origem, destino);
        this.peso = peso;
    }

    public int getId() {

        return id;
    }

    public double getPeso() {

        return peso;
    }

    public Vertice getOrigem() {

        return origem;
    }

    public Vertice getDestino() {

        return destino;
    }
}
