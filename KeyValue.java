/**
 * Represents an immutable key-value pair.
 */
public class KeyValue {

    private final String key;
    private final String value;


    /**
     * Constructs a Keyvalue pair.
     * @param key the key string.
     * @param value the value string.
     */
    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return The key string.
     */
    public String getKey() {
        return key;
    }

    /**
     * @return The value string.
     */
    public String getValue() {
        return value;
    }

}
