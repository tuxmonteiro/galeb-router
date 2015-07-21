package io.galeb.core.extractable;

import io.galeb.core.loadbalance.hash.ExtractableKey;

public interface RequestURI extends ExtractableKey {

    enum UriCriterion {
        SIMPLE("simple"),
        FULL("full");

        private String simpleName;
        private UriCriterion(String simpleName) {
            this.simpleName = simpleName;
        }

        @Override
        public String toString() {
            return simpleName;
        }
    }

    public static final String DEFAULT_URI = "/";

    public static final String DEFAULT_CRITERION = UriCriterion.SIMPLE.toString();

    @Override
    default String get(Object extractable) {
        return DEFAULT_URI;
    }
}
