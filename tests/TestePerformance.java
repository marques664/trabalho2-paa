package tests;

import algoritmos.AlgoritmosEmGrafosImpl;
import grafos.Grafo;
import grafos.GrafoListaAdjacencia;
import grafos.GrafoMatrizAdjacencia;
import grafos.GrafoMatrizIncidencia;
import grafos.Vertice;

import java.util.Random;

public class TestePerformance {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("            TESTE DE PERFORMANCE E ESCALABILIDADE           ");
        System.out.println("============================================================");

        AlgoritmosEmGrafosImpl alg = new AlgoritmosEmGrafosImpl();
        Random rng = new Random(42); // Seed fixo para reprodutibilidade

        // -------------------------------------------------------------
        // PARTE 1: Comparativo das 3 Representações Computacionais
        // -------------------------------------------------------------
        System.out.println("\n[PARTE 1] Desempenho de Carga por Representação (|V| crescente com 4 arestas/vértice):");
        int[] tamanhosRep = {50, 100, 250, 500, 1000};

        System.out.printf("%-8s | %-15s | %-15s | %-15s\n", "Vértices", "Lista Adj (ms)", "Matriz Adj (ms)", "Matriz Inc (ms)");
        System.out.println("------------------------------------------------------------------");

        for (int V : tamanhosRep) {
            int arestasPorVertice = 4;
            int totalArestas = V * arestasPorVertice;

            // 1. Lista de Adjacência
            long t0 = System.nanoTime();
            Grafo gLista = new GrafoListaAdjacencia(V);
            inserirArestasAleatorias(gLista, V, totalArestas, rng);
            long tLista = (System.nanoTime() - t0) / 1_000_000;

            // 2. Matriz de Adjacência
            t0 = System.nanoTime();
            Grafo gMatAdj = new GrafoMatrizAdjacencia(V);
            inserirArestasAleatorias(gMatAdj, V, totalArestas, rng);
            long tMatAdj = (System.nanoTime() - t0) / 1_000_000;

            // 3. Matriz de Incidência (redimensionamento dinâmico de colunas)
            String tMatIncStr;
            if (V <= 1000) {
                t0 = System.nanoTime();
                Grafo gMatInc = new GrafoMatrizIncidencia(V);
                inserirArestasAleatorias(gMatInc, V, totalArestas, rng);
                long tMatInc = (System.nanoTime() - t0) / 1_000_000;
                tMatIncStr = tMatInc + " ms";
            } else {
                tMatIncStr = "Inviável (>10s)";
            }

            System.out.printf("%-8d | %-15s | %-15s | %-15s\n", V, tLista + " ms", tMatAdj + " ms", tMatIncStr);
        }

        // -------------------------------------------------------------
        // PARTE 2: Benchmark dos Algoritmos na Lista de Adjacência
        // -------------------------------------------------------------
        System.out.println("\n[PARTE 2] Tempo de Execução dos Algoritmos (Lista de Adjacência, densidade moderada):");
        int[] tamanhosAlg = {100, 500, 1000, 2500, 5000};

        System.out.printf("%-8s | %-8s | %-8s | %-10s | %-10s | %-12s | %-10s\n",
                "|V|", "DFS", "BFS", "Kosaraju", "Kruskal", "Dijkstra(O(V²))", "Ford-Fulkerson");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (int V : tamanhosAlg) {
            int totalArestas = V * 5; // Grafo com grau médio de saída 5
            Grafo g = new GrafoListaAdjacencia(V);
            inserirArestasAleatorias(g, V, totalArestas, rng);

            // 1. DFS
            long t0 = System.nanoTime();
            alg.buscaEmProfundidade(g);
            long tDFS = (System.nanoTime() - t0) / 1_000_000;

            // 2. BFS
            t0 = System.nanoTime();
            alg.buscaEmLargura(g);
            long tBFS = (System.nanoTime() - t0) / 1_000_000;

            // 3. Kosaraju (CFC)
            t0 = System.nanoTime();
            alg.componentesFortementeConexos(g);
            long tKosaraju = (System.nanoTime() - t0) / 1_000_000;

            // 4. Kruskal (AGM)
            t0 = System.nanoTime();
            alg.arvoreGeradoraMinima(g);
            long tKruskal = (System.nanoTime() - t0) / 1_000_000;

            // 5. Dijkstra O(V^2)
            long t0Dijk = System.nanoTime();
            alg.caminhoMinimo(g, g.vertices().get(0), g.vertices().get(V - 1));
            long tDijkstra = (System.nanoTime() - t0Dijk) / 1_000_000;

            // 6. Ford-Fulkerson (Edmonds-Karp)
            String tFlowStr;
            if (V <= 1000) {
                long t0Flow = System.nanoTime();
                alg.fluxoMaximo(g, g.vertices().get(0), g.vertices().get(V - 1));
                long tFlow = (System.nanoTime() - t0Flow) / 1_000_000;
                tFlowStr = tFlow + " ms";
            } else {
                tFlowStr = "Lento (>5s)";
            }

            System.out.printf("%-8d | %-8s | %-8s | %-10s | %-10s | %-15s | %-10s\n",
                    V, tDFS + " ms", tBFS + " ms", tKosaraju + " ms", tKruskal + " ms", tDijkstra + " ms", tFlowStr);
        }

        System.out.println("============================================================");
    }

    private static void inserirArestasAleatorias(Grafo g, int V, int totalArestas, Random rng) {
        try {
            for (int i = 0; i < totalArestas; i++) {
                int u = rng.nextInt(V);
                int v = rng.nextInt(V);
                double peso = 1.0 + rng.nextInt(100);
                g.adicionarAresta(g.vertices().get(u), g.vertices().get(v), peso);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
