package parser.app.webscraper.scraperlogic.logic.outputFile;

public enum OutputFileType {
    XLSX("XLSX"),
    CSV("CSV");

    private final String value;

    OutputFileType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static OutputFileType fromValue(String value) {
        for (OutputFileType outputFileType : OutputFileType.values()) {
            if (outputFileType.value.equals(value)) {
                return outputFileType;
            }
        }
        throw new IllegalArgumentException("Unsupported value: " + value);
    }
}
