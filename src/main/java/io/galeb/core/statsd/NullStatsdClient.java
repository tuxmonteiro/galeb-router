package io.galeb.core.statsd;

import javax.enterprise.inject.Alternative;

@Alternative
public class NullStatsdClient implements StatsdClient {

}