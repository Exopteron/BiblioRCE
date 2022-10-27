package com.exopteron.forgemodnew;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = TestMod.MODID, version = TestMod.VERSION, acceptableRemoteVersions = "*", dependencies = "")
public class TestMod {

    @Instance
    public static TestMod INSTANCE;
    public static final String MODID = "BiblioPOC";
    public static final String VERSION = "0.1.0";
    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("DIRT BLOCK >> " + Blocks.dirt.getUnlocalizedName());
    }
    @EventHandler
    public void init(FMLPostInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new POCExploitCommand());
    }
    @EventHandler
    public void wrold(FMLServerStartingEvent e) {
/*         CommandHandler ch = (CommandHandler) e.getServer().getCommandManager();
        ch.registerCommand(new CrayCommand());
        ch.registerCommand(new FuzzCommand()); */
    } 


}
