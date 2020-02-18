package GBase;

import Interfaces.iGrafo;
import Interfaces.iRepresentacao;
import Representações.EnumRepresentacao;
import static Representações.EnumRepresentacao.MatrizAdjacenciaRep;
import Representações.ListaAdjacenciaRep;
import Representações.MatrizAdjacenciaRep;
import Representações.MatrizIncidenciaRep;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

public class Grafo implements iGrafo {

    //___________________\\
    // >>> ATRIBUTOS <<< \\
    //―――――――――――――――――――\\
    private EnumRepresentacao tRepresentacao;
    public iRepresentacao grafoRepresentacao;
    File arq = new File("src\\Interfaces\\dados.dat");

    public Grafo(EnumRepresentacao tRepresentacao) throws Exception {

        this.tRepresentacao = tRepresentacao;

        switch (tRepresentacao) {

            case ListaAdjacenciaRep:
                grafoRepresentacao = new ListaAdjacenciaRep();
                break;

            case MatrizAdjacenciaRep:
                grafoRepresentacao = new MatrizAdjacenciaRep();
                break;

            case MatrizIncidenciaRep:
                grafoRepresentacao = new MatrizIncidenciaRep();
                break;

            default:
                throw new Exception("Tipo Inesistente !!!");
        }

        grafoRepresentacao.FileRead(this.arq);
    }

    public Grafo(EnumRepresentacao tRepresentacao, File arq) throws Exception {

        this(tRepresentacao);
        grafoRepresentacao.FileRead(arq);
    }

    //___________________\\
    // >>> INTERFACE <<< \\
    //―――――――――――――――――――\\
    @Override
    public int quantVertices() {

        return grafoRepresentacao.getQuantVertices();
    }

    @Override
    public int quantArestas() {

        return grafoRepresentacao.getQuantArestas();
    }

    @Override
    public Aresta getAresta(Vertice origem, Vertice destino) {

        return grafoRepresentacao.getAresta(origem, destino);
    }

    @Override
    public boolean veriConexo(iRepresentacao grafoRepresentacao) {

        if (componenteConexo(grafoRepresentacao) > 1) {
            return false;
        }
        return true;
    }

