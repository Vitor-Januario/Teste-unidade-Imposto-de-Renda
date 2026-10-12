/**
 * Classificação da renda mensal nas faixas da Tabela do IR Anual (Figura 2a).
 *
 * A Figura 2a informa, para cada faixa de rendimento mensal, o desconto de IR e a economia
 * anual estimada. O programa devolve a faixa aplicável; não calcula o valor do imposto,
 * pois a fórmula não faz parte da especificação.
 *
 * A numeração dos comentários corresponde aos nós do GFC do método classificarValido.
 */
public final class CalculadoraIR {

    private CalculadoraIR() { }

    /** Classe C1 (texto): entrada digitada pelo usuário, aceita "5250.00" ou "5250,00" (sem separador de milhar). */
    public static FaixaIR classificarTexto(String entrada) {
        if (entrada == null || entrada.isBlank()) {
            throw new IllegalArgumentException("Renda inválida: entrada vazia.");
        }
        try {
            return classificar(Double.parseDouble(entrada.trim().replace(',', '.')));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Renda inválida: entrada não numérica.");
        }
    }

    /** Valida as classes inválidas (C1: NaN/infinito; C2: renda negativa) e classifica a renda. */
    public static FaixaIR classificar(double rendaMensal) {
        if (Double.isNaN(rendaMensal) || Double.isInfinite(rendaMensal) || rendaMensal < 0.0) {
            throw new IllegalArgumentException("Renda inválida: deve ser um número finito e não negativo.");
        }
        return classificarValido(rendaMensal);
    }

    /** Método representado no GFC (blocos 1 a 12). */
    static FaixaIR classificarValido(double rendaMensal) {
        FaixaIR faixa;                                   // 1
        if (rendaMensal <= 5000.00) {                    // 1
            faixa = FaixaIR.ISENCAO_TOTAL;               // 2
        } else if (rendaMensal <= 5500.00) {             // 3
            faixa = FaixaIR.DESCONTO_75;                 // 4
        } else if (rendaMensal <= 6000.00) {             // 5
            faixa = FaixaIR.DESCONTO_50;                 // 6
        } else if (rendaMensal <= 6500.00) {             // 7
            faixa = FaixaIR.DESCONTO_25;                 // 8
        } else if (rendaMensal <= 7350.00) {             // 9
            faixa = FaixaIR.DESCONTO_10;                 // 10
        } else {
            faixa = FaixaIR.ALIQUOTA_INTEGRAL;           // 11
        }
        return faixa;                                    // 12
    }
}
