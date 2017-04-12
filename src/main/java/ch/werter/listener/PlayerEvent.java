package ch.werter.listener;

import ch.werter.Quake;
import ch.werter.QuakePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

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
        Player player = event.getPlayer();
        quake.registerPlayer(event.getPlayer());
    }

}
