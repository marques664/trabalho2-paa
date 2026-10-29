package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FordFulkersonFluxoMaximo {

    public double executar(Grafo g) {
        if (g == null || g.numeroDeVertices() < 2) {
            return 0.0;
        }
        // Fonte padrão: vértice 0; Sumidouro padrão: último vértice
        return executar(g, g.vertices().get(0), g.vertices().get(g.numeroDeVertices() - 1));
    }

    public double executar(Grafo g, Vertice fonte, Vertice sumidouro) {
        if (g == null || fonte == null || sumidouro == null) {
            return 0.0;
        }

        int n = g.numeroDeVertices();
        int s = fonte.id();
        int t = sumidouro.id();

        if (s == t) {
            return 0.0;
        }

        // Matriz de capacidade residual
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

        // Enquanto existir um caminho aumentante na rede residual de s até t
        while (bfsCaminhoAumentante(rCap, s, t, pai, n)) {
            // 1. Acha o gargalo (menor capacidade livre no caminho)
            double gargalo = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = pai[v]) {
                int u = pai[v];
                gargalo = Math.min(gargalo, rCap[u][v]);
            }

            // 2. Atualiza a capacidade residual direta e reversa
            for (int v = t; v != s; v = pai[v]) {
                int u = pai[v];
                rCap[u][v] -= gargalo;
                rCap[v][u] += gargalo;
            }

            // 3. Soma ao fluxo acumulado
            fluxoMaximo += gargalo;
        }

        return fluxoMaximo;
    }

    // BFS para achar o caminho aumentante mais curto em número de arestas (Edmonds-Karp)
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
