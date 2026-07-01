package com.IfElseStudios.Antiqua_Tempora.event;

import com.IfElseStudios.Antiqua_Tempora.Antiqua_Tempora;
import com.IfElseStudios.Antiqua_Tempora.client.renderer.KingArthurRenderer;
import com.IfElseStudios.Antiqua_Tempora.init.ModEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Antiqua_Tempora.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.KING_ARTHUR.get(), KingArthurRenderer::new);
    }
}
