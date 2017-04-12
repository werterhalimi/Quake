package ch.werter;

import ch.werter.listener.PlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Created by werter on 12.04.2017.
 */
public class Quake extends JavaPlugin {

    private HashMap<Player,QuakePlayer> quakePlayers = new HashMap<Player, QuakePlayer>();

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerEvent(this),this);
    }

    public void registerPlayer(Player player){
        this.quakePlayers.put(player,new QuakePlayer());
    }

    public QuakePlayer getQuakePlayer(Player player){
        return  this.quakePlayers.get(player);
    }
}
