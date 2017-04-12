package ch.werter;

import org.bukkit.entity.Player;

/**
 * Created by werter on 12.04.2017.
 */
public class QuakePlayer {

    private Player player;

    public QuakePlayer (Player player){
        this.player = player;
    }

    public Player getPlayer(){
        return this.player;
    }
}
