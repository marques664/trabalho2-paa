package grafos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Implementação da interface AlgoritmosEmGrafos.
 */
public class AlgoritmosEmGrafosImpl implements AlgoritmosEmGrafos {

    // Cores para o DFS e BFS (Cormen)
    public static final int BRANCO = 0;
    public static final int CINZA = 1;
    public static final int PRETO = 2;

    private final FileManager fileManager;

    // Estruturas auxiliares do DFS (vetores indexados por id do vértice)
    private int[] d_dfs;       // Tempos de descoberta
    private int[] f_dfs;       // Tempos de finalização
    private Vertice[] pi_dfs;  // Antecessor / pai na árvore DFS
    private int[] cor_dfs;     // Cor do vértice
    private int tempo_dfs;

    private ArrayList<Aresta> arestasArvoreDFS;
    private ArrayList<Aresta> arestasRetornoDFS;
    private ArrayList<Aresta> arestasAvancoDFS;
    private ArrayList<Aresta> arestasCruzamentoDFS;

    // Estruturas auxiliares do BFS (vetores indexados por id do vértice)
    private int[] d_bfs;       // Distâncias / tempos de descoberta
    private Vertice[] pi_bfs;  // Antecessor / pai na árvore BFS
    private int[] cor_bfs;     // Cor do vértice
    private ArrayList<Aresta> arestasArvoreBFS;

    public AlgoritmosEmGrafosImpl() {
        this.fileManager = new FileManager();
    }

    @Override
    public Grafo carregarGrafo(String path, TipoDeRepresentacao t) throws Exception {
        ArrayList<String> linhas = fileManager.stringReader(path);
        if (linhas == null || linhas.isEmpty()) {
            throw new Exception("Não foi possível ler o arquivo ou arquivo vazio: " + path);
        }

        // Primeira linha: número de vértices
        String primeiraLinha = linhas.get(0).trim();
        int numVertices;
        try {
            numVertices = Integer.parseInt(primeiraLinha);
        } catch (NumberFormatException e) {
            throw new Exception("Formato inválido na primeira linha (esperado número de vértices): " + primeiraLinha);
        }

        // Instancia a estrutura correspondente ao enum
        Grafo grafo;
        switch (t) {
            case MATRIZ_DE_ADJACENCIA:
                grafo = new GrafoMatrizAdjacencia(numVertices);
                break;
            case MATRIZ_DE_INCIDENCIA:
                grafo = new GrafoMatrizIncidencia(numVertices);
                break;
            case LISTA_DE_ADJACENCIA:
                grafo = new GrafoListaAdjacencia(numVertices);
                break;
            default:
                throw new IllegalArgumentException("Tipo de representação desconhecido: " + t);
        }

        // Linhas seguintes: lista de adjacências com pesos
        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) {
                continue;
            }

            int indexPrimeiroEspaco = linha.indexOf(' ');
            if (indexPrimeiroEspaco == -1) {
                // Vértice sem arestas de saída (apenas o id na linha)
                continue;
            }

            int origemId = Integer.parseInt(linha.substring(0, indexPrimeiroEspaco).trim());
            String resto = linha.substring(indexPrimeiroEspaco + 1).trim();

