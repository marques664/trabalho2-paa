# Projeto e Análise de Algoritmos — Trabalho Prático 2
**IFSULDEMINAS — Campus Poços de Caldas**  
**Disciplina:** Projeto e Análise de Algoritmos (PAA) | **Prof.:** Douglas Castilho  
**Tema:** Teoria dos Grafos em Java  

---

## 1. Visão Geral da Arquitetura

O projeto foi organizado de forma modular em dois pacotes principais, separando a modelagem do grafo da execução dos algoritmos e preservando integralmente as interfaces fornecidas pelo professor:

* **`src/grafos/`:** Contém as interfaces base (`Grafo`, `AlgoritmosEmGrafos`), as entidades de domínio (`Vertice`, `Aresta`, `TipoDeRepresentacao`, `FileManager`) e as 3 classes de representação computacional:
  * `GrafoMatrizAdjacencia`: Matriz booleana e de pesos $O(V^2)$.
  * `GrafoMatrizIncidencia`: Matriz de incidência vértices $\times$ arestas $O(V \times A)$.
  * `GrafoListaAdjacencia`: Vetor de listas de adjacência $O(V + A)$.
* **`src/algoritmos/`:** Contém as classes especialistas com responsabilidade única:
  * `BuscaEmProfundidade`: DFS, tempos $d/f$, antecessores $\pi$ e classificação de arestas.
  * `BuscaEmLargura`: BFS, distâncias/níveis $d$ e antecessores $\pi$.
  * `DeteccaoCiclos`: Identificação de ciclos baseada em arestas de retorno.
  * `KosarajuCFC`: Componentes Fortemente Conexos e geração do Grafo Reduzido.
  * `KruskalAGM`: Árvore Geradora Mínima com Union-Find em vetores primitivos.
  * `DijkstraCaminhoMinimo`: Menor caminho e reconstituição do trajeto ordenado.
  * `FordFulkersonFluxoMaximo`: Fluxo máximo em rede residual (Edmonds-Karp).
  * `AlgoritmosEmGrafosImpl`: Fachada (*Facade*) que implementa `AlgoritmosEmGrafos`.
