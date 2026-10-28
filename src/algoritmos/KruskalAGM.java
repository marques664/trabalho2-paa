package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Algoritmo de Kruskal para Árvore Geradora Mínima (AGM).
 * Utiliza conjuntos disjuntos (Union-Find) com arrays primitivos (pai e rank).
 */
public class KruskalAGM {

    public Collection<Aresta> executar(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            return new ArrayList<>();
        }

        int n = g.numeroDeVertices();
        List<Aresta> todasArestas = new ArrayList<>();

        // Coleta todas as arestas evitando laços e arestas duplicadas
        try {
            for (Vertice u : g.vertices()) {
                for (Vertice v : g.adjacentesDe(u)) {
                    if (u.id() == v.id()) {
                        continue; // AGM não possui laços
                    }
                    ArrayList<Aresta> arestasEntre = g.arestasEntre(u, v);
                    if (!arestasEntre.isEmpty()) {
                        Aresta a = arestasEntre.get(0);
                        boolean jaExiste = false;
                        for (Aresta existente : todasArestas) {
                            if ((existente.origem().id() == u.id() && existente.destino().id() == v.id()) ||
                                (existente.origem().id() == v.id() && existente.destino().id() == u.id())) {
                                jaExiste = true;
                                if (a.peso() < existente.peso()) {
                                    existente.setarPeso(a.peso());
                                }
                                break;
                            }
                        }
                        if (!jaExiste) {
                            todasArestas.add(new Aresta(u, v, a.peso()));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ordena pelo peso em ordem crescente
        todasArestas.sort(Comparator.comparingDouble(Aresta::peso));

        // Union-Find com vetores primitivos
        int[] pai = new int[n];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            pai[i] = i;
            rank[i] = 0;
        }

        List<Aresta> agm = new ArrayList<>();

        for (Aresta a : todasArestas) {
            int raizU = find(pai, a.origem().id());
            int raizV = find(pai, a.destino().id());

            if (raizU != raizV) {
                agm.add(a);
                union(pai, rank, raizU, raizV);
                if (agm.size() == n - 1) {
                    break;
                }
            }
        }

        return agm;
    }

    private int find(int[] pai, int i) {
        int raiz = i;
        while (raiz != pai[raiz]) {
            raiz = pai[raiz];
        }
        // Compressão de caminho
        int atual = i;
        while (atual != raiz) {
            int prox = pai[atual];
            pai[atual] = raiz;
            atual = prox;
        }
        return raiz;
    }

    private void union(int[] pai, int[] rank, int raizU, int raizV) {
        if (rank[raizU] < rank[raizV]) {
            pai[raizU] = raizV;
        } else if (rank[raizU] > rank[raizV]) {
            pai[raizV] = raizU;
        } else {
            pai[raizV] = raizU;
            rank[raizU]++;
        }
    }

    public double calcularCusto(Collection<Aresta> arestas) throws Exception {
        if (arestas == null) {
            throw new Exception("Coleção de arestas nula.");
        }
        double total = 0.0;
        for (Aresta a : arestas) {
            total += a.peso();
        }
        return total;
    }
}
