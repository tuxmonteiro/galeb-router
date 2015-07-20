package io.galeb.core.loadbalance.hash;

public interface KeyTypeLocator {

    public enum KeyType {
        SOURCE_IP("sourceIP"),
        COOKIE("cookie"),
        URI("uri");

        private final String simpleName;
        private KeyType(String simpleName) {
            this.simpleName = simpleName;
        }

        @Override
        public String toString() {
            return simpleName;
        }
    }

    static final KeyTypeLocator NULL = new KeyTypeLocator() {
        // NULL
    };

    static final String DEFAULT_KEY_TYPE = KeyType.SOURCE_IP.toString();

    default ExtractableKey getKey(String keyType) {
        return ExtractableKey.NULL;
    }

}
