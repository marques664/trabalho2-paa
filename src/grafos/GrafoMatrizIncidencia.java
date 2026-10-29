package grafos;

import java.util.ArrayList;

// Representacao de Grafo por Matriz de Incidencia (V x A).
// Linhas representam vertices e colunas representam arestas.
// Convencao para digrafos: -1 na origem (saida), +1 no destino (chegada) e 2 em lacos.
public class GrafoMatrizIncidencia implements Grafo {

    private final int numVertices;
    private final ArrayList<Vertice> vertices;
    private final ArrayList<Aresta> arestas;
    private int[][] matrizIncidencia;

    public GrafoMatrizIncidencia(int numVertices) {
        this.numVertices = Math.max(0, numVertices);
        this.vertices = new ArrayList<>(this.numVertices);
        this.arestas = new ArrayList<>();
        this.matrizIncidencia = new int[this.numVertices][0];

        for (int i = 0; i < this.numVertices; i++) {
            this.vertices.add(new Vertice(i));
        }
    }

    private boolean verticeValido(Vertice v) {
        return v != null && v.id() >= 0 && v.id() < numVertices;
    }

    // Busca a coluna correspondente a aresta de u para v
    private int encontrarColunaDaAresta(int u, int v) {
        for (int j = 0; j < arestas.size(); j++) {
            Aresta a = arestas.get(j);
            if (a.origem().id() == u && a.destino().id() == v) {
                return j;
            }
        }
        return -1;
    }

    @Override
    public void adicionarAresta(Vertice origem, Vertice destino) throws Exception {
        adicionarAresta(origem, destino, 1.0);
    }

    // Adiciona uma nova coluna a matriz redimensionando-a e marcando saida (-1) e chegada (+1)
    @Override
    public void adicionarAresta(Vertice origem, Vertice destino, double peso) throws Exception {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return;
        }
        int u = origem.id();
        int v = destino.id();

        int colExistente = encontrarColunaDaAresta(u, v);
        if (colExistente != -1) {
            arestas.get(colExistente).setarPeso(peso);
            return;
        }

        Aresta novaAresta = new Aresta(vertices.get(u), vertices.get(v), peso);
        arestas.add(novaAresta);

        int novasColunas = arestas.size();
        int[][] novaMatriz = new int[numVertices][novasColunas];

        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < novasColunas - 1; j++) {
                novaMatriz[i][j] = matrizIncidencia[i][j];
            }
        }

        int novaCol = novasColunas - 1;
        if (u == v) {
            novaMatriz[u][novaCol] = 2;  // laco
        } else {
            novaMatriz[u][novaCol] = -1; // saida
            novaMatriz[v][novaCol] = 1;  // chegada
        }

        this.matrizIncidencia = novaMatriz;
    }

    // Verifica se ha coluna conectando origem e destino
    @Override
    public boolean existeAresta(Vertice origem, Vertice destino) {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return false;
        }
        return encontrarColunaDaAresta(origem.id(), destino.id()) != -1;
    }

    // Conta quantas arestas saem deste vertice (marcadas como -1 ou 2)
    @Override
    public int grauDoVertice(Vertice vertice) throws Exception {
        if (!verticeValido(vertice)) {
            return 0;
        }
        int u = vertice.id();
        int grau = 0;
        for (int j = 0; j < arestas.size(); j++) {
            if (matrizIncidencia[u][j] == -1 || matrizIncidencia[u][j] == 2) {
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
        return arestas.size();
    }

    // Retorna os vertices de destino para cada coluna onde este vertice e origem
    @Override
    public ArrayList<Vertice> adjacentesDe(Vertice vertice) throws Exception {
        ArrayList<Vertice> adjacentes = new ArrayList<>();
        if (!verticeValido(vertice)) {
            return adjacentes;
        }
        int u = vertice.id();

        for (int j = 0; j < arestas.size(); j++) {
            if (matrizIncidencia[u][j] == 2) {
                adjacentes.add(vertices.get(u));
            } else if (matrizIncidencia[u][j] == -1) {
                adjacentes.add(arestas.get(j).destino());
            }
        }
        return adjacentes;
    }

    // Altera o peso da aresta na lista interna de arestas
    @Override
    public void setarPeso(Vertice origem, Vertice destino, double peso) throws Exception {
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return;
        }
        int col = encontrarColunaDaAresta(origem.id(), destino.id());
        if (col != -1) {
            arestas.get(col).setarPeso(peso);
        }
    }

    // Retorna a aresta cadastrada para a coluna especifica
    @Override
    public ArrayList<Aresta> arestasEntre(Vertice origem, Vertice destino) throws Exception {
        ArrayList<Aresta> resultado = new ArrayList<>();
        if (!verticeValido(origem) || !verticeValido(destino)) {
            return resultado;
        }
        int col = encontrarColunaDaAresta(origem.id(), destino.id());
        if (col != -1) {
            resultado.add(arestas.get(col));
        }
        return resultado;
    }

    @Override
    public ArrayList<Vertice> vertices() {
        return vertices;
    }
}
