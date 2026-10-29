package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Algoritmo de Busca em Largura (BFS).
 */
public class BuscaEmLargura {

    public static final int BRANCO = 0;
    public static final int CINZA = 1;
    public static final int PRETO = 2;

    private int[] d;              // Distâncias / níveis de descoberta
    private Vertice[] pi;         // Pais / antecessores na árvore BFS
    private int[] cor;            // Cores (estados) dos vértices
    private ArrayList<Aresta> arestasArvore;

    public Collection<Aresta> executar(Grafo g, Vertice verticeInicial) {
        if (g == null || g.numeroDeVertices() == 0) {
            return new ArrayList<>();
        }

        int n = g.numeroDeVertices();
        cor = new int[n];
        d = new int[n];
        pi = new Vertice[n];
        arestasArvore = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            cor[i] = BRANCO;
            d[i] = -1; // -1 indica inalcançável
            pi[i] = null;
        }

        if (verticeInicial == null || verticeInicial.id() < 0 || verticeInicial.id() >= n) {
            return arestasArvore;
        }

        Queue<Vertice> fila = new LinkedList<>();

        int s = verticeInicial.id();
        cor[s] = CINZA;
        d[s] = 0;
        pi[s] = null;
        fila.add(verticeInicial);

        while (!fila.isEmpty()) {
            Vertice u = fila.poll();
            for (Vertice v : vizinhos(g, u)) {
                if (cor[v.id()] == BRANCO) {
                    cor[v.id()] = CINZA;
                    d[v.id()] = d[u.id()] + 1;
                    pi[v.id()] = u;
                    arestasArvore.add(obterAresta(g, u, v));
                    fila.add(v);
                }
            }
            cor[u.id()] = PRETO;
        }

        return arestasArvore;
    }

    private List<Vertice> vizinhos(Grafo g, Vertice u) {
        try {
            return g.adjacentesDe(u);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private Aresta obterAresta(Grafo g, Vertice u, Vertice v) {
        try {
            ArrayList<Aresta> arestas = g.arestasEntre(u, v);
            if (arestas != null && !arestas.isEmpty()) {
                return arestas.get(0);
            }
        } catch (Exception ignored) {}
        return new Aresta(u, v);
    }

    public int[] getD() { return d; }
    public Vertice[] getPi() { return pi; }
    public Collection<Aresta> arestasDeArvore() { return arestasArvore != null ? arestasArvore : new ArrayList<>(); }
}
