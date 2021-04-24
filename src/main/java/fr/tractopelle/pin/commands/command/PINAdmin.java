package fr.tractopelle.pin.commands.command;

import fr.tractopelle.pin.CorePlugin;
import fr.tractopelle.pin.commands.PCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PINAdmin extends PCommand {

    private final CorePlugin corePlugin;

    public PINAdmin(CorePlugin corePlugin) {
        super(corePlugin, "pinadmin", true, "PIN.ADMIN");
        this.corePlugin = corePlugin;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {

        String prefix = corePlugin.getConfiguration().getString("PREFIX");

        if (args.length != 2) {

            corePlugin.getConfiguration().getStringList("USAGE-ADMIN").forEach(commandSender::sendMessage);
            return false;

        } else {

            Player target = Bukkit.getPlayerExact(args[1]);

            if (target == null) {

                commandSender.sendMessage(prefix + corePlugin.getConfiguration().getString("NOT-ONLINE"));
                return false;

            }

            for (String key : corePlugin.getPinConfiguration().getConfigurationSection("PLAYERS").getKeys(false)) {

                if (key.contains(target.getUniqueId().toString())) {

                    if (args[0].equalsIgnoreCase("delete")) {

                        commandSender.sendMessage(prefix + corePlugin.getConfiguration().getString("DELETE-PIN")
                                .replace("%player%", target.getName()));

                        corePlugin.getPinConfiguration().set("PLAYERS." + key , null);
                        corePlugin.getPinConfiguration().save();

                        return false;


                    } else if (args[0].equalsIgnoreCase("check")) {

                        if(corePlugin.getConfiguration().getBoolean("DISABLE-CHECK-COMMAND")){

                            commandSender.sendMessage(prefix + corePlugin.getConfiguration().getString("CHECK-COMMAND-DISABLE"));

                        } else {

                            commandSender.sendMessage(prefix + corePlugin.getConfiguration().getString("CHECK-PIN")
                                    .replace("%player%", target.getName())
                                    .replace("%pin%", corePlugin.getPinConfiguration().getString("PLAYERS." + key + ".PIN")));

                        }

                        return false;


                    } else {

                        corePlugin.getConfiguration().getStringList("USAGE-ADMIN").forEach(commandSender ::sendMessage);
                        return false;

                    }

                } else {

                    commandSender.sendMessage(prefix + corePlugin.getConfiguration().getString("NOT-HAVE-A-PIN"));
                    return false;

                }
            }
        }
        return false;
    }
}
