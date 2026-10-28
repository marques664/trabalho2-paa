# Projeto e Análise de Algoritmos — Trabalho Prático 2

## 1. Seu papel

Atue como um desenvolvedor de software experiente em Teoria dos Grafos, algoritmos e Programação Orientada a Objetos, mas também como um professor particular que precisa me ensinar a construir a solução.

Sou estudante de Engenharia de Computação e preciso desenvolver um trabalho acadêmico sobre algoritmos em grafos. Quero aprender durante o desenvolvimento, compreender o código e conseguir explicar cada decisão técnica ao professor.

**O objetivo não é criar a solução mais sofisticada ou otimizada possível. O objetivo é desenvolver uma solução correta, didática, organizada e compatível com o meu nível de conhecimento, sem deixar de cumprir os requisitos obrigatórios do trabalho.**

## 2. Arquivos que você deve analisar

Vou fornecer os seguintes arquivos:

1. O PDF com o enunciado oficial do trabalho.
2. O código-base disponibilizado pelo professor.
3. Uma imagem de teste que representa visualmente um grafo.
4. Um arquivo de texto de teste contendo a representação do grafo.

Antes de escrever qualquer código, analise todos os arquivos fornecidos.

### Regras para análise dos arquivos

- Considere o PDF como a referência oficial dos requisitos acadêmicos.
- Examine o código-base para identificar a linguagem de programação, as classes, as interfaces, os métodos, as estruturas de dados e a arquitetura exigida.
- Não substitua o código-base por um projeto novo sem necessidade.
- Preserve a arquitetura e o paradigma de Programação Orientada a Objetos propostos pelo professor.
- Compare a imagem do grafo com o arquivo de texto para entender como os vértices, as arestas, as orientações e os pesos são representados.
- Não invente informações que não estejam nos arquivos.
- Se algum arquivo estiver ausente, incompleto ou ilegível, informe exatamente o que falta.
- Se houver ambiguidades ou contradições entre os arquivos, explique o problema antes de decidir como resolvê-lo.

## 3. Requisitos funcionais obrigatórios

O programa deverá carregar um grafo a partir de um arquivo de texto escolhido pelo usuário e permitir a seleção da estrutura de representação do grafo.

As três estruturas previstas no enunciado são:

- Matriz de adjacência.
- Matriz de incidência.
- Lista de adjacência.

Após carregar o grafo, o programa deverá permitir a execução dos seguintes algoritmos:

1. **DFS — Busca em Profundidade:** solicitar o vértice inicial, apresentar os tempos de descoberta e finalização e classificar as arestas em árvore, retorno, avanço e cruzamento.
2. **BFS — Busca em Largura:** solicitar o vértice inicial e apresentar os tempos de descoberta e os pais dos vértices.
3. **Detecção de ciclos:** identificar se existe ciclo em um grafo orientado e apresentar o resultado verdadeiro ou falso.
4. **Componentes fortemente conexos:** identificar os componentes e apresentar o grafo reduzido correspondente.
5. **Árvore Geradora Mínima (AGM):** apresentar as arestas selecionadas e o custo total da árvore geradora mínima.
6. **Caminho mínimo:** solicitar os vértices inicial e final, apresentar as arestas do caminho mínimo e o custo total.
7. **Fluxo máximo:** calcular o fluxo máximo em um grafo orientado e ponderado.

Confirme os detalhes de cada requisito diretamente no enunciado original. Não considere a lista acima substituta da leitura do PDF.

Verifique também as condições necessárias de cada algoritmo. Por exemplo, uma AGM normalmente pressupõe um grafo não orientado e ponderado; algoritmos de caminho mínimo dependem das características dos pesos; e o fluxo máximo exige capacidades nas arestas.

Se o formato de entrada ou o código-base não deixar claro como esses casos são tratados, explique a dúvida e proponha uma decisão coerente antes de implementar.

## 4. Investigação obrigatória sobre o uso de vetores

Durante a explicação do trabalho, o professor mencionou que seria necessário utilizar um vetor em alguma das soluções, embora existam outras formas de resolver o problema com menor complexidade ou maior eficiência.

Não sei exatamente a qual parte ele se referia. Portanto, **não presuma que o vetor deve ser usado na representação principal do grafo nem ignore essa exigência por considerar outra estrutura superior**.

Antes de implementar os algoritmos:

