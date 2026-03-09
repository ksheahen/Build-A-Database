public class KeyValue {

    private String key;
    private String value;

    // constructor getters setters bs that java loves
    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Getters
    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    // Setters
    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
