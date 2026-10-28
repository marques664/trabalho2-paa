package algoritmos;

import grafos.Grafo;

/**
 * Algoritmo para detecção de ciclo em grafo orientado.
 * Baseia-se na identificação de arestas de retorno na DFS (Cormen).
 */
public class DeteccaoCiclos {

    public boolean existeCiclo(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            return false;
        }
        BuscaEmProfundidade dfs = new BuscaEmProfundidade();
        dfs.executar(g, g.vertices().get(0));
        return !dfs.arestasDeRetorno().isEmpty();
    }
}
