package algoritmos;

import grafos.Grafo;

// Deteccao de Ciclos em Digrafos
// Um digrafo contem ciclo se e somente se sua DFS encontrar ao menos uma aresta de retorno
public class DeteccaoCiclos {

    // Retorna true se houver ciclo no grafo ou false se for aciclico (DAG)
    public boolean existeCiclo(Grafo g) {
        if (g == null || g.numeroDeVertices() == 0) {
            return false;
        }
        BuscaEmProfundidade dfs = new BuscaEmProfundidade();
        dfs.executar(g, g.vertices().get(0));
        return !dfs.arestasDeRetorno().isEmpty();
    }
}
