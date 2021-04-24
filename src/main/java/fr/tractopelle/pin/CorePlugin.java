package fr.tractopelle.pin;

import fr.tractopelle.pin.commands.command.PIN;
import fr.tractopelle.pin.commands.command.PINAdmin;
import fr.tractopelle.pin.config.Config;
import fr.tractopelle.pin.listeners.PlayerListener;
import fr.tractopelle.pin.profile.PlayersManager;
import fr.tractopelle.pin.task.PINMove;
import fr.tractopelle.pin.utils.Logger;
import fr.tractopelle.pin.utils.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin {

    private final Logger log = new Logger(this.getDescription().getFullName());
    private Config configuration;
    private Config pinConfiguration;
    private PlayersManager playersManager;
    private Title title;

    @Override
    public void onEnable() {

        this.init();

    }

    private void init() {

        this.configuration = new Config(this, "config");

        this.pinConfiguration = new Config(this, "pin");

        this.registerCommands();

        this.registerListeners();

        this.playersManager = new PlayersManager(this);

        new PINMove(this).runTaskTimerAsynchronously(this, 20, 20);

        this.title = new Title(this);

        log.info("=======================================", Logger.LogType.SUCCESS);
        log.info(" Plugin initialization in progress...", Logger.LogType.SUCCESS);
        log.info(" Author: Tractopelle#4020", Logger.LogType.SUCCESS);
        log.info("=======================================", Logger.LogType.SUCCESS);

        System.out.println(getPinConfiguration().getConfigurationSection("PLAYERS").getKeys(false));

    }

    @Override
    public void onDisable() {

        for (Player player : playersManager.getInLogin().keySet()) {

            player.kickPlayer(" " + configuration.getString("PREFIX") + "\n"
                    + " " + configuration.getString("KICK-RELOAD") + " ");

        }

    }

    private void registerCommands() {

        new PIN(this);
        new PINAdmin(this);

    }

    private void registerListeners() {

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);

    }

    public Config getConfiguration() {
        return configuration;
    }

    public Config getPinConfiguration() {
        return pinConfiguration;
    }

    public PlayersManager getPlayersManager() {
        return playersManager;
    }

    public Title getTitle() {
        return title;
    }

}



