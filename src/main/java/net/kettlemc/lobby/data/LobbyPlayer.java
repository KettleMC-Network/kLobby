package net.kettlemc.lobby.data;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "players")
public class LobbyPlayer {

    @Id
    private String uuid;

    private double x;
    private double y;
    private double z;

    private float yaw;
    private float pitch;

    private String world;

    private Visibility visibility;

    public LobbyPlayer(String uuid, Location location) {
        this.uuid = uuid;
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.world = location.getWorld().getName();
        this.visibility = Visibility.VISIBLE;
    }

    @Deprecated
    public LobbyPlayer() {
    }

    public String uuid() {
        return uuid;
    }

    public Location location() {
        return new Location(world == null ? Bukkit.getWorlds().get(0) : Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public void location(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.world = location.getWorld().getName();
    }

    public Visibility visibility() {
        return visibility;
    }

    public void visibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public enum Visibility {
        VISIBLE,
        HIDDEN
    }

}
