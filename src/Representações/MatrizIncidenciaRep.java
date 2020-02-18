package Representações;

import GBase.Aresta;
import GBase.Vertice;
import Interfaces.iRepresentacao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MatrizIncidenciaRep implements iRepresentacao {

    //___________________\\
    // >>> ATRIBUTOS <<< \\
    //―――――――――――――――――――\\
    private FileReader fr;
    private BufferedReader br;

    private ArrayList<Vertice> vertices = new ArrayList<Vertice>();
    private ArrayList<Aresta> arestas = new ArrayList<Aresta>();
    private Aresta[][] representacao;

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

            int tam = (vert_ares[1] * (vert_ares[1] - 1)) / 2;

            if (!digrafo) {

                tam = tam * 2;
            }

            representacao = new Aresta[vert_ares[0]][tam];

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

        if (!vertices.contains(vertice)) {

            if (vertices.isEmpty()) {

                vertices.add(vertice);

            } else {

                for (int i = 0; i < vertices.size(); i++) {

                    if (vertices.get(i).getId() > vertice.getId()) {

                        vertices.add(i, vertice);
                        return;
                    }
                }
                vertices.add(vertice);
            }
        }
    }

    @Override
    public void removeVertice(Vertice vertice) {

        vertices.remove(vertice);
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

        addVertice(aresta.getOrigem());
        addVertice(aresta.getDestino());

        representacao[aresta.getOrigem().getId() - 1][aresta.getId() - 1] = aresta;
        representacao[aresta.getDestino().getId() - 1][aresta.getId() - 1] = aresta;

        arestas.add(aresta);

        if (!digrafo) {

            cont++;
            Aresta temp = new Aresta(cont, destino, origem, aresta.getPeso());
            representacao[aresta.getOrigem().getId() - 1][aresta.getId()] = temp;
            representacao[aresta.getDestino().getId() - 1][aresta.getId()] = temp;
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

        representacao[aresta.getOrigem().getId() - 1][aresta.getDestino().getId() - 1] = null;

        if (!digrafo) {

            representacao[aresta.getDestino().getId() - 1][aresta.getOrigem().getId() - 1] = null;
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

        System.out.println("\n>>>>>> REPRESENTAÇÃO EM MATRIZ DE INCIDENCIA <<<<<<\n");

        for (int i = 0; i < arestas.size(); i++) {

            System.out.print("\t" + arestas.get(i).getOrigem().getId() + "→" + arestas.get(i).getDestino().getId());
        }

        System.out.println("\n\t ________________________________________________________________________________________________");
        for (int i = 0; i < vertices.size(); i++) {

            System.out.print("    " + vertices.get(i).getId() + "\t│");
            for (int j = 0; j < arestas.size(); j++) {

                if (representacao[i][j] != null) {

                    if (representacao[i][j].getOrigem().getId() == i + 1) {

                        System.out.print(representacao[i][j].getPeso() + "\t");

                    } else if (representacao[i][j].getDestino().getId() == i + 1) {

                        System.out.print(-representacao[i][j].getPeso() + "\t");
                    }
                } else {
                    System.out.print("0\t");
                }
            }
            System.out.println("│");
        }
        System.out.println("\t ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯");
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

        MatrizIncidenciaRep copy = new MatrizIncidenciaRep();

        copy.arestas = (ArrayList<Aresta>) this.arestas.clone();
        copy.vertices = (ArrayList<Vertice>) this.vertices.clone();
        copy.representacao = this.representacao.clone();

        return copy;
    }

}
