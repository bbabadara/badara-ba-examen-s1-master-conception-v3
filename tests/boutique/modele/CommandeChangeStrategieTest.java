package boutique.modele;

import boutique.config.AppConfig;
import boutique.config.Configuration;
import boutique.livraison.StrategieLivraison;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandeChangeStrategieTest {

    @Test
    void leChangementDeStrategieModifieLeTotalAvantValidation() {
        Commande commande = new Commande("CL-9", zero(), AppConfig.getInstance());
        commande.ajouterProduit(new Produit("LIVRE", 2_000, 300), 2);

        assertEquals(4_000, commande.getSousTotalCents());
        assertEquals(4_000, commande.getTotalCents());

        commande.changerLivraison(fraisDe(700));
        assertEquals(4_700, commande.getTotalCents());

        commande.changerLivraison(fraisDe(0));
        assertEquals(4_000, commande.getTotalCents());
    }

    private static StrategieLivraison fraisDe(int frais) {
        return (poids, sousTotal) -> frais;
    }

    private static StrategieLivraison zero() {
        return (poids, sousTotal) -> 0;
    }
}