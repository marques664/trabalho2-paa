package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.GrafoListaAdjacencia;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Algoritmo de Kosaraju-Sharir para Componentes Fortemente Conexos (CFC)
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
        for (Vertice u : g.vertices()) {
            for (Vertice v : vizinhos(g, u)) {
                double peso = obterPeso(g, u, v);
                adicionarAresta(grafoTransposto, grafoTransposto.vertices().get(v.id()),
                                                grafoTransposto.vertices().get(u.id()), peso);
            }
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

        for (Vertice u : g.vertices()) {
            for (Vertice v : vizinhos(g, u)) {
                int cU = componenteDoVertice[u.id()];
                int cV = componenteDoVertice[v.id()];

                if (cU != cV) {
                    Vertice compOrigem = grafoReduzido.vertices().get(cU);
                    Vertice compDestino = grafoReduzido.vertices().get(cV);
                    if (!grafoReduzido.existeAresta(compOrigem, compDestino)) {
                        adicionarAresta(grafoReduzido, compOrigem, compDestino, 1.0);
                    }
                }
            }
        }

        return grafoReduzido;
    }

    private void preencherOrdemFinalizacao(Grafo g, Vertice u, boolean[] visitado, List<Vertice> ordem) {
        visitado[u.id()] = true;
        for (Vertice v : vizinhos(g, u)) {
            if (!visitado[v.id()]) {
                preencherOrdemFinalizacao(g, v, visitado, ordem);
            }
        }
        ordem.add(u);
    }

    private void dfsGrafoTransposto(Grafo gt, Vertice u, boolean[] visitado, List<Vertice> componente, Grafo gOriginal) {
        visitado[u.id()] = true;
        componente.add(gOriginal.vertices().get(u.id()));
        for (Vertice v : vizinhos(gt, u)) {
            if (!visitado[v.id()]) {
                dfsGrafoTransposto(gt, v, visitado, componente, gOriginal);
            }
        }
    }

    private List<Vertice> vizinhos(Grafo g, Vertice u) {
        try {
            return g.adjacentesDe(u);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private double obterPeso(Grafo g, Vertice u, Vertice v) {
        try {
            ArrayList<Aresta> arestas = g.arestasEntre(u, v);
            if (arestas != null && !arestas.isEmpty()) {
                return arestas.get(0).peso();
            }
        } catch (Exception ignored) {}
        return 1.0;
    }

    private void adicionarAresta(Grafo g, Vertice u, Vertice v, double peso) {
        try {
            g.adicionarAresta(u, v, peso);
        } catch (Exception ignored) {}
    }

    public List<List<Vertice>> getComponentes() {
        return componentes;
    }
}
