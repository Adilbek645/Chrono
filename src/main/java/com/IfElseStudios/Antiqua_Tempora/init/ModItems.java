package com.IfElseStudios.Antiqua_Tempora.init;

import com.IfElseStudios.Antiqua_Tempora.Antiqua_Tempora;
import com.IfElseStudios.Antiqua_Tempora.item.CustomSwordItem;
import com.IfElseStudios.Antiqua_Tempora.item.ExcaliburSword;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Antiqua_Tempora.MODID);

    public static final RegistryObject<Item> DURENDAL = ITEMS.register("durendal",
            () -> new CustomSwordItem(Tiers.NETHERITE, 5, -2.4F, new Item.Properties()));

    public static final RegistryObject<Item> EXCALIBUR = ITEMS.register("excalibur",
            () -> new ExcaliburSword(Tiers.NETHERITE, 7, -1F, new Item.Properties()));

    public static final RegistryObject<Item> MURAMASA = ITEMS.register("muramasa",
            () -> new ExcaliburSword(Tiers.NETHERITE, 10, -2.9F, new Item.Properties()));

    public static final RegistryObject<Item> KING_ARTHUR_SPAWN_EGG = ITEMS.register("king_arthur_spawn_egg",
            () -> new net.minecraftforge.common.ForgeSpawnEggItem(com.IfElseStudios.Antiqua_Tempora.init.ModEntities.KING_ARTHUR, 0xFFD700, 0xFF0000, new Item.Properties()));

    public static final RegistryObject<Item> MASAMUNE = ITEMS.register("masamune",
            () -> new ExcaliburSword(Tiers.NETHERITE, 4, -0.5F, new Item.Properties()));
}