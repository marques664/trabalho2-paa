package grafos;

import java.util.ArrayList;

// Representacao de Grafo por Lista de Adjacencia
// Usa um vetor de listas (ArrayList<Aresta>[]), indexado diretamente pelo id do vertice
public class GrafoListaAdjacencia implements Grafo {

    private final int numVertices;
    private int numArestas;
    private final ArrayList<Vertice> vertices;
    private final ArrayList<Aresta>[] listaAdjacencia;

    @SuppressWarnings("unchecked")
    public GrafoListaAdjacencia(int numVertices) {
        this.numVertices = Math.max(0, numVertices);
        this.numArestas = 0;
        this.vertices = new ArrayList<>(this.numVertices);
        this.listaAdjacencia = (ArrayList<Aresta>[]) new ArrayList[this.numVertices];

        for (int i = 0; i < this.numVertices; i++) {
            this.vertices.add(new Vertice(i));
            this.listaAdjacencia[i] = new ArrayList<>();
        }
    }

    private boolean verticeValido(Vertice v) {
        return v != null && v.id() >= 0 && v.id() < numVertices;
    }

    @Override
    public void adicionarAresta(Vertice origem, Vertice destino) throws Exception {
        adicionarAresta(origem, destino, 1.0);
    }

    // Adiciona a aresta na lista da origem. Se ja existir, apenas atualiza o peso.
    @Override
    public void adicionarAresta(Vertice origem, Vertice destino, double peso) throws Exception {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return;
        }
        int u = origem.id();

        for (Aresta a : listaAdjacencia[u]) {
            if (a.destino().id() == destino.id()) {
                a.setarPeso(peso);
                return;
            }
        }

        listaAdjacencia[u].add(new Aresta(vertices.get(u), vertices.get(destino.id()), peso));
        numArestas++;
    }

    // Percorre a lista de adjacencia da origem buscando se o destino esta presente
    @Override
    public boolean existeAresta(Vertice origem, Vertice destino) {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return false;
        }
        for (Aresta a : listaAdjacencia[origem.id()]) {
            if (a.destino().id() == destino.id()) {
                return true;
            }
        }
        return false;
    }

    // Grau de saida do vertice (quantidade de arestas na lista dele)
    @Override
    public int grauDoVertice(Vertice vertice) throws Exception {
        if (!verticeValido(vertice)) {
            return 0;
        }
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

    // Retorna todos os vertices alcancaveis diretamente a partir deste vertice
    @Override
    public ArrayList<Vertice> adjacentesDe(Vertice vertice) throws Exception {
        ArrayList<Vertice> adjacentes = new ArrayList<>();
        if (!verticeValido(vertice)) {
            return adjacentes;
        }
        for (Aresta a : listaAdjacencia[vertice.id()]) {
            adjacentes.add(a.destino());
        }
        return adjacentes;
    }

    // Altera o peso da aresta direcionada entre origem e destino
    @Override
    public void setarPeso(Vertice origem, Vertice destino, double peso) throws Exception {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return;
        }
        for (Aresta a : listaAdjacencia[origem.id()]) {
            if (a.destino().id() == destino.id()) {
                a.setarPeso(peso);
                return;
            }
        }
    }

    // Retorna as arestas direcionadas que ligam a origem ao destino
    @Override
    public ArrayList<Aresta> arestasEntre(Vertice origem, Vertice destino) throws Exception {
        ArrayList<Aresta> resultado = new ArrayList<>();
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return resultado;
        }
        for (Aresta a : listaAdjacencia[origem.id()]) {
            if (a.destino().id() == destino.id()) {
                resultado.add(a);
            }
        }
        return resultado;
    }

    @Override
    public ArrayList<Vertice> vertices() {
        return vertices;
    }
}
