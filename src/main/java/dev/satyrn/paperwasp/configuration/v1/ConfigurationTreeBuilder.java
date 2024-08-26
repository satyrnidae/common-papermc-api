package dev.satyrn.paperwasp.configuration.v1;

import dev.satyrn.lunamoth.util.function.v1.TriFunction;
import dev.satyrn.paperwasp.configuration.node.logic.v1.DifficultyNode;
import dev.satyrn.paperwasp.configuration.node.logic.v1.SwitchNode;
import dev.satyrn.paperwasp.configuration.node.primitive.v1.*;
import dev.satyrn.paperwasp.configuration.node.v1.*;
import org.bukkit.Color;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.checkerframework.common.returnsreceiver.qual.This;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConfigurationTreeBuilder {
    private final static Logger log = Logger.getLogger(ConfigurationTreeBuilder.class.getName());

    private RootNode rootNode;
    private Node<?> currentNode;

    public ConfigurationTreeBuilder(final @NotNull Configuration configuration) {
        this(configuration, null);
    }

    public ConfigurationTreeBuilder(final @NotNull Configuration configuration,
                                    final @Nullable BiConsumer<RootNode, Integer> upgradeConsumer) {
        log.fine("Begin building new configuration");
        this.currentNode = this.rootNode = new RootNode(configuration);
        if (upgradeConsumer != null) {
            this.rootNode.addUpgradeConsumer(upgradeConsumer);
        }
    }

    public ConfigurationTreeBuilder(final @NotNull RootNode rootNode) {
        log.fine("Begin modifying existing configuration");
        this.currentNode = this.rootNode = rootNode;
    }

    public Node<?> getCurrentNode() {
        this.requireCurrentlyBuildingConfiguration();
        return this.currentNode;
    }

    public RootNode getRootNode() {
        this.requireCurrentlyBuildingConfiguration();
        return this.rootNode;
    }

    public @This ConfigurationTreeBuilder navigateToParent() {
        this.requireCurrentlyBuildingConfiguration();
        final @Nullable Node<?> parent = this.currentNode.getParent();
        if (parent == null) {
            log.warning("Attempted to index into null parent while building configuration");
            this.navigateTo(this.rootNode);
        } else {
            this.navigateTo(this.currentNode.getParent());
        }
        return this;
    }

    public @This ConfigurationTreeBuilder navigateToRoot() {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(this.rootNode);
        return this;
    }

    public @This ConfigurationTreeBuilder navigateToChild(final @NotNull String exactName) {
        Objects.requireNonNull(exactName);
        this.requireCurrentlyBuildingConfiguration();
        if (!this.currentNode.isEmpty()) {
            throw new IllegalStateException("Current node has no children");
        }
        final Optional<Node<?>> matchChild = this.currentNode.getChildren().stream().filter(node -> exactName.equals(node.getName())).findAny();
        if (matchChild.isEmpty()) {
            throw new IllegalStateException("No child nodes matched the given name.");
        }
        this.navigateTo(matchChild.get());
        return this;
    }

    private void navigateTo(final @NotNull Node<?> newNode) {
        this.requireCurrentlyBuildingConfiguration();
        log.log(Level.FINE, String.format("Move from %s to %s", this.currentNode.getBasePath(), newNode.getBasePath()));
        this.currentNode = newNode;
    }

    private void requireCurrentlyBuildingConfiguration() {
        if (this.currentNode == null) {
            throw new IllegalStateException("Not currently building a configuration schema");
        }
    }

    /**
     * Finishes setup of a customized root node.
     *
     * @param rootNode The root of the custom configuration
     */
    public static void initializeConfiguration(final @NotNull RootNode rootNode) {
        new ConfigurationTreeBuilder(rootNode).finish();
    }

    public RootNode finish() {
        this.requireCurrentlyBuildingConfiguration();
        final RootNode config = this.rootNode;
        config.upgrade();
        log.fine("Finished building configuration");
        this.currentNode = null;
        this.rootNode = null;
        return config;
    }

    public @This ConfigurationTreeBuilder addSection(final @NotNull String name) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new SectionNode(name, this.currentNode));
        return this;
    }

    public @This ConfigurationTreeBuilder addShort(final @NotNull String name) {
        return this.addShort(name, (short)0, null, null);
    }

    public @This ConfigurationTreeBuilder addShort(final @NotNull String name,
                                                   final short defaultValue) {
        return this.addShort(name, defaultValue, null, null);
    }

    public @This ConfigurationTreeBuilder addShort(final @NotNull String name,
                                                   final @Nullable Short min,
                                                   final @Nullable Short max) {
        return this.addShort(name, (short)0, min, max);
    }

    public @This ConfigurationTreeBuilder addShort(final @NotNull String name,
                                                   final short defaultValue,
                                                   final @Nullable Short min,
                                                   final @Nullable Short max) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new ShortNode(name, this.currentNode, min, max).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addByte(final @NotNull String name) {
        return this.addByte(name, (byte)0, null, null);
    }

    public @This ConfigurationTreeBuilder addByte(final @NotNull String name,
                                                  final byte defaultValue) {
        return this.addByte(name, defaultValue, null, null);
    }

    public @This ConfigurationTreeBuilder addByte(final @NotNull String name,
                                                  final @Nullable Byte min,
                                                  final @Nullable Byte max) {
        return this.addByte(name, (byte)0, min, max);
    }

    public @This ConfigurationTreeBuilder addByte(final @NotNull String name,
                                                  final byte defaultValue,
                                                  final @Nullable Byte min,
                                                  final @Nullable Byte max) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new ByteNode(name, this.currentNode, min, max).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addInt(final @NotNull String name) {
        return this.addInt(name, 0, null, null);
    }

    public @This ConfigurationTreeBuilder addInt(final @NotNull String name, int defaultValue) {
        return this.addInt(name, defaultValue, null, null);
    }

    public @This ConfigurationTreeBuilder addInt(final @NotNull String name,
                                                 final @Nullable Integer min,
                                                 final @Nullable Integer max) {
        return this.addInt(name, 0, min, max);
    }

    public @This ConfigurationTreeBuilder addInt(final @NotNull String name,
                                                 final int defaultValue,
                                                 final @Nullable Integer min,
                                                 final @Nullable Integer max) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new IntegerNode(name, this.currentNode, min, max).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addDouble(final @NotNull String name) {
        return this.addDouble(name, 0.0D, null, null);
    }

    public @This ConfigurationTreeBuilder addDouble(final @NotNull String name,
                                                    final double defaultValue) {
        return this.addDouble(name, defaultValue, null, null);
    }

    public @This ConfigurationTreeBuilder addDouble (final @NotNull String name,
                                                     final @Nullable Double min,
                                                     final @Nullable Double max) {
        return this.addDouble(name, 0.0D, min, max);
    }

    public @This ConfigurationTreeBuilder addDouble(final @NotNull String name,
                                                    final double defaultValue,
                                                    final @Nullable Double minValue,
                                                    final @Nullable Double maxValue) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new DoubleNode(name, this.currentNode, minValue, maxValue).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addFloat(final @NotNull String name) {
        return this.addFloat(name, 0.0F, null, null);
    }

    public @This ConfigurationTreeBuilder addFloat(final @NotNull String name,
                                                   final float defaultValue) {
        return this.addFloat(name, defaultValue, null, null);
    }

    public @This ConfigurationTreeBuilder addFloat (final @NotNull String name,
                                                     final @Nullable Float min,
                                                     final @Nullable Float max) {
        return this.addFloat(name, 0.0F, min, max);
    }

    public @This ConfigurationTreeBuilder addFloat(final @NotNull String name,
                                                   final float defaultValue,
                                                   final @Nullable Float minValue,
                                                   final @Nullable Float maxValue) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new FloatNode(name, this.currentNode, minValue, maxValue).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addBoolean(final @NotNull String name) {
        return this.addBoolean(name, false);
    }

    public @This ConfigurationTreeBuilder addBoolean(final @NotNull String name,
                                                     final boolean defaultValue) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new BooleanNode(name, this.currentNode).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addCharacter(final @NotNull String name) {
        return this.addCharacter(name, Character.MIN_VALUE);
    }

    public @This ConfigurationTreeBuilder addCharacter(final @NotNull String name,
                                                       final char defaultValue) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new CharacterNode(name, this.currentNode).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addLong(final @NotNull String name) {
        return this.addLong(name, 0L, null, null);
    }

    public @This ConfigurationTreeBuilder addLong(final @NotNull String name,
                                                  final long defaultValue) {
        return this.addLong(name, defaultValue, null, null);
    }

    public @This ConfigurationTreeBuilder addLong(final @NotNull String name,
                                                  final @Nullable Long min,
                                                  final @Nullable Long max) {
        return this.addLong(name, 0L, min, max);
    }

    public @This ConfigurationTreeBuilder addLong(final @NotNull String name,
                                                  final long defaultValue,
                                                  final @Nullable Long min,
                                                  final @Nullable Long max) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new LongNode(name, this.currentNode, min, max).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addBigDecimal(final @NotNull String name) {
        return this.addBigDecimal(name, BigDecimal.ZERO, null, null);
    }

    public @This ConfigurationTreeBuilder addBigDecimal(final @NotNull String name,
                                                        final @NotNull BigDecimal defaultValue) {
        return this.addBigDecimal(name, defaultValue, null, null);
    }

    public @This ConfigurationTreeBuilder addBigDecimal(final @NotNull String name,
                                                        final @Nullable BigDecimal min,
                                                        final @Nullable BigDecimal max) {
        return this.addBigDecimal(name, BigDecimal.ZERO, min, max);
    }

    public @This ConfigurationTreeBuilder addBigDecimal(final @NotNull String name,
                                                        final @NotNull BigDecimal defaultValue,
                                                        final @Nullable BigDecimal min,
                                                        final @Nullable BigDecimal max) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new BigDecimalNode(name, this.currentNode, min, max).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addBigInteger(final @NotNull String name) {
        return this.addBigInteger(name, BigInteger.ZERO, null, null);
    }

    public @This ConfigurationTreeBuilder addBigInteger(final @NotNull String name,
                                                        final @NotNull BigInteger defaultValue) {
        return this.addBigInteger(name, defaultValue, null, null);
    }

    public @This ConfigurationTreeBuilder addBigInteger(final @NotNull String name,
                                                        final @Nullable BigInteger min,
                                                        final @Nullable BigInteger max) {
        return this.addBigInteger(name, BigInteger.ZERO, min, max);
    }

    public @This ConfigurationTreeBuilder addBigInteger(final @NotNull String name,
                                                        final @NotNull BigInteger defaultValue,
                                                        final @Nullable BigInteger min,
                                                        final @Nullable BigInteger max) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new BigIntegerNode(name, this.currentNode, min, max).setDefaultValue(defaultValue));
        return this;
    }

    public @This <E extends Enum<E>> ConfigurationTreeBuilder addEnum(final @NotNull String name,
                                                                      final @NotNull E defaultValue,
                                                                      final @NotNull Function<String, E> parseFunction) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new EnumNode<E>(name, this.currentNode) {
                            @Override
                            public E parse(@Nullable String value) {
                                return parseFunction.apply(value);
                            }
                        }.setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addString(final @NotNull String name) {
        return this.addString(name, null);
    }

    public @This ConfigurationTreeBuilder addString(final @NotNull String name,
                                                    final @Nullable String defaultValue) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new StringNode(name, this.currentNode).setDefaultValue(defaultValue));
        return this;
    }

    public @This <T extends ConfigurationSerializable> ConfigurationTreeBuilder addSerializable(final @NotNull String name,
                                                                                                final @NotNull Class<T> serializableClass) {
        return this.addSerializable(name, serializableClass,null);
    }

    public @This <T extends ConfigurationSerializable> ConfigurationTreeBuilder addSerializable(final @NotNull String name,
                                                                                                final @NotNull Class<T> serializableClass,
                                                                                                final @Nullable T defaultValue) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new SerializableNode<>(name, this.currentNode, serializableClass).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addWildcardList(final @NotNull String name) {
        return this.addWildcardList(name, null);
    }

    public @This ConfigurationTreeBuilder addWildcardList(final @NotNull String name,
                                                          final @Nullable List<?> defaultValue) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new WildcardListNode(name, this.currentNode).setDefaultValue(defaultValue));
        return this;
    }

    public @This <T> ConfigurationTreeBuilder addObject(final @NotNull String name,
                                                        final @NotNull Class<T> objectClass) {
        return this.addObject(name, objectClass, null);
    }

    public @This <T> ConfigurationTreeBuilder addObject(final @NotNull String name,
                                                        final @NotNull Class<T> objectClass,
                                                        final @Nullable T defaultValue) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(new ObjectNode<>(name, this.currentNode, objectClass).setDefaultValue(defaultValue));
        return this;
    }

    public @This ConfigurationTreeBuilder addVector(final @NotNull String name) {
        return this.addVector(name, null);
    }

    public @This ConfigurationTreeBuilder addVector(final @NotNull String name,
                                                    final @Nullable Vector defaultValue) {
        return this.addSimpleNode(name, defaultValue, ConfigurationSection::getVector);
    }

    public @This ConfigurationTreeBuilder addOfflinePlayer(final @NotNull String name) {
        return this.addOfflinePlayer(name, null);
    }

    public @This ConfigurationTreeBuilder addOfflinePlayer(final @NotNull String name,
                                                           final @Nullable OfflinePlayer defaultValue) {
        return this.addSimpleNode(name, defaultValue, ConfigurationSection::getOfflinePlayer);
    }

    public @This ConfigurationTreeBuilder addItemStack(final @NotNull String name) {
        return this.addItemStack(name, null);
    }

    public @This ConfigurationTreeBuilder addItemStack(final @NotNull String name,
                                                       final @Nullable ItemStack defaultValue) {
        return this.addSimpleNode(name, defaultValue, ConfigurationSection::getItemStack);
    }

    public @This ConfigurationTreeBuilder addColor(final @NotNull String name) {
        return this.addColor(name, null);
    }

    public @This ConfigurationTreeBuilder addColor(final @NotNull String name,
                                                   final @Nullable Color defaultValue) {
        return this.addSimpleNode(name, defaultValue, ConfigurationSection::getColor);
    }

    public @This ConfigurationTreeBuilder addLocation(final @NotNull String name) {
        return this.addLocation(name, null);
    }

    public @This ConfigurationTreeBuilder addLocation(final @NotNull String name,
                                                      final @Nullable Location defaultValue) {
        return this.addSimpleNode(name, defaultValue, ConfigurationSection::getLocation);
    }

    public @This <T> ConfigurationTreeBuilder addSimpleNode(final @NotNull String name,
                                                            final @Nullable T defaultValue,
                                                            final @NotNull TriFunction<Configuration, String, T, T> getFromConfig) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(Node.getSimpleNode(name, this.currentNode, getFromConfig).setDefaultValue(defaultValue));
        return this;
    }

    public @This <T> ConfigurationTreeBuilder addSimpleList(final @NotNull String name,
                                                            final @NotNull BiFunction<Configuration, String, List<T>> getFromConfig) {
        return this.addSimpleList(name, null, getFromConfig);
    }

    public @This <T> ConfigurationTreeBuilder addSimpleList(final @NotNull String name,
                                                            final @Nullable List<T> defaultValues,
                                                            final @NotNull BiFunction<Configuration, String, List<T>> getFromConfig) {
        this.requireCurrentlyBuildingConfiguration();
        this.navigateTo(ListNode.getSimpleListNode(name, this.currentNode, getFromConfig).setDefaultValue(defaultValues));
        return this;
    }

    public @This ConfigurationTreeBuilder addStringList(final @NotNull String name) {
        return this.addStringList(name, null);
    }

    public @This ConfigurationTreeBuilder addStringList(final @NotNull String name,
                                                        final @Nullable List<String> defaultValues) {
        return this.addSimpleList(name, defaultValues, ConfigurationSection::getStringList);
    }

    public @This ConfigurationTreeBuilder addIntList(final @NotNull String name) {
        return this.addIntList(name, null);
    }

    public @This ConfigurationTreeBuilder addIntList(final @NotNull String name,
                                                     final @Nullable List<Integer> defaultValues) {
        return this.addSimpleList(name, defaultValues, ConfigurationSection::getIntegerList);
    }

    public @This ConfigurationTreeBuilder addBooleanList(final @NotNull String name) {
        return this.addBooleanList(name, null);
    }

    public @This ConfigurationTreeBuilder addBooleanList(final @NotNull String name,
                                                         final @Nullable List<Boolean> defaultValues) {
        return this.addSimpleList(name, defaultValues, ConfigurationSection::getBooleanList);
    }

    public @This ConfigurationTreeBuilder addDoubleList(final @NotNull String name) {
        return this.addDoubleList(name, null);
    }

    public @This ConfigurationTreeBuilder addDoubleList(final @NotNull String name,
                                                        final @Nullable List<Double> defaultValues) {
        return this.addSimpleList(name, defaultValues, ConfigurationSection::getDoubleList);
    }

    public @This ConfigurationTreeBuilder addFloatList(final @NotNull String name) {
        return this.addFloatList(name, null);
    }

    public @This ConfigurationTreeBuilder addFloatList(final @NotNull String name,
                                                       final @Nullable List<Float> defaultValues) {
        return this.addSimpleList(name, defaultValues, ConfigurationSection::getFloatList);
    }

    public @This ConfigurationTreeBuilder addLongList(final @NotNull String name) {
        return this.addLongList(name, null);
    }

    public @This ConfigurationTreeBuilder addLongList(final @NotNull String name,
                                                      final @Nullable List<Long> defaultValues) {
        return this.addSimpleList(name, defaultValues, ConfigurationSection::getLongList);
    }

    public @This ConfigurationTreeBuilder addByteList(final @NotNull String name) {
        return this.addByteList(name, null);
    }

    public @This ConfigurationTreeBuilder addByteList(final @NotNull String name,
                                                      final @Nullable List<Byte> defaultValues) {
        return this.addSimpleList(name, defaultValues, ConfigurationSection::getByteList);
    }

    public @This ConfigurationTreeBuilder addShortList(final @NotNull String name) {
        return this.addShortList(name, null);
    }

    public @This ConfigurationTreeBuilder addShortList(final @NotNull String name,
                                                       final @Nullable List<Short> defaultValues) {
        return this.addSimpleList(name, defaultValues, ConfigurationSection::getShortList);
    }

    public @This ConfigurationTreeBuilder addCharacterList(final @NotNull String name) {
        return this.addCharacterList(name, null);
    }
    public @This ConfigurationTreeBuilder addCharacterList(final @NotNull String name,
                                                           final @Nullable List<Character> defaultValues) {
        return this.addSimpleList(name, defaultValues, ConfigurationSection::getCharacterList);
    }

    public @This ConfigurationTreeBuilder addMapList(final @NotNull String name) {
        return this.addMapList(name, null);
    }

    public @This ConfigurationTreeBuilder addMapList(final @NotNull String name,
                                                     final List<Map<?, ?>> defaultValues) {
        return this.addSimpleList(name, defaultValues, ConfigurationSection::getMapList);
    }

    public @This ConfigurationTreeBuilder removeNode(final @NotNull String name) {
        this.requireCurrentlyBuildingConfiguration();
        final @NotNull Optional<Node<?>> childNode = this.currentNode.getChildren().stream()
                .filter(node -> name.equals(node.getName()))
                .findAny();
        if (childNode.isPresent()) {
            log.fine("Removed node " + childNode.get().getBasePath() + " from " + this.currentNode.getBasePath() + ", node is now orphaned!");
            this.currentNode.remove(childNode.get());
        } else {
            log.warning("No matching child node was found during remove.");
        }
        return this;
    }

    public @This ConfigurationTreeBuilder removeNode() {
        this.requireCurrentlyBuildingConfiguration();
        if (this.currentNode == this.rootNode || this.currentNode.getParent() == null) {
            throw new IllegalStateException("Cannot remove root node");
        }
        Node<?> parentNode = this.currentNode.getParent();
        log.fine("Removed node " + this.currentNode.getBasePath() + " from " + parentNode.getBasePath() + ", node is now orphaned!");
        parentNode.remove(this.currentNode);
        this.navigateTo(parentNode);
        return this;
    }

    public @This <X extends Node<?> & MovableNode> ConfigurationTreeBuilder addCustomNode(final @NotNull X customNode) {
        this.requireCurrentlyBuildingConfiguration();
        customNode.moveTo(this.currentNode);
        this.navigateTo(customNode);
        return this;
    }

    public @This <E extends Enum<E>, T> ConfigurationTreeBuilder addSwitch(final @NotNull String name,
                                                                           final @NotNull Class<T> valueClass,
                                                                           final @Nullable T defaultValue,
                                                                           final @Nullable Map<E, T> subKeys) {
        this.requireCurrentlyBuildingConfiguration();
        final SwitchNode<E, T> switchNode = new SwitchNode<>(name, this.currentNode);
        switchNode.setDefaultValue(defaultValue);
        this.navigateTo(switchNode);
        if (subKeys != null) {
            for (E key : subKeys.keySet()) {
                T subKeyValue = subKeys.get(key);
                Node<T> subKey = Node.getSimpleNode(key.name().toLowerCase(Locale.ROOT), this.currentNode, (config, path, $default) -> config.getObject(path, valueClass, $default));
                subKey.setDefaultValue(subKeyValue);
                switchNode.registerSubNode(key, subKey);
            }
        }
        return this;
    }

    public @This <T> ConfigurationTreeBuilder addDifficultySwitch(final @NotNull String name,
                                                                  final @NotNull Class<T> valueClass) {
        return this.addDifficultySwitch(name, valueClass, null, null, null, null, null);
    }

    public @This <T> ConfigurationTreeBuilder addDifficultySwitch(final @NotNull String name,
                                                                  final @NotNull Class<T> valueClass,
                                                                  final @Nullable T defaultValue) {
        return this.addDifficultySwitch(name, valueClass, defaultValue, defaultValue, defaultValue, defaultValue, defaultValue);
    }

    public @This <T> ConfigurationTreeBuilder addDifficultySwitch(final @NotNull String name,
                                                                  final @NotNull Class<T> valueClass,
                                                                  final @Nullable T defaultValue,
                                                                  final @Nullable T defaultPeacefulValue,
                                                                  final @Nullable T defaultEasyValue,
                                                                  final @Nullable T defaultNormalValue,
                                                                  final @Nullable T defaultHardValue) {
        this.requireCurrentlyBuildingConfiguration();
        final DifficultyNode<T> newNode = new DifficultyNode<>(name, this.currentNode) {
            private final @NotNull Node<T> peacefulNode = Node.getSimpleNode(Difficulty.PEACEFUL.name().toLowerCase(Locale.ROOT), this, (c, p, d) -> Node.getConfiguredValue(valueClass, c, p, d));
            private final @NotNull Node<T> easyNode = Node.getSimpleNode(Difficulty.EASY.name().toLowerCase(Locale.ROOT), this, (c, p, d) -> Node.getConfiguredValue(valueClass, c, p, d));
            private final @NotNull Node<T> normalNode = Node.getSimpleNode(Difficulty.NORMAL.name().toLowerCase(Locale.ROOT), this, (c, p, d) -> Node.getConfiguredValue(valueClass, c, p, d));
            private final @NotNull Node<T> hardNode = Node.getSimpleNode(Difficulty.HARD.name().toLowerCase(Locale.ROOT), this, (c, p, d) -> Node.getConfiguredValue(valueClass, c, p, d));
            @Override
            public @NotNull Node<T> getPeacefulNode() {
                return peacefulNode;
            }

            @Override
            public @NotNull Node<T> getEasyNode() {
                return easyNode;
            }

            @Override
            public @NotNull Node<T> getNormalNode() {
                return normalNode;
            }

            @Override
            public @NotNull Node<T> getHardNode() {
                return hardNode;
            }
        };
        newNode.setDefaultValue(defaultValue);
        newNode.getPeacefulNode().setDefaultValue(defaultPeacefulValue);
        newNode.getEasyNode().setDefaultValue(defaultEasyValue);
        newNode.getNormalNode().setDefaultValue(defaultNormalValue);
        newNode.getHardNode().setDefaultValue(defaultHardValue);
        this.navigateTo(newNode);
        return this;
    }
}
