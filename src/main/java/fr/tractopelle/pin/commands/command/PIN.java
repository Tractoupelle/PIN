package fr.tractopelle.pin.commands.command;

import fr.tractopelle.pin.CorePlugin;
import fr.tractopelle.pin.commands.PCommand;
import fr.tractopelle.pin.utils.Logger;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class PIN extends PCommand {

    private final CorePlugin corePlugin;

    public PIN(CorePlugin corePlugin) {
        super(corePlugin, "pin", false, "PIN.USE");
        this.corePlugin = corePlugin;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {

        String prefix = corePlugin.getConfiguration().getString("PREFIX");

        Player player = (Player) commandSender;
        String playerUUID = player.getUniqueId().toString();

        if (args.length != 1) {

            player.sendMessage(prefix + corePlugin.getConfiguration().getString("USAGE"));
            return false;

        } else {

            if (corePlugin.getPlayersManager().getInLogin().containsKey(player)) {

                String pin = args[0];

                if (corePlugin.getPinConfiguration().getConfigurationSection("PLAYERS").getKeys(false)
                        .stream().anyMatch(s -> s.equals(playerUUID))) {

                    if (corePlugin.getPinConfiguration().getString("PLAYERS." + playerUUID + ".PIN").equals(pin)) {

                        player.sendMessage(prefix + corePlugin.getConfiguration().getString("SUCCESS-PIN"));

                        corePlugin.getTitle()
                                .sendTitle(corePlugin.getConfiguration().getBoolean("TITLE.ACTIVE"),
                                        player,
                                        corePlugin.getConfiguration().getString("TITLE.PREFIX"),
                                        corePlugin.getConfiguration().getString("SUCCESS-PIN"));

                        corePlugin.getPlayersManager().endLogin(player);

                        player.removePotionEffect(PotionEffectType.BLINDNESS);

                    } else {

                        if(corePlugin.getPlayersManager().containsProfileList(player)){

                            corePlugin.getPlayersManager().getProfileList().stream()
                                    .filter(profile1 -> profile1.getPlayer().equals(player)).findFirst().ifPresent(profile1 -> {

                                        if(profile1.getFailTime() + 1 == corePlugin.getConfiguration().getInt("WRONG-TIME-KICK")){

                                            player.kickPlayer(" " + corePlugin.getConfiguration().getString("PREFIX") + "\n"
                                                    + " " + corePlugin.getConfiguration().getString("WRONG-CODE-ACHIEVED") + " ");

                                            Logger.info(corePlugin.getConfiguration().getString("WRONG-CODE-ACHIEVED-NOTIFY").replace("%player%", player.getName()), Logger.LogType.ERROR);

                                            for(Player players : Bukkit.getOnlinePlayers()){

                                                if(players.hasPermission("PIN.NOTIFY")){

                                                    players.sendMessage(prefix + corePlugin.getConfiguration().getString("WRONG-CODE-ACHIEVED-NOTIFY").replace("%player%", player.getName()));

                                                }
                                            }

                                        } else {

                                            profile1.addFailTime();

                                            player.sendMessage(prefix + corePlugin.getConfiguration().getString("FAIL-PIN"));

                                            corePlugin.getTitle()
                                                    .sendTitle(corePlugin.getConfiguration().getBoolean("TITLE.ACTIVE"),
                                                            player,
                                                            corePlugin.getConfiguration().getString("TITLE.PREFIX"),
                                                            corePlugin.getConfiguration().getString("FAIL-PIN"));

                                        }

                                    });

                            return false;

                        } else {

                            corePlugin.getPlayersManager().addInProfileList(player);

                        }

                        player.sendMessage(prefix + corePlugin.getConfiguration().getString("FAIL-PIN"));

                        corePlugin.getTitle()
                                .sendTitle(corePlugin.getConfiguration().getBoolean("TITLE.ACTIVE"),
                                        player,
                                        corePlugin.getConfiguration().getString("TITLE.PREFIX"),
                                        corePlugin.getConfiguration().getString("FAIL-PIN"));

                    }

                } else {

                    // CREATE PIN

                    if (StringUtils.isNumeric(pin)) {

                        player.sendMessage(prefix + corePlugin.getConfiguration().getString("SUCCESS-CREATE").replace("%pin%", pin));

                        corePlugin.getTitle()
                                .sendTitle(corePlugin.getConfiguration().getBoolean("TITLE.ACTIVE"),
                                        player,
                                        corePlugin.getConfiguration().getString("TITLE.PREFIX"),
                                        corePlugin.getConfiguration().getString("SUCCESS-CREATE").replace("%pin%", pin));

                        corePlugin.getPinConfiguration().set("PLAYERS." + playerUUID + ".PIN", pin);
                        corePlugin.getPinConfiguration().save();

                        corePlugin.getPlayersManager().endLogin(player);

                        player.removePotionEffect(PotionEffectType.BLINDNESS);


                    } else {

                        player.sendMessage(prefix + corePlugin.getConfiguration().getString("NUMERIC-ONLY"));

                        corePlugin.getTitle()
                                .sendTitle(corePlugin.getConfiguration().getBoolean("TITLE.ACTIVE"),
                                        player,
                                        corePlugin.getConfiguration().getString("TITLE.PREFIX"),
                                        corePlugin.getConfiguration().getString("NUMERIC-ONLY"));

                    }

                }

                return false;

            } else {

                player.sendMessage(prefix + corePlugin.getConfiguration().getString("ALREADY-LOG"));

                corePlugin.getTitle()
                        .sendTitle(corePlugin.getConfiguration().getBoolean("TITLE.ACTIVE"),
                                player,
                                corePlugin.getConfiguration().getString("TITLE.PREFIX"),
                                corePlugin.getConfiguration().getString("ALREADY-LOG"));
            }
        }


        return false;

    }
}
