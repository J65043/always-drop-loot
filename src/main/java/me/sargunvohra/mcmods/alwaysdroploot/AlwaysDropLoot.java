package me.sargunvohra.mcmods.alwaysdroploot;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule;
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule;
import net.minecraft.world.level.GameRules;

public class AlwaysDropLoot implements ModInitializer {

  public static final GameRules.Key<GameRules.BooleanValue> ALWAYS_DROP_XP = GameRuleRegistry.register(
    "always-drop-loot:alwaysDropXp",
    GameRules.Category.DROPS,
    GameRuleFactory.createBooleanRule(true)
  );

  public static final GameRules.Key<DoubleRule> PASSIVE_XP_MODIFIER = GameRuleRegistry.register(
    "always-drop-loot:passiveXpModifier",
    GameRules.Category.DROPS,
    GameRuleFactory.createDoubleRule(1.0)
  );

  public static final GameRules.Key<EnumRule<LootDropMode>> LOOT_DROP_MODE = GameRuleRegistry.register(
    "always-drop-loot:lootDropMode",
    GameRules.Category.DROPS,
    GameRuleFactory.createEnumRule(LootDropMode.ALWAYS_AS_PLAYER)
  );

  @Override
  public void onInitialize() {}
}
