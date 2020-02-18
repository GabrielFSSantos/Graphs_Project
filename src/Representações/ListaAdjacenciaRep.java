package Representações;

import GBase.Aresta;
import GBase.Vertice;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import Interfaces.iRepresentacao;

public class ListaAdjacenciaRep implements iRepresentacao {

    //___________________\\
    // >>> ATRIBUTOS <<< \\
    //―――――――――――――――――――\\
    private FileReader fr;
    private BufferedReader br;

    private HashMap<Vertice, HashSet<Vertice>> representacao = new HashMap<>();

    private ArrayList<Aresta> arestas = new ArrayList<Aresta>();
    private ArrayList<Vertice> vertices = new ArrayList<Vertice>();

    private int vert_ares[] = new int[2];
    private boolean digrafo;
    private int cont = 0;

    @Override
    public void FileRead(File arq) {
        try {

            fr = new FileReader(arq);
            br = new BufferedReader(fr);

            String linha = br.readLine();
            String[] info = linha.split(" ");

            vert_ares[0] = Integer.parseInt(info[0]);
            vert_ares[1] = Integer.parseInt(info[1]);
            digrafo = info[2].equalsIgnoreCase("d");

            while (br.ready()) {

                cont++;

                linha = br.readLine();
                info = linha.split(" ");

                Vertice origem = new Vertice(Integer.parseInt(info[0]));
                Vertice destino = new Vertice(Integer.parseInt(info[1]));
                this.addAresta(new Aresta(cont, origem, destino, Double.parseDouble(info[2])));
            }
        } catch (Exception ex) {

            System.out.println("Erro ao obter dimensão, arquivo pode estar vazio ou não está no padrão Definido.");
            System.exit(0);
        }
    }

    //_________________\\
    // >>> VERTICE <<< \\
    //―――――――――――――――――\\
    @Override
    public void addVertice(Vertice vertice) {

        if (representacao.containsKey(vertice)) {
            return;
        }
        representacao.put(vertice, new HashSet<>());
        vertices.add(vertice);
    }

    @Override
    public void removeVertice(Vertice vertice) {

        vertices.remove(vertice);
        representacao.remove(vertice);
    }

    @Override
    public ArrayList<Vertice> getArrayVertice() {

        return vertices;
    }

    @Override
    public int getQuantVertices() {

        return vertices.size();
    }

    //________________\\
    // >>> ARESTA <<< \\
    //――――――――――――――――\\
    @Override
    public void addAresta(Aresta aresta) {

        Vertice origem = aresta.getOrigem();
        Vertice destino = aresta.getDestino();

        addVertice(origem);
        addVertice(destino);

        representacao.get(origem).add(destino);
        arestas.add(aresta);

        if (!digrafo) {

            cont++;

            representacao.get(destino).add(origem);

            Aresta temp = new Aresta(cont, destino, origem, aresta.getPeso());
            arestas.add(temp);
        }
    }

    @Override
    public Aresta getAresta(Vertice origem, Vertice destino) {

        for (int i = 0; i < arestas.size(); i++) {

            if (arestas.get(i).getOrigem().getId() == origem.getId() && arestas.get(i).getDestino().getId() == destino.getId()) {

                return arestas.get(i);
            }
        }
        return null;
    }

    @Override
    public void removeAresta(Aresta aresta) {

        representacao.get(aresta.getOrigem()).remove(aresta.getDestino());

        if (!digrafo) {

            representacao.get(aresta.getDestino()).remove(aresta.getOrigem());
            arestas.remove(getAresta(aresta.getDestino(), aresta.getOrigem()));
        }
        arestas.remove(aresta);

    }

    @Override
    public void setArrayArestas(ArrayList<Aresta> arestas) {

        this.arestas.clear();
        this.arestas = arestas;
    }

    @Override
    public ArrayList<Aresta> getArrayAresta() {

        return arestas;
    }

    @Override
    public int getQuantArestas() {

        return arestas.size();
    }

    //_______________\\
    // >>> GRAFO <<< \\
    //―――――――――――――――\\
    @Override
    public void printGrafo() {

        System.out.println("\n>>>>>> REPRESENTAÇÃO EM LISTA ADJACENTE <<<<<<\n");

        for (Map.Entry<Vertice, HashSet<Vertice>> mapEntry : representacao.entrySet()) {

            Vertice aux = mapEntry.getKey();

            System.out.print("Vertice " + aux.getId() + ":\n");

            for (Vertice value : mapEntry.getValue()) {

                Aresta temp = getAresta(aux, value);
                System.out.print("Aresta: " + aux.getId() + " → " + temp.getDestino().getId() + " Peso: " + temp.getPeso() + "\n");
            }
            System.out.println();
        }
    }

    @Override
    public boolean getDigrafo() {

        if (digrafo == true) {

            return true;
        }
        return false;
    }

    @Override
    public Object clone() {

        ListaAdjacenciaRep copy = new ListaAdjacenciaRep();

        copy.arestas = (ArrayList<Aresta>) this.arestas.clone();
        copy.vertices = (ArrayList<Vertice>) this.vertices.clone();
        copy.representacao = (HashMap<Vertice, HashSet<Vertice>>) this.representacao.clone();

        return copy;
    }

}
