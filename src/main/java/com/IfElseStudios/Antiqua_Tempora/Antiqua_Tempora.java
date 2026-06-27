package com.IfElseStudios.Antiqua_Tempora;

import com.IfElseStudios.Antiqua_Tempora.init.ModItems;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Antiqua_Tempora.MODID)
public class Antiqua_Tempora
{
    public static final String MODID = "antiqua_tempora_mod";

    private static final Logger LOGGER = LogManager.getLogger();

    public Antiqua_Tempora() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);
    }
}
