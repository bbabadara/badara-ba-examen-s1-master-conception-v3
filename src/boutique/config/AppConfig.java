package boutique.config;

public enum AppConfig implements Configuration {

    INSTANCE;

    private static final int SEUIL_GRATUITE_DEFAUT = 10_000;
    private static final int POIDS_MAX_DEFAUT = 30_000;

    private static Configuration configuration = new ConfigurationParDefaut();

    public static AppConfig getInstance() {
        return INSTANCE;
    }

    public static void override(Configuration nouvelleConfiguration) {
        configuration = nouvelleConfiguration;
    }

    public static void reset() {
        configuration = new ConfigurationParDefaut();
    }

    @Override
    public int getSeuilGratuiteCents() {
        return configuration.getSeuilGratuiteCents();
    }

    @Override
    public int getPoidsMaxGrammes() {
        return configuration.getPoidsMaxGrammes();
    }

    private static final class ConfigurationParDefaut implements Configuration {

        @Override
        public int getSeuilGratuiteCents() {
            return SEUIL_GRATUITE_DEFAUT;
        }

        @Override
        public int getPoidsMaxGrammes() {
            return POIDS_MAX_DEFAUT;
        }
    }
}