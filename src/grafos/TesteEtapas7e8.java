package grafos;

import algoritmos.AlgoritmosEmGrafosImpl;
import java.util.ArrayList;

public class TesteEtapas7e8 {
    public static void main(String[] args) {
        System.out.println("=== Testando Etapas 7 e 8: Caminho Minimo e Fluxo Maximo ===");
        AlgoritmosEmGrafosImpl algoritmos = new AlgoritmosEmGrafosImpl();
        String caminhoArquivo = "docs/Teste.txt";

        try {
            // Carrega o grafo oficial
            Grafo g = algoritmos.carregarGrafo(caminhoArquivo, TipoDeRepresentacao.LISTA_DE_ADJACENCIA);
            System.out.println("Grafo carregado com sucesso (" + g.numeroDeVertices() + " vertices, " + g.numeroDeArestas() + " arestas).");

            // -------------------------------------------------------------
            // TESTE ETAPA 7: Caminho Mínimo (Dijkstra)
            // -------------------------------------------------------------
            System.out.println("\n--- [ETAPA 7] Teste de Caminho Minimo (Dijkstra) ---");

            // Teste 1: Caminho de 0 até 4
            Vertice v0 = g.vertices().get(0);
            Vertice v4 = g.vertices().get(4);
            ArrayList<Aresta> caminho0_4 = algoritmos.caminhoMinimo(g, v0, v4);
            double custo0_4 = algoritmos.custoDoCaminhoMinimo(g, caminho0_4, v0, v4);

            System.out.println("Caminho minimo de 0 a 4:");
            for (Aresta a : caminho0_4) {
                System.out.println("  " + a.origem().id() + " -> " + a.destino().id() + " (peso: " + a.peso() + ")");
            }
            System.out.println("Custo total (0 a 4): " + custo0_4 + " (esperado: 62.0)");
            assert Math.abs(custo0_4 - 62.0) < 0.001 : "Custo 0 a 4 deveria ser 62.0";

            // Teste 2: Caminho de 2 até 4
            Vertice v2 = g.vertices().get(2);
            ArrayList<Aresta> caminho2_4 = algoritmos.caminhoMinimo(g, v2, v4);
            double custo2_4 = algoritmos.custoDoCaminhoMinimo(g, caminho2_4, v2, v4);
            System.out.println("\nCaminho minimo de 2 a 4:");
            for (Aresta a : caminho2_4) {
                System.out.println("  " + a.origem().id() + " -> " + a.destino().id() + " (peso: " + a.peso() + ")");
            }
            System.out.println("Custo total (2 a 4): " + custo2_4 + " (esperado: 8.0)");
            assert Math.abs(custo2_4 - 8.0) < 0.001 : "Custo 2 a 4 deveria ser 8.0";

            // Teste 3: Caminho de 4 até 1
            Vertice v1 = g.vertices().get(1);
            ArrayList<Aresta> caminho4_1 = algoritmos.caminhoMinimo(g, v4, v1);
            double custo4_1 = algoritmos.custoDoCaminhoMinimo(g, caminho4_1, v4, v1);
            System.out.println("\nCaminho minimo de 4 a 1:");
            for (Aresta a : caminho4_1) {
                System.out.println("  " + a.origem().id() + " -> " + a.destino().id() + " (peso: " + a.peso() + ")");
            }
            System.out.println("Custo total (4 a 1): " + custo4_1 + " (esperado: 82.0 via 4->0->1)");
            assert Math.abs(custo4_1 - 82.0) < 0.001 : "Custo 4 a 1 deveria ser 82.0";

            // -------------------------------------------------------------
            // TESTE ETAPA 8: Fluxo Máximo (Ford-Fulkerson)
            // -------------------------------------------------------------
            System.out.println("\n--- [ETAPA 8] Teste de Fluxo Maximo (Ford-Fulkerson) ---");

            // Teste 1: Fluxo padrão (fonte 0 até sumidouro 4) no grafo de teste
            double fluxo0_4 = algoritmos.fluxoMaximo(g);
            System.out.println("Fluxo maximo no grafo de teste (0 -> 4): " + fluxo0_4 + " (esperado: 1.0)");
            assert Math.abs(fluxo0_4 - 1.0) < 0.001 : "Fluxo 0 a 4 esperado: 1.0";

            // Teste 2: Fonte 0 até sumidouro 2
            double fluxo0_2 = algoritmos.fluxoMaximo(g, v0, v2);
            System.out.println("Fluxo maximo (0 -> 2): " + fluxo0_2 + " (esperado: 4.0)");
            assert Math.abs(fluxo0_2 - 4.0) < 0.001 : "Fluxo 0 a 2 esperado: 4.0";

            // Teste 3: Rede clássica de fluxo (Diamante) para validação profunda
            // s=0, A=1, B=2, t=3
            // 0 -> 1 (cap 10), 0 -> 2 (cap 10)
            // 1 -> 3 (cap 10), 2 -> 3 (cap 10)
            // 1 -> 2 (cap 5)
            // Fluxo maximo esperado: 20.0
            System.out.println("\nTestando com rede de fluxo classica (diamante 4 vertices)...");
            Grafo redeDiamante = new GrafoListaAdjacencia(4);
            redeDiamante.adicionarAresta(redeDiamante.vertices().get(0), redeDiamante.vertices().get(1), 10.0);
            redeDiamante.adicionarAresta(redeDiamante.vertices().get(0), redeDiamante.vertices().get(2), 10.0);
            redeDiamante.adicionarAresta(redeDiamante.vertices().get(1), redeDiamante.vertices().get(3), 10.0);
            redeDiamante.adicionarAresta(redeDiamante.vertices().get(2), redeDiamante.vertices().get(3), 10.0);
            redeDiamante.adicionarAresta(redeDiamante.vertices().get(1), redeDiamante.vertices().get(2), 5.0);

            double fluxoDiamante = algoritmos.fluxoMaximo(redeDiamante);
            System.out.println("Fluxo maximo na rede diamante: " + fluxoDiamante + " (esperado: 20.0)");
            assert Math.abs(fluxoDiamante - 20.0) < 0.001 : "Fluxo na rede diamante deveria ser 20.0";

            System.out.println("\n=== Todos os testes das Etapas 7 e 8 passaram com sucesso! ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
