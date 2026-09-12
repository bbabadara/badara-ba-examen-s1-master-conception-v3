package boutique.integration;

import boutique.config.AppConfig;
import boutique.config.Configuration;
import boutique.livraison.StrategieStandard;
import boutique.modele.Commande;
import boutique.modele.EtatCommande;
import boutique.modele.Produit;
import boutique.notification.Notifier;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationValidationTest {

    @Test
    void validationCompleteAvecConfigurationReelle() {
        Configuration config = AppConfig.getInstance();
        Commande commande = new Commande("CLIENT-R-1",
                new StrategieStandard(config), config);
        commande.ajouterProduit(new Produit("PHONE", 8_900, 200), 1);
        commande.ajouterProduit(new Produit("HUILE", 549, 1_100), 2);

        NotifierMemoire memoire = new NotifierMemoire();
        commande.valider(memoire);

        int attendu = (8_900 + 2 * 549) + 500;
        assertEquals(EtatCommande.VALIDEE, commande.getEtat());
        assertTrue(commande.isValidee());
        assertEquals(attendu, commande.getTotalValideCents());
        assertEquals(1, memoire.notifications.size());
        assertEquals("CLIENT-R-1", memoire.notifications.get(0).clientId);
        assertEquals(attendu, memoire.notifications.get(0).totalCents);
    }

    @Test
    void poidsMaximalDepasseLaValidationEstRefusee() {
        Configuration config = AppConfig.getInstance();
        Commande commande = new Commande("CLIENT-R-2",
                new StrategieStandard(config), config);
        Produit lourd = new Produit("PLAQUE", 4_999, 8_000);
        commande.ajouterProduit(lourd, 4);

        NotifierMemoire memoire = new NotifierMemoire();
        try {
            commande.valider(memoire);
        } catch (RuntimeException ignore) {
        }

        assertEquals(32_000, commande.getPoidsTotalGrammes());
        assertEquals(EtatCommande.BROUILLON, commande.getEtat());
        assertEquals(0, memoire.notifications.size());
    }

    static class NotifierMemoire implements Notifier {

        final List<Notification> notifications = new ArrayList<>();

        @Override
        public void notifier(String clientId, int totalCents) {
            notifications.add(new Notification(clientId, totalCents));
        }
    }

    record Notification(String clientId, int totalCents) {
    }
}