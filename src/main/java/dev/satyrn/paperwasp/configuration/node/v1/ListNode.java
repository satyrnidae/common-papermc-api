package dev.satyrn.paperwasp.configuration.node.v1;

import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public abstract class ListNode<T> extends Node<List<T>> {
    private final @NotNull List<T> values = new ArrayList<>();

    protected ListNode(final @NotNull String name,
                       final @NotNull Node<?> parent) {
        super(name, parent);
    }

    public static <T> ListNode<T> getSimpleListNode(final @NotNull String name,
                                                    final @NotNull Node<?> parent,
                                                    final @NotNull BiFunction<Configuration, String, List<T>> getConfiguredValues) {
        return new ListNode<>(name, parent) {
            @Override
            protected @Nullable List<T> getConfiguredValue(@NotNull String path, @Nullable List<T> defaultValues) {
                @Nullable List<T> values = getConfiguredValues.apply(this.getConfiguration(), path);
                if (values == null) {
                    values = defaultValues;
                }
                return values;
            }
        };
    }

    @Override
    public @Unmodifiable @Nullable List<T> get() {
        return List.copyOf(this.values);
    }

    @Override
    public void accept(final @Unmodifiable @Nullable List<T> values) {
        this.values.clear();
        if (values != null) {
            this.values.addAll(values);
        }
    }

    @Override
    protected final void setConfiguredValue(@NotNull String path, @Nullable List<T> value) {
        if (value == null || value.isEmpty()) {
            this.getConfiguration().set(path, null);
        } else {
            this.getConfiguration().set(path, value);
        }
    }
}
