package com.IfElseStudios.Antiqua_Tempora.event;

import com.IfElseStudios.Antiqua_Tempora.Antiqua_Tempora;
import com.IfElseStudios.Antiqua_Tempora.entity.KingArthurBoss;
import com.IfElseStudios.Antiqua_Tempora.init.ModEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Antiqua_Tempora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.KING_ARTHUR.get(), KingArthurBoss.createAttributes().build());
    }
}