    @Override
    public boolean veriCompleto(iRepresentacao grafoRepresentacao) {

        ArrayList<Vertice> vertices = grafoRepresentacao.getArrayVertice();

        for (int i = 0; i < vertices.size(); i++) {

            for (int j = 0; j < vertices.size(); j++) {

                if (vertices.get(i).getId() != vertices.get(j).getId() && grafoRepresentacao.getAresta(vertices.get(i), vertices.get(j)) == null) {

                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void printInfoGrafo() {

        grafoRepresentacao.printGrafo();

        System.out.println("Quantidade de Arestas: " + grafoRepresentacao.getQuantArestas());
        System.out.println("Quantidade de Vertices: " + grafoRepresentacao.getQuantVertices());
        System.out.println("Componentes: " + componenteConexo(grafoRepresentacao));
        System.out.println("Direcionado: " + grafoRepresentacao.getDigrafo());
        System.out.println("Completo: " + veriCompleto(grafoRepresentacao));
        System.out.println("Conexo: " + veriConexo(grafoRepresentacao));

        System.out.print("\nBUSCA EM PROFUNDIDADE: ");
        buscaProfundidade(grafoRepresentacao, grafoRepresentacao.getArrayVertice().get(0));

        System.out.print("\nBUSCA EM LARGURA: ");
        buscaLargura(grafoRepresentacao, grafoRepresentacao.getArrayVertice().get(0));

        System.out.print("\nORDENAÇÃO TOPOLÓGICA: ");
        ordenacaoTopologica(grafoRepresentacao, grafoRepresentacao.getArrayVertice().get(0));

        System.out.print("\nDIJKSTRA: ");
        iRepresentacao clone = (iRepresentacao) grafoRepresentacao.clone();
        dijkstra(clone, clone.getArrayVertice().get(0));

        System.out.print("\nCIRCUITO EULERIANO: ");
        fleury(grafoRepresentacao, grafoRepresentacao.getArrayVertice().get(0));

        System.out.println("\nGRAFO COMPLETADO:");
        complementoGrafo(grafoRepresentacao);
    }

    //_________________\\
    // >>> METODOS <<< \\
    //―――――――――――――――――\\
    public void buscaProfundidade(iRepresentacao grafoRepresentacao, Vertice vertice) {

        ArrayList<Vertice> verticeList = grafoRepresentacao.getArrayVertice();

        int visitados[] = new int[verticeList.size()];

        for (int i = 0; i < visitados.length; i++) {

            visitados[i] = 0;
        }

        profundidade(visitados, vertice.getId() - 1, vertice);
        System.out.println();

    }

    public void buscaLargura(iRepresentacao grafoRepresentacao, Vertice vertice) {

        ArrayList<Vertice> verticeList = grafoRepresentacao.getArrayVertice();

        int visitados[] = new int[verticeList.size()];

        for (int i = 0; i < visitados.length; i++) {

            visitados[i] = 0;
        }

        visitados[vertice.getId() - 1] = 1;

        ArrayList<Vertice> fila = new ArrayList<Vertice>();
        fila.add(vertice);

        System.out.print(vertice.getId());

        boolean flag = true;
        ArrayList<Vertice> adjacentes = null;

        while (!fila.isEmpty()) {

            Vertice aux = fila.get(0);
            fila.remove(0);

            if (flag == true) {

                adjacentes = getAdjacentes(grafoRepresentacao, aux);
                for (int i = 0; i < adjacentes.size(); i++) {

                    if (visitados[adjacentes.get(i).getId() - 1] == 1) {

                        adjacentes.remove(i);

                    }
                }
            }

            if (visitados[aux.getId() - 1] == 0) {

                System.out.print(aux.getId() + "→");
                visitados[aux.getId() - 1] = 1;
            }

            while (!adjacentes.isEmpty()) {

                if (visitados[adjacentes.get(0).getId() - 1] == 0) {

                    visitados[adjacentes.get(0).getId() - 1] = 1;
                    fila.add(adjacentes.get(0));

                    System.out.print("→" + adjacentes.get(0).getId());

                    adjacentes.remove(0);
                    flag = false;

                    if (adjacentes.isEmpty()) {

                        flag = true;
                    }

                } else {

                    adjacentes.remove(0);
                    flag = true;
                }
            }
        }
        System.out.println();
    }

    public void ordenacaoTopologica(iRepresentacao grafoRepresentacao, Vertice vertice) {

        ArrayList<Vertice> verticeList = grafoRepresentacao.getArrayVertice();

        int visitados[] = new int[verticeList.size()];

        for (int i = 0; i < visitados.length; i++) {

            visitados[i] = 0;
        }

        profOrdenacaoT(visitados, vertice.getId() - 1, vertice);
        System.out.println();
    }

    public void dijkstra(iRepresentacao grafoRepresentacao, Vertice vertice) {

        ArrayList<Vertice> vertices = grafoRepresentacao.getArrayVertice();
        double dist[] = new double[vertices.size()];
        Vertice pred[] = new Vertice[vertices.size()];

        for (int i = 0; i < vertices.size(); i++) {

            dist[i] = Double.POSITIVE_INFINITY;
            pred[i] = null;
        }
        dist[vertice.getId() - 1] = 0;
        ArrayList<Vertice> verticesAux = vertices;

        while (verticesAux.size() != 0) {

            Vertice verticeAux = minDist(verticesAux, dist);
            verticesAux.remove(verticeAux);

            ArrayList<Vertice> adjacent = getAdjacentes(grafoRepresentacao, verticeAux);

            for (int i = 0; i < adjacent.size(); i++) {

                if (dist[adjacent.get(i).getId() - 1] > dist[verticeAux.getId() - 1] + grafoRepresentacao.getAresta(verticeAux, adjacent.get(i)).getPeso()) {

                    dist[adjacent.get(i).getId() - 1] = dist[verticeAux.getId() - 1] + grafoRepresentacao.getAresta(verticeAux, adjacent.get(i)).getPeso();
                    pred[adjacent.get(i).getId() - 1] = verticeAux;
                }

            }
        }

        System.out.print(dist[0]);
        for (int i = 1; i < dist.length; i++) {

            System.out.print(" → " + dist[i]);
        }

        System.out.print("\nPred: ");

        System.out.print(pred[1].getId());
        for (int i = 2; i < pred.length; i++) {

            if (pred[i] != null) {

                System.out.print(" → " + pred[i].getId());
            } else {

                System.out.print("null");
            }
        }

        System.out.println();
    }

    boolean fleury(iRepresentacao grafoRepresentacao, Vertice vertice) {

        ArrayList<Aresta> circuito = new ArrayList<Aresta>();
        Vertice primeiro = vertice;
        iRepresentacao copia = (iRepresentacao) grafoRepresentacao.clone();

        copia = indireto(copia);

        int debito = 0;
        Aresta uv = arestasOf(grafoRepresentacao, vertice).get(0);

        if (ponte(copia, uv, debito) == 1) {

            return false;
        }

        Aresta prev = uv;
        Vertice v = uv.getDestino();
        copia.removeAresta(uv);
        circuito.add(uv);
        int flag = 0;

        while (vertice.getId() != primeiro.getId() || arestasOf(copia, v).size() != 0 || arestasOf(copia, vertice).size() != 0) {

            if (flag == 0) {

                vertice = v;
                flag = 1;
            }

            Vertice vBridge = null;
            boolean edgeBridge = true;
            ArrayList<Vertice> vertices = getAdjacentes(copia, vertice);

            for (int i = 0; i < vertices.size(); i++) {

                uv = copia.getAresta(vertice, vertices.get(i));
                debito = ponte(copia, copia.getAresta(vertice, vertices.get(i)), debito);

                if (ponte(copia, copia.getAresta(vertice, vertices.get(i)), debito) != 1) {

                    circuito.add(uv);
                    copia.removeAresta(uv);
                    edgeBridge = false;
                    vertice = uv.getDestino();
                    continue;

                } else {

                    vBridge = vertices.get(i);
                }

            }
            if (edgeBridge) {
                if (vBridge == null) {

                    System.out.println("Não");
                    return false;

                }
                circuito.add(grafoRepresentacao.getAresta(vertice, vBridge));
                copia.removeAresta(grafoRepresentacao.getAresta(vertice, vBridge));
            }
        }

        System.out.println("Sim");
        System.out.println("Circuito euleriano: ");

        for (int i = 0; i < circuito.size(); i++) {
            System.out.print(circuito.get(i).toString() + " | ");
        }

        return true;
    }

    public void complementoGrafo(iRepresentacao grafoRepresentacao) {

        ArrayList<Vertice> vertices = grafoRepresentacao.getArrayVertice();
        ArrayList<Aresta> complemento = new ArrayList<Aresta>();

        int totalEdge = grafoRepresentacao.getQuantArestas() + 1;

        iRepresentacao copia = (iRepresentacao) grafoRepresentacao.clone();

        for (int i = 0; i < vertices.size(); i++) {

            for (int j = 0; j < vertices.size(); j++) {

                if (vertices.get(i).getId() != vertices.get(j).getId() && copia.getAresta(vertices.get(i), vertices.get(j)) == null) {

                    copia.addAresta(new Aresta(totalEdge, vertices.get(i), vertices.get(j)));
                    totalEdge++;
                }
            }
        }

        copia.printGrafo();
    }

    //____________________\\
    // >>> AUXILIARES <<< \\
    //――――――――――――――――――――\\
    ArrayList<Vertice> getAdjacentes(iRepresentacao grafoRepresentacao, Vertice vertice) {

        ArrayList<Aresta> arestas = grafoRepresentacao.getArrayAresta();
        ArrayList<Vertice> adjVertices = new ArrayList<Vertice>();

        for (int i = 0; i < arestas.size(); i++) {

            if (vertice.getId() == arestas.get(i).getOrigem().getId()) {

                adjVertices.add(arestas.get(i).getDestino());
            }
        }
        return adjVertices;
    }

    public void profundidade(int visitados[], int i, Vertice vertice) {     //Para busca em profundidade

        visitados[vertice.getId() - 1] = 1;

        ArrayList<Vertice> adjacentes = getAdjacentes(grafoRepresentacao, vertice);

        System.out.print(vertice.getId());

        for (int j = 0; j < adjacentes.size(); j++) {

            if (visitados[adjacentes.get(j).getId() - 1] == 0) {

                System.out.print("→");
                profundidade(visitados, j, adjacentes.get(j));

            }
        }
    }

    public void profOrdenacaoT(int visitados[], int i, Vertice vertice) { // para ordenação topológica

        visitados[vertice.getId() - 1] = 1;

        ArrayList<Vertice> adjacentes = getAdjacentes(grafoRepresentacao, vertice);

        for (int j = 0; j < adjacentes.size(); j++) {

            if (visitados[adjacentes.get(j).getId() - 1] == 0) {

                profOrdenacaoT(visitados, j, adjacentes.get(j));
                System.out.print("→");
            }
        }
        System.out.print(vertice.getId());

    }

    boolean pesquisaConectado(iRepresentacao grafoRepresentacao, Vertice vertice) {

        ArrayList<Vertice> vertices = grafoRepresentacao.getArrayVertice();

        int visitados[] = new int[vertices.size()];

        for (int i = 0; i < visitados.length; i++) {

            visitados[i] = 0;
        }

        int removidos[] = new int[visitados.length];

        for (int i = 0; i < removidos.length; i++) {

            removidos[i] = 0;
        }

        visitados[vertices.get(0).getId() - 1] = 1;
        removidos[vertices.get(0).getId() - 1] = 1;

        Stack<Vertice> pilha = new Stack<Vertice>();
        pilha.push(vertices.get(0));
        Vertice aux;

        boolean flag = true;
        ArrayList<Vertice> adjacentes = null;

        while (!pilha.empty()) {
            aux = pilha.elementAt(pilha.size() - 1);

            if (flag == true) {

                adjacentes = getAdjacentes(grafoRepresentacao, aux);
            }
            for (int i = 0; i < adjacentes.size(); i++) {

                if (removidos[adjacentes.get(i).getId() - 1] == 1) {

                    adjacentes.remove(i);
                }
            }

            if (!adjacentes.isEmpty() && visitados[adjacentes.get(0).getId() - 1] == 0) {

                visitados[adjacentes.get(0).getId() - 1] = 1;
                pilha.push(adjacentes.get(0));

                removidos[adjacentes.get(0).getId() - 1] = 1;
                adjacentes.remove(0);
                flag = false;

                if (adjacentes.size() == 0) {

                    flag = true;
                }

            } else {

                pilha.pop();
                flag = true;
            }

        }
        for (int i = 0; i < visitados.length; i++) {

            if (visitados[i] == 0) {

                return false;
            }
        }
        return true;
    }

    boolean nulo(int vetor[]) {

        for (int i = 0; i < vetor.length; i++) {

            if (vetor[i] == 0) {

                return false;
            }
        }
        return true;
    }

    public iRepresentacao indireto(iRepresentacao grafoRepresentacao) {

        ArrayList<Aresta> arestas = grafoRepresentacao.getArrayAresta();
        iRepresentacao copia = (iRepresentacao) grafoRepresentacao.clone();

        int count = arestas.size() + 1;

        for (int i = 0; i < arestas.size(); i++) {

            if (!arestas.contains(copia.getAresta(arestas.get(i).getDestino(), arestas.get(i).getOrigem()))) {

                Vertice a = arestas.get(i).getDestino();
                Vertice b = arestas.get(i).getOrigem();
                copia.addAresta(new Aresta(count, a, b));
                count++;
            }
        }
        return copia;
    }

    ArrayList<Aresta> arestasOf(iRepresentacao grafoRepresentacao, Vertice vertice) {

        ArrayList<Aresta> arestas = grafoRepresentacao.getArrayAresta();
        ArrayList<Aresta> newArestas = new ArrayList<Aresta>();

        for (int i = 0; i < arestas.size(); i++) {

            if (arestas.get(i).getOrigem().getId() == vertice.getId()) {

                newArestas.add(arestas.get(i));
            }
        }
        return newArestas;
    }

    public Vertice getVertice(ArrayList<Vertice> vertices, int i) { // Para dijkstra

        for (int j = 0; j < vertices.size(); j++) {

            if (vertices.get(j).getId() == i + 1) {

                return vertices.get(j);
            }
        }
        return null;
    }

    public Vertice minDist(ArrayList<Vertice> vertices, double dist[]) { // Para dijkstra

        double min = dist[vertices.get(0).getId() - 1];
        Vertice vertice = vertices.get(0);

        for (int i = 1; i < vertices.size(); i++) {

            if (dist[vertices.get(i).getId() - 1] < min) {

                min = dist[vertices.get(i).getId() - 1];
                vertice = vertices.get(i);
            }
        }
        return vertice;
    }

    int ponte(iRepresentacao grafoRepresentacao, Aresta uv, int debito) { // Para fleury

        iRepresentacao copia = (iRepresentacao) grafoRepresentacao.clone();

        copia = indireto(copia);
        copia.removeAresta(uv);

        int connexGraph = componenteConexo(grafoRepresentacao);
        int connexCopy = componenteConexo(copia);

        if (arestasOf(copia, uv.getOrigem()).size() == 0) {

            debito--;
        }

        connexCopy += debito;

        if (connexGraph < connexCopy) {

            return 1;

        } else {

            return debito;
        }
    }

    public int componenteConexoU(iRepresentacao grafoRepresentacao) {

        ArrayList<Vertice> vertices = grafoRepresentacao.getArrayVertice();
        int visitados[] = new int[vertices.size()];

        for (int i = 0; i < vertices.size(); i++) {

            visitados[i] = 0;
        }

        int componente = 0;
        for (int i = 0; i < vertices.size(); i++) {

            if (visitados[vertices.get(i).getId() - 1] == 0) {
                componente++;
            }
        }

        return componente;
    }

    public int componenteConexo(iRepresentacao grafoRepresentacao) {

        ArrayList<Vertice> vertices = grafoRepresentacao.getArrayVertice();
        int[] visitados = new int[vertices.size()];

        for (int i = 0; i < vertices.size(); i++) {

            visitados[i] = 0;
        }

        int componente = 0;
        for (int i = 0; i < vertices.size(); i++) {

            if (visitados[vertices.get(i).getId() - 1] == 0) {

                componente++;
                profConexo(grafoRepresentacao, vertices.get(i), visitados, vertices.get(i).getId() - 1);
            }
        }

        return componente;
    }

    public void profConexoU(iRepresentacao p_GraphRepresentation, Vertice vertice, int[] visitados, int i) {

        visitados[vertice.getId() - 1] = 1;
    }

    public void profConexo(iRepresentacao grafoRepresentacao, Vertice vertice, int[] visitados, int i) {

        visitados[vertice.getId() - 1] = 1;
        ArrayList<Vertice> adjacent = getAdjacentes(grafoRepresentacao, vertice);

        for (int j = 0; j < adjacent.size(); j++) {

            if (visitados[adjacent.get(j).getId() - 1] == 0) {

                profConexo(grafoRepresentacao, adjacent.get(j), visitados, j);
            }
        }
    }

}
