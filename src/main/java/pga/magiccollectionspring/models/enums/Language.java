package pga.magiccollectionspring.models.enums;

public enum Language {

    ENGLISH("EN", "Inglés"),
    SPANISH("ES", "Español"),
    FRENCH("FR", "Francés"),
    GERMAN("DE", "Alemán"),
    ITALIAN("IT", "Italiano"),
    PORTUGUESE("PT", "Portugués"),
    JAPANESE("JP", "Japonés"),
    CHINESE("CN", "Chino"),
    RUSSIAN("RU", "Ruso"),
    KOREAN("KR", "Coreano");

    private final String code;
    private final String spanishName;

    Language(String code, String spanishName) {
        this.code = code;
        this.spanishName = spanishName;
    }

    public String getCode() {
        return code;
    }

    public String getSpanishName() {
        return spanishName;
    }

    public static Language fromCode(String text) {
        if (text == null) throw new IllegalArgumentException("El idioma no puede estar vacío");

        for (Language l : Language.values()) {
            if (l.code.equalsIgnoreCase(text) ||
                    l.name().equalsIgnoreCase(text) ||
                    l.spanishName.equalsIgnoreCase(text)) {
                return l;
            }
        }
        throw new IllegalArgumentException("Idioma no soportado: " + text);
    }
}
