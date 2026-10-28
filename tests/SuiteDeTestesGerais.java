package tests;

import algoritmos.AlgoritmosEmGrafosImpl;
import grafos.Aresta;
import grafos.Grafo;
import grafos.TipoDeRepresentacao;
import grafos.Vertice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SuiteDeTestesGerais {

    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("   SUÍTE DE TESTES AUTOMATIZADOS COM MÚLTIPLOS GRAFOS       ");
        System.out.println("============================================================");

        AlgoritmosEmGrafosImpl alg = new AlgoritmosEmGrafosImpl();
        int testesPassados = 0;
        int totalTestes = 0;

        try {
            // -------------------------------------------------------------
            // CASO 1: Grafo Acíclico Dirigido (DAG)
            // -------------------------------------------------------------
            System.out.println("\n[Teste 1] Grafo Acíclico (01_dag_aciclico.txt)...");
            totalTestes++;
            Grafo dag = alg.carregarGrafo("tests/01_dag_aciclico.txt", TipoDeRepresentacao.LISTA_DE_ADJACENCIA);
            boolean cicloDag = alg.existeCiclo(dag);
            System.out.println("  Possui ciclo? " + cicloDag + " (esperado: false)");
            assert !cicloDag : "DAG não deveria ter ciclo";

            ArrayList<Aresta> caminho0_5 = alg.caminhoMinimo(dag, dag.vertices().get(0), dag.vertices().get(5));
            double custo0_5 = alg.custoDoCaminhoMinimo(dag, caminho0_5, dag.vertices().get(0), dag.vertices().get(5));
            System.out.println("  Caminho mínimo 0 -> 5: custo = " + custo0_5 + " (esperado: 27.0 via 0->1->3->5)");
            assert Math.abs(custo0_5 - 27.0) < 0.001 : "Custo 0->5 deveria ser 27.0";
            testesPassados++;

            // -------------------------------------------------------------
            // CASO 2: Múltiplos Componentes Fortemente Conexos (CFC)
            // -------------------------------------------------------------
            System.out.println("\n[Teste 2] Múltiplos CFCs (02_multiplos_cfc.txt)...");
            totalTestes++;
            Grafo cfcGrafo = alg.carregarGrafo("tests/02_multiplos_cfc.txt", TipoDeRepresentacao.LISTA_DE_ADJACENCIA);
            Grafo reduzido = alg.componentesFortementeConexos(cfcGrafo);
            List<List<Vertice>> componentes = alg.getUltimosComponentes();
            System.out.println("  Total de componentes: " + componentes.size() + " (esperado: 3)");
            System.out.println("  Supervértices no reduzido: " + reduzido.numeroDeVertices() + " (esperado: 3)");
            System.out.println("  Arestas entre componentes: " + reduzido.numeroDeArestas() + " (esperado: 2)");
            assert componentes.size() == 3 : "Deveria ter 3 componentes";
            assert reduzido.numeroDeVertices() == 3 : "Grafo reduzido deveria ter 3 supervértices";
            assert reduzido.numeroDeArestas() == 2 : "Grafo reduzido deveria ter 2 arestas";
            testesPassados++;

            // -------------------------------------------------------------
            // CASO 3: Grafo Desconexo com Vértice Isolado
            // -------------------------------------------------------------
            System.out.println("\n[Teste 3] Grafo Desconexo (03_desconexo.txt)...");
            totalTestes++;
            Grafo desc = alg.carregarGrafo("tests/03_desconexo.txt", TipoDeRepresentacao.LISTA_DE_ADJACENCIA);
            alg.buscaEmLargura(desc, desc.vertices().get(0));
            int[] d_bfs = alg.getD_BFS();
            System.out.println("  Distância até vértice 6 isolado a partir de 0: " + d_bfs[6] + " (esperado: -1 inalcançável)");
            assert d_bfs[6] == -1 : "Vértice 6 deveria ser inalcançável (-1)";
            testesPassados++;

            // -------------------------------------------------------------
            // CASO 4: Rede de Fluxo Clássica de Cormen
            // -------------------------------------------------------------
            System.out.println("\n[Teste 4] Rede de Fluxo de Cormen (04_rede_fluxo.txt)...");
            totalTestes++;
            Grafo fluxoGrafo = alg.carregarGrafo("tests/04_rede_fluxo.txt", TipoDeRepresentacao.LISTA_DE_ADJACENCIA);
            double fluxoMax = alg.fluxoMaximo(fluxoGrafo, fluxoGrafo.vertices().get(0), fluxoGrafo.vertices().get(5));
            System.out.println("  Fluxo Máximo 0 -> 5: " + fluxoMax + " (esperado: 23.0)");
            assert Math.abs(fluxoMax - 23.0) < 0.001 : "Fluxo máximo deveria ser 23.0";
            testesPassados++;

            // -------------------------------------------------------------
            // CASO 5: Grafo Denso e Árvore Geradora Mínima (AGM)
            // -------------------------------------------------------------
            System.out.println("\n[Teste 5] Grafo Denso e AGM (05_grafo_denso.txt)...");
            totalTestes++;
            Grafo denso = alg.carregarGrafo("tests/05_grafo_denso.txt", TipoDeRepresentacao.LISTA_DE_ADJACENCIA);
            Collection<Aresta> agm = alg.arvoreGeradoraMinima(denso);
            double custoAgm = alg.custoDaArvoreGeradora(denso, agm);
            System.out.println("  Arestas na AGM: " + agm.size() + " (esperado: 4 para 5 vértices)");
            System.out.println("  Custo total da AGM: " + custoAgm + " (esperado: 6.0)");
            assert agm.size() == 4 : "AGM para 5 vértices deve ter 4 arestas";
            assert Math.abs(custoAgm - 6.0) < 0.001 : "Custo da AGM deveria ser 6.0";
            testesPassados++;

            System.out.println("\n------------------------------------------------------------");
            System.out.println("RESULTADO: " + testesPassados + " de " + totalTestes + " testes executados com 100% de sucesso!");
            System.out.println("------------------------------------------------------------");

        } catch (Exception e) {
            System.err.println("Falha na execução dos testes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
