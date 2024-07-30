package me.sargunvohra.mcmods.alwaysdroploot.mixin;

import me.sargunvohra.mcmods.alwaysdroploot.AlwaysDropLoot;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootItemKilledByPlayerCondition.class)
public class KilledByPlayerLootConditionMixin {

  @Inject(
    at = @At("RETURN"),
    method = "test(Lnet/minecraft/world/level/storage/loot/LootContext;)Z",
    cancellable = true
  )
  private void lootDropMode(
    LootContext lootContext,
    CallbackInfoReturnable<Boolean> cir
  ) {
    switch (
      lootContext
        .getLevel()
        .getGameRules()
        .getRule(AlwaysDropLoot.LOOT_DROP_MODE)
        .get()
    ) {
      case VANILLA:
        break;
      case VANILLA_INVERSE:
        cir.setReturnValue(!cir.getReturnValue());
        break;
      case ALWAYS_AS_PLAYER:
        cir.setReturnValue(true);
        break;
      case NEVER_AS_PLAYER:
        cir.setReturnValue(false);
        break;
    }
  }
}
