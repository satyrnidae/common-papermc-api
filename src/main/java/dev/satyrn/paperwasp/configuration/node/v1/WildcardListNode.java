package dev.satyrn.paperwasp.configuration.node.v1;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public final class WildcardListNode extends Node<List<?>> {
    private final @NotNull List<Object> value = new ArrayList<>();

    public WildcardListNode(final @NotNull String name, final @NotNull Node<?> parent) {
        super(name, parent);
    }

    @Override
    public @Unmodifiable @Nullable List<?> get() {
        return List.copyOf(this.value);
    }

    @Override
    public void accept(final @Unmodifiable @Nullable List<?> values) {
        this.value.clear();
        if (values != null) {
            this.value.addAll(values);
        }
    }

    @Override
    protected @Nullable List<?> getConfiguredValue(@NotNull String path, @Nullable List<?> defaultValue) {
        return this.getConfiguration().getList(path, defaultValue);
    }

    @Override
    protected void setConfiguredValue(@NotNull String path, @Nullable List<?> value) {
        if (value == null || value.isEmpty()) {
            this.getConfiguration().set(path, null);
        } else {
            this.getConfiguration().set(path, value);
        }
    }
}