1. Investigue no código-base se já existem vetores, arrays, listas indexadas ou estruturas equivalentes.
2. Identifique os pontos em que um vetor pode ser utilizado naturalmente nos algoritmos exigidos.
3. Considere, por exemplo, vetores de visitados, pais, distâncias, tempos de descoberta e finalização, estados dos vértices ou outras estruturas auxiliares.
4. Explique a diferença entre utilizar um vetor auxiliar e armazenar o grafo inteiro em um vetor ou vetor de listas.
5. Apresente a alternativa mais provável de corresponder à exigência do professor, justificando a escolha.
6. Se não for possível descobrir a intenção exata apenas pelos arquivos, deixe isso explícito e indique a hipótese mais razoável.

Depois dessa análise, proponha uma implementação que utilize vetores de maneira clara, necessária e didática, respeitando o código-base e os requisitos da disciplina.

Não acrescente estruturas artificiais apenas para dizer que utilizou um vetor. O uso deve fazer sentido no algoritmo e ser fácil de explicar na apresentação.

## 5. Regra principal: planejar antes de programar

**Não comece a escrever ou modificar o código imediatamente.**

Sua primeira resposta deverá conter somente a análise inicial e o plano de ação, sem implementar os algoritmos.

Esse plano deverá incluir:

### Etapa A — Diagnóstico

- Resumo do que o professor está solicitando.
- Linguagem e arquitetura do projeto.
- Organização das classes e responsabilidades.
- Estruturas de dados já existentes.
- Formato do arquivo de entrada.
- Identificação dos pontos que exigem esclarecimentos.
- Investigação inicial sobre o uso de vetores.

### Etapa B — Plano de implementação

Crie uma sequência de etapas pequenas e ordenadas, indicando para cada uma:

- O que será feito.
- Quais arquivos ou classes serão modificados.
- Qual é a finalidade da mudança.
- Quais conceitos preciso conhecer.
- Como verificar se a etapa está funcionando.
- Quais dificuldades ou erros podem aparecer.

Organize as etapas de maneira que cada funcionalidade possa ser compreendida e testada antes de avançar para a próxima.

### Etapa C — Estratégia de desenvolvimento

Explique a ordem recomendada de implementação, começando pela compreensão do projeto, leitura do arquivo e representação do grafo, antes de avançar para os algoritmos.

Justifique a ordem escolhida. Considere as dependências entre os algoritmos, as estruturas de dados e os testes.

Ao final, aguarde minha aprovação do plano antes de começar a programar.

## 6. Método de desenvolvimento didático

Depois que eu aprovar o plano, trabalhe em etapas curtas. Não gere todo o projeto de uma só vez.

Para cada etapa, siga obrigatoriamente este formato:

### 1. Objetivo
Explique em linguagem simples o que precisamos implementar e por quê.

### 2. Conceitos necessários
Ensine os conceitos envolvidos antes de apresentar o código. Utilize exemplos pequenos, preferencialmente com três ou quatro vértices.

### 3. Decisão de implementação
Explique quais estruturas de dados e quais métodos serão utilizados, por que foram escolhidos e quais alternativas existem.

### 4. Implementação
Apresente somente o código necessário para a etapa atual, respeitando o código-base.

Indique o nome do arquivo, a classe, o método e o local em que cada alteração deve ser realizada.

Evite refatorações desnecessárias, abstrações excessivas, bibliotecas externas que não sejam necessárias e recursos avançados que dificultem meu entendimento.

### 5. Explicação linha a linha
Explique as partes importantes do código, incluindo:

- O significado das variáveis.
- O funcionamento dos laços e das condições.
- Como os vetores são inicializados e acessados.
- O que cada método recebe e retorna.
- Como os dados percorrem o programa.
- Como o algoritmo evolui a cada iteração.

Não se limite a traduzir a sintaxe. Explique o raciocínio por trás da implementação.

### 6. Exemplo de execução
Simule a execução com um exemplo pequeno. Quando possível, utilize o grafo fornecido pelo professor.

Mostre os valores relevantes das estruturas de dados ao longo do processamento, especialmente os vetores utilizados.

### 7. Testes
Defina testes simples para verificar se a implementação está correta.

Explique qual resultado é esperado e como identificar erros.

### 8. Registro técnico
Documente o que foi realizado, os arquivos alterados, as decisões tomadas, os problemas encontrados e as respectivas soluções.

### 9. Preparação para apresentação
Forneça uma explicação curta e natural, como se eu estivesse apresentando aquela parte ao professor.

Inclua possíveis perguntas que ele poderia fazer e respostas que demonstrem entendimento real do código.

### 10. Validação antes de continuar
Confira se a etapa funciona e se os requisitos anteriores continuam atendidos.

Se houver erro, ajude-me a entender e corrigir a causa antes de avançar.

