package net.kettlemc.lobby.data;

import net.kettlemc.kcommon.bukkit.itemstack.base64.Base64ItemStack;
import org.bukkit.inventory.ItemStack;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.HashMap;

@Entity(name = "lobby_warp")
public class LobbyWarp {

    public static final HashMap<Integer, LobbyWarp> WARPS = new HashMap<>();

    private String name;
    private String version;
    private String modpack;

    @Id
    private int slot;
    private String warp;
    private String itemStackBase64;

    @Transient
    private ItemStack itemStack;

    public LobbyWarp(String name, String version, String modpack, int slot, String warp, ItemStack itemStack) {
        this.name = name;
        this.version = version;
        this.modpack = modpack;
        this.slot = slot;
        this.warp = warp;
        this.itemStack = itemStack;
        this.itemStackBase64 = Base64ItemStack.encode(itemStack);
    }

    @Deprecated
    public LobbyWarp() {
    }

    public String name() {
        return name;
    }

    public String version() {
        return version;
    }

    public String modpack() {
        return modpack;
    }

    public int slot() {
        return slot;
    }

    public String warp() {
        return warp;
    }

    public String itemStackBase64() {
        return itemStackBase64;
    }

    public ItemStack itemStack() {
        if (itemStack == null) {
            itemStack = Base64ItemStack.decode(itemStackBase64);
        }
        return itemStack;
    }


}
