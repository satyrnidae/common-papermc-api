package dev.satyrn.paperwasp.configuration.node.v1;

import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SectionNode extends Node<Void> {
    protected SectionNode(final @NotNull Configuration config) {
        super(config);
    }


    public SectionNode(final @NotNull String name,
                       final @NotNull Node<?> parent) {
        super(name, parent);
    }

    @Override
    public @Nullable Void get() {
        return null;
    }

    @Override
    public void accept(final @Nullable Void value) {
        throw new UnsupportedOperationException("Cannot set the value of a section node.");
    }

    @Override
    protected @Nullable Void getConfiguredValue(@NotNull String path, @Nullable Void defaultValue) {
        return null;
    }
}
