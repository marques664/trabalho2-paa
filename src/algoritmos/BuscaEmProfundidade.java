package algoritmos;

import grafos.Aresta;
import grafos.Grafo;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

// Busca em Profundidade (DFS)
// Percorre o grafo recursivamente, calculando tempos de descoberta (d) e termino (f),
// antecessores (pi) e classificando as arestas (arvore, retorno, avanco e cruzamento).
public class BuscaEmProfundidade {

    // Cores da busca (padrao Cormen)
    public static final int BRANCO = 0; // nao visitado
    public static final int CINZA = 1;  // em processamento na pilha recursiva
    public static final int PRETO = 2;  // finalizado

    private int[] d;              // tempo de descoberta
    private int[] f;              // tempo de termino
    private Vertice[] pi;         // antecessor na arvore de busca
    private int[] cor;
    private int tempo;

    private ArrayList<Aresta> arestasArvore;
    private ArrayList<Aresta> arestasRetorno;
    private ArrayList<Aresta> arestasAvanco;
    private ArrayList<Aresta> arestasCruzamento;

    // Inicializa os vetores de controle e executa a DFS comecando pelo vertice indicado
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

        // Inicia pelo vertice inicial informado pelo usuario
        if (verticeInicial != null && verticeInicial.id() >= 0 && verticeInicial.id() < n) {
            if (cor[verticeInicial.id()] == BRANCO) {
                visitaDFS(g, verticeInicial);
            }
        }

        // Visita os demais vertices para garantir que todos os componentes sejam cobertos
        for (Vertice u : g.vertices()) {
            if (cor[u.id()] == BRANCO) {
                visitaDFS(g, u);
            }
        }

        return arestasArvore;
    }

    // Funcao recursiva principal: visita o vertice u, avanca para os vizinhos e classifica as arestas
    private void visitaDFS(Grafo g, Vertice u) {
        tempo++;
        d[u.id()] = tempo;
        cor[u.id()] = CINZA;

        for (Vertice v : vizinhos(g, u)) {
            Aresta aresta = obterAresta(g, u, v);

            if (cor[v.id()] == BRANCO) {
                // Aresta de arvore: descobre um novo vertice
                arestasArvore.add(aresta);
                pi[v.id()] = u;
                visitaDFS(g, v);
            } else if (cor[v.id()] == CINZA) {
                // Aresta de retorno: encontra ancestral ainda cinza na pilha (indica ciclo no digrafo)
                arestasRetorno.add(aresta);
            } else if (d[u.id()] < d[v.id()]) {
                // Aresta de avanco: aponta para um descendente ja finalizado
                arestasAvanco.add(aresta);
            } else {
                // Aresta de cruzamento: conecta ramos diferentes ja finalizados
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
