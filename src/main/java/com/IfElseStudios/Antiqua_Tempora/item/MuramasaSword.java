package com.IfElseStudios.Antiqua_Tempora.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class MuramasaSword extends SwordItem {

    public MuramasaSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    // 1. Нажатие ПКМ: начинаем использование (зарядку)
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(stack);
    }

    // 2. Сколько можно держать кнопку (очень долго, чтобы игрок мог держать сколько захочет)
    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    // 3. Анимация зарядки (SPEAR выглядит как подготовка сильного удара)
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.SPEAR;
    }

    // 4. Отпускание кнопки ПКМ: сам удар
    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        // Сколько тиков игрок держал кнопку
        int chargeTicks = this.getUseDuration(stack) - timeLeft;
        if (chargeTicks < 5) return; // Слишком мало держал (защита от случайных кликов)

        // 40 тиков = 2 секунды (полный заряд). Считаем процент заряда (от 0.0 до 1.0)
        float chargeFraction = Math.min((float) chargeTicks / 40.0F, 1.0F);
        
        // Урон: базовый 10 + бонус до 30 (итого от 10 до 40)
        float damage = 10.0F + (30.0F * chargeFraction);

        if (!level.isClientSide()) {
            Vec3 lookVec = player.getLookAngle();
            
            // Центр удара смещен на 2 блока вперед по направлению взгляда
            Vec3 attackCenter = player.position().add(0, player.getEyeHeight() * 0.5, 0).add(lookVec.scale(2.0));
            
            // Создаем коробку (хитбокс) радиусом 3 блока от центра
            AABB hitbox = new AABB(
                    attackCenter.x - 3.0, attackCenter.y - 1.5, attackCenter.z - 3.0,
                    attackCenter.x + 3.0, attackCenter.y + 1.5, attackCenter.z + 3.0
            );

            // Ищем всех живых существ в этой зоне, кроме самого игрока
            List<LivingEntity> targets = level.getEntitiesOfClass(LivingEntity.class, hitbox, e -> e != player && e.isAlive());

            // Создаем магический урон (он пробивает обычную броню и щиты в Майне)
            DamageSource damageSource = level.damageSources().magic();

            for (LivingEntity target : targets) {
                // Если у цели есть щит, отключаем его принудительно (как делает топор)
                if (target instanceof Player targetPlayer && targetPlayer.isBlocking()) {
                    targetPlayer.disableShield(true);
                }
                
                if (target.hurt(damageSource, damage)) {
                    // Отбрасывание в направлении взгляда игрока (зависит от заряда)
                    target.knockback(0.5 * chargeFraction, -lookVec.x, -lookVec.z);
                }
            }

            // Звук мощного взмаха
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 1.0F, 0.5F + (chargeFraction * 0.5F));
            
            // Частицы SWEEP_ATTACK перед игроком
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, attackCenter.x, attackCenter.y, attackCenter.z, (int)(5 * chargeFraction) + 1, 1.0, 0.5, 1.0, 0.0);
            }

            // Кулдаун после использования: 30 тиков (1.5 секунды), чтобы нельзя было спамить
            player.getCooldowns().addCooldown(this, 30);
        }
    }
}