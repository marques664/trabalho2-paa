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

// Busca em Largura (BFS)
// Percorre o grafo nivel a nivel usando uma fila

public class BuscaEmLargura {

    private int[] d;              // distancia / nivel a partir da raiz
    private Vertice[] pi;         // antecessor na arvore BFS
    private int[] cor;
    private ArrayList<Aresta> arestasArvore;

    // Executa a busca em largura a partir do vertice raiz
    public Collection<Aresta> executar(Grafo g, Vertice verticeInicial) {
        int n = g.numeroDeVertices();
        cor = new int[n];
        d = new int[n];
        pi = new Vertice[n];
        arestasArvore = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            cor[i] = 0;   // 0 = branco (nao visitado)
            d[i] = -1;    // -1 = inalcancavel
            pi[i] = null;
        }

        if (verticeInicial == null || verticeInicial.id() < 0 || verticeInicial.id() >= n) {
            return arestasArvore;
        }

        // Fila FIFO para processar os vertices por ordem de descoberta
        Queue<Vertice> fila = new LinkedList<>();

        int s = verticeInicial.id();
        cor[s] = 1; // 1 = cinza (descoberto)
        d[s] = 0;
        fila.add(verticeInicial);

        while (!fila.isEmpty()) {
            Vertice u = fila.poll();

            for (Vertice v : vizinhos(g, u)) {
                if (cor[v.id()] == 0) {
                    cor[v.id()] = 1;
                    d[v.id()] = d[u.id()] + 1;
                    pi[v.id()] = u;
                    arestasArvore.add(obterAresta(g, u, v));
                    fila.add(v);
                }
            }
            cor[u.id()] = 2; // 2 = preto (finalizado)
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
            if (!arestas.isEmpty()) {
                return arestas.get(0);
            }
        } catch (Exception ignored) {}
        return new Aresta(u, v);
    }

    public int[] getD() { return d; }
    public Vertice[] getPi() { return pi; }
    public Collection<Aresta> arestasDeArvore() { return arestasArvore; }
}
