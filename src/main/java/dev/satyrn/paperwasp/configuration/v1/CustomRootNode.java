package dev.satyrn.paperwasp.configuration.v1;

import dev.satyrn.paperwasp.configuration.node.v1.RootNode;
import org.bukkit.configuration.Configuration;
import org.jetbrains.annotations.NotNull;

/**
 * A base class for root nodes that implements upgrades as an abstract method.
 * <p>
 * This class extends {@link RootNode} and provides a mechanism for handling version upgrades
 * by defining the {@link #upgrade(int)} method. Subclasses are required to implement the upgrade
 * logic for different versions.
 * </p>
 *
 * <p>The {@code CustomRootNode} class automatically sets the upgrade consumer to invoke
 * the {@link #performUpgrade(RootNode, int)} method, ensuring that the {@link #upgrade(int)}
 * method is called during upgrades.</p>
 *
 * @since 3.0.0-paper-api.1.21-R0.1-SNAPSHOT
 */
public abstract class CustomRootNode extends RootNode {
    /**
     * Initializes a new custom root node with the provided configuration and sets up the upgrade process.
     * <p>
     * This constructor registers an upgrade consumer that invokes the {@link CustomRootNode#performUpgrade(RootNode, int)} method,
     * allowing subclasses to define custom upgrade logic via the {@link #upgrade(int)} method.
     * Multiple upgrade consumers may still be added to the root node, enabling complex upgrade processes if needed.
     * Upgrades are applied in first-in/first-out order, and will start with {@link #upgrade(int)}.
     *
     * @param configuration The configuration to associate with this root node.
     */
    public CustomRootNode(final @NotNull Configuration configuration) {
        super(configuration);
        this.addUpgradeConsumer(CustomRootNode::performUpgrade);
    }

    /**
     * Performs the upgrade logic from the given previous version to the current version.
     * <p>
     * Subclasses should override this method to define specific upgrade logic that needs to occur
     * when the configuration version is updated. The method receives the previous version number,
     * allowing the implementation to handle version-specific migrations or adjustments.
     *
     * @param previousVersion The previous version of the configuration from which the upgrade is occurring.
     */
    public abstract void upgrade(int previousVersion);

    /**
     * Performs the upgrade for the given root node.
     * <p>
     * If the root node is an instance of {@code CustomRootNode}, this method invokes the
     * {@link #upgrade(int)} method on the node.
     * </p>
     *
     * @param node    The root node to be upgraded.
     * @param version The target version from which the node should be upgraded.
     */
    public static void performUpgrade(RootNode node, int version) {
        if (node instanceof CustomRootNode customNode) {
            customNode.upgrade(version);
        }
    }
}
