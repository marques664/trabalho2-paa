package grafos;

import algoritmos.AlgoritmosEmGrafosImpl;
import java.util.Collection;

public class TesteEtapas3e4 {
    public static void main(String[] args) {
        System.out.println("=== Testando Etapas 3 e 4: DFS, BFS e Ciclos ===");
        AlgoritmosEmGrafosImpl algoritmos = new AlgoritmosEmGrafosImpl();
        String caminhoArquivo = "docs/Teste.txt";

        try {
            // Carrega com Lista de Adjacência
            Grafo g = algoritmos.carregarGrafo(caminhoArquivo, TipoDeRepresentacao.LISTA_DE_ADJACENCIA);
            System.out.println("Grafo de teste carregado com sucesso (" + g.numeroDeVertices() + " vertices, " + g.numeroDeArestas() + " arestas).");

            // -------------------------------------------------------------
            // TESTE ETAPA 3: DFS
            // -------------------------------------------------------------
            System.out.println("\n--- [ETAPA 3] Teste de Busca em Profundidade (DFS) ---");
            Vertice inicioDFS = g.vertices().get(0);
            Collection<Aresta> arvoreDFS = algoritmos.buscaEmProfundidade(g, inicioDFS);

            System.out.println("Tabela de Descoberta (d), Finalizacao (f) e Pai (pi):");
            int[] d_dfs = algoritmos.getD_DFS();
            int[] f_dfs = algoritmos.getF_DFS();
            Vertice[] pi_dfs = algoritmos.getPi_DFS();
            for (Vertice v : g.vertices()) {
                String paiStr = (pi_dfs[v.id()] != null) ? String.valueOf(pi_dfs[v.id()].id()) : "null (raiz)";
                System.out.println("  Vertice " + v.id() + " -> d: " + d_dfs[v.id()] + ", f: " + f_dfs[v.id()] + ", pai: " + paiStr);
            }

            System.out.println("\nClassificacao das Arestas:");
            System.out.println("  Arestas de Arvore:     " + algoritmos.arestasDeArvore(g));
            System.out.println("  Arestas de Retorno:    " + algoritmos.arestasDeRetorno(g));
            System.out.println("  Arestas de Avanco:     " + algoritmos.arestasDeAvanco(g));
            System.out.println("  Arestas de Cruzamento: " + algoritmos.arestasDeCruzamento(g));

            // -------------------------------------------------------------
            // TESTE ETAPA 4: BFS
            // -------------------------------------------------------------
            System.out.println("\n--- [ETAPA 4] Teste de Busca em Largura (BFS) ---");
            Vertice inicioBFS = g.vertices().get(0);
            Collection<Aresta> arvoreBFS = algoritmos.buscaEmLargura(g, inicioBFS);

            System.out.println("Tabela BFS: Descoberta/Distancia (d) e Pai (pi):");
            int[] d_bfs = algoritmos.getD_BFS();
            Vertice[] pi_bfs = algoritmos.getPi_BFS();
            for (Vertice v : g.vertices()) {
                String paiStr = (pi_bfs[v.id()] != null) ? String.valueOf(pi_bfs[v.id()].id()) : "null (raiz)";
                System.out.println("  Vertice " + v.id() + " -> d (nivel): " + d_bfs[v.id()] + ", pai: " + paiStr);
            }
            System.out.println("Arestas da Arvore BFS: " + arvoreBFS);

            // -------------------------------------------------------------
            // TESTE ETAPA 4: Detecção de Ciclo
            // -------------------------------------------------------------
            System.out.println("\n--- [ETAPA 4] Teste de Deteccao de Ciclo ---");
            boolean temCiclo = algoritmos.existeCiclo(g);
            System.out.println("O grafo de teste possui ciclo? " + temCiclo + " (Esperado: true)");
            assert temCiclo : "Deveria ter detectado ciclo no grafo de teste!";

            // Teste adicional com um grafo acíclico (DAG) para validar o caso 'false'
            System.out.println("\nTestando com um Grafo Aciclico (DAG: 0->1, 1->2, 0->2)...");
            Grafo dag = new GrafoListaAdjacencia(3);
            dag.adicionarAresta(dag.vertices().get(0), dag.vertices().get(1));
            dag.adicionarAresta(dag.vertices().get(1), dag.vertices().get(2));
            dag.adicionarAresta(dag.vertices().get(0), dag.vertices().get(2));

            boolean temCicloDAG = algoritmos.existeCiclo(dag);
            System.out.println("O DAG possui ciclo? " + temCicloDAG + " (Esperado: false)");
            assert !temCicloDAG : "Nao deveria detectar ciclo no DAG!";

            System.out.println("\n=== Todos os testes das Etapas 3 e 4 passaram com sucesso! ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
