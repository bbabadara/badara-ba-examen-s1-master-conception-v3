package boutique.modele;

import boutique.config.AppConfig;
import boutique.livraison.StrategieLivraison;
import boutique.notification.Notifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandeVerrouilleTest {

    @Test
    void uneCommandeValideeRefuseTouteModificationEtRevalidation() {
        Commande commande = commandeValidee();

        assertThrows(IllegalStateException.class,
                () -> commande.ajouterProduit(new Produit("ROUGE", 100, 10), 1));
        assertThrows(IllegalStateException.class,
                () -> commande.changerLivraison((poids, sousTotal) -> 0));
        assertThrows(IllegalStateException.class, () -> commande.valider(notifVide()));

        assertEquals(commande.getTotalCents(), commande.getTotalValideCents());
        assertEquals(EtatCommande.VALIDEE, commande.getEtat());
    }

    private static Commande commandeValidee() {
        Commande commande = new Commande("CL-Z", (poids, sousTotal) -> 0,
                AppConfig.getInstance());
        commande.ajouterProduit(new Produit("BLEU", 1_500, 200), 1);
        commande.valider(notifVide());
        return commande;
    }

    private static Notifier notifVide() {
        return (clientId, total) -> {
        };
    }
}