package io.galeb.core.util;

public interface SourceIP {

    //Useful http headers
    public final String HTTP_HEADER_XREAL_IP         = "X-Real-IP";
    public final String HTTP_HEADER_X_FORWARDED_FOR  = "X-Forwarded-For";

    public String get();

    public SourceIP pullFrom(final Object extractable);

}
