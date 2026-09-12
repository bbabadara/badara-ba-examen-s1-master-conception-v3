package boutique.livraison;

public abstract class AbstractStrategieLivraison implements StrategieLivraison {

    @Override
    public final int calculerFrais(int poidsGrammes, int sousTotalCents) {
        if (poidsGrammes < 0) {
            throw new IllegalArgumentException("Le poids ne peut pas être négatif.");
        }
        if (sousTotalCents < 0) {
            throw new IllegalArgumentException("Le sous-total ne peut pas être négatif.");
        }
        return calculer(poidsGrammes, sousTotalCents);
    }

    protected abstract int calculer(int poidsGrammes, int sousTotalCents);
}