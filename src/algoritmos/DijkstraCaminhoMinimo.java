package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Algoritmo de Dijkstra para Caminho Mínimo entre pares de vértices em grafos com pesos não negativos.
 * Utiliza vetores primitivos para distâncias, pais e controle de visitados.
 */
public class DijkstraCaminhoMinimo {

    public ArrayList<Aresta> executar(Grafo g, Vertice origem, Vertice destino) {
        if (g == null || origem == null || destino == null) {
            return new ArrayList<>();
        }

        int n = g.numeroDeVertices();
        int s = origem.id();
        int t = destino.id();

        if (s < 0 || s >= n || t < 0 || t >= n || s == t) {
            return new ArrayList<>();
        }

        double[] dist = new double[n];
        Vertice[] pi = new Vertice[n];
        Aresta[] arestaPai = new Aresta[n];
        boolean[] visitado = new boolean[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            pi[i] = null;
            arestaPai[i] = null;
            visitado[i] = false;
        }

        dist[s] = 0.0;

        for (int count = 0; count < n; count++) {
            int u = -1;
            double menorDist = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && dist[i] < menorDist) {
                    menorDist = dist[i];
                    u = i;
                }
            }

            if (u == -1 || dist[u] == Double.POSITIVE_INFINITY) {
                break;
            }

            visitado[u] = true;
            if (u == t) {
                break;
            }

            Vertice verticeU = g.vertices().get(u);
            for (Vertice v : vizinhos(g, verticeU)) {
                int idV = v.id();
                if (!visitado[idV]) {
                    ArrayList<Aresta> arestasUV = arestas(g, verticeU, v);
                    if (!arestasUV.isEmpty()) {
                        Aresta a = arestasUV.get(0);
                        double novoCusto = dist[u] + a.peso();
                        if (novoCusto < dist[idV]) {
                            dist[idV] = novoCusto;
                            pi[idV] = verticeU;
                            arestaPai[idV] = a;
                        }
                    }
                }
            }
        }

        if (dist[t] == Double.POSITIVE_INFINITY) {
            return new ArrayList<>();
        }

        ArrayList<Aresta> caminho = new ArrayList<>();
        int atual = t;
        while (atual != s && arestaPai[atual] != null) {
            caminho.add(arestaPai[atual]);
            atual = pi[atual].id();
        }

        Collections.reverse(caminho);
        return caminho;
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

    public double calcularCusto(Grafo g, ArrayList<Aresta> arestas, Vertice origem, Vertice destino) {
        if (arestas == null || arestas.isEmpty()) {
            return 0.0;
        }
        double custoTotal = 0.0;
        for (Aresta a : arestas) {
            custoTotal += a.peso();
        }
        return custoTotal;
    }
}
