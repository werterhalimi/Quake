package ch.werter.listener;

import ch.werter.Quake;
import ch.werter.QuakePlayer;
import ch.werter.Status;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

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
    public void onPlayerDamage(EntityDamageEvent event){
        event.setCancelled(true);
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
        event.setJoinMessage(ChatColor.YELLOW + "[QUAKE] " + ChatColor.AQUA + player.getName() + ChatColor.GRAY + " a rejoint la partie");
        if(Bukkit.getOnlinePlayers().size() >= 2)
            quake.startGame();
    }


    @EventHandler
    public void foodLevelChange(FoodLevelChangeEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public  void onPlayerInteract(final PlayerInteractEvent event){
        if(!quake.isStatus(Status.INGAME))
            return;
        if(!event.hasItem() || !event.getItem().getType().equals(Material.WOOD_HOE) || !quake.getQuakePlayer(event.getPlayer()).canShoot()) {
            return;
        }
        System.out.println(event.getPlayer().getName());
        event.setCancelled(true);
        Player player = event.getPlayer();
        QuakePlayer quakePlayer = quake.getQuakePlayer(player);
        for (Block block : player.getLineOfSight((Set<Material>) null, 100)) {
            player.getWorld().spawnParticle(Particle.FLAME , block.getLocation().getX(),block.getLocation().getY(),block.getLocation().getZ(),1,0,0,0,0);
            for (Player killed : Bukkit.getOnlinePlayers())
                if (killed.getLocation().distance(block.getLocation()) < 1) {
                    if(killed == player)return;
                    quake.getQuakePlayer(killed).kill(player);
                    quakePlayer.addKill();
                    break;
                }
        }
        quakePlayer.setCanShoot(false);
    }
}
