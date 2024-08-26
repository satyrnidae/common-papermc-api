package dev.satyrn.paperwasp.configuration.node.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class EnumNode<E extends Enum<E>> extends Node<E> {
    private E value;

    protected EnumNode(final @NotNull String name,
                       final @NotNull Node<?> parent) {
        super(name, parent);
    }

    protected EnumNode(final @NotNull String name) {
        super(name);
    }

    @Override
    public @Nullable E get() {
        return this.value;
    }

    @Override
    public void accept(final @Nullable E value) {
        this.value = value;
    }

    @Override
    protected @Nullable E getConfiguredValue(@NotNull String path, @Nullable E defaultValue) {
        final String stringValue = this.getConfiguration().getString(path, defaultValue == null ? null : defaultValue.name());
        return this.parse(stringValue);
    }

    @Override
    protected void setConfiguredValue(@NotNull String path, @Nullable E value) {
        final String stringValue = value == null ? null : value.toString();
        this.getConfiguration().set(path, stringValue);
    }

    protected abstract E parse(final @Nullable String value);
}
