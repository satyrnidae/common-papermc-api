package dev.satyrn.paperwasp.configuration.node.v1;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class SerializableNode<T extends ConfigurationSerializable> extends Node<T> {
    private T value;
    private final Class<T> serializableClass;

    public SerializableNode(final @NotNull String name,
                            final @NotNull Node<?> parent,
                            final @NotNull Class<T> serializableClass) {
        super(name, parent);
        this.serializableClass = serializableClass;
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
        return this.getConfiguration().getSerializable(path, this.serializableClass, defaultValue);
    }
}
