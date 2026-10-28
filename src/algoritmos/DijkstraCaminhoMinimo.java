package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collections;

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

        if (s < 0 || s >= n || t < 0 || t >= n) {
            return new ArrayList<>();
        }

        if (s == t) {
            return new ArrayList<>(); // Distância zero, nenhuma aresta necessária
        }

        // Vetores primitivos auxiliares (Dijkstra clássico)
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
            // Busca linear no vetor pelo vértice de menor distância não visitado (O(V^2))
            int u = -1;
            double menorDist = Double.POSITIVE_INFINITY;
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && dist[i] < menorDist) {
                    menorDist = dist[i];
                    u = i;
                }
            }

            // Se o menor for infinito ou chegamos ao destino, podemos encerrar
            if (u == -1 || dist[u] == Double.POSITIVE_INFINITY) {
                break;
            }

            visitado[u] = true;
            if (u == t) {
                break; // Destino alcançado
            }

            Vertice verticeU = g.vertices().get(u);
            try {
                for (Vertice v : g.adjacentesDe(verticeU)) {
                    int idV = v.id();
                    if (!visitado[idV]) {
                        ArrayList<Aresta> arestasUV = g.arestasEntre(verticeU, v);
                        if (!arestasUV.isEmpty()) {
                            Aresta a = arestasUV.get(0);
                            double novoCusto = dist[u] + a.peso();
                            // Relaxamento da aresta
                            if (novoCusto < dist[idV]) {
                                dist[idV] = novoCusto;
                                pi[idV] = verticeU;
                                arestaPai[idV] = a;
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Se o destino permaneceu inalcançável
        if (dist[t] == Double.POSITIVE_INFINITY) {
            return new ArrayList<>();
        }

        // Reconstrói o caminho de t de volta até s usando o vetor arestaPai
        ArrayList<Aresta> caminho = new ArrayList<>();
        int atual = t;
        while (atual != s && arestaPai[atual] != null) {
            caminho.add(arestaPai[atual]);
            atual = pi[atual].id();
        }

        // Inverte a lista para ficar na ordem s -> ... -> t
        Collections.reverse(caminho);
        return caminho;
    }

    public double calcularCusto(Grafo g, ArrayList<Aresta> arestas, Vertice origem, Vertice destino) throws Exception {
        if (arestas == null || origem == null || destino == null) {
            throw new Exception("Parâmetros inválidos para cálculo do custo do caminho.");
        }

        if (origem.id() == destino.id()) {
            if (arestas.isEmpty()) {
                return 0.0;
            }
        }

        if (arestas.isEmpty()) {
            throw new Exception("Não existe caminho entre " + origem.id() + " e " + destino.id() + ".");
        }

        // Valida se a primeira aresta sai da origem
        if (arestas.get(0).origem().id() != origem.id()) {
            throw new Exception("A primeira aresta não inicia no vértice de origem informado.");
        }

        // Valida se a última aresta chega ao destino
        if (arestas.get(arestas.size() - 1).destino().id() != destino.id()) {
            throw new Exception("A última aresta não termina no vértice de destino informado.");
        }

        // Valida a continuidade das arestas e soma os pesos
        double custoTotal = 0.0;
        for (int i = 0; i < arestas.size(); i++) {
            Aresta atual = arestas.get(i);
            custoTotal += atual.peso();

            if (i > 0) {
                Aresta anterior = arestas.get(i - 1);
                if (anterior.destino().id() != atual.origem().id()) {
                    throw new Exception("A sequência de arestas não é um caminho contínuo!");
                }
            }
        }

        return custoTotal;
    }
}
