package ch.werter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by werter on 12.04.2017.
 */
public class QuakePlayer {

    private  Quake quake;
    private boolean canShoot;
    private Player player;
    private int kill;

    public QuakePlayer (Player player, Quake quake){
        this.quake = quake;
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }

    public int getKill(){
        return  this.kill;
    }

    public void addKill(int kill){
        this.kill =+ kill;
        if(kill == 25)
            quake.endGame(this);

    }

    public boolean canShoot() {
        return canShoot;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
    }

    public void kill(Player killer){
        this.getPlayer().teleport(((ArrayList<Location>) quake.getConfig().getList("spawn_random")).get(new Random().nextInt(3)));
        Bukkit.broadcastMessage(ChatColor.YELLOW + killer.getName() + " a tué " + player.getName());
    }

}
