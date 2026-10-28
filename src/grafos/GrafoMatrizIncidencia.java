package grafos;

import java.util.ArrayList;

/**
 * Representação de Grafo utilizando Matriz de Incidência.
 * Em um dígrafo, cada coluna representa uma aresta e cada linha representa um vértice.
 * Para uma aresta de u para v (u != v): linha u recebe -1 (saída) e linha v recebe +1 (chegada).
 * Para um laço de u para u: linha u recebe 2.
 */
public class GrafoMatrizIncidencia implements Grafo {

    private final int numVertices;
    private final ArrayList<Vertice> vertices;
    private final ArrayList<Aresta> arestas;
    private int[][] matrizIncidencia; // linhas: vértices, colunas: arestas

    public GrafoMatrizIncidencia(int numVertices) {
        if (numVertices < 0) {
            throw new IllegalArgumentException("O número de vértices não pode ser negativo.");
        }
        this.numVertices = numVertices;
        this.vertices = new ArrayList<>(numVertices);
        this.arestas = new ArrayList<>();
        this.matrizIncidencia = new int[numVertices][0];

        for (int i = 0; i < numVertices; i++) {
            this.vertices.add(new Vertice(i));
        }
    }

    private void validarVertice(Vertice v) throws Exception {
        if (v == null || v.id() < 0 || v.id() >= numVertices) {
            throw new Exception("Vértice inválido ou fora dos limites do grafo: " + v);
        }
    }

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

    @Override
    public void adicionarAresta(Vertice origem, Vertice destino, double peso) throws Exception {
        validarVertice(origem);
        validarVertice(destino);
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

        // Copia dados anteriores
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < novasColunas - 1; j++) {
                novaMatriz[i][j] = matrizIncidencia[i][j];
            }
        }

        // Preenche a nova coluna da aresta
        int novaCol = novasColunas - 1;
        if (u == v) {
            novaMatriz[u][novaCol] = 2; // Laço
        } else {
            novaMatriz[u][novaCol] = -1; // Origem (saída)
            novaMatriz[v][novaCol] = 1;  // Destino (chegada)
        }

        this.matrizIncidencia = novaMatriz;
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
        return encontrarColunaDaAresta(u, v) != -1;
    }

    @Override
    public int grauDoVertice(Vertice vertice) throws Exception {
        validarVertice(vertice);
        // Em dígrafos, o grau de saída é a quantidade de arestas que partem de vertice
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

    @Override
    public ArrayList<Vertice> adjacentesDe(Vertice vertice) throws Exception {
        validarVertice(vertice);
        int u = vertice.id();
        ArrayList<Vertice> adjacentes = new ArrayList<>();

        for (int j = 0; j < arestas.size(); j++) {
            if (matrizIncidencia[u][j] == 2) {
                // Laço: o próprio vértice é adjacente
                adjacentes.add(vertices.get(u));
            } else if (matrizIncidencia[u][j] == -1) {
                // Encontra qual linha possui +1 nesta coluna j
                for (int dest = 0; dest < numVertices; dest++) {
                    if (matrizIncidencia[dest][j] == 1) {
                        adjacentes.add(vertices.get(dest));
                        break;
                    }
                }
            }
        }
        return adjacentes;
    }

    @Override
    public void setarPeso(Vertice origem, Vertice destino, double peso) throws Exception {
        validarVertice(origem);
        validarVertice(destino);
        int col = encontrarColunaDaAresta(origem.id(), destino.id());
        if (col == -1) {
            throw new Exception("Não existe aresta entre " + origem.id() + " e " + destino.id() + ".");
        }
        arestas.get(col).setarPeso(peso);
    }

    @Override
    public ArrayList<Aresta> arestasEntre(Vertice origem, Vertice destino) throws Exception {
        validarVertice(origem);
        validarVertice(destino);
        ArrayList<Aresta> resultado = new ArrayList<>();
        int col = encontrarColunaDaAresta(origem.id(), destino.id());
        if (col != -1) {
            resultado.add(arestas.get(col));
        }
        return resultado;
    }

    @Override
    public ArrayList<Vertice> vertices() {
        return new ArrayList<>(vertices);
    }
}
