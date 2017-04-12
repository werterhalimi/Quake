package ch.werter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;

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

    public void addKill(){
        this.kill++;
        player.setLevel(kill);
        if(kill >= 25) {
            quake.endGame(this);
        }

    }

    public boolean canShoot() {
        return canShoot;
    }

    public void setCanShoot(boolean canShoot) {
        this.canShoot = canShoot;
        if (!canShoot) {
            player.setExp(0);
            new BukkitRunnable() {
                public void run() {
                    if (player.getExp() == 1) {
                        setCanShoot(true);
                        this.cancel();
                        return;
                    }
                    player.setExp(player.getExp()+ (float) 0.25);
                }
            }.runTaskTimer(this.quake, 20, 20);
        }
    }
    public void kill(Player killer){
        this.getPlayer().teleport(((ArrayList<Location>) quake.getConfig().getList("spawn_random")).get(new Random().nextInt(3)));
        this.getPlayer().getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScore(killer.getName() + " kill: " + quake.getQuakePlayer(killer).getKill());
        Bukkit.broadcastMessage(ChatColor.YELLOW + killer.getName() + " a tué " + player.getName());
    }

}
