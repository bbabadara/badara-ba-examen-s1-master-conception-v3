package boutique;

import boutique.config.AppConfig;
import boutique.config.Configuration;
import boutique.livraison.StrategieExpress;
import boutique.livraison.StrategieRetrait;
import boutique.livraison.StrategieStandard;
import boutique.modele.Catalogue;
import boutique.modele.Commande;
import boutique.modele.Produit;
import boutique.notification.NotifierConsole;

import java.text.DecimalFormat;

public final class Demo {

    private Demo() {
    }

    public static void main(String[] args) {
        Configuration config = AppConfig.getInstance();

        Catalogue catalogue = new Catalogue();
        Produit ecran = new Produit("ECR-27", 12_990, 2_800);
        Produit clavier = new Produit("CLA-MECA", 8_490, 900);
        Produit souris = new Produit("SOU-OPT", 3_990, 150);
        catalogue.ajouter(ecran);
        catalogue.ajouter(clavier);
        catalogue.ajouter(souris);

        Commande commande = new Commande("CLIENT-001",
                new StrategieStandard(config), config);
        commande.ajouterProduit(ecran, 1);
        commande.ajouterProduit(clavier, 1);
        commande.ajouterProduit(souris, 2);

        afficher("état", commande);
        afficher("total en Standard", commande);

        commande.changerLivraison(new StrategieExpress());
        afficher("total en Express", commande);

        commande.changerLivraison(new StrategieRetrait());
        afficher("total en Retrait", commande);

        commande.changerLivraison(new StrategieStandard(config));
        afficher("total en Standard (commande de 25 460 c < seuil de gratuité)", commande);

        commande.valider(new NotifierConsole());
        System.out.println("validée : " + commande.isValidee()
                + " — total figé : " + commande.getTotalValideCents() + " c");
    }

    private static void afficher(String libelle, Commande commande) {
        System.out.println(libelle + " : poids " + commande.getPoidsTotalGrammes()
                + " g, sous-total " + commande.getSousTotalCents()
                + " c, frais " + commande.getFraisLivraisonCents()
                + " c, total " + commande.getTotalCents() + " c"
                + " (" + centimesEnEuros(commande.getTotalCents()) + " €)");
    }

    private static String centimesEnEuros(int centimes) {
        return new DecimalFormat("0.00").format(centimes / 100.0);
    }
}