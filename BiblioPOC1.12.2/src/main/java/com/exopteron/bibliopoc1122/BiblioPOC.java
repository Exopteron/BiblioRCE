package com.exopteron.bibliopoc1122;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = BiblioPOC.MODID, name = BiblioPOC.NAME, version = BiblioPOC.VERSION)
public class BiblioPOC
{
    public static final String MODID = "bibliopoc1122";
    public static final String NAME = "Example Mod";
    public static final String VERSION = "1.0";

    @EventHandler
    public void init(FMLPostInitializationEvent event)
    {
        ClientCommandHandler.instance.registerCommand(new JarPOCCommand());
    }
}
