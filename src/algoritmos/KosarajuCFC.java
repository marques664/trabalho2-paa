package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.GrafoListaAdjacencia;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.List;

/**
 * Algoritmo de Kosaraju-Sharir para detecção de Componentes Fortemente Conexos (CFC)
 * e geração do Grafo Reduzido (Grafo de Condensação).
 */
public class KosarajuCFC {

    private List<List<Vertice>> componentes = new ArrayList<>();

    public Grafo executar(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            componentes = new ArrayList<>();
            return new GrafoListaAdjacencia(0);
        }

        int n = g.numeroDeVertices();

        // 1. DFS no grafo original para obter a ordem de finalização
        boolean[] visitado = new boolean[n];
        List<Vertice> ordemFinalizacao = new ArrayList<>();

        for (Vertice u : g.vertices()) {
            if (!visitado[u.id()]) {
                preencherOrdemFinalizacao(g, u, visitado, ordemFinalizacao);
            }
        }

        // 2. Constrói o grafo transposto G^T (arestas invertidas v -> u)
        Grafo grafoTransposto = new GrafoListaAdjacencia(n);
        try {
            for (Vertice u : g.vertices()) {
                for (Vertice v : g.adjacentesDe(u)) {
                    ArrayList<Aresta> arestasOriginais = g.arestasEntre(u, v);
                    double peso = arestasOriginais.isEmpty() ? 1.0 : arestasOriginais.get(0).peso();
                    grafoTransposto.adicionarAresta(grafoTransposto.vertices().get(v.id()),
                                                   grafoTransposto.vertices().get(u.id()), peso);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. DFS no grafo transposto na ordem inversa de finalização
        boolean[] visitadoGT = new boolean[n];
        componentes = new ArrayList<>();
        int[] componenteDoVertice = new int[n];

        for (int i = ordemFinalizacao.size() - 1; i >= 0; i--) {
            Vertice u = ordemFinalizacao.get(i);
            if (!visitadoGT[u.id()]) {
                List<Vertice> componenteAtual = new ArrayList<>();
                dfsGrafoTransposto(grafoTransposto, grafoTransposto.vertices().get(u.id()),
                                  visitadoGT, componenteAtual, g);

                int idComponente = componentes.size();
                for (Vertice v : componenteAtual) {
                    componenteDoVertice[v.id()] = idComponente;
                }
                componentes.add(componenteAtual);
            }
        }

        // 4. Constrói o Grafo Reduzido
        int qtdComponentes = componentes.size();
        Grafo grafoReduzido = new GrafoListaAdjacencia(qtdComponentes);

        try {
            for (Vertice u : g.vertices()) {
                for (Vertice v : g.adjacentesDe(u)) {
                    int cU = componenteDoVertice[u.id()];
                    int cV = componenteDoVertice[v.id()];

                    // Se a aresta conecta componentes distintos, adiciona no grafo reduzido
                    if (cU != cV) {
                        Vertice compOrigem = grafoReduzido.vertices().get(cU);
                        Vertice compDestino = grafoReduzido.vertices().get(cV);
                        if (!grafoReduzido.existeAresta(compOrigem, compDestino)) {
                            grafoReduzido.adicionarAresta(compOrigem, compDestino, 1.0);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return grafoReduzido;
    }

    private void preencherOrdemFinalizacao(Grafo g, Vertice u, boolean[] visitado, List<Vertice> ordem) {
        visitado[u.id()] = true;
        try {
            for (Vertice v : g.adjacentesDe(u)) {
                if (!visitado[v.id()]) {
                    preencherOrdemFinalizacao(g, v, visitado, ordem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ordem.add(u);
    }

    private void dfsGrafoTransposto(Grafo gt, Vertice u, boolean[] visitado, List<Vertice> componente, Grafo gOriginal) {
        visitado[u.id()] = true;
        componente.add(gOriginal.vertices().get(u.id()));
        try {
            for (Vertice v : gt.adjacentesDe(u)) {
                if (!visitado[v.id()]) {
                    dfsGrafoTransposto(gt, v, visitado, componente, gOriginal);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<List<Vertice>> getComponentes() {
        return componentes;
    }
}
