/**
 * Faixas da Figura 2a (Tabela do IR para ano-base 2026, Fonte: Governo Federal).
 * Cada faixa guarda o desconto de IR e a economia anual estimada informados na tabela.
 */
public enum FaixaIR {

    ISENCAO_TOTAL("Isenção total", 4356.89),
    DESCONTO_75("Desconto de 75%", 3367.68),
    DESCONTO_50("Desconto de 50%", 2378.47),
    DESCONTO_25("Desconto de 25%", 1389.26),
    DESCONTO_10("Desconto de 10%", 978.62),
    ALIQUOTA_INTEGRAL("Aplicação da alíquota de 27,5%", null); // a tabela não informa economia

    private final String descontoIR;
    private final Double economiaAnualEstimada;

    FaixaIR(String descontoIR, Double economiaAnualEstimada) {
        this.descontoIR = descontoIR;
        this.economiaAnualEstimada = economiaAnualEstimada;
    }

    public String getDescontoIR() {
        return descontoIR;
    }

    /** Economia anual estimada da faixa; null na faixa acima de R$ 7.350,00 ("-" na tabela). */
    public Double getEconomiaAnualEstimada() {
        return economiaAnualEstimada;
    }
}
