package me.sargunvohra.mcmods.alwaysdroploot.mixin;


import me.sargunvohra.mcmods.alwaysdroploot.AlwaysDropLoot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
  @Shadow
  protected int lastHurtByPlayerTime;

  @Inject(
    at = @At("RETURN"),
    method = "isAlwaysExperienceDropper",
    cancellable = true
  )
  private void alwaysDropXp(CallbackInfoReturnable<Boolean> cir) {
    var enabled =
      ((LivingEntity) (Object) this).level().getGameRules()
        .getRule(AlwaysDropLoot.ALWAYS_DROP_XP).get();
    if(enabled)
    {
      cir.setReturnValue(true);
    }
  }
  @ModifyArg(
    at = @At(
      value = "INVOKE",
      target = "Lnet/minecraft/world/entity/ExperienceOrb;award(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)V"
    ) ,
    method = "dropExperience",
    index = 2
  )
  private int adjustXpAmount(int originalXpAmount) {
    if (this.lastHurtByPlayerTime <= 0) {
      var modifier = ((LivingEntity) (Object) this).level().getGameRules()
        .getRule(AlwaysDropLoot.PASSIVE_XP_MODIFIER).get();
      return (int) Math.round(originalXpAmount * modifier);
    } else {
      return originalXpAmount;
    }
  }
}
