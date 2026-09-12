package boutique.livraison;

public final class StrategieExpress extends AbstractStrategieLivraison {

    private static final int FRAIS_BASE = 1_000;
    private static final int FRAIS_PAR_KG_COMMENCE = 200;

    @Override
    protected int calculer(int poidsGrammes, int sousTotalCents) {
        int kilosEntames = (poidsGrammes + 999) / 1_000;
        return FRAIS_BASE + kilosEntames * FRAIS_PAR_KG_COMMENCE;
    }
}