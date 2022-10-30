package com.exopteron.bibliopoc1122;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
public class Utils {
    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP player() {
        return mc().player;
    }

    public static void sendPacket(String channel, ByteBuf data) {
        sendPacket(new FMLProxyPacket(new PacketBuffer(data), channel));
    }

    public static void sendPacket(Packet packet) {
        mc().getConnection().sendPacket(packet);
    }


    public static ByteBuf bufWriter(Object... data) {
        return bufWriter(Unpooled.buffer(0), data);
    }

    public static void sendPacket(String channel, Object... data) {
        sendPacket(channel, bufWriter(data));
    }

    public static ByteBuf bufWriter(ByteBuf buf, Object... data) {
        for (Object o : data) {
            if (o instanceof Integer) {
                buf.writeInt((Integer) o);
            } else if (o instanceof Byte) {
                buf.writeByte((Byte) o);
            } else if (o instanceof Short) {
                buf.writeShort((Short) o);
            } else if (o instanceof Float) {
                buf.writeFloat((Float) o);
            } else if (o instanceof String) {
                ByteBufUtils.writeUTF8String(buf, (String) o);
            } else if (o instanceof ItemStack) {
                ByteBufUtils.writeItemStack(buf, (ItemStack) o);
            } else if (o instanceof NBTTagCompound) {
                ByteBufUtils.writeTag(buf, (NBTTagCompound) o);
            } else if (o instanceof ByteBuf) {
                buf.writeBytes((ByteBuf) o);
            } else if (o instanceof Double) {
                buf.writeDouble((Double) o);
            } else if (o instanceof Boolean) {
                buf.writeBoolean((Boolean) o);
            } else if (o instanceof byte[]) {
                buf.writeBytes((byte[]) o);
            } else if (o instanceof Long) {
                buf.writeLong((Long) o);
            } else if (o instanceof int[]) {
                for (int i : (int[]) o) {
                    bufWriter(buf, i);
                }
            } else if (o instanceof byte[]) {
                for (byte i : (byte[]) o) {
                    bufWriter(buf, i);
                }
            }
        }
        return buf;
    }

    public static GuiContainer guiContainer() {
        return guiContainer(currentScreen());
    }

    public static GuiScreen currentScreen() {
        return mc().currentScreen;
    }
    public static NBTTagCompound nbtItem(ItemStack item) {
        return item.writeToNBT(new NBTTagCompound());
    }
    public static NBTTagCompound nbtItem(ItemStack item, int slot, String slotName) {
        NBTTagCompound tag = nbtItem(item);
        tag.setByte(slotName, (byte) slot);
        return tag;
    }
    public static GuiContainer guiContainer(GuiScreen screen) {
        return screen instanceof GuiContainer ? (GuiContainer) screen : null;
    }

    public static boolean isInGame() {
        return player() != null && mc().world != null;
    }
}
