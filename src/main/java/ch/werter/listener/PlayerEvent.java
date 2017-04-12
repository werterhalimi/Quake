package ch.werter.listener;

import ch.werter.Quake;
import ch.werter.QuakePlayer;
import ch.werter.Status;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

/**
 * Created by werter on 12.04.2017.
 */
public class PlayerEvent implements Listener {

    private Quake quake;

    public  PlayerEvent (Quake quake){
        this.quake = quake;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        if(!quake.isStatus(Status.WAITING_FOR_PLAYER)){
            event.getPlayer().kickPlayer("La partie a déjà commencé");
            return;
        }
        Player player = event.getPlayer();
        quake.registerPlayer(event.getPlayer());
        player.teleport((Location) quake.getConfig().get("spawn"));
        player.sendMessage(ChatColor.YELLOW + "[QUAKE]" + ChatColor.GRAY + " vous avez rejoint le jeu quake.");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "[QUAKE] " + ChatColor.AQUA + player.getName() + ChatColor.GRAY + " a rejoint la partie");
    }

    @EventHandler
    public  void onPlayerInteract(PlayerInteractEvent event){
        if(!quake.isStatus(Status.INGAME))
            return;
        if(!event.hasItem() || !event.getItem().getType().equals(Material.WOOD_HOE) || !quake.getQuakePlayer(event.getPlayer()).canShoot())
            return;
        final Player player = event.getPlayer();
        final QuakePlayer quakePlayer = quake.getQuakePlayer(player);
        for (Block block : player.getLineOfSight((Set<Material>) null, 100)) {
            player.getWorld().playEffect(block.getLocation(), Effect.BOW_FIRE,1);
            for (Player killed : Bukkit.getOnlinePlayers())
                if (killed.getLocation().distance(block.getLocation()) < 0.5) {
                    quake.getQuakePlayer(killed).kill(player);
                    quakePlayer.addKill(1);
                    break;
                }
        }
        quakePlayer.setCanShoot(false);
        new BukkitRunnable(){
            public void run() {
                if(player.getExp() == 1){
                    quakePlayer.setCanShoot(true);
                    this.cancel();
                    return;
                }
                player.setExp(player.getExp() + (float) 0.25);
            }
        }.runTaskTimer(this.quake,20,20);
    }


}
