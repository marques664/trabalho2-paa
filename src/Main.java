import algoritmos.AlgoritmosEmGrafosImpl;
import grafos.Aresta;
import grafos.Grafo;
import grafos.TipoDeRepresentacao;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AlgoritmosEmGrafosImpl algoritmos = new AlgoritmosEmGrafosImpl();
    private static Grafo grafoAtual;
    private static TipoDeRepresentacao tipoAtual;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Trabalho Pratico 2: Teoria dos Grafos ===");

        carregarGrafo();

        while (true) {
            System.out.println("\n---------------- MENU ----------------");
            System.out.println("Grafo: " + grafoAtual.numeroDeVertices() + " vertices, " +
                               grafoAtual.numeroDeArestas() + " arestas (" + tipoAtual + ")");
            System.out.println("1 - Busca em Profundidade (DFS)");
            System.out.println("2 - Busca em Largura (BFS)");
            System.out.println("3 - Existe Ciclo?");
            System.out.println("4 - Componentes Fortemente Conexos (CFC)");
            System.out.println("5 - Arvore Geradora Minima (AGM)");
            System.out.println("6 - Caminho Minimo (Dijkstra)");
            System.out.println("7 - Fluxo Maximo (Ford-Fulkerson)");
            System.out.println("8 - Carregar outro arquivo ou representacao");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            String opcao = scanner.nextLine().trim();

            if ("0".equals(opcao)) {
                System.out.println("Fim do programa.");
                break;
            } else if ("1".equals(opcao)) {
                executarDFS();
            } else if ("2".equals(opcao)) {
                executarBFS();
            } else if ("3".equals(opcao)) {
                executarCiclo();
            } else if ("4".equals(opcao)) {
                executarCFC();
            } else if ("5".equals(opcao)) {
                executarAGM();
            } else if ("6".equals(opcao)) {
                executarCaminhoMinimo();
            } else if ("7".equals(opcao)) {
                executarFluxoMaximo();
            } else if ("8".equals(opcao)) {
                carregarGrafo();
            } else {
                System.out.println("Opcao invalida!");
            }
        }
    }

    private static void carregarGrafo() {
        while (true) {
            System.out.print("\nCaminho do arquivo (ENTER para 'docs/Teste.txt'): ");
            String path = scanner.nextLine().trim();
            if (path.isEmpty()) {
                path = "docs/Teste.txt";
            }

            System.out.println("Escolha a representacao:");
            System.out.println("1 - Matriz de Adjacencia");
            System.out.println("2 - Matriz de Incidencia");
            System.out.println("3 - Lista de Adjacencia");
            System.out.print("Opcao: ");
            String escolha = scanner.nextLine().trim();

            TipoDeRepresentacao tipo = TipoDeRepresentacao.LISTA_DE_ADJACENCIA;
            if ("1".equals(escolha)) {
                tipo = TipoDeRepresentacao.MATRIZ_DE_ADJACENCIA;
            } else if ("2".equals(escolha)) {
                tipo = TipoDeRepresentacao.MATRIZ_DE_INCIDENCIA;
            }

            try {
                grafoAtual = algoritmos.carregarGrafo(path, tipo);
                tipoAtual = tipo;
                System.out.println("Grafo carregado com sucesso!");
                return;
            } catch (Exception e) {
                System.out.println("Erro ao carregar: " + e.getMessage());
            }
        }
    }

    private static void executarDFS() {
        System.out.print("\nVertice inicial (ENTER para 0): ");
        int inicioId = lerVertice(0);
        Vertice vInicio = grafoAtual.vertices().get(inicioId);

        algoritmos.buscaEmProfundidade(grafoAtual, vInicio);

        int[] d = algoritmos.getD_DFS();
        int[] f = algoritmos.getF_DFS();
        Vertice[] pi = algoritmos.getPi_DFS();

        System.out.println("\nTempos de Descoberta (d), Termino (f) e Antecessor (pi):");
        for (Vertice v : grafoAtual.vertices()) {
            String pai = (pi[v.id()] != null) ? String.valueOf(pi[v.id()].id()) : "null";
            System.out.println("Vertice " + v.id() + " -> d: " + d[v.id()] + ", f: " + f[v.id()] + ", pai: " + pai);
        }

        System.out.println("\nClassificacao das arestas:");
        System.out.println("  Arvore:     " + formatarArestas(algoritmos.arestasDeArvore(grafoAtual)));
        System.out.println("  Retorno:    " + formatarArestas(algoritmos.arestasDeRetorno(grafoAtual)));
        System.out.println("  Avanco:     " + formatarArestas(algoritmos.arestasDeAvanco(grafoAtual)));
        System.out.println("  Cruzamento: " + formatarArestas(algoritmos.arestasDeCruzamento(grafoAtual)));
    }

    private static void executarBFS() {
        System.out.print("\nVertice inicial (ENTER para 0): ");
        int inicioId = lerVertice(0);
        Vertice vInicio = grafoAtual.vertices().get(inicioId);

        Collection<Aresta> arvore = algoritmos.buscaEmLargura(grafoAtual, vInicio);
        int[] d = algoritmos.getD_BFS();
        Vertice[] pi = algoritmos.getPi_BFS();

        System.out.println("\nDistancia (d) e Antecessor (pi):");
        for (Vertice v : grafoAtual.vertices()) {
            String dist = (d[v.id()] != -1) ? String.valueOf(d[v.id()]) : "inalcancavel";
            String pai = (pi[v.id()] != null) ? String.valueOf(pi[v.id()].id()) : "null";
            System.out.println("Vertice " + v.id() + " -> d: " + dist + ", pai: " + pai);
        }
        System.out.println("Arestas da arvore BFS: " + formatarArestas(arvore));
    }

    private static void executarCiclo() {
        boolean ciclo = algoritmos.existeCiclo(grafoAtual);
        System.out.println("\nExiste ciclo no grafo? " + (ciclo ? "VERDADEIRO" : "FALSO"));
        if (ciclo) {
            System.out.println("Arestas de retorno que fecham ciclo: " + formatarArestas(algoritmos.arestasDeRetorno(grafoAtual)));
        }
    }

    private static void executarCFC() throws Exception {
        Grafo reduzido = algoritmos.componentesFortementeConexos(grafoAtual);
        List<List<Vertice>> componentes = algoritmos.getUltimosComponentes();

        System.out.println("\nTotal de componentes encontrados: " + componentes.size());
        for (int i = 0; i < componentes.size(); i++) {
            System.out.print("Componente " + i + ": { ");
            for (Vertice v : componentes.get(i)) {
                System.out.print(v.id() + " ");
            }
            System.out.println("}");
        }

        System.out.println("\nGrafo Reduzido:");
        System.out.println("Vertices no reduzido: " + reduzido.numeroDeVertices());
        System.out.println("Arestas no reduzido: " + reduzido.numeroDeArestas());
        for (Vertice u : reduzido.vertices()) {
            ArrayList<Vertice> vizinhos = reduzido.adjacentesDe(u);
            if (!vizinhos.isEmpty()) {
                System.out.print("C" + u.id() + " aponta para -> ");
                for (Vertice v : vizinhos) {
                    System.out.print("C" + v.id() + " ");
                }
                System.out.println();
            }
        }
    }

    private static void executarAGM() throws Exception {
        Collection<Aresta> agm = algoritmos.arvoreGeradoraMinima(grafoAtual);
        double custo = algoritmos.custoDaArvoreGeradora(grafoAtual, agm);

        System.out.println("\nArestas da Arvore Geradora Minima:");
        for (Aresta a : agm) {
            System.out.println("  " + a.origem().id() + " - " + a.destino().id() + " (peso " + a.peso() + ")");
        }
        System.out.println("Custo total: " + custo);
    }

    private static void executarCaminhoMinimo() throws Exception {
        System.out.print("\nOrigem (s): ");
        int s = lerVertice(0);
        System.out.print("Destino (t): ");
        int t = lerVertice(grafoAtual.numeroDeVertices() - 1);

        Vertice vOrigem = grafoAtual.vertices().get(s);
        Vertice vDestino = grafoAtual.vertices().get(t);

        ArrayList<Aresta> caminho = algoritmos.caminhoMinimo(grafoAtual, vOrigem, vDestino);
        double custo = algoritmos.custoDoCaminhoMinimo(grafoAtual, caminho, vOrigem, vDestino);

        if (caminho.isEmpty() && s != t) {
            System.out.println("Nao existe caminho entre " + s + " e " + t);
        } else {
            System.out.println("Arestas do caminho minimo:");
            for (Aresta a : caminho) {
                System.out.println("  " + a.origem().id() + " -> " + a.destino().id() + " (peso " + a.peso() + ")");
            }
            System.out.println("Custo total: " + custo);
        }
    }

    private static void executarFluxoMaximo() {
        System.out.print("\nFonte s (ENTER para 0): ");
        int s = lerVertice(0);
        System.out.print("Sumidouro t (ENTER para " + (grafoAtual.numeroDeVertices() - 1) + "): ");
        int t = lerVertice(grafoAtual.numeroDeVertices() - 1);

        Vertice vFonte = grafoAtual.vertices().get(s);
        Vertice vSumidouro = grafoAtual.vertices().get(t);

        double fluxo = algoritmos.fluxoMaximo(grafoAtual, vFonte, vSumidouro);
        System.out.println("Fluxo Maximo de " + s + " para " + t + ": " + fluxo);
    }

    private static int lerVertice(int padrao) {
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return padrao;
        }
        try {
            int v = Integer.parseInt(input);
            if (v >= 0 && v < grafoAtual.numeroDeVertices()) {
                return v;
            }
        } catch (Exception ignored) {}
        return padrao;
    }

    private static String formatarArestas(Collection<Aresta> arestas) {
        if (arestas == null || arestas.isEmpty()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder("[ ");
        for (Aresta a : arestas) {
            sb.append("(").append(a.origem().id()).append("->").append(a.destino().id()).append(") ");
        }
        sb.append("]");
        return sb.toString();
    }
}
