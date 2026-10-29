package grafos;

import java.util.ArrayList;

// Representacao de Grafo por Matriz de Adjacencia
// Usa uma matriz booleana para existencia de arestas e uma matriz double para os pesos
public class GrafoMatrizAdjacencia implements Grafo {

    private final int numVertices;
    private int numArestas;
    private final ArrayList<Vertice> vertices;
    private final boolean[][] matrizAdjacencia;
    private final double[][] matrizPesos;

    public GrafoMatrizAdjacencia(int numVertices) {
        this.numVertices = Math.max(0, numVertices);
        this.numArestas = 0;
        this.vertices = new ArrayList<>(this.numVertices);
        this.matrizAdjacencia = new boolean[this.numVertices][this.numVertices];
        this.matrizPesos = new double[this.numVertices][this.numVertices];

        for (int i = 0; i < this.numVertices; i++) {
            this.vertices.add(new Vertice(i));
        }
    }

    private boolean verticeValido(Vertice v) {
        return v != null && v.id() >= 0 && v.id() < numVertices;
    }

    @Override
    public void adicionarAresta(Vertice origem, Vertice destino) throws Exception {
        adicionarAresta(origem, destino, 1.0);
    }

    // Marca true na matriz na posicao [origem][destino] e armazena o peso
    @Override
    public void adicionarAresta(Vertice origem, Vertice destino, double peso) throws Exception {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return;
        }
        int u = origem.id();
        int v = destino.id();

        if (!matrizAdjacencia[u][v]) {
            matrizAdjacencia[u][v] = true;
            numArestas++;
        }
        matrizPesos[u][v] = peso;
    }

    // Consulta direta na matriz em O(1)
    @Override
    public boolean existeAresta(Vertice origem, Vertice destino) {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return false;
        }
        return matrizAdjacencia[origem.id()][destino.id()];
    }

    // Conta quantas posicoes estao marcadas como true na linha do vertice
    @Override
    public int grauDoVertice(Vertice vertice) throws Exception {
        if (!verticeValido(vertice)) {
            return 0;
        }
        int grau = 0;
        int u = vertice.id();
        for (int v = 0; v < numVertices; v++) {
            if (matrizAdjacencia[u][v]) {
                grau++;
            }
        }
        return grau;
    }

    @Override
    public int numeroDeVertices() {
        return numVertices;
    }

    @Override
    public int numeroDeArestas() {
        return numArestas;
    }

    // Varre a linha do vertice buscando todas as colunas que possuem aresta
    @Override
    public ArrayList<Vertice> adjacentesDe(Vertice vertice) throws Exception {
        ArrayList<Vertice> adjacentes = new ArrayList<>();
        if (!verticeValido(vertice)) {
            return adjacentes;
        }
        int u = vertice.id();
        for (int v = 0; v < numVertices; v++) {
            if (matrizAdjacencia[u][v]) {
                adjacentes.add(vertices.get(v));
            }
        }
        return adjacentes;
    }

    // Atualiza o valor na matriz de pesos
    @Override
    public void setarPeso(Vertice origem, Vertice destino, double peso) throws Exception {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return;
        }
        matrizPesos[origem.id()][destino.id()] = peso;
    }

    // Se existir aresta na matriz, retorna a aresta com seu respectivo peso
    @Override
    public ArrayList<Aresta> arestasEntre(Vertice origem, Vertice destino) throws Exception {
        ArrayList<Aresta> arestas = new ArrayList<>();
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return arestas;
        }
        int u = origem.id();
        int v = destino.id();
        if (matrizAdjacencia[u][v]) {
            arestas.add(new Aresta(vertices.get(u), vertices.get(v), matrizPesos[u][v]));
        }
        return arestas;
    }

    @Override
    public ArrayList<Vertice> vertices() {
        return vertices;
    }
}
