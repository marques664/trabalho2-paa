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
        if (numVertices < 0) {
            throw new IllegalArgumentException("O número de vértices não pode ser negativo.");
        }
        this.numVertices = numVertices;
        this.numArestas = 0;
        this.vertices = new ArrayList<>(numVertices);
        this.matrizAdjacencia = new boolean[numVertices][numVertices];
        this.matrizPesos = new double[numVertices][numVertices];

        for (int i = 0; i < numVertices; i++) {
            this.vertices.add(new Vertice(i));
        }
    }

    private void validarVertice(Vertice v) throws Exception {
        if (v == null || v.id() < 0 || v.id() >= numVertices) {
            throw new Exception("Vértice inválido ou fora dos limites do grafo: " + v);
        }
    }

    @Override
    public void adicionarAresta(Vertice origem, Vertice destino) throws Exception {
        adicionarAresta(origem, destino, 1.0);
    }

    @Override
    public void adicionarAresta(Vertice origem, Vertice destino, double peso) throws Exception {
        validarVertice(origem);
        validarVertice(destino);
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
        if (origem == null || destino == null) {
            return false;
        }
        int u = origem.id();
        int v = destino.id();
        if (u < 0 || u >= numVertices || v < 0 || v >= numVertices) {
            return false;
        }
        return matrizAdjacencia[u][v];
    }

    @Override
    public int grauDoVertice(Vertice vertice) throws Exception {
        validarVertice(vertice);
        // Em dígrafos, o grau de saída (número de arestas que partem do vértice)
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
        validarVertice(vertice);
        ArrayList<Vertice> adjacentes = new ArrayList<>();
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
        validarVertice(origem);
        validarVertice(destino);
        int u = origem.id();
        int v = destino.id();
        if (!matrizAdjacencia[u][v]) {
            throw new Exception("Não existe aresta entre " + origem.id() + " e " + destino.id() + ".");
        }
        matrizPesos[u][v] = peso;
    }

    @Override
    public ArrayList<Aresta> arestasEntre(Vertice origem, Vertice destino) throws Exception {
        validarVertice(origem);
        validarVertice(destino);
        ArrayList<Aresta> arestas = new ArrayList<>();
        int u = origem.id();
        int v = destino.id();
        if (matrizAdjacencia[u][v]) {
            arestas.add(new Aresta(vertices.get(u), vertices.get(v), matrizPesos[u][v]));
        }
        return arestas;
    }

    @Override
    public ArrayList<Vertice> vertices() {
        return new ArrayList<>(vertices);
    }
}
