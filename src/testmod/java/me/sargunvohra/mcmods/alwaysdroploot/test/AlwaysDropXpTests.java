package me.sargunvohra.mcmods.alwaysdroploot.test;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.*;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.StructureBlockEntity;

public class AlwaysDropXpTests {

  private static final String TEMPLATE =
    "always-drop-loot-testmod:1x2x1_chamber";

  private void testCase(
    GameTestHelper helper,
    boolean enabled,
    boolean expectPresent
  ) {
    TestUtil.runCommand(
      helper,
      "gamerule always-drop-loot:alwaysDropXp " + enabled
    );
    TestUtil.runCommand(
      helper,
      "gamerule always-drop-loot:passiveXpModifier 1.0"
    );
    helper.spawn(EntityType.CREEPER, 1, 2, 1).kill();
    if (expectPresent) {
      helper.succeedIf(() ->
        helper.assertEntityPresent(EntityType.EXPERIENCE_ORB, 1, 2, 1)
      );
    } else {
      helper.succeedIf(() ->
        helper.assertEntityNotPresent(EntityType.EXPERIENCE_ORB, 1, 2, 1)
      );
    }
  }

  @GameTest(template = TEMPLATE, batch = "alwaysDropXp=true")
  public void enabled(GameTestHelper helper) {
    testCase(helper, true, true);
  }

  @GameTest(template = TEMPLATE, batch = "alwaysDropXp=false")
  public void disabled(GameTestHelper helper) {
    testCase(helper, false, false);
  }
}
