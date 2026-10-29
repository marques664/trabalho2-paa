package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BuscaEmProfundidade {

    // Cores da busca (Cormen)
    public static final int BRANCO = 0; // não visitado
    public static final int CINZA = 1;  // na pilha de recursão
    public static final int PRETO = 2;  // finalizado

    private int[] d;              // tempo de descoberta
    private int[] f;              // tempo de término
    private Vertice[] pi;         // pai na árvore
    private int[] cor;
    private int tempo;

    private ArrayList<Aresta> arestasArvore;
    private ArrayList<Aresta> arestasRetorno;
    private ArrayList<Aresta> arestasAvanco;
    private ArrayList<Aresta> arestasCruzamento;

    public Collection<Aresta> executar(Grafo g, Vertice verticeInicial) {
        int n = g.numeroDeVertices();
        cor = new int[n];
        d = new int[n];
        f = new int[n];
        pi = new Vertice[n];
        tempo = 0;

        arestasArvore = new ArrayList<>();
        arestasRetorno = new ArrayList<>();
        arestasAvanco = new ArrayList<>();
        arestasCruzamento = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            cor[i] = BRANCO;
            pi[i] = null;
        }

        // Começa pelo vértice escolhido pelo usuário
        if (verticeInicial != null && verticeInicial.id() >= 0 && verticeInicial.id() < n) {
            if (cor[verticeInicial.id()] == BRANCO) {
                visitaDFS(g, verticeInicial);
            }
        }

        // Visita o restante dos vértices para cobrir todo o grafo
        for (Vertice u : g.vertices()) {
            if (cor[u.id()] == BRANCO) {
                visitaDFS(g, u);
            }
        }

        return arestasArvore;
    }

    private void visitaDFS(Grafo g, Vertice u) {
        tempo++;
        d[u.id()] = tempo;
        cor[u.id()] = CINZA;

        for (Vertice v : vizinhos(g, u)) {
            Aresta aresta = obterAresta(g, u, v);

            if (cor[v.id()] == BRANCO) {
                // Aresta de árvore: descobriu um vértice novo
                arestasArvore.add(aresta);
                pi[v.id()] = u;
                visitaDFS(g, v);
            } else if (cor[v.id()] == CINZA) {
                // Aresta de retorno: encontrou um ancestral ativo (tem ciclo!)
                arestasRetorno.add(aresta);
            } else if (d[u.id()] < d[v.id()]) {
                // Aresta de avanço: aponta para um descendente já finalizado
                arestasAvanco.add(aresta);
            } else {
                // Aresta de cruzamento: aponta para outro ramo da busca
                arestasCruzamento.add(aresta);
            }
        }

        cor[u.id()] = PRETO;
        tempo++;
        f[u.id()] = tempo;
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

    public Collection<Aresta> arestasDeArvore() { return arestasArvore; }
    public Collection<Aresta> arestasDeRetorno() { return arestasRetorno; }
    public Collection<Aresta> arestasDeAvanco() { return arestasAvanco; }
    public Collection<Aresta> arestasDeCruzamento() { return arestasCruzamento; }

    public int[] getD() { return d; }
    public int[] getF() { return f; }
    public Vertice[] getPi() { return pi; }
}
