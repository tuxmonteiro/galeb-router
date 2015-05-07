package io.galeb.core.queue;

import java.io.Serializable;

public interface QueueManager {

    public static final QueueManager NULL = new QueueManager() {
        @Override
        public void register(QueueListener queueListener) {
            // NULL
        }

        @Override
        public void sendEvent(Serializable data) {
            // NULL
        }
    };

    public void register(QueueListener queueListener);

    public void sendEvent(Serializable data);

}
