import algoritmos.AlgoritmosEmGrafosImpl;
import grafos.Aresta;
import grafos.Grafo;
import grafos.TipoDeRepresentacao;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Ponto de entrada interativo do programa.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AlgoritmosEmGrafosImpl algoritmos = new AlgoritmosEmGrafosImpl();
    private static Grafo grafoAtual = null;
    private static TipoDeRepresentacao tipoAtual = null;

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("   PROJETO E ANÁLISE DE ALGORITMOS - TRABALHO PRÁTICO 2    ");
        System.out.println("                TEORIA DOS GRAFOS EM JAVA                  ");
        System.out.println("============================================================");

        carregarGrafoInterativo();

        boolean executando = true;
        while (executando) {
            exibirMenu();
            System.out.print("\nEscolha uma opção: ");
            String opcaoStr = scanner.nextLine().trim();

            switch (opcaoStr) {
                case "1":
                    executarDFS();
                    break;
                case "2":
                    executarBFS();
                    break;
                case "3":
                    executarCiclo();
                    break;
                case "4":
                    executarCFC();
                    break;
                case "5":
                    executarAGM();
                    break;
                case "6":
                    executarCaminhoMinimo();
                    break;
                case "7":
                    executarFluxoMaximo();
                    break;
                case "8":
                    carregarGrafoInterativo();
                    break;
                case "0":
                    executando = false;
                    System.out.println("\nEncerrando o programa. Bom trabalho e boa apresentação!");
                    break;
                default:
                    System.out.println("\n[!] Opção inválida. Digite um número de 0 a 8.");
            }

            if (executando) {
                System.out.println("\nPressione ENTER para voltar ao menu...");
                scanner.nextLine();
            }
        }
    }

    private static void carregarGrafoInterativo() {
        boolean carregado = false;
        while (!carregado) {
            System.out.println("\n--- CARREGAMENTO DO GRAFO ---");
            System.out.print("Informe o caminho do arquivo (Pressione ENTER para 'docs/Teste.txt'): ");
            String path = scanner.nextLine().trim();
            if (path.isEmpty()) {
                path = "docs/Teste.txt";
            }

            System.out.println("\nEscolha a Estrutura de Representação:");
            System.out.println("  1 - Matriz de Adjacência");
            System.out.println("  2 - Matriz de Incidência");
            System.out.println("  3 - Lista de Adjacência");
            System.out.print("Opção (1-3): ");
            String escolhaEstrutura = scanner.nextLine().trim();

            TipoDeRepresentacao tipo;
            switch (escolhaEstrutura) {
                case "1":
                    tipo = TipoDeRepresentacao.MATRIZ_DE_ADJACENCIA;
                    break;
                case "2":
                    tipo = TipoDeRepresentacao.MATRIZ_DE_INCIDENCIA;
                    break;
                case "3":
                default:
                    tipo = TipoDeRepresentacao.LISTA_DE_ADJACENCIA;
                    break;
            }

            try {
                grafoAtual = algoritmos.carregarGrafo(path, tipo);
                tipoAtual = tipo;
                carregado = true;
                System.out.println("\n[+] Grafo carregado com sucesso!");
                System.out.println("    Arquivo: " + path);
                System.out.println("    Estrutura: " + tipo);
                System.out.println("    Total de Vértices: " + grafoAtual.numeroDeVertices());
                System.out.println("    Total de Arestas:  " + grafoAtual.numeroDeArestas());
            } catch (Exception e) {
                System.err.println("\n[-] Erro ao carregar o grafo: " + e.getMessage());
                System.out.println("Tente novamente informando um arquivo válido.");
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("\n============================================================");
        System.out.println("                      MENU DE ALGORITMOS                    ");
        System.out.println("  Grafo atual: " + grafoAtual.numeroDeVertices() + " vértices | " +
                           grafoAtual.numeroDeArestas() + " arestas | " + tipoAtual);
        System.out.println("============================================================");
        System.out.println("  1 - Busca em Profundidade (DFS)");
        System.out.println("  2 - Busca em Largura (BFS)");
        System.out.println("  3 - Detecção de Ciclos");
        System.out.println("  4 - Componentes Fortemente Conexos (CFC - Kosaraju)");
        System.out.println("  5 - Árvore Geradora Mínima (AGM - Kruskal)");
        System.out.println("  6 - Caminho Mínimo (Dijkstra)");
        System.out.println("  7 - Fluxo Máximo (Ford-Fulkerson)");
        System.out.println("  8 - Recarregar outro grafo ou estrutura");
        System.out.println("  0 - Sair");
        System.out.println("============================================================");
    }

    private static void executarDFS() {
        System.out.println("\n--- [1] BUSCA EM PROFUNDIDADE (DFS) ---");
        int inicioId = solicitarVertice("Informe o vértice inicial da busca (ex: 0): ", 0);
        Vertice vInicio = grafoAtual.vertices().get(inicioId);

        Collection<Aresta> arvore = algoritmos.buscaEmProfundidade(grafoAtual, vInicio);

        System.out.println("\nTempos de Descoberta (d), Finalização (f) e Antecessor (π):");
        int[] d = algoritmos.getD_DFS();
        int[] f = algoritmos.getF_DFS();
        Vertice[] pi = algoritmos.getPi_DFS();

        for (Vertice v : grafoAtual.vertices()) {
            String paiStr = (pi[v.id()] != null) ? String.valueOf(pi[v.id()].id()) : "null (raiz)";
            System.out.printf("  Vértice %d -> d: %2d | f: %2d | pai (π): %s\n", v.id(), d[v.id()], f[v.id()], paiStr);
        }

        System.out.println("\nClassificação das Arestas:");
        System.out.println("  * Arestas de Árvore:     " + formatarArestas(algoritmos.arestasDeArvore(grafoAtual)));
        System.out.println("  * Arestas de Retorno:    " + formatarArestas(algoritmos.arestasDeRetorno(grafoAtual)));
        System.out.println("  * Arestas de Avanço:     " + formatarArestas(algoritmos.arestasDeAvanco(grafoAtual)));
        System.out.println("  * Arestas de Cruzamento: " + formatarArestas(algoritmos.arestasDeCruzamento(grafoAtual)));
    }

    private static void executarBFS() {
        System.out.println("\n--- [2] BUSCA EM LARGURA (BFS) ---");
        int inicioId = solicitarVertice("Informe o vértice inicial da busca (ex: 0): ", 0);
        Vertice vInicio = grafoAtual.vertices().get(inicioId);

        Collection<Aresta> arvore = algoritmos.buscaEmLargura(grafoAtual, vInicio);

        System.out.println("\nTempos de Descoberta/Distância (d) e Antecessor (π):");
        int[] d = algoritmos.getD_BFS();
        Vertice[] pi = algoritmos.getPi_BFS();

        for (Vertice v : grafoAtual.vertices()) {
            String distStr = (d[v.id()] != -1) ? String.valueOf(d[v.id()]) : "inalcançável";
            String paiStr = (pi[v.id()] != null) ? String.valueOf(pi[v.id()].id()) : "null (raiz)";
            System.out.printf("  Vértice %d -> d: %12s | pai (π): %s\n", v.id(), distStr, paiStr);
        }

        System.out.println("\nArestas da Árvore BFS:");
        System.out.println("  " + formatarArestas(arvore));
    }

    private static void executarCiclo() {
        System.out.println("\n--- [3] DETECÇÃO DE CICLOS ---");
        boolean temCiclo = algoritmos.existeCiclo(grafoAtual);
        if (temCiclo) {
            System.out.println("Resultado: VERDADEIRO (O grafo orientado contém pelo menos um ciclo).");
            System.out.println("Arestas de retorno identificadas que fecham ciclos:");
            System.out.println("  " + formatarArestas(algoritmos.arestasDeRetorno(grafoAtual)));
        } else {
            System.out.println("Resultado: FALSO (O grafo é um Grafo Acíclico Dirigido - DAG).");
        }
    }

    private static void executarCFC() {
        System.out.println("\n--- [4] COMPONENTES FORTEMENTE CONEXOS (KOSARAJU) ---");
        Grafo reduzido = algoritmos.componentesFortementeConexos(grafoAtual);
        List<List<Vertice>> componentes = algoritmos.getUltimosComponentes();

        System.out.println("Total de componentes encontrados: " + componentes.size());
        for (int i = 0; i < componentes.size(); i++) {
            System.out.print("  Componente C" + i + " -> { ");
            for (Vertice v : componentes.get(i)) {
                System.out.print(v.id() + " ");
            }
            System.out.println("}");
        }

        System.out.println("\nGrafo Reduzido (Grafo de Condensação):");
        System.out.println("  Número de supervértices: " + reduzido.numeroDeVertices());
        System.out.println("  Número de arestas entre componentes: " + reduzido.numeroDeArestas());
        try {
            for (Vertice u : reduzido.vertices()) {
                ArrayList<Vertice> vizinhos = reduzido.adjacentesDe(u);
                if (!vizinhos.isEmpty()) {
                    System.out.print("  C" + u.id() + " aponta para -> ");
                    for (Vertice v : vizinhos) {
                        System.out.print("C" + v.id() + " ");
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void executarAGM() {
        System.out.println("\n--- [5] ÁRVORE GERADORA MÍNIMA (KRUSKAL) ---");
        try {
            Collection<Aresta> agm = algoritmos.arvoreGeradoraMinima(grafoAtual);
            double custo = algoritmos.custoDaArvoreGeradora(grafoAtual, agm);

            System.out.println("Arestas selecionadas para a AGM:");
            for (Aresta a : agm) {
                System.out.printf("  (%d - %d) com peso %.2f\n", a.origem().id(), a.destino().id(), a.peso());
            }
            System.out.printf("\nCusto Total da Árvore Geradora Mínima: %.2f\n", custo);
        } catch (Exception e) {
            System.err.println("Erro ao calcular AGM: " + e.getMessage());
        }
    }

    private static void executarCaminhoMinimo() {
        System.out.println("\n--- [6] CAMINHO MÍNIMO (DIJKSTRA) ---");
        int s = solicitarVertice("Informe o vértice de origem (s): ", 0);
        int t = solicitarVertice("Informe o vértice de destino (t): ", grafoAtual.numeroDeVertices() - 1);

        Vertice vOrigem = grafoAtual.vertices().get(s);
        Vertice vDestino = grafoAtual.vertices().get(t);

        try {
            ArrayList<Aresta> caminho = algoritmos.caminhoMinimo(grafoAtual, vOrigem, vDestino);
            double custo = algoritmos.custoDoCaminhoMinimo(grafoAtual, caminho, vOrigem, vDestino);

            if (s == t) {
                System.out.println("Origem e destino são o mesmo vértice. Custo total: 0.0");
                return;
            }

            if (caminho.isEmpty()) {
                System.out.println("Não foi encontrado nenhum caminho entre " + s + " e " + t + ".");
            } else {
                System.out.println("Arestas do caminho mínimo em ordem:");
                for (int i = 0; i < caminho.size(); i++) {
                    Aresta a = caminho.get(i);
                    System.out.printf("  Salto %d: (%d -> %d) [peso: %.2f]\n", i + 1, a.origem().id(), a.destino().id(), a.peso());
                }
                System.out.printf("Custo Total do Caminho Mínimo: %.2f\n", custo);
            }
        } catch (Exception e) {
            System.err.println("Erro ao calcular caminho mínimo: " + e.getMessage());
        }
    }

    private static void executarFluxoMaximo() {
        System.out.println("\n--- [7] FLUXO MÁXIMO (FORD-FULKERSON) ---");
        int s = solicitarVertice("Informe o vértice fonte (s) [padrão: 0]: ", 0);
        int t = solicitarVertice("Informe o vértice sumidouro (t) [padrão: " + (grafoAtual.numeroDeVertices() - 1) + "]: ",
                                 grafoAtual.numeroDeVertices() - 1);

        Vertice vFonte = grafoAtual.vertices().get(s);
        Vertice vSumidouro = grafoAtual.vertices().get(t);

        double fluxo = algoritmos.fluxoMaximo(grafoAtual, vFonte, vSumidouro);
        System.out.printf("Fluxo Máximo da fonte %d ao sumidouro %d: %.2f\n", s, t, fluxo);
    }

    private static int solicitarVertice(String prompt, int defaultVal) {
        int max = grafoAtual.numeroDeVertices() - 1;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                return defaultVal;
            }
            try {
                int val = Integer.parseInt(input);
                if (val >= 0 && val <= max) {
                    return val;
                }
                System.out.println("[!] Vértice inválido. Deve estar entre 0 e " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("[!] Digite um número inteiro válido.");
            }
        }
    }

    private static String formatarArestas(Collection<Aresta> arestas) {
        if (arestas == null || arestas.isEmpty()) {
            return "[] (nenhuma)";
        }
        StringBuilder sb = new StringBuilder("[");
        int count = 0;
        for (Aresta a : arestas) {
            if (count > 0) sb.append(", ");
            sb.append("(").append(a.origem().id()).append(" -> ").append(a.destino().id())
              .append(", peso: ").append(a.peso()).append(")");
            count++;
        }
        sb.append("]");
        return sb.toString();
    }
}
