package io.galeb.core.extractable;

import java.util.UUID;

import io.galeb.core.loadbalance.hash.ExtractableKey;

public interface RequestCookie extends ExtractableKey {

    public static final String DEFAULT_COOKIE = UUID.randomUUID().toString();

    @Override
    default String get(Object extractable) {
        return DEFAULT_COOKIE;
    }
}
