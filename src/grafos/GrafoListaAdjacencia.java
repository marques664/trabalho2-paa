package grafos;

import java.util.ArrayList;

/**
 * Representação de Grafo utilizando Lista de Adjacência.
 * Utiliza um vetor de listas (ArrayList<Aresta>[]), indexado diretamente pelo id do vértice de origem.
 */
public class GrafoListaAdjacencia implements Grafo {

    private final int numVertices;
    private int numArestas;
    private final ArrayList<Vertice> vertices;
    @SuppressWarnings("unchecked")
    private final ArrayList<Aresta>[] listaAdjacencia;

    @SuppressWarnings("unchecked")
    public GrafoListaAdjacencia(int numVertices) {
        if (numVertices < 0) {
            throw new IllegalArgumentException("O número de vértices não pode ser negativo.");
        }
        this.numVertices = numVertices;
        this.numArestas = 0;
        this.vertices = new ArrayList<>(numVertices);
        this.listaAdjacencia = (ArrayList<Aresta>[]) new ArrayList[numVertices];

        for (int i = 0; i < numVertices; i++) {
            this.vertices.add(new Vertice(i));
            this.listaAdjacencia[i] = new ArrayList<>();
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

        // Se já existe, apenas atualiza o peso
        for (Aresta a : listaAdjacencia[u]) {
            if (a.destino().id() == destino.id()) {
                a.setarPeso(peso);
                return;
            }
        }

        listaAdjacencia[u].add(new Aresta(vertices.get(u), vertices.get(destino.id()), peso));
        numArestas++;
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
        for (Aresta a : listaAdjacencia[u]) {
            if (a.destino().id() == v) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int grauDoVertice(Vertice vertice) throws Exception {
        validarVertice(vertice);
        // Em dígrafos, o grau de saída é o tamanho da lista de adjacência de u
        return listaAdjacencia[vertice.id()].size();
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
        for (Aresta a : listaAdjacencia[vertice.id()]) {
            adjacentes.add(a.destino());
        }
        return adjacentes;
    }

    @Override
    public void setarPeso(Vertice origem, Vertice destino, double peso) throws Exception {
        validarVertice(origem);
        validarVertice(destino);
        for (Aresta a : listaAdjacencia[origem.id()]) {
            if (a.destino().id() == destino.id()) {
                a.setarPeso(peso);
                return;
            }
        }
        throw new Exception("Não existe aresta entre " + origem.id() + " e " + destino.id() + ".");
    }

    @Override
    public ArrayList<Aresta> arestasEntre(Vertice origem, Vertice destino) throws Exception {
        validarVertice(origem);
        validarVertice(destino);
        ArrayList<Aresta> resultado = new ArrayList<>();
        for (Aresta a : listaAdjacencia[origem.id()]) {
            if (a.destino().id() == destino.id()) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    @Override
    public ArrayList<Vertice> vertices() {
        return new ArrayList<>(vertices);
    }
}
