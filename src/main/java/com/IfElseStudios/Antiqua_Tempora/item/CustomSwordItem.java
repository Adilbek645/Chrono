package com.IfElseStudios.Antiqua_Tempora.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.core.particles.ParticleTypes;

import java.util.List;

public class CustomSwordItem extends SwordItem {

    public CustomSwordItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    // Атака мечом наносит 1 сердце владельцу, игнорируя броню (!!!, урон то магический )
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide()) {
            // проверка для того чтобы игрок не умер
            if (attacker.getHealth() > 10.0F) {
                attacker.hurt(attacker.level().damageSources().magic(), 1.0F);
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    // Способность на правую кнопку мыши
    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!level.isClientSide()) {
            float damage = player.getHealth(); 
            float totalHeal = 0;
            float maxDamage = 25;

            float trueDamage = maxDamage - damage;

            // Ищем всех существ в радиусе 1.5 блоков вокруг игрока
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(1.5));

            for (LivingEntity entity : entities) {
                // проверка чтобы не били себя или собак и т.д
                if (entity != player && entity.isAlive() && !player.isAlliedTo(entity)) {
                    // Наносим урон
                    boolean hurt = entity.hurt(level.damageSources().playerAttack(player), trueDamage);
                    if (hurt) {
                        // Добавляем отхил
                        totalHeal += trueDamage * 0.50F;
                        
                        // Отбрасываем врага
                        double dx = player.getX() - entity.getX();
                        double dz = player.getZ() - entity.getZ();
                        entity.knockback(0.5D, dx, dz);
                    }
                }
            }

            // Лечим игрока, если был нанесен хоть какой-то урон
            if (totalHeal > 0) {
                player.heal(totalHeal);
            }

            // Эффекты
            
            // Звук взмаха мечом
            level.playSound(null, player.getX(), player.getY(), player.getZ(), 
                    SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.PLAYERS, 2.0F, 1.0F);

            //  Партиклы
            if (level instanceof ServerLevel serverLevel) {
                //  3 (!) Частицы
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK, 
                        player.getX(), player.getY() + 1.0, player.getZ(), 
                        3, 0.5, 0.0, 0.5, 0.0);
            }

            // Кулдаун (в тиках если что)
            player.getCooldowns().addCooldown(this, 100);
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}