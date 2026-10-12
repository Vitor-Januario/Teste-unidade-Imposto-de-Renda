# Teste de unidade – Classificação do Imposto de Renda (Trabalho 1 de Qualidade de Software)

Implementação em Java da Tabela do IR Anual (Figura 2a) e testes de unidade com JUnit 5.

O programa recebe a renda mensal e devolve a faixa da tabela: o desconto de IR e a economia anual
estimada que a própria Figura 2a informa. Ele não calcula o valor do imposto, porque a fórmula não
faz parte da especificação.

## Estrutura

| Arquivo | Conteúdo |
|---|---|
| `src/main/java/FaixaIR.java` | As 6 faixas da Figura 2a (desconto e economia anual estimada) |
| `src/main/java/CalculadoraIR.java` | `classificarTexto` (entrada em texto), `classificar` (valida classes inválidas) e `classificarValido` (método do GFC, blocos 1 a 12) |
| `src/test/java/CalculadoraIRTest.java` | 51 casos de teste em 5 grupos |

## Testes

| Grupo | O que verifica | Origem |
|---|---|---|
| ClassesValidas | Dados de entrada das classes C3 a C8 | Tabela 2a |
| ValoresLimiteValidos | LIV e LSV de cada classe válida | Tabela 2a |
| SaidaEsperada | Desconto e economia anual de cada faixa | Figura 2a |
| ClassesInvalidas | C1 (texto, vazio, NaN, infinito) e C2 (renda negativa) | Tabela 2a |
| CaminhosGFC | Os 6 caminhos independentes do GFC | Quadro 2a |

## Como executar

Requer Java 17 ou superior e Maven.

```
mvn test
```