            String[] itens = resto.split(";");
            for (String item : itens) {
                String arestaStr = item.trim();
                if (arestaStr.isEmpty()) {
                    continue;
                }

                int dashIndex = arestaStr.indexOf('-');
                if (dashIndex == -1) {
                    throw new Exception("Formato de aresta inválido (esperado destino-peso): " + arestaStr);
                }

                int destinoId = Integer.parseInt(arestaStr.substring(0, dashIndex).trim());
                double peso = Double.parseDouble(arestaStr.substring(dashIndex + 1).trim());

                Vertice u = grafo.vertices().get(origemId);
                Vertice v = grafo.vertices().get(destinoId);
                grafo.adicionarAresta(u, v, peso);
            }
        }

        return grafo;
    }

    // =========================================================================
    // ETAPA 3: BUSCA EM PROFUNDIDADE (DFS) E CLASSIFICAÇÃO DE ARESTAS
    // =========================================================================

    /**
     * Executa a Busca em Profundidade iniciando pelo vértice padrão (id 0).
     */
    @Override
    public Collection<Aresta> buscaEmProfundidade(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            return new ArrayList<>();
        }
        return buscaEmProfundidade(g, g.vertices().get(0));
    }

    /**
     * Sobrecarga de busca em profundidade com vértice inicial informado pelo usuário.
     */
    public Collection<Aresta> buscaEmProfundidade(Grafo g, Vertice verticeInicial) {
        int n = g.numeroDeVertices();
        cor_dfs = new int[n];
        d_dfs = new int[n];
        f_dfs = new int[n];
        pi_dfs = new Vertice[n];
        tempo_dfs = 0;

        arestasArvoreDFS = new ArrayList<>();
        arestasRetornoDFS = new ArrayList<>();
        arestasAvancoDFS = new ArrayList<>();
        arestasCruzamentoDFS = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            cor_dfs[i] = BRANCO;
            pi_dfs[i] = null;
        }

        // Visita primeiro a partir do vértice inicial informado
        if (verticeInicial != null && verticeInicial.id() >= 0 && verticeInicial.id() < n) {
            if (cor_dfs[verticeInicial.id()] == BRANCO) {
                visitaDFS(g, verticeInicial);
            }
        }

        // Visita os demais vértices para cobrir todo o grafo (floresta DFS)
        for (Vertice u : g.vertices()) {
            if (cor_dfs[u.id()] == BRANCO) {
                visitaDFS(g, u);
            }
        }

        return arestasArvoreDFS;
    }

    private void visitaDFS(Grafo g, Vertice u) {
        tempo_dfs++;
        d_dfs[u.id()] = tempo_dfs;
        cor_dfs[u.id()] = CINZA;

        try {
            for (Vertice v : g.adjacentesDe(u)) {
                Aresta aresta = obterAresta(g, u, v);

                if (cor_dfs[v.id()] == BRANCO) {
                    // Aresta de Árvore: leva a um vértice ainda não descoberto
                    arestasArvoreDFS.add(aresta);
                    pi_dfs[v.id()] = u;
                    visitaDFS(g, v);
                } else if (cor_dfs[v.id()] == CINZA) {
                    // Aresta de Retorno: leva a um ancestral na árvore DFS (ciclo)
                    arestasRetornoDFS.add(aresta);
                } else { // PRETO
                    // Se d[u] < d[v], u foi descoberto antes de v -> Aresta de Avanço (descendente)
                    // Se d[u] > d[v], v foi descoberto antes de u -> Aresta de Cruzamento
                    if (d_dfs[u.id()] < d_dfs[v.id()]) {
                        arestasAvancoDFS.add(aresta);
                    } else {
                        arestasCruzamentoDFS.add(aresta);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cor_dfs[u.id()] = PRETO;
        tempo_dfs++;
        f_dfs[u.id()] = tempo_dfs;
    }

    private Aresta obterAresta(Grafo g, Vertice u, Vertice v) throws Exception {
        ArrayList<Aresta> arestas = g.arestasEntre(u, v);
        if (arestas != null && !arestas.isEmpty()) {
            return arestas.get(0);
        }
        return new Aresta(u, v);
    }

    @Override
    public Collection<Aresta> arestasDeArvore(Grafo g) {
        if (arestasArvoreDFS == null) {
            buscaEmProfundidade(g);
        }
        return arestasArvoreDFS;
    }

    @Override
    public Collection<Aresta> arestasDeRetorno(Grafo g) {
        if (arestasRetornoDFS == null) {
            buscaEmProfundidade(g);
        }
        return arestasRetornoDFS;
    }

    @Override
    public Collection<Aresta> arestasDeAvanco(Grafo g) {
        if (arestasAvancoDFS == null) {
            buscaEmProfundidade(g);
        }
        return arestasAvancoDFS;
    }

    @Override
    public Collection<Aresta> arestasDeCruzamento(Grafo g) {
        if (arestasCruzamentoDFS == null) {
            buscaEmProfundidade(g);
        }
        return arestasCruzamentoDFS;
    }

    // Getters auxiliares para exibição dos tempos e pais da DFS
    public int[] getD_DFS() { return d_dfs; }
    public int[] getF_DFS() { return f_dfs; }
    public Vertice[] getPi_DFS() { return pi_dfs; }

    // =========================================================================
    // ETAPA 4: BUSCA EM LARGURA (BFS) E DETECÇÃO DE CICLOS
    // =========================================================================

    /**
     * Executa a Busca em Largura iniciando pelo vértice padrão (id 0).
     */
    @Override
    public Collection<Aresta> buscaEmLargura(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            return new ArrayList<>();
        }
        return buscaEmLargura(g, g.vertices().get(0));
    }

    /**
     * Sobrecarga de busca em largura com vértice inicial informado pelo usuário.
     */
    public Collection<Aresta> buscaEmLargura(Grafo g, Vertice inicio) {
        int n = g.numeroDeVertices();
        cor_bfs = new int[n];
        d_bfs = new int[n];
        pi_bfs = new Vertice[n];
        arestasArvoreBFS = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            cor_bfs[i] = BRANCO;
            d_bfs[i] = -1; // -1 indica inalcançável / infinito
            pi_bfs[i] = null;
        }

        if (inicio == null || inicio.id() < 0 || inicio.id() >= n) {
            return arestasArvoreBFS;
        }

        Queue<Vertice> fila = new LinkedList<>();

        int s = inicio.id();
        cor_bfs[s] = CINZA;
        d_bfs[s] = 0;
        pi_bfs[s] = null;
        fila.add(inicio);

        while (!fila.isEmpty()) {
            Vertice u = fila.poll();
            try {
                for (Vertice v : g.adjacentesDe(u)) {
                    if (cor_bfs[v.id()] == BRANCO) {
                        cor_bfs[v.id()] = CINZA;
                        d_bfs[v.id()] = d_bfs[u.id()] + 1;
                        pi_bfs[v.id()] = u;
                        arestasArvoreBFS.add(obterAresta(g, u, v));
                        fila.add(v);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cor_bfs[u.id()] = PRETO;
        }

        return arestasArvoreBFS;
    }

    // Getters auxiliares para exibição dos tempos e pais da BFS
    public int[] getD_BFS() { return d_bfs; }
    public Vertice[] getPi_BFS() { return pi_bfs; }

    /**
     * Um dígrafo contém ciclo se e somente se sua busca em profundidade contiver
     * pelo menos uma aresta de retorno (teorema clássico de PAA / Cormen).
     */
    @Override
    public boolean existeCiclo(Grafo g) {
        buscaEmProfundidade(g);
        return !arestasDeRetorno(g).isEmpty();
    }

    // =========================================================================
    // DEMAIS ALGORITMOS (ETAPAS SEGUINTES)
    // =========================================================================

    @Override
    public Grafo componentesFortementeConexos(Grafo g) {
        throw new UnsupportedOperationException("Será implementado na Etapa 5.");
    }

    @Override
    public Collection<Aresta> arvoreGeradoraMinima(Grafo g) {
        throw new UnsupportedOperationException("Será implementado na Etapa 6.");
    }

    @Override
    public double custoDaArvoreGeradora(Grafo g, Collection<Aresta> arestas) throws Exception {
        throw new UnsupportedOperationException("Será implementado na Etapa 6.");
    }

    @Override
    public ArrayList<Aresta> caminhoMinimo(Grafo g, Vertice origem, Vertice destino) {
        throw new UnsupportedOperationException("Será implementado na Etapa 7.");
    }

    @Override
    public double custoDoCaminhoMinimo(Grafo g, ArrayList<Aresta> arestas, Vertice origem, Vertice destino) throws Exception {
        throw new UnsupportedOperationException("Será implementado na Etapa 7.");
    }

    @Override
    public double fluxoMaximo(Grafo g) {
        throw new UnsupportedOperationException("Será implementado na Etapa 8.");
    }
}
