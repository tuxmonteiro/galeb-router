package io.galeb.core.util;

public interface SourceIP {

    public static final String DEFAULT_SOURCE_IP = "127.0.0.1";

    //Useful http headers
    public static final String HTTP_HEADER_XREAL_IP         = "X-Real-IP";
    public static final String HTTP_HEADER_X_FORWARDED_FOR  = "X-Forwarded-For";

    public String getRealSourceIP();

    public SourceIP pullFrom(final Object extractable);

}
