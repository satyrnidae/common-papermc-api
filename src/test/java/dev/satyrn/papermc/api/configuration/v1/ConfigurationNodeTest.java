package dev.satyrn.papermc.api.configuration.v1;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

/**
 * Tests the configuration node classes.
 *
 * @author Isabel Maskrey
 * @since 1.10.0
 */
class ConfigurationNodeTest {
    @Mock Plugin testPlugin = mock(Plugin.class);
    @Mock FileConfiguration fileConfiguration = mock(FileConfiguration.class);

    /**
     * Initializes the mocks.
     */
    ConfigurationNodeTest() {
        when(testPlugin.getConfig()).thenReturn(fileConfiguration);
        when(fileConfiguration.get(any(), any())).thenReturn(null);
    }

    /**
     * We want to ensure that a node still equals itself. The check should
     */
    @Test void a_node_should_be_equivalent_to_itself() {
        RootNode rootNode = new RootNodeImpl(testPlugin);

        ConfigurationNode<?> node = new NullNode(rootNode, "testNode");

        assertEquals(node, node);
    }

    /**
     * We want to ensure that two nodes which do not contain children will match when compared together, regardless of
     * class, as long as the base paths match each other.
     */
    @Test void two_nodes_should_be_equal_if_their_base_paths_are_equivalent_even_if_the_nodes_are_a_different_type() {
        RootNode rootNode = new RootNodeImpl(testPlugin);

        // The first node, at path "[root.]sameItem"
        ConfigurationNode<?> firstNode = new FalseNode(rootNode, "testNode");

        // The second node, at path "[root.]sameItem"
        ConfigurationNode<?> secondNode = new NullNode(rootNode, "testNode");

        // Both nodes are equivalent
        assertEquals(firstNode, secondNode);
    }

    /**
     * If we try to add two nodes with the same base path, the list should only admit one of the two nodes.
     */
    @Test void two_nodes_should_not_be_added_to_root_if_their_base_paths_are_equivalent() {
        RootNode rootNode = new RootNodeImpl(testPlugin);

        int baseSize = rootNode.getChildren().size();

        // The first node, at path "[root.]sameItem"
        new FalseNode(rootNode, "sameItem");

        // The second node, at path "[root.]sameItem"
        new NullNode(rootNode, "sameItem") {
            @Override
            public @NotNull NodePriority getPriority() {
                return NodePriority.HIGH;
            }
        };

        assertEquals(rootNode.getChildren().size(), baseSize + 1);
    }

    /**
     * When the value path is pulled for a node with no children its value should match the base path.
     */
    @Test void value_path_should_match_base_path_for_a_node_with_no_children() {
        RootNode rootNode = new RootNodeImpl(testPlugin);

        ConfigurationNode<?> testNode = new NullNode(rootNode, "testNode");

        assertEquals(testNode.getValuePath(), testNode.getBasePath());
    }

    /**
     * When the value path is pulled for a node with children it should differ from the base path by the value of the
     * value node.
     */
    @Test void value_path_should_not_match_base_path_for_a_node_with_children() {
        RootNode rootNode = new RootNodeImpl(testPlugin);
        ConfigurationNode<?> parent = new NullNode(rootNode, "parent");
        new FalseNode(parent, "child");

        var valueNodeName = parent.getValueNodeName();

        assertNotEquals(parent.getValuePath(), parent.getBasePath());
        assertEquals(parent.getValuePath(), parent.getBasePath() + '.' + valueNodeName);
    }
}