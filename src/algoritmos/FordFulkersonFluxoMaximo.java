package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Algoritmo de Ford-Fulkerson (Edmonds-Karp via BFS) para Fluxo Máximo.
 * Utiliza matriz residual e vetores primitivos para rastrear caminhos aumentantes.
 */
public class FordFulkersonFluxoMaximo {

    public double executar(Grafo g) {
        if (g == null || g.numeroDeVertices() < 2) {
            return 0.0;
        }
        return executar(g, g.vertices().get(0), g.vertices().get(g.numeroDeVertices() - 1));
    }

    public double executar(Grafo g, Vertice fonte, Vertice sumidouro) {
        if (g == null || fonte == null || sumidouro == null) {
            return 0.0;
        }

        int n = g.numeroDeVertices();
        int s = fonte.id();
        int t = sumidouro.id();

        if (s < 0 || s >= n || t < 0 || t >= n || s == t) {
            return 0.0;
        }

        double[][] rCap = new double[n][n];

        for (Vertice u : g.vertices()) {
            for (Vertice v : vizinhos(g, u)) {
                if (u.id() != v.id()) {
                    ArrayList<Aresta> arestasUV = arestas(g, u, v);
                    if (!arestasUV.isEmpty()) {
                        rCap[u.id()][v.id()] += arestasUV.get(0).peso();
                    }
                }
            }
        }

        double fluxoMaximo = 0.0;
        int[] pai = new int[n];

        while (bfsCaminhoAumentante(rCap, s, t, pai, n)) {
            double gargalo = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = pai[v]) {
                int u = pai[v];
                gargalo = Math.min(gargalo, rCap[u][v]);
            }

            for (int v = t; v != s; v = pai[v]) {
                int u = pai[v];
                rCap[u][v] -= gargalo;
                rCap[v][u] += gargalo;
            }

            fluxoMaximo += gargalo;
        }

        return fluxoMaximo;
    }

    private boolean bfsCaminhoAumentante(double[][] rCap, int s, int t, int[] pai, int n) {
        boolean[] visitado = new boolean[n];
        for (int i = 0; i < n; i++) {
            visitado[i] = false;
            pai[i] = -1;
        }

        Queue<Integer> fila = new LinkedList<>();
        fila.add(s);
        visitado[s] = true;

        while (!fila.isEmpty()) {
            int u = fila.poll();

            for (int v = 0; v < n; v++) {
                if (!visitado[v] && rCap[u][v] > 1e-9) {
                    fila.add(v);
                    pai[v] = u;
                    visitado[v] = true;

                    if (v == t) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private List<Vertice> vizinhos(Grafo g, Vertice u) {
        try {
            return g.adjacentesDe(u);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private ArrayList<Aresta> arestas(Grafo g, Vertice u, Vertice v) {
        try {
            return g.arestasEntre(u, v);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
