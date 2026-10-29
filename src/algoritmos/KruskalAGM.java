package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Algoritmo de Kruskal (AGM)
// Ordena as arestas por peso e usa Union-Find sobre o vetor pai para evitar ciclos.
public class KruskalAGM {

    // Encontra as V - 1 arestas de menor custo que conectam todos os vertices do grafo
    public Collection<Aresta> executar(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            return new ArrayList<>();
        }

        int n = g.numeroDeVertices();
        List<Aresta> todasArestas = new ArrayList<>();

        // Coleta as arestas sem duplicar e ignorando lacos (u == v)
        for (Vertice u : g.vertices()) {
            for (Vertice v : vizinhos(g, u)) {
                if (u.id() == v.id()) {
                    continue; // laco nao entra na AGM
                }
                ArrayList<Aresta> arestasEntre = arestas(g, u, v);
                if (!arestasEntre.isEmpty()) {
                    Aresta a = arestasEntre.get(0);
                    boolean jaExiste = false;
                    for (Aresta e : todasArestas) {
                        if ((e.origem().id() == u.id() && e.destino().id() == v.id()) ||
                            (e.origem().id() == v.id() && e.destino().id() == u.id())) {
                            jaExiste = true;
                            if (a.peso() < e.peso()) {
                                e.setarPeso(a.peso());
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

        // Ordenacao gulosa das arestas por peso crescente
        todasArestas.sort(Comparator.comparingDouble(Aresta::peso));

        // Vetor pai para gerenciar os conjuntos disjuntos (Union-Find)
        int[] pai = new int[n];
        for (int i = 0; i < n; i++) {
            pai[i] = i; // Cada vertice comeca como sua propria raiz
        }

        List<Aresta> agm = new ArrayList<>();

        for (Aresta a : todasArestas) {
            int raizU = find(pai, a.origem().id());
            int raizV = find(pai, a.destino().id());

            // Se as raizes forem diferentes, os vertices estao em conjuntos distintos (nao fecha ciclo)
            if (raizU != raizV) {
                agm.add(a);
                pai[raizU] = raizV; // uniao dos dois conjuntos
                if (agm.size() == n - 1) {
                    break; // AGM completa
                }
            }
        }

        return agm;
    }

    // Busca a raiz do conjunto no vetor pai
    private int find(int[] pai, int i) {
        while (pai[i] != i) {
            i = pai[i];
        }
        return i;
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

    // Calcula a soma dos pesos de todas as arestas selecionadas na AGM
    public double calcularCusto(Collection<Aresta> arestas) {
        if (arestas == null) {
            return 0.0;
        }
        double total = 0.0;
        for (Aresta a : arestas) {
            total += a.peso();
        }
        return total;
    }
}
