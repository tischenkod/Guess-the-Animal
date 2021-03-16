package animals;

public enum FileType {
    JSON("json"),
    XML("xml"),
    YAML("yaml");

    String extension;

    FileType(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return extension;
    }

}
