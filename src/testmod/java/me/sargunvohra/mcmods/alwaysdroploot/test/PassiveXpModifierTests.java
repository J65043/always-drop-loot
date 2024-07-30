package me.sargunvohra.mcmods.alwaysdroploot.test;

import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.*;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.entity.StructureBlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class PassiveXpModifierTests {

  private static final String TEMPLATE =
    "always-drop-loot-testmod:1x2x1_chamber";

  private void testCase(
    GameTestHelper helper,
    boolean alwaysDropXp,
    double passiveXpModifier,
    boolean asPlayer,
    int expectedPoints
  ) {
    TestUtil.runCommand(
      helper,
      "gamerule always-drop-loot:alwaysDropXp " + alwaysDropXp
    );
    TestUtil.runCommand(
      helper,
      "gamerule always-drop-loot:passiveXpModifier " + passiveXpModifier
    );
    var entity = helper.spawn(EntityType.CREEPER, 1, 2, 1);
    if (asPlayer) {
      var damage = entity
        .damageSources()
        .playerAttack(helper.makeMockPlayer(GameType.SURVIVAL));
      entity.hurt(damage, 1000f);
    } else {
      entity.kill();
    }
    var points = helper
      .getLevel()
      .getEntitiesOfClass(
        ExperienceOrb.class,
        AABB.unitCubeFromLowerCorner(helper.absoluteVec(new Vec3(1, 2, 1)))
      )
      .stream()
      .map(ExperienceOrb::getValue)
      .reduce(0, Integer::sum);
    if (expectedPoints != points) {
      helper.fail("Expected " + expectedPoints + " points, got " + points);
    } else {
      helper.succeed();
    }
  }

  @GameTest(template = TEMPLATE, batch = "alwaysDropXp=false;lootDropMode=0.5")
  public void onlyWhenAlwaysDropXpWithPlayer(GameTestHelper helper) {
    testCase(helper, false, 0.5, true, 5);
  }

  @GameTest(template = TEMPLATE, batch = "alwaysDropXp=false;lootDropMode=0.5")
  public void onlyWhenAlwaysDropXpWithoutPlayer(GameTestHelper helper) {
    testCase(helper, false, 0.5, false, 0);
  }

  @GameTest(template = TEMPLATE, batch = "alwaysDropXp=true;lootDropMode=0.5")
  public void halfModifierWithPlayer(GameTestHelper helper) {
    testCase(helper, true, 0.5, true, 5);
  }

  @GameTest(template = TEMPLATE, batch = "alwaysDropXp=true;lootDropMode=0.5")
  public void halfModifierWithoutPlayer(GameTestHelper helper) {
    testCase(helper, true, 0.5, false, 3);
  }

  @GameTest(template = TEMPLATE, batch = "alwaysDropXp=true;lootDropMode=1.0")
  public void defaultModifierWithPlayer(GameTestHelper helper) {
    testCase(helper, true, 1.0, true, 5);
  }

  @GameTest(template = TEMPLATE, batch = "alwaysDropXp=true;lootDropMode=1.0")
  public void defaultModifierWithoutPlayer(GameTestHelper helper) {
    testCase(helper, true, 1.0, false, 5);
  }

  @GameTest(template = TEMPLATE, batch = "alwaysDropXp=true;lootDropMode=2.0")
  public void doubleModifierWithPlayer(GameTestHelper helper) {
    testCase(helper, true, 2.0, true, 5);
  }

  @GameTest(template = TEMPLATE, batch = "alwaysDropXp=true;lootDropMode=2.0")
  public void doubleModifierWithoutPlayer(GameTestHelper helper) {
    testCase(helper, true, 2.0, false, 10);
  }
}
