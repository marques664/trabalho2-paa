package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// Algoritmo de Ford-Fulkerson (Edmonds-Karp) para Fluxo Maximo
// Busca repetidamente caminhos aumentantes na rede residual usando BFS ate saturar a capacidade.
public class FordFulkersonFluxoMaximo {

    // Versao padrao: fonte = primeiro vertice (0), sumidouro = ultimo vertice (n - 1)
    public double executar(Grafo g) {
        if (g == null || g.numeroDeVertices() < 2) {
            return 0.0;
        }
        return executar(g, g.vertices().get(0), g.vertices().get(g.numeroDeVertices() - 1));
    }

    // Calcula o valor do fluxo maximo que pode ser enviado da fonte ate o sumidouro
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

        // Matriz de capacidade residual (guarda capacidade livre direta e fluxo reverso)
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

        // Enquanto existir caminho aumentante com capacidade residual positiva de s ate t
        while (bfsCaminhoAumentante(rCap, s, t, pai, n)) {
            // 1. Identifica o gargalo (menor capacidade livre ao longo do caminho encontrado)
            double gargalo = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = pai[v]) {
                int u = pai[v];
                gargalo = Math.min(gargalo, rCap[u][v]);
            }

            // 2. Atualiza as capacidades residuais (subtrai na ida e soma na volta para permitir cancelamento)
            for (int v = t; v != s; v = pai[v]) {
                int u = pai[v];
                rCap[u][v] -= gargalo;
                rCap[v][u] += gargalo;
            }

            // 3. Incrementa o fluxo maximo acumulado
            fluxoMaximo += gargalo;
        }

        return fluxoMaximo;
    }

    // BFS para encontrar o caminho aumentante mais curto em numero de arestas (Edmonds-Karp)
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
