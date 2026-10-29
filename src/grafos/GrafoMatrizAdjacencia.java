package grafos;

import java.util.ArrayList;

/**
 * Representação de Grafo utilizando Matriz de Adjacência.
 * Utiliza arrays primitivos bidimensionais para existência de arestas e pesos.
 */
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

    @Override
    public boolean existeAresta(Vertice origem, Vertice destino) {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return false;
        }
        return matrizAdjacencia[origem.id()][destino.id()];
    }

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

    @Override
    public void setarPeso(Vertice origem, Vertice destino, double peso) throws Exception {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return;
        }
        matrizPesos[origem.id()][destino.id()] = peso;
    }

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
