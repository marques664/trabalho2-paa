package algoritmos;

import grafos.Grafo;

public class DeteccaoCiclos {

    // Um dígrafo tem ciclo se e somente se a DFS encontrar pelo menos uma aresta de retorno
    public boolean existeCiclo(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            return false;
        }
        BuscaEmProfundidade dfs = new BuscaEmProfundidade();
        dfs.executar(g, g.vertices().get(0));
        return !dfs.arestasDeRetorno().isEmpty();
    }
}
