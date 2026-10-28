package grafos;

import algoritmos.AlgoritmosEmGrafosImpl;
import java.util.ArrayList;

public class TesteEtapas1e2 {
    public static void main(String[] args) {
        System.out.println("=== Testando Etapas 1 e 2 ===");
        AlgoritmosEmGrafosImpl algoritmos = new AlgoritmosEmGrafosImpl();
        String caminhoArquivo = "docs/Teste.txt";

        TipoDeRepresentacao[] tipos = {
            TipoDeRepresentacao.MATRIZ_DE_ADJACENCIA,
            TipoDeRepresentacao.MATRIZ_DE_INCIDENCIA,
            TipoDeRepresentacao.LISTA_DE_ADJACENCIA
        };

        for (TipoDeRepresentacao tipo : tipos) {
            System.out.println("\n-------------------------------------------");
            System.out.println("Testando representação: " + tipo);
            try {
                Grafo g = algoritmos.carregarGrafo(caminhoArquivo, tipo);
                System.out.println("Grafo carregado com sucesso!");
                System.out.println("Número de vértices: " + g.numeroDeVertices() + " (esperado: 5)");
                System.out.println("Número de arestas: " + g.numeroDeArestas() + " (esperado: 10)");

                if (g.numeroDeVertices() != 5 || g.numeroDeArestas() != 10) {
                    System.err.println("ERRO: Contagem de vértices ou arestas incorreta!");
                }

                // Imprime a lista de adjacências de cada vértice
                for (Vertice v : g.vertices()) {
                    ArrayList<Vertice> adj = g.adjacentesDe(v);
                    System.out.print("Vértice " + v.id() + " (grau " + g.grauDoVertice(v) + ") -> ");
                    for (Vertice vizinho : adj) {
                        ArrayList<Aresta> arestas = g.arestasEntre(v, vizinho);
                        double peso = arestas.isEmpty() ? -1 : arestas.get(0).peso();
                        System.out.print(vizinho.id() + "(peso " + peso + ") ");
                    }
                    System.out.println();
                }

                // Testes de integridade específicos baseados em imagem_teste.jpg
                assert g.existeAresta(g.vertices().get(0), g.vertices().get(0)) : "Laço no 0 falhou";
                assert g.existeAresta(g.vertices().get(0), g.vertices().get(1)) : "Aresta 0->1 falhou";
                assert g.existeAresta(g.vertices().get(2), g.vertices().get(3)) : "Aresta 2->3 falhou";
                assert !g.existeAresta(g.vertices().get(1), g.vertices().get(0)) : "Aresta inexistente 1->0 acusou existente";

            } catch (Exception e) {
                System.err.println("Erro ao testar " + tipo + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
        System.out.println("\n=== Todos os testes das Etapas 1 e 2 executados com sucesso! ===");
    }
}
