package dev.satyrn.paperwasp.configuration.node.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class StringNode extends Node<String> {
    private String value;

    public StringNode(final @NotNull String name,
                      final @NotNull Node<?> parent) {
        super(name, parent);
    }

    @Override
    public @Nullable String get() {
        return this.value;
    }

    @Override
    public void accept(@Nullable String value) {
        this.value = value;
    }

    @Override
    protected @Nullable String getConfiguredValue(@NotNull String path, @Nullable String defaultValue) {
        return this.getConfiguration().getString(path, defaultValue);
    }
}
