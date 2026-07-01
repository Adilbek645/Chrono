package com.IfElseStudios.Antiqua_Tempora.init;

import com.IfElseStudios.Antiqua_Tempora.Antiqua_Tempora;
import com.IfElseStudios.Antiqua_Tempora.entity.KingArthurBoss;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Antiqua_Tempora.MODID);

    public static final RegistryObject<EntityType<KingArthurBoss>> KING_ARTHUR = ENTITIES.register("king_arthur",
            () -> EntityType.Builder.of(KingArthurBoss::new, MobCategory.MONSTER)
                    .sized(0.8F, 2.0F) // Размер хитбокса
                    .build("king_arthur"));
}