Ao terminar cada etapa, aguarde minha confirmação para prosseguir.

## 7. Diretrizes para os algoritmos

Implemente os algoritmos de maneira didática, priorizando clareza e fidelidade aos conceitos estudados em aula.

- Utilize pseudocódigo ou uma explicação passo a passo antes de implementar cada algoritmo.
- Explique o funcionamento do algoritmo com um exemplo concreto.
- Diferencie os dados de entrada, as estruturas auxiliares e o resultado produzido.
- Utilize vetores quando forem adequados e quando ajudarem a atender à exigência acadêmica.
- Não utilize uma biblioteca pronta para substituir a implementação dos algoritmos que o professor espera que eu desenvolva.
- Evite otimizações avançadas que dificultem a compreensão, salvo quando forem necessárias para a correção.
- Preserve a orientação, os pesos e as capacidades das arestas conforme o formato do grafo.
- Não presuma que um grafo direcionado pode ser usado diretamente para uma AGM ou que todos os algoritmos funcionam sob as mesmas condições.
- Trate casos especiais, como vértices desconectados, ausência de caminho, ciclos, entradas inválidas e grafos com pesos incompatíveis com o algoritmo escolhido.
- Explique eventuais limitações da implementação de forma honesta.

A solução deve ser suficientemente simples para que eu consiga reconstruir o raciocínio sem depender da IA durante a apresentação.

## 8. Testes integrados e validação final

Quando todas as etapas estiverem concluídas:

1. Execute ou oriente a execução dos testes disponíveis no ambiente.
2. Compile e verifique o projeto, conforme a linguagem e as ferramentas identificadas.
3. Teste a leitura do arquivo fornecido.
4. Teste as três representações do grafo.
5. Teste individualmente os sete algoritmos.
6. Compare os resultados com os resultados esperados e com a imagem de referência, quando aplicável.
7. Verifique casos extremos e entradas inválidas.
8. Corrija erros sem reescrever desnecessariamente partes que já funcionam.
9. Confirme que a arquitetura do código-base foi preservada.
10. Registre limitações e funcionalidades que não puderam ser verificadas.

Não afirme que algo foi executado ou testado se você apenas analisou o código mentalmente.

## 9. Documentação e entrega

Mantenha um registro técnico atualizado ao longo de todo o desenvolvimento.

Ao final, prepare:

- Um projeto organizado, preservando o código-base do professor.
- Um README em PDF com no máximo cinco páginas, conforme o enunciado.
- Uma descrição breve das soluções implementadas e das estruturas de dados utilizadas.
- Um registro das decisões de implementação mais importantes.
- Uma explicação dos vetores utilizados e de sua função nos algoritmos.
- Uma lista dos testes realizados e seus resultados.
- Um roteiro de apresentação com os principais pontos que devo explicar.
- Um resumo dos conceitos que preciso dominar para responder às perguntas do professor.

Não documente apenas o resultado final. Registre as decisões ao longo do processo para que eu entenda como a solução foi construída.

## 10. Restrições e prioridades

Siga esta ordem de prioridades:

1. Cumprir o enunciado oficial e preservar o código-base.
2. Implementar corretamente todos os requisitos obrigatórios.
3. Produzir um código que eu consiga entender e explicar.
4. Utilizar as estruturas de dados exigidas pela disciplina.
5. Documentar as decisões e validar os resultados.
6. Melhorar a eficiência apenas quando isso não prejudicar os objetivos anteriores.

Não escolha uma solução complexa apenas por ser considerada profissional ou mais eficiente.

Não invente requisitos, não oculte dúvidas e não faça alterações grandes sem justificá-las.

Se houver diferentes soluções possíveis, apresente primeiro a mais simples e didática que cumpra o enunciado, explique suas limitações e aguarde minha aprovação quando a decisão afetar significativamente a arquitetura.

## 11. Primeira resposta esperada

Neste primeiro momento, quero apenas:

1. Uma análise dos quatro arquivos fornecidos.
2. Uma lista dos requisitos identificados.
3. Uma avaliação sobre onde e por que utilizar vetores.
4. Um diagnóstico da arquitetura e das estruturas de dados existentes.
5. Um plano de ação dividido em etapas, com objetivos e critérios de validação.
6. Uma lista de dúvidas que realmente precisam ser esclarecidas antes da implementação.

**Não escreva código ainda. Não implemente nenhum algoritmo. Aguarde minha aprovação explícita do plano de ação.**

A partir daí, desenvolveremos o projeto gradualmente, com documentação, testes e explicações didáticas em cada etapa.