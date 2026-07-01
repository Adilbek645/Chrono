package com.IfElseStudios.Antiqua_Tempora.entity;

import com.IfElseStudios.Antiqua_Tempora.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class KingArthurBoss extends Monster {

    private final ServerBossEvent bossEvent = (ServerBossEvent)(new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.YELLOW, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);
    private int attackTimer = 0;

    public KingArthurBoss(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 500;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new ArthurSpecialAttacksGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void setCustomName(Component name) {
        super.setCustomName(name);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean hitByPlayer) {
        super.dropCustomDeathLoot(source, looting, hitByPlayer);
        // Выпадение Экскалибура
        this.spawnAtLocation(new ItemStack(ModItems.EXCALIBUR.get()));
    }

    // Класс для специальных атак (Рывок, Призыв, Удар светом)
    static class ArthurSpecialAttacksGoal extends Goal {
        private final KingArthurBoss boss;
        private int attackCooldown = 100;

        public ArthurSpecialAttacksGoal(KingArthurBoss boss) {
            this.boss = boss;
        }

        @Override
        public boolean canUse() {
            return this.boss.getTarget() != null && this.boss.getTarget().isAlive();
        }

        @Override
        public void tick() {
            LivingEntity target = this.boss.getTarget();
            if (target == null) return;

            if (attackCooldown > 0) {
                attackCooldown--;
                return;
            }

            double distance = this.boss.distanceToSqr(target);
            int attackType = this.boss.random.nextInt(3);

            if (attackType == 0) {
                // Призыв скелетов
                for (int i = 0; i < 3; i++) {
                    Skeleton skeleton = EntityType.SKELETON.create(this.boss.level());
                    if (skeleton != null) {
                        skeleton.moveTo(this.boss.getX() + (this.boss.random.nextDouble() - 0.5) * 4, this.boss.getY(), this.boss.getZ() + (this.boss.random.nextDouble() - 0.5) * 4, 0.0F, 0.0F);
                        this.boss.level().addFreshEntity(skeleton);
                    }
                }
            } else if (attackType == 1 && distance > 16.0) {
                // Рывок
                Vec3 direction = new Vec3(target.getX() - this.boss.getX(), 0, target.getZ() - this.boss.getZ()).normalize().scale(2.5);
                this.boss.setDeltaMovement(this.boss.getDeltaMovement().add(direction.x, 0.5, direction.z));
            } else {
                // Удар молнией
                LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(this.boss.level());
                if (lightning != null) {
                    lightning.moveTo(target.position());
                    this.boss.level().addFreshEntity(lightning);
                }
            }

            attackCooldown = 60 + this.boss.random.nextInt(60); // 3 to 6 seconds
        }
    }
}
