package io.galeb.core.logging;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public abstract class Loggable {

    @Inject
    protected Logger logger;

    @PostConstruct
    private void init() {
        logger.setSource(this);
    }

    protected Loggable() {

    }

    public void onLog(String levelName, String message) {
        logger.log(levelName, message);
    }

}
