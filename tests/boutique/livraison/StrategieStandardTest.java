package boutique.livraison;

import boutique.config.AppConfig;
import boutique.config.Configuration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StrategieStandardTest {

    private final Configuration config = AppConfig.getInstance();

    @Test
    void sousLeSeuilPayeLesFraisStandard() {
        StrategieStandard strategie = new StrategieStandard(config);
        assertEquals(500, strategie.calculerFrais(100, 9_999));
    }

    @Test
    void auSeuilOuAuDelaIlNyAPasDeFrais() {
        StrategieStandard strategie = new StrategieStandard(config);
        assertEquals(0, strategie.calculerFrais(100, 10_000));
        assertEquals(0, strategie.calculerFrais(100, 12_345));
    }

    @Test
    void montantsNegatifsRefuses() {
        StrategieStandard strategie = new StrategieStandard(config);
        assertThrows(IllegalArgumentException.class, () -> strategie.calculerFrais(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> strategie.calculerFrais(0, -1));
    }

    @Test
    void leSeuilEstBorneParLaConfigurationInjected() {
        Configuration seuilBas = new Configuration() {
            @Override
            public int getSeuilGratuiteCents() {
                return 3_000;
            }

            @Override
            public int getPoidsMaxGrammes() {
                return 30_000;
            }
        };
        StrategieStandard strategie = new StrategieStandard(seuilBas);
        assertEquals(500, strategie.calculerFrais(100, 2_999));
        assertEquals(0, strategie.calculerFrais(100, 3_000));
    }
}