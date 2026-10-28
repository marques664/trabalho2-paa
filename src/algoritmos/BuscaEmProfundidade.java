package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Algoritmo de Busca em Profundidade (DFS) com classificação de arestas.
 */
public class BuscaEmProfundidade {

    public static final int BRANCO = 0;
    public static final int CINZA = 1;
    public static final int PRETO = 2;

    private int[] d;              // Tempos de descoberta
    private int[] f;              // Tempos de finalização
    private Vertice[] pi;         // Pais / antecessores na árvore DFS
    private int[] cor;            // Cores (estados) dos vértices
    private int tempo;

    private ArrayList<Aresta> arestasArvore;
    private ArrayList<Aresta> arestasRetorno;
    private ArrayList<Aresta> arestasAvanco;
    private ArrayList<Aresta> arestasCruzamento;

    public Collection<Aresta> executar(Grafo g, Vertice verticeInicial) {
        if (g == null || g.numeroDeVertices() == 0) {
            return new ArrayList<>();
        }

        int n = g.numeroDeVertices();
        cor = new int[n];
        d = new int[n];
        f = new int[n];
        pi = new Vertice[n];
        tempo = 0;

        arestasArvore = new ArrayList<>();
        arestasRetorno = new ArrayList<>();
        arestasAvanco = new ArrayList<>();
        arestasCruzamento = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            cor[i] = BRANCO;
            pi[i] = null;
        }

        // 1. Visita a partir do vértice inicial escolhido
        if (verticeInicial != null && verticeInicial.id() >= 0 && verticeInicial.id() < n) {
            if (cor[verticeInicial.id()] == BRANCO) {
                visitaDFS(g, verticeInicial);
            }
        }

        // 2. Visita os demais vértices não alcançados (floresta DFS)
        for (Vertice u : g.vertices()) {
            if (cor[u.id()] == BRANCO) {
                visitaDFS(g, u);
            }
        }

        return arestasArvore;
    }

    private void visitaDFS(Grafo g, Vertice u) {
        tempo++;
        d[u.id()] = tempo;
        cor[u.id()] = CINZA;

        try {
            for (Vertice v : g.adjacentesDe(u)) {
                Aresta aresta = obterAresta(g, u, v);

                if (cor[v.id()] == BRANCO) {
                    arestasArvore.add(aresta);
                    pi[v.id()] = u;
                    visitaDFS(g, v);
                } else if (cor[v.id()] == CINZA) {
                    arestasRetorno.add(aresta);
                } else { // PRETO
                    if (d[u.id()] < d[v.id()]) {
                        arestasAvanco.add(aresta);
                    } else {
                        arestasCruzamento.add(aresta);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        cor[u.id()] = PRETO;
        tempo++;
        f[u.id()] = tempo;
    }

    private Aresta obterAresta(Grafo g, Vertice u, Vertice v) throws Exception {
        ArrayList<Aresta> arestas = g.arestasEntre(u, v);
        if (arestas != null && !arestas.isEmpty()) {
            return arestas.get(0);
        }
        return new Aresta(u, v);
    }

    public Collection<Aresta> arestasDeArvore() { return arestasArvore != null ? arestasArvore : new ArrayList<>(); }
    public Collection<Aresta> arestasDeRetorno() { return arestasRetorno != null ? arestasRetorno : new ArrayList<>(); }
    public Collection<Aresta> arestasDeAvanco() { return arestasAvanco != null ? arestasAvanco : new ArrayList<>(); }
    public Collection<Aresta> arestasDeCruzamento() { return arestasCruzamento != null ? arestasCruzamento : new ArrayList<>(); }

    public int[] getD() { return d; }
    public int[] getF() { return f; }
    public Vertice[] getPi() { return pi; }
}
