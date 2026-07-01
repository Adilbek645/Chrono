package com.IfElseStudios.Antiqua_Tempora.client.renderer;

import com.IfElseStudios.Antiqua_Tempora.entity.KingArthurBoss;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class KingArthurRenderer extends MobRenderer<KingArthurBoss, HumanoidModel<KingArthurBoss>> {
    // Временная текстура Стива/Зомби до добавления своей
    private static final ResourceLocation TEXTURE = new ResourceLocation("minecraft:textures/entity/zombie/zombie.png");

    public KingArthurRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);
    }

    @Override
    public ResourceLocation getTextureLocation(KingArthurBoss entity) {
        return TEXTURE;
    }
}
