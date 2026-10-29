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
            throw new Exception("Arquivo não encontrado ou vazio: " + path);
        }

        // Primeira linha tem a quantidade de vértices
        int numVertices = Integer.parseInt(linhas.get(0).trim());

        // Instancia a estrutura escolhida
        Grafo grafo;
        if (t == TipoDeRepresentacao.MATRIZ_DE_ADJACENCIA) {
            grafo = new GrafoMatrizAdjacencia(numVertices);
        } else if (t == TipoDeRepresentacao.MATRIZ_DE_INCIDENCIA) {
            grafo = new GrafoMatrizIncidencia(numVertices);
        } else {
            grafo = new GrafoListaAdjacencia(numVertices);
        }

        // As linhas seguintes contêm: origem destino-peso; destino-peso; ...
        for (int i = 1; i < linhas.size(); i++) {
            String linha = linhas.get(i).trim();
            if (linha.isEmpty()) {
                continue;
            }

            int primeiroEspaco = linha.indexOf(' ');
            if (primeiroEspaco == -1) {
                continue;
            }

            int origemId = Integer.parseInt(linha.substring(0, primeiroEspaco).trim());
            String resto = linha.substring(primeiroEspaco + 1).trim();

            String[] arestasStr = resto.split(";");
            for (String item : arestasStr) {
                String arestaTexto = item.trim();
                if (arestaTexto.isEmpty()) {
                    continue;
                }

                int dash = arestaTexto.indexOf('-');
                if (dash != -1) {
                    int destinoId = Integer.parseInt(arestaTexto.substring(0, dash).trim());
                    double peso = Double.parseDouble(arestaTexto.substring(dash + 1).trim());

                    Vertice u = grafo.vertices().get(origemId);
                    Vertice v = grafo.vertices().get(destinoId);
                    grafo.adicionarAresta(u, v, peso);
                }
            }
        }

        return grafo;
    }

    // -------------------------------------------------------------------------
    // DFS
    // -------------------------------------------------------------------------
    @Override
    public Collection<Aresta> buscaEmProfundidade(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) return new ArrayList<>();
        return buscaEmProfundidade(g, g.vertices().get(0));
    }

    public Collection<Aresta> buscaEmProfundidade(Grafo g, Vertice verticeInicial) {
        dfsAtual = new BuscaEmProfundidade();
        return dfsAtual.executar(g, verticeInicial);
    }

    @Override
    public Collection<Aresta> arestasDeArvore(Grafo g) {
        if (dfsAtual == null) buscaEmProfundidade(g);
        return dfsAtual.arestasDeArvore();
    }

    @Override
    public Collection<Aresta> arestasDeRetorno(Grafo g) {
        if (dfsAtual == null) buscaEmProfundidade(g);
        return dfsAtual.arestasDeRetorno();
    }

    @Override
    public Collection<Aresta> arestasDeAvanco(Grafo g) {
        if (dfsAtual == null) buscaEmProfundidade(g);
        return dfsAtual.arestasDeAvanco();
    }

    @Override
    public Collection<Aresta> arestasDeCruzamento(Grafo g) {
        if (dfsAtual == null) buscaEmProfundidade(g);
        return dfsAtual.arestasDeCruzamento();
    }

    public int[] getD_DFS() { return dfsAtual != null ? dfsAtual.getD() : null; }
    public int[] getF_DFS() { return dfsAtual != null ? dfsAtual.getF() : null; }
    public Vertice[] getPi_DFS() { return dfsAtual != null ? dfsAtual.getPi() : null; }

    // -------------------------------------------------------------------------
    // BFS
    // -------------------------------------------------------------------------
    @Override
    public Collection<Aresta> buscaEmLargura(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) return new ArrayList<>();
        return buscaEmLargura(g, g.vertices().get(0));
    }

    public Collection<Aresta> buscaEmLargura(Grafo g, Vertice verticeInicial) {
        bfsAtual = new BuscaEmLargura();
        return bfsAtual.executar(g, verticeInicial);
    }

    public int[] getD_BFS() { return bfsAtual != null ? bfsAtual.getD() : null; }
    public Vertice[] getPi_BFS() { return bfsAtual != null ? bfsAtual.getPi() : null; }

    // -------------------------------------------------------------------------
    // CICLOS
    // -------------------------------------------------------------------------
    @Override
    public boolean existeCiclo(Grafo g) {
        return cicloDetector.existeCiclo(g);
    }

    // -------------------------------------------------------------------------
    // COMPONENTES FORTEMENTE CONEXOS
    // -------------------------------------------------------------------------
    @Override
    public Grafo componentesFortementeConexos(Grafo g) {
        return kosaraju.executar(g);
    }

    public List<List<Vertice>> getUltimosComponentes() {
        return kosaraju.getComponentes();
    }

    // -------------------------------------------------------------------------
    // ÁRVORE GERADORA MÍNIMA
    // -------------------------------------------------------------------------
    @Override
    public Collection<Aresta> arvoreGeradoraMinima(Grafo g) {
        return kruskal.executar(g);
    }

    @Override
    public double custoDaArvoreGeradora(Grafo g, Collection<Aresta> arestas) throws Exception {
        return kruskal.calcularCusto(arestas);
    }

    // -------------------------------------------------------------------------
    // CAMINHO MÍNIMO
    // -------------------------------------------------------------------------
    @Override
    public ArrayList<Aresta> caminhoMinimo(Grafo g, Vertice origem, Vertice destino) {
        return dijkstra.executar(g, origem, destino);
    }

    @Override
    public double custoDoCaminhoMinimo(Grafo g, ArrayList<Aresta> arestas, Vertice origem, Vertice destino) throws Exception {
        return dijkstra.calcularCusto(g, arestas, origem, destino);
    }

    // -------------------------------------------------------------------------
    // FLUXO MÁXIMO
    // -------------------------------------------------------------------------
    @Override
    public double fluxoMaximo(Grafo g) {
        return fordFulkerson.executar(g);
    }

    public double fluxoMaximo(Grafo g, Vertice fonte, Vertice sumidouro) {
        return fordFulkerson.executar(g, fonte, sumidouro);
    }
}
