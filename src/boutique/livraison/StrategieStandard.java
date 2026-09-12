package boutique.livraison;

import boutique.config.Configuration;

public final class StrategieStandard extends AbstractStrategieLivraison {

    private static final int FRAIS_STANDARD = 500;

    private final Configuration configuration;

    public StrategieStandard(Configuration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("La configuration est requise.");
        }
        this.configuration = configuration;
    }

    @Override
    protected int calculer(int poidsGrammes, int sousTotalCents) {
        if (sousTotalCents >= configuration.getSeuilGratuiteCents()) {
            return 0;
        }
        return FRAIS_STANDARD;
    }
}