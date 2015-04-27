package io.galeb.core.eventbus;

public interface EventBusListener {

    public static final EventBusListener NULL = new EventBusListener() {
        @Override
        public void onEvent(Event event) {
            return;
        }

        @Override
        public IEventBus getEventBus() {
            return IEventBus.NULL;
        }
    };

    public void onEvent(final Event event);

    public IEventBus getEventBus();

}
