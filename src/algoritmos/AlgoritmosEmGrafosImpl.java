package algoritmos;

import grafos.AlgoritmosEmGrafos;
import grafos.Aresta;
import grafos.FileManager;
import grafos.Grafo;
import grafos.GrafoListaAdjacencia;
import grafos.GrafoMatrizAdjacencia;
import grafos.GrafoMatrizIncidencia;
import grafos.TipoDeRepresentacao;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementação da interface AlgoritmosEmGrafos.
 * Atua como fachada, delegando as operações para as classes especialistas de algoritmos.
 */
public class AlgoritmosEmGrafosImpl implements AlgoritmosEmGrafos {

    private final FileManager fileManager;

    private BuscaEmProfundidade dfsAtual;
    private BuscaEmLargura bfsAtual;
    private final DeteccaoCiclos cicloDetector;
    private final KosarajuCFC kosaraju;
    private final KruskalAGM kruskal;
    private final DijkstraCaminhoMinimo dijkstra;
    private final FordFulkersonFluxoMaximo fordFulkerson;

    public AlgoritmosEmGrafosImpl() {
        this.fileManager = new FileManager();
        this.cicloDetector = new DeteccaoCiclos();
        this.kosaraju = new KosarajuCFC();
        this.kruskal = new KruskalAGM();
        this.dijkstra = new DijkstraCaminhoMinimo();
        this.fordFulkerson = new FordFulkersonFluxoMaximo();
    }

    @Override
    public Grafo carregarGrafo(String path, TipoDeRepresentacao t) throws Exception {
        ArrayList<String> linhas = fileManager.stringReader(path);
        if (linhas == null || linhas.isEmpty()) {
            throw new Exception("Não foi possível ler o arquivo ou arquivo vazio: " + path);
        }

        String primeiraLinha = linhas.get(0).trim();
        int numVertices;
        try {
            numVertices = Integer.parseInt(primeiraLinha);
        } catch (NumberFormatException e) {
            throw new Exception("Formato inválido na primeira linha (esperado número de vértices): " + primeiraLinha);
        }

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

        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) {
                continue;
            }

            int indexPrimeiroEspaco = linha.indexOf(' ');
            if (indexPrimeiroEspaco == -1) {
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
    // DFS (BUSCA EM PROFUNDIDADE)
    // =========================================================================

    @Override
    public Collection<Aresta> buscaEmProfundidade(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            return new ArrayList<>();
        }
        return buscaEmProfundidade(g, g.vertices().get(0));
    }

    public Collection<Aresta> buscaEmProfundidade(Grafo g, Vertice verticeInicial) {
        dfsAtual = new BuscaEmProfundidade();
        return dfsAtual.executar(g, verticeInicial);
    }

    @Override
    public Collection<Aresta> arestasDeArvore(Grafo g) {
        if (dfsAtual == null) {
            buscaEmProfundidade(g);
        }
        return dfsAtual.arestasDeArvore();
    }

    @Override
    public Collection<Aresta> arestasDeRetorno(Grafo g) {
        if (dfsAtual == null) {
            buscaEmProfundidade(g);
        }
        return dfsAtual.arestasDeRetorno();
    }

    @Override
    public Collection<Aresta> arestasDeAvanco(Grafo g) {
        if (dfsAtual == null) {
            buscaEmProfundidade(g);
        }
        return dfsAtual.arestasDeAvanco();
    }

    @Override
    public Collection<Aresta> arestasDeCruzamento(Grafo g) {
        if (dfsAtual == null) {
            buscaEmProfundidade(g);
        }
        return dfsAtual.arestasDeCruzamento();
    }

    public int[] getD_DFS() { return dfsAtual != null ? dfsAtual.getD() : null; }
    public int[] getF_DFS() { return dfsAtual != null ? dfsAtual.getF() : null; }
    public Vertice[] getPi_DFS() { return dfsAtual != null ? dfsAtual.getPi() : null; }

    // =========================================================================
    // BFS (BUSCA EM LARGURA) E CICLOS
    // =========================================================================

    @Override
    public Collection<Aresta> buscaEmLargura(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            return new ArrayList<>();
        }
        return buscaEmLargura(g, g.vertices().get(0));
    }

    public Collection<Aresta> buscaEmLargura(Grafo g, Vertice verticeInicial) {
        bfsAtual = new BuscaEmLargura();
        return bfsAtual.executar(g, verticeInicial);
    }

    public int[] getD_BFS() { return bfsAtual != null ? bfsAtual.getD() : null; }
    public Vertice[] getPi_BFS() { return bfsAtual != null ? bfsAtual.getPi() : null; }

    @Override
    public boolean existeCiclo(Grafo g) {
        return cicloDetector.existeCiclo(g);
    }

    // =========================================================================
    // COMPONENTES FORTEMENTE CONEXOS (KOSARAJU)
    // =========================================================================

    @Override
    public Grafo componentesFortementeConexos(Grafo g) {
        return kosaraju.executar(g);
    }

    public List<List<Vertice>> getUltimosComponentes() {
        return kosaraju.getComponentes();
    }

    // =========================================================================
    // ÁRVORE GERADORA MÍNIMA (KRUSKAL)
    // =========================================================================

    @Override
    public Collection<Aresta> arvoreGeradoraMinima(Grafo g) {
        return kruskal.executar(g);
    }

    @Override
    public double custoDaArvoreGeradora(Grafo g, Collection<Aresta> arestas) throws Exception {
        return kruskal.calcularCusto(arestas);
    }

    // =========================================================================
    // ETAPA 7: CAMINHO MÍNIMO (DIJKSTRA)
    // =========================================================================

    @Override
    public ArrayList<Aresta> caminhoMinimo(Grafo g, Vertice origem, Vertice destino) {
        return dijkstra.executar(g, origem, destino);
    }

    @Override
    public double custoDoCaminhoMinimo(Grafo g, ArrayList<Aresta> arestas, Vertice origem, Vertice destino) throws Exception {
        return dijkstra.calcularCusto(g, arestas, origem, destino);
    }

    // =========================================================================
    // ETAPA 8: FLUXO MÁXIMO (FORD-FULKERSON)
    // =========================================================================

    @Override
    public double fluxoMaximo(Grafo g) {
        return fordFulkerson.executar(g);
    }

    public double fluxoMaximo(Grafo g, Vertice fonte, Vertice sumidouro) {
        return fordFulkerson.executar(g, fonte, sumidouro);
    }
}
