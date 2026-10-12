import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Testes de unidade da classificação do IR (JUnit 5).
 * Os dados de entrada e as saídas esperadas são os da Tabela 2a (classes C1 a C8 e valores limite)
 * e do Quadro 2a (caminhos independentes do GFC).
 */
class CalculadoraIRTest {

    @Nested
    @DisplayName("Partição de equivalência: classes válidas C3 a C8 (dados de entrada da Tabela 2a)")
    class ClassesValidas {

        @ParameterizedTest(name = "{0}: R = {1} -> {2}")
        @CsvSource({
            "C3, 1000.00,  ISENCAO_TOTAL",
            "C3, 2500.00,  ISENCAO_TOTAL",
            "C3, 4000.00,  ISENCAO_TOTAL",
            "C4, 5100.00,  DESCONTO_75",
            "C4, 5250.00,  DESCONTO_75",
            "C4, 5400.00,  DESCONTO_75",
            "C5, 5600.00,  DESCONTO_50",
            "C5, 5750.00,  DESCONTO_50",
            "C5, 5900.00,  DESCONTO_50",
            "C6, 6100.00,  DESCONTO_25",
            "C6, 6250.00,  DESCONTO_25",
            "C6, 6400.00,  DESCONTO_25",
            "C7, 6600.00,  DESCONTO_10",
            "C7, 7000.00,  DESCONTO_10",
            "C7, 7200.00,  DESCONTO_10",
            "C8, 7500.00,  ALIQUOTA_INTEGRAL",
            "C8, 8000.00,  ALIQUOTA_INTEGRAL",
            "C8, 10000.00, ALIQUOTA_INTEGRAL"
        })
        void deveClassificarNaFaixaDaClasse(String classe, double renda, FaixaIR esperada) {
            assertEquals(esperada, CalculadoraIR.classificar(renda));
        }
    }

    @Nested
    @DisplayName("Análise de valor limite: LIV e LSV de cada classe válida")
    class ValoresLimiteValidos {

        @ParameterizedTest(name = "R = {0} -> {1}")
        @CsvSource({
            "0.00,    ISENCAO_TOTAL",     // C3 LIV
            "5000.00, ISENCAO_TOTAL",     // C3 LSV / C4 LII
            "5000.01, DESCONTO_75",       // C4 LIV / C3 LSI
            "5500.00, DESCONTO_75",       // C4 LSV / C5 LII
            "5500.01, DESCONTO_50",       // C5 LIV / C4 LSI
            "6000.00, DESCONTO_50",       // C5 LSV / C6 LII
            "6000.01, DESCONTO_25",       // C6 LIV / C5 LSI
            "6500.00, DESCONTO_25",       // C6 LSV / C7 LII
            "6500.01, DESCONTO_10",       // C7 LIV / C6 LSI
            "7350.00, DESCONTO_10",       // C7 LSV / C8 LII
            "7350.01, ALIQUOTA_INTEGRAL"  // C8 LIV / C7 LSI
        })
        void deveRespeitarOsLimitesDasFaixas(double renda, FaixaIR esperada) {
            assertEquals(esperada, CalculadoraIR.classificar(renda));
        }
    }

    @Nested
    @DisplayName("Saída esperada: desconto e economia anual informados na Figura 2a")
    class SaidaEsperada {

        @ParameterizedTest(name = "{0} -> {1} (economia R$ {2})")
        @CsvSource({
            "ISENCAO_TOTAL, Isenção total,   4356.89",
            "DESCONTO_75,   Desconto de 75%, 3367.68",
            "DESCONTO_50,   Desconto de 50%, 2378.47",
            "DESCONTO_25,   Desconto de 25%, 1389.26",
            "DESCONTO_10,   Desconto de 10%, 978.62"
        })
        void deveInformarDescontoEEconomia(FaixaIR faixa, String desconto, double economia) {
            assertEquals(desconto, faixa.getDescontoIR());
            assertEquals(economia, faixa.getEconomiaAnualEstimada().doubleValue(), 0.001);
        }

        @ParameterizedTest(name = "R = {0} -> alíquota de 27,5% sem economia informada")
        @ValueSource(doubles = {7350.01, 8000.00})
        void acimaDe7350NaoTemEconomiaInformada(double renda) {
            FaixaIR faixa = CalculadoraIR.classificar(renda);
            assertEquals(FaixaIR.ALIQUOTA_INTEGRAL, faixa);
            assertNull(faixa.getEconomiaAnualEstimada());
        }
    }

    @Nested
    @DisplayName("Partição de equivalência: classes inválidas C1 e C2 (mensagem de erro)")
    class ClassesInvalidas {

        @ParameterizedTest(name = "C2: R = {0} -> exceção")
        @ValueSource(doubles = {-0.01, -1.00, -5000.00}) // -0,01 = LSI de C2 / LII de C3
        void rendaNegativaDeveSerRejeitada(double renda) {
            assertThrows(IllegalArgumentException.class, () -> CalculadoraIR.classificar(renda));
        }

        @ParameterizedTest(name = "C1: R = {0} -> exceção")
        @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
        void rendaNaoFinitaDeveSerRejeitada(double renda) {
            assertThrows(IllegalArgumentException.class, () -> CalculadoraIR.classificar(renda));
        }

        @ParameterizedTest(name = "C1: entrada \"{0}\" -> exceção")
        @ValueSource(strings = {"ABC", "", "   "})
        void entradaNaoNumericaOuVaziaDeveSerRejeitada(String entrada) {
            assertThrows(IllegalArgumentException.class, () -> CalculadoraIR.classificarTexto(entrada));
        }
    }

    @Nested
    @DisplayName("Caminhos independentes do GFC (Quadro 2a)")
    class CaminhosGFC {

        @ParameterizedTest(name = "Caminho {0}: {1} | R = {2} -> {3}")
        @CsvSource({
            "1, 1-2-12,          4000.00, ISENCAO_TOTAL",
            "2, 1-3-4-12,        5250.00, DESCONTO_75",
            "3, 1-3-5-6-12,      5750.00, DESCONTO_50",
            "4, 1-3-5-7-8-12,    6250.00, DESCONTO_25",
            "5, 1-3-5-7-9-10-12, 7000.00, DESCONTO_10",
            "6, 1-3-5-7-9-11-12, 8000.00, ALIQUOTA_INTEGRAL"
        })
        void devePercorrerCadaCaminho(int numero, String caminho, double renda, FaixaIR esperada) {
            assertEquals(esperada, CalculadoraIR.classificarValido(renda));
        }
    }
}
