package grafos;

import algoritmos.AlgoritmosEmGrafosImpl;
import java.util.Collection;
import java.util.List;

public class TesteEtapas5e6 {
    public static void main(String[] args) {
        System.out.println("=== Testando Etapas 5 e 6: CFC e AGM ===");
        AlgoritmosEmGrafosImpl algoritmos = new AlgoritmosEmGrafosImpl();
        String caminhoArquivo = "docs/Teste.txt";

        try {
            // Carrega o grafo de teste oficial
            Grafo g = algoritmos.carregarGrafo(caminhoArquivo, TipoDeRepresentacao.LISTA_DE_ADJACENCIA);
            System.out.println("Grafo carregado com sucesso (" + g.numeroDeVertices() + " vertices, " + g.numeroDeArestas() + " arestas).");

            // -------------------------------------------------------------
            // TESTE ETAPA 5: Componentes Fortemente Conexos (CFC)
            // -------------------------------------------------------------
            System.out.println("\n--- [ETAPA 5] Teste de Componentes Fortemente Conexos (Kosaraju) ---");
            Grafo grafoReduzido = algoritmos.componentesFortementeConexos(g);
            List<List<Vertice>> componentes = algoritmos.getUltimosComponentes();

            System.out.println("Quantidade de componentes encontrados no grafo de teste: " + componentes.size());
            for (int i = 0; i < componentes.size(); i++) {
                System.out.print("  Componente C" + i + ": { ");
                for (Vertice v : componentes.get(i)) {
                    System.out.print(v.id() + " ");
                }
                System.out.println("}");
            }
            System.out.println("Grafo Reduzido:");
            System.out.println("  Numero de supervertices: " + grafoReduzido.numeroDeVertices());
            System.out.println("  Numero de arestas no reduzido: " + grafoReduzido.numeroDeArestas());

            // Teste adicional de CFC com múltiplos componentes distintos:
            // C0: {0, 1} (com 0->1 e 1->0)
            // C1: {2, 3} (com 2->3 e 3->2)
            // Aresta entre componentes: 1 -> 2
            System.out.println("\nTestando CFC com grafo de multiplos componentes...");
            Grafo gMulti = new GrafoListaAdjacencia(4);
            gMulti.adicionarAresta(gMulti.vertices().get(0), gMulti.vertices().get(1));
            gMulti.adicionarAresta(gMulti.vertices().get(1), gMulti.vertices().get(0));
            gMulti.adicionarAresta(gMulti.vertices().get(2), gMulti.vertices().get(3));
            gMulti.adicionarAresta(gMulti.vertices().get(3), gMulti.vertices().get(2));
            gMulti.adicionarAresta(gMulti.vertices().get(1), gMulti.vertices().get(2)); // aresta de ponte

            Grafo redMulti = algoritmos.componentesFortementeConexos(gMulti);
            List<List<Vertice>> compsMulti = algoritmos.getUltimosComponentes();
            System.out.println("Quantidade de componentes no grafo multiplo: " + compsMulti.size() + " (esperado: 2)");
            for (int i = 0; i < compsMulti.size(); i++) {
                System.out.print("  C" + i + ": { ");
                for (Vertice v : compsMulti.get(i)) {
                    System.out.print(v.id() + " ");
                }
                System.out.println("}");
            }
            System.out.println("Grafo Reduzido com multiplos componentes:");
            System.out.println("  Supervertices: " + redMulti.numeroDeVertices() + " (esperado: 2)");
            System.out.println("  Arestas entre supervertices: " + redMulti.numeroDeArestas() + " (esperado: 1)");
            assert compsMulti.size() == 2 : "Deveria ter 2 componentes";
            assert redMulti.numeroDeArestas() == 1 : "Deveria ter 1 aresta conectando C0 e C1";

            // -------------------------------------------------------------
            // TESTE ETAPA 6: Árvore Geradora Mínima (AGM)
            // -------------------------------------------------------------
            System.out.println("\n--- [ETAPA 6] Teste de Arvore Geradora Minima (Kruskal) ---");
            Collection<Aresta> agm = algoritmos.arvoreGeradoraMinima(g);
            double custoTotal = algoritmos.custoDaArvoreGeradora(g, agm);

            System.out.println("Arestas selecionadas para a AGM:");
            for (Aresta a : agm) {
                System.out.println("  " + a.origem().id() + " -- " + a.destino().id() + " (peso: " + a.peso() + ")");
            }
            System.out.println("Custo total da AGM: " + custoTotal);

            // Verificações: para 5 vértices, a AGM deve ter 4 arestas e custo 44.0 (1 + 4 + 7 + 32)
            assert agm.size() == 4 : "AGM para 5 vertices deve ter 4 arestas!";
            assert Math.abs(custoTotal - 44.0) < 0.001 : "Custo total da AGM esperado: 44.0!";

            System.out.println("\n=== Todos os testes das Etapas 5 e 6 passaram com sucesso! ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
