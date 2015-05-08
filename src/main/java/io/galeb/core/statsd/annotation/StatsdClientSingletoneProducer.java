package io.galeb.core.statsd.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Qualifier;
import javax.inject.Singleton;

@Qualifier
@Singleton
@Target({ ElementType.TYPE,
          ElementType.METHOD,
          ElementType.FIELD,
          ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StatsdClientSingletoneProducer {

}
