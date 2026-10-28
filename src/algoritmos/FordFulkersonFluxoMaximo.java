package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Algoritmo de Ford-Fulkerson (implementação de Edmonds-Karp usando BFS)
 * para cálculo do Fluxo Máximo em grafo orientado ponderado por capacidades.
 * Utiliza matriz e vetores para a rede residual e rastreamento de caminhos aumentantes.
 */
public class FordFulkersonFluxoMaximo {

    public double executar(Grafo g) {
        if (g == null || g.numeroDeVertices() < 2) {
            return 0.0;
        }
        // Por padrão, adota o primeiro vértice (id 0) como fonte e o último como sumidouro
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

        // Matriz de capacidade residual
        double[][] rCap = new double[n][n];

        // Inicializa as capacidades a partir das arestas do grafo original
        try {
            for (Vertice u : g.vertices()) {
                for (Vertice v : g.adjacentesDe(u)) {
                    if (u.id() != v.id()) { // Ignora laços em rede de fluxo
                        ArrayList<Aresta> arestasUV = g.arestasEntre(u, v);
                        if (!arestasUV.isEmpty()) {
                            rCap[u.id()][v.id()] += arestasUV.get(0).peso();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        double fluxoMaximo = 0.0;
        int[] pai = new int[n];

        // Enquanto existir um caminho aumentante de s para t na rede residual
        while (bfsCaminhoAumentante(rCap, s, t, pai, n)) {
            // 1. Encontra a capacidade residual mínima (gargalo) no caminho encontrado
            double gargalo = Double.POSITIVE_INFINITY;
            for (int v = t; v != s; v = pai[v]) {
                int u = pai[v];
                gargalo = Math.min(gargalo, rCap[u][v]);
            }

            // 2. Atualiza as capacidades residuais ao longo do caminho
            for (int v = t; v != s; v = pai[v]) {
                int u = pai[v];
                rCap[u][v] -= gargalo; // Reduz a capacidade residual direta
                rCap[v][u] += gargalo; // Aumenta a capacidade residual reversa
            }

            // 3. Soma ao fluxo máximo total
            fluxoMaximo += gargalo;
        }

        return fluxoMaximo;
    }

    /**
     * Busca em Largura (BFS) na rede residual para encontrar um caminho de s para t.
     */
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
                // Se ainda não visitado e há capacidade residual disponível
                if (!visitado[v] && rCap[u][v] > 1e-9) {
                    fila.add(v);
                    pai[v] = u;
                    visitado[v] = true;

                    if (v == t) {
                        return true; // Encontrou caminho aumentante até o sumidouro
                    }
                }
            }
        }

        return false;
    }
}
