package boutique.modele;

import boutique.config.Configuration;
import boutique.livraison.StrategieLivraison;
import boutique.notification.Notifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Commande {

    private final String clientId;
    private final Configuration configuration;
    private final List<LigneCommande> lignes = new ArrayList<>();

    private StrategieLivraison strategie;
    private EtatCommande etat = EtatCommande.BROUILLON;
    private int totalValideCents;

    public Commande(String clientId, StrategieLivraison strategie, Configuration configuration) {
        if (clientId == null || clientId.isBlank()) {
            throw new IllegalArgumentException("L'identifiant du client est obligatoire.");
        }
        if (strategie == null) {
            throw new IllegalArgumentException("Une stratégie de livraison est requise.");
        }
        if (configuration == null) {
            throw new IllegalArgumentException("Une configuration est requise.");
        }
        this.clientId = clientId;
        this.strategie = strategie;
        this.configuration = configuration;
    }

    public String getClientId() {
        return clientId;
    }

    public EtatCommande getEtat() {
        return etat;
    }

    public boolean isValidee() {
        return etat == EtatCommande.VALIDEE;
    }

    public void ajouterProduit(Produit produit, int quantite) {
        verifierCommandeModifiable();
        if (produit == null) {
            throw new IllegalArgumentException("Le produit est obligatoire.");
        }
        if (quantite <= 0) {
            throw new IllegalArgumentException("La quantité doit être strictement positive.");
        }
        lignes.add(new LigneCommande(produit, quantite));
    }

    public void changerLivraison(StrategieLivraison nouvelleStrategie) {
        verifierCommandeModifiable();
        if (nouvelleStrategie == null) {
            throw new IllegalArgumentException("La nouvelle stratégie ne peut pas être nulle.");
        }
        this.strategie = nouvelleStrategie;
    }

    public int getSousTotalCents() {
        int total = 0;
        for (LigneCommande ligne : lignes) {
            total += ligne.getSousTotalCents();
        }
        return total;
    }

    public int getPoidsTotalGrammes() {
        int total = 0;
        for (LigneCommande ligne : lignes) {
            total += ligne.getPoidsGrammes();
        }
        return total;
    }

    public int getFraisLivraisonCents() {
        return strategie.calculerFrais(getPoidsTotalGrammes(), getSousTotalCents());
    }

    public int getTotalCents() {
        return getSousTotalCents() + getFraisLivraisonCents();
    }

    public int getTotalValideCents() {
        if (etat != EtatCommande.VALIDEE) {
            throw new IllegalStateException("La commande n'est pas encore validée.");
        }
        return totalValideCents;
    }

    public void valider(Notifier notifier) {
        verifierCommandeModifiable();
        if (notifier == null) {
            throw new IllegalArgumentException("Un notifier est requis pour valider.");
        }
        if (lignes.isEmpty()) {
            throw new RefusValidationException("La commande est vide.");
        }
        if (getPoidsTotalGrammes() > configuration.getPoidsMaxGrammes()) {
            throw new RefusValidationException(
                    "Poids maximal dépassé (" + getPoidsTotalGrammes() + " g).");
        }
        this.totalValideCents = getTotalCents();
        this.etat = EtatCommande.VALIDEE;
        notifier.notifier(clientId, totalValideCents);
    }

    public List<LigneCommande> getLignes() {
        return Collections.unmodifiableList(lignes);
    }

    private void verifierCommandeModifiable() {
        if (etat == EtatCommande.VALIDEE) {
            throw new IllegalStateException("Commande validée : aucune modification possible.");
        }
    }

    static final class LigneCommande {

        private final Produit produit;
        private final int quantite;

        LigneCommande(Produit produit, int quantite) {
            this.produit = produit;
            this.quantite = quantite;
        }

        int getSousTotalCents() {
            return produit.getPrixCents() * quantite;
        }

        int getPoidsGrammes() {
            return produit.getPoidsGrammes() * quantite;
        }

        Produit getProduit() {
            return produit;
        }

        int getQuantite() {
            return quantite;
        }
    }
}