* **`src/Main.java`:** Menu interativo de terminal para carregar grafos e rodar os algoritmos.
* **`tests/`:** Instâncias de teste em `.txt` com casos de borda e piores casos (`01_dag_aciclico.txt`, `02_multiplos_cfc.txt`, `03_desconexo.txt`, `04_rede_fluxo.txt`, `05_grafo_denso.txt`) e a classe de benchmark [TestePerformance.java](file:///C:/faculdade/PAA/Trabalho2/tests/TestePerformance.java).

---

## 2. O Papel dos Vetores no Projeto

O projeto utiliza **vetores primitivos** como espinha dorsal:
1. **Na Representação:** A Lista de Adjacência é um vetor de listas `ArrayList<Aresta>[]` indexado diretamente pelo ID do vértice de origem em $O(1)$. As matrizes de adjacência e incidência são vetores bidimensionais (`boolean[][]`, `double[][]`, `int[][]`).
2. **Nos Algoritmos:**
   * DFS e BFS: vetores indexados por ID (`int[] cor`, `int[] d`, `int[] f`, `Vertice[] pi`).
   * Kruskal: estrutura Union-Find gerenciada pelos vetores `int[] pai` e `int[] rank`.
   * Dijkstra: vetores de distâncias `double[] dist`, visitados `boolean[]` e `Aresta[] arestaPai`.
   * Ford-Fulkerson: matriz residual `double[][] rCap` e vetor de caminho `int[] pai`.

---

## 3. Resumo dos Algoritmos Implementados

| Algoritmo | Método de Resolução | Complexidade | Saída Gerada |
|---|---|---|---|
| **DFS** | Recursão com 3 cores (Branco, Cinza, Preto) | $O(V + A)$ | Tempos $d$ e $f$, antecessores $\pi$ e 4 tipos de arestas (árvore, retorno, avanço, cruzamento). |
| **BFS** | Fila FIFO (*First-In, First-Out*) | $O(V + A)$ | Distância em saltos $d$, antecessores $\pi$ e arestas da árvore BFS. |
| **Ciclo** | Teorema da Aresta de Retorno na DFS | $O(V + A)$ | Booleano (`true` se dígrafo tiver ciclo, `false` se for DAG). |
| **CFC** | Kosaraju-Sharir (duas DFSs + Grafo Transposto) | $O(V + A)$ | Lista de componentes e Grafo Reduzido (Condensação). |
| **AGM** | Kruskal com Union-Find em vetor | $O(A \log A)$ | Arestas da árvore geradora e custo total. |
| **Caminho Mínimo** | Dijkstra clássico com relaxamento de arestas | $O(V^2)$ | Rota ordenada de arestas de $s$ a $t$ e custo acumulado. |
| **Fluxo Máximo** | Ford-Fulkerson com caminhos aumentantes BFS | $O(V \cdot A^2)$ | Valor escalar do fluxo máximo da rede. |

---

## 4. Testes e Análise de Performance e Escalabilidade

### 4.1. Validação com o Grafo de Teste Oficial (`docs/Teste.txt`, $|V|=5$, $|A|=10$)
* **DFS:** 4 arestas de árvore, 6 arestas de retorno (incluindo laços $0 \to 0$ e $1 \to 1$).
* **BFS:** Distâncias $0 \to 0, 1, 2, 3, 4$ em níveis $0, 1, 2, 3, 4$.
* **Ciclo:** `true` (ciclos identificados).
* **CFC:** 1 único componente $\{0, 1, 2, 3, 4\}$; grafo reduzido com 1 nó.
* **AGM:** Arestas $(2-3: 1), (1-2: 4), (3-4: 7), (4-0: 32)$ com custo total **44.0**.
* **Caminho Mínimo (0 para 4):** Rota $0 \to 1 \to 2 \to 3 \to 4$ com custo **62.0**.
* **Fluxo Máximo (0 para 4):** Gargalo na aresta $2 \to 3$ limitando o fluxo a **1.0**.

### 4.2. Estudo Empírico de Performance e Limites de Viabilidade
Executamos testes empíricos em grafos sintéticos com tamanhos variando de $50$ a $5.000$ vértices:

| Tamanho \|V\| | Lista Adj (Carga) | Matriz Inc (Carga) | DFS / BFS | Kosaraju | Kruskal | Ford-Fulkerson |
|---|---|---|---|---|---|---|
| **100** | 0 ms | 19 ms | 1 ms | 0 ms | 13 ms | 1 ms |
| **500** | 2 ms | 1.080 ms | 1 ms | 4 ms | 30 ms | 2 ms |
| **1.000** | 6 ms | 8.586 ms | 0 ms | 10 ms | 62 ms | 59 ms |
| **2.500** | 12 ms | *Inviável (>15s)* | 2 ms | 50 ms | 121 ms | *Lento (>5s)* |
| **5.000** | 25 ms | *Inviável* | 4 ms | 175 ms | 493 ms | *Inviável* |

**Pontos onde o processamento se torna inviável:**
1. **Matriz de Incidência:** Torna-se inviável a partir de **$|V| \ge 1.000$** vértices devido ao custo quadrático de redimensionamento e alocação de colunas por aresta.
2. **Matriz de Adjacência:** Torna-se inviável por estouro de memória (`OutOfMemoryError`) a partir de **$|V| \ge 15.000$** (uma matriz de $15.000 \times 15.000$ exige centenas de megabytes de memória contígua).
3. **Ford-Fulkerson:** Torna-se inviável a partir de **$|V| \ge 2.500$** vértices densamente conectados ($O(V \cdot A^2)$).
4. **Lista de Adjacência com DFS/BFS/Kosaraju:** Mantém excelente viabilidade, executando em menos de 200 ms mesmo para $5.000$ vértices.

---

## 5. Como Executar

**Compilação:**
```powershell
$files = (Get-ChildItem -Path src,tests -Recurse -Filter *.java).FullName
javac -encoding UTF-8 -sourcepath "src;tests" -d bin $files
```

**Interface Interativa:**
```powershell
java -cp bin Main
```

**Execução dos Testes de Performance:**
```powershell
java -cp bin tests.TestePerformance
```
