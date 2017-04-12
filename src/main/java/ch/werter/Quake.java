package ch.werter;

import ch.werter.listener.PlayerEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by werter on 12.04.2017.
 */
public class Quake extends JavaPlugin {

    private HashMap<Player,QuakePlayer> quakePlayers = new HashMap<Player, QuakePlayer>();
    private Status status;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(new PlayerEvent(this),this);
        this.status = Status.WAITING_FOR_PLAYER;
    }

    public void startGame(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.registerNewObjective("Quake","dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        for(Player player : Bukkit.getOnlinePlayers()){
            player.teleport(((ArrayList<Location>) getConfig().getList("spawn_random")).get(new Random().nextInt(3)));
            player.setScoreboard(scoreboard);
        }

    }

    public void endGame(QuakePlayer winner){
        setStatus(Status.END);
        for(Player player : Bukkit.getOnlinePlayers())
            player.teleport((Location) getConfig().get("spawn"));
        Bukkit.broadcastMessage(winner.getPlayer().getName() + " a gagné la partie");
    }

    public void registerPlayer(Player player){
        this.quakePlayers.put(player,new QuakePlayer(player,this));
    }

    public QuakePlayer getQuakePlayer(Player player){
        return  this.quakePlayers.get(player);
    }

    public boolean isStatus(Status status){
        return this.status == status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public Status getStatus(){
        return  this.status;
    }
}
