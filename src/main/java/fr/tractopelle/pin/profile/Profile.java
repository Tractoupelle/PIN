package fr.tractopelle.pin.profile;

import org.bukkit.entity.Player;

public class Profile {

    private Player player;
    private int failTime;

    public Profile(Player player) {
        this.player = player;
        this.failTime = 1;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getFailTime() {
        return failTime;
    }

    public void addFailTime() {
        this.failTime = this.failTime + 1;
    }
}
