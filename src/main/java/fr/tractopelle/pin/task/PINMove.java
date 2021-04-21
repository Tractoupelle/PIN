package fr.tractopelle.pin.task;

import fr.tractopelle.pin.CorePlugin;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PINMove extends BukkitRunnable {

    private final CorePlugin corePlugin;

    public PINMove(CorePlugin corePlugin) {
        this.corePlugin = corePlugin;
    }

    @Override
    public void run() {

        if(!corePlugin.getPlayersManager().getInLogin().isEmpty()){

            for(Player player : corePlugin.getPlayersManager().getInLogin().keySet()){

                if(!corePlugin.getPlayersManager().getInLogin().get(player).equals(player.getLocation())){
                    player.teleport(corePlugin.getPlayersManager().getInLogin().get(player));
                }
            }
        }
    }
}
