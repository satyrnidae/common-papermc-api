package dev.satyrn.paperwasp.configuration.node.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ObjectNode<T> extends Node<T> {
    private T value;
    private final Class<T> objectClass;

    public ObjectNode(final @NotNull String name,
                      final @NotNull Node<?> parent,
                      final @NotNull Class<T> objectClass) {
        super(name, parent);
        this.objectClass = objectClass;
    }

    @Override
    public @Nullable T get() {
        return this.value;
    }

    @Override
    public void accept(@Nullable T value) {
        this.value = value;
    }

    @Override
    protected @Nullable T getConfiguredValue(@NotNull String path, @Nullable T defaultValue) {
        return this.getConfiguration().getObject(path, this.objectClass, defaultValue);
    }
}
