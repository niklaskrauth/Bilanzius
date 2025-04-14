    private static Localization instance;

    public static Localization getInstance()
    {
        if (instance == null) {
            instance = new Localization("en"); // Standard auf Englisch
        }
        return instance;
    }