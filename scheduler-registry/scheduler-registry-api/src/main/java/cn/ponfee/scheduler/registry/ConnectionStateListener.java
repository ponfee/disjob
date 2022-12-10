package cn.ponfee.scheduler.registry;

import java.util.function.Consumer;

/**
 * Connection state changed listener
 *
 * @param <C> the client
 * @author Ponfee
 */
public interface ConnectionStateListener<C> {

    /**
     * Connected
     *
     * @param client the client
     */
    void onConnected(C client);

    /**
     * Disconnected
     *
     * @param client the client
     */
    void onDisconnected(C client);

    static <C> Builder<C> builder() {
        return new Builder<>();
    }

    final class Builder<C> {
        private Consumer<C> onConnected;
        private Consumer<C> onDisconnected;

        public Builder<C> onConnected(Consumer<C> onConnected) {
            this.onConnected = onConnected;
            return this;
        }

        public Builder<C> onDisconnected(Consumer<C> onDisconnected) {
            this.onDisconnected = onDisconnected;
            return this;
        }

        public ConnectionStateListener<C> build() {
            return new ConnectionStateListener<C>() {
                @Override
                public void onConnected(C client) {
                    if (onConnected != null) {
                        onConnected.accept(client);
                    }
                }

                @Override
                public void onDisconnected(C client) {
                    if (onDisconnected != null) {
                        onDisconnected.accept(client);
                    }
                }
            };
        }
    }

}
