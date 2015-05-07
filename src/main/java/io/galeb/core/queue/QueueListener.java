package io.galeb.core.queue;

import java.io.Serializable;

public interface QueueListener {

    public static final QueueListener NULL = new QueueListener() {
        @Override
        public void onEvent(Serializable data) {
            // NULL
        }
    };

    public void onEvent(Serializable data);

}
