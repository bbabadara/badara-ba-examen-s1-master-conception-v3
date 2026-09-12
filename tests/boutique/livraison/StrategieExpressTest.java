package boutique.livraison;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StrategieExpressTest {

    @Test
    void laBaseSAppliqueMemesansPoids() {
        StrategieExpress strategie = new StrategieExpress();
        assertEquals(1_000, strategie.calculerFrais(0, 0));
    }

    @Test
    void chaqueKiloEntammeCompteEnPlus() {
        StrategieExpress strategie = new StrategieExpress();
        assertEquals(1_200, strategie.calculerFrais(999, 0));
        assertEquals(1_200, strategie.calculerFrais(1_000, 0));
        assertEquals(1_400, strategie.calculerFrais(1_001, 0));
        assertEquals(1_400, strategie.calculerFrais(2_000, 0));
        assertEquals(1_600, strategie.calculerFrais(2_001, 0));
    }

    @Test
    void montantsNegatifsRefuses() {
        StrategieExpress strategie = new StrategieExpress();
        assertThrows(IllegalArgumentException.class, () -> strategie.calculerFrais(-5, 0));
        assertThrows(IllegalArgumentException.class, () -> strategie.calculerFrais(0, -5));
    }
}