package boutique.modele;

public final class Produit {

    private final String reference;
    private final int prixCents;
    private final int poidsGrammes;

    public Produit(String reference, int prixCents, int poidsGrammes) {
        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException("La référence est obligatoire.");
        }
        if (prixCents < 0) {
            throw new IllegalArgumentException("Le prix doit être positif ou nul.");
        }
        if (poidsGrammes <= 0) {
            throw new IllegalArgumentException("Le poids doit être strictement positif.");
        }
        this.reference = reference;
        this.prixCents = prixCents;
        this.poidsGrammes = poidsGrammes;
    }

    public String getReference() {
        return reference;
    }

    public int getPrixCents() {
        return prixCents;
    }

    public int getPoidsGrammes() {
        return poidsGrammes;
    }

    @Override
    public String toString() {
        return "Produit{" + reference + ", " + prixCents + " c, " + poidsGrammes + " g}";
    }
}