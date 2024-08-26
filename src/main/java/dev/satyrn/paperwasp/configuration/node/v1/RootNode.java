package dev.satyrn.paperwasp.configuration.node.v1;

import dev.satyrn.paperwasp.configuration.node.primitive.v1.IntegerNode;
import dev.satyrn.paperwasp.configuration.v1.ConfigurationTreeBuilder;
import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class RootNode extends SectionNode {
    private final IntegerNode version = new IntegerNode("_version", this, 0, null);
    private final @NotNull List<BiConsumer<RootNode, Integer>> upgradeConsumers = new ArrayList<>();

    public RootNode(final @NotNull Configuration config) {
        super(config);
    }

    public int getVersion() {
        return this.version.get();
    }

    public void addUpgradeConsumer(final @NotNull BiConsumer<RootNode, Integer> upgradeConsumer) {
        this.upgradeConsumers.add(upgradeConsumer);
    }

    public void upgrade() {
        for (BiConsumer<RootNode, Integer> upgradeConsumer : this.upgradeConsumers) {
            upgradeConsumer.accept(this, this.getVersion());
        }
        // Reload after upgrade to make sure everything is fully up-to-date
        this.refresh();
    }

    /**
     * Initializes the root node instance, loading all of its values from the configuration object.
     */
    public void initialize() {
        ConfigurationTreeBuilder.initializeConfiguration(this);
    }
}
