package com.IfElseStudios.Antiqua_Tempora.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class MasamuneSword extends SwordItem {

    public MasamuneSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            // У огненного меча другая способность: Огнестойкость и Сила
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 0));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100, 1));

            player.getCooldowns().addCooldown(this, 400); // Кулдаун дольше (20 сек)
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}