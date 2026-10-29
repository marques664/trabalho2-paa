package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.GrafoListaAdjacencia;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KosarajuCFC {

    private List<List<Vertice>> componentes = new ArrayList<>();

    public Grafo executar(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            componentes = new ArrayList<>();
            return new GrafoListaAdjacencia(0);
        }

        int n = g.numeroDeVertices();

        // Passo 1: DFS no grafo original para guardar a ordem que os vértices finalizam
        boolean[] visitado = new boolean[n];
        List<Vertice> ordemFinalizacao = new ArrayList<>();

        for (Vertice u : g.vertices()) {
            if (!visitado[u.id()]) {
                dfsOrdem(g, u, visitado, ordemFinalizacao);
            }
        }

        // Passo 2: Criar o grafo transposto (inverter todas as arestas)
        Grafo grafoTransposto = new GrafoListaAdjacencia(n);
        for (Vertice u : g.vertices()) {
            for (Vertice v : vizinhos(g, u)) {
                double peso = obterPeso(g, u, v);
                adicionarAresta(grafoTransposto, grafoTransposto.vertices().get(v.id()),
                                                grafoTransposto.vertices().get(u.id()), peso);
            }
        }

        // Passo 3: DFS no grafo transposto desempilhando na ordem inversa de término
        boolean[] visitadoGT = new boolean[n];
        componentes = new ArrayList<>();
        int[] componenteDoVertice = new int[n];

        for (int i = ordemFinalizacao.size() - 1; i >= 0; i--) {
            Vertice u = ordemFinalizacao.get(i);
            if (!visitadoGT[u.id()]) {
                List<Vertice> compAtual = new ArrayList<>();
                dfsTransposto(grafoTransposto, grafoTransposto.vertices().get(u.id()), visitadoGT, compAtual, g);

                int idComp = componentes.size();
                for (Vertice v : compAtual) {
                    componenteDoVertice[v.id()] = idComp;
                }
                componentes.add(compAtual);
            }
        }

        // Passo 4: Montar o Grafo Reduzido (cada componente vira um supervértice)
        Grafo reduzido = new GrafoListaAdjacencia(componentes.size());

        for (Vertice u : g.vertices()) {
            for (Vertice v : vizinhos(g, u)) {
                int cU = componenteDoVertice[u.id()];
                int cV = componenteDoVertice[v.id()];

                // Se a aresta conecta componentes diferentes, adiciona no reduzido
                if (cU != cV) {
                    Vertice compOrigem = reduzido.vertices().get(cU);
                    Vertice compDestino = reduzido.vertices().get(cV);
                    if (!reduzido.existeAresta(compOrigem, compDestino)) {
                        adicionarAresta(reduzido, compOrigem, compDestino, 1.0);
                    }
                }
            }
        }

        return reduzido;
    }

    private void dfsOrdem(Grafo g, Vertice u, boolean[] visitado, List<Vertice> ordem) {
        visitado[u.id()] = true;
        for (Vertice v : vizinhos(g, u)) {
            if (!visitado[v.id()]) {
                dfsOrdem(g, v, visitado, ordem);
            }
        }
        ordem.add(u);
    }

    private void dfsTransposto(Grafo gt, Vertice u, boolean[] visitado, List<Vertice> componente, Grafo gOriginal) {
        visitado[u.id()] = true;
        componente.add(gOriginal.vertices().get(u.id()));
        for (Vertice v : vizinhos(gt, u)) {
            if (!visitado[v.id()]) {
                dfsTransposto(gt, v, visitado, componente, gOriginal);
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
            if (!arestas.isEmpty()) {
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
