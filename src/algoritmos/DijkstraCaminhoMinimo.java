package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Algoritmo de Dijkstra para Caminho Minimo
// Calcula o menor trajeto entre origem e destino em grafos com pesos nao-negativos
// utilizando busca linear no vetor de distancias O(V^2).
public class DijkstraCaminhoMinimo {

    // Retorna a sequencia ordenada de arestas que formam o caminho minimo de origem ate destino
    public ArrayList<Aresta> executar(Grafo g, Vertice origem, Vertice destino) {
        if (g == null || origem == null || destino == null) {
            return new ArrayList<>();
        }

        int n = g.numeroDeVertices();
        int s = origem.id();
        int t = destino.id();

        if (s == t) {
            return new ArrayList<>();
        }

        // Vetores de controle do Dijkstra
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
            // Seleciona o vertice nao-visitado com a menor distancia estimada
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
                break; // Destino alcancado
            }

            // Relaxamento das arestas que partem de u
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
            return new ArrayList<>(); // Nao ha caminho possivel ate o destino
        }

        // Reconstroi o caminho de tras para frente usando o vetor arestaPai
        ArrayList<Aresta> caminho = new ArrayList<>();
        int atual = t;
        while (atual != s && arestaPai[atual] != null) {
            caminho.add(arestaPai[atual]);
            atual = pi[atual].id();
        }

        Collections.reverse(caminho); // Inverte para ficar ordenado de s ate t
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

    // Calcula a soma dos pesos das arestas do caminho percorrido
    public double calcularCusto(Grafo g, ArrayList<Aresta> arestas, Vertice origem, Vertice destino) {
        if (arestas == null || arestas.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (Aresta a : arestas) {
            total += a.peso();
        }
        return total;
    }
}
