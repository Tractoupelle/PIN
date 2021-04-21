package fr.tractopelle.pin.listeners;

import fr.tractopelle.pin.CorePlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    private final CorePlugin corePlugin;

    public PlayerListener(CorePlugin corePlugin) {
        this.corePlugin = corePlugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){

        String prefix = corePlugin.getConfiguration().getString("PREFIX");

        Player player = event.getPlayer();
        String playerUUID = player.getUniqueId().toString();

        if(player.hasPermission("PIN.USE")) {

            corePlugin.getPlayersManager().addInLogin(player, player.getLocation());

            for (String key : corePlugin.getPinConfiguration().getConfigurationSection("PLAYERS").getKeys(false)) {

                if (key.contains(playerUUID)) {

                    player.sendMessage(prefix + corePlugin.getConfiguration().getString("ENTER-PIN"));

                    corePlugin.getTitle()
                            .sendTitle(corePlugin.getConfiguration().getBoolean("TITLE.ACTIVE"),
                                    player,
                                    corePlugin.getConfiguration().getString("TITLE.PREFIX"),
                                    corePlugin.getConfiguration().getString("ENTER-PIN"));


                } else {

                    player.sendMessage(prefix + corePlugin.getConfiguration().getString("CREATE-PIN"));

                    corePlugin.getTitle()
                            .sendTitle(corePlugin.getConfiguration().getBoolean("TITLE.ACTIVE"),
                                    player,
                                    corePlugin.getConfiguration().getString("TITLE.PREFIX"),
                                    corePlugin.getConfiguration().getString("CREATE-PIN"));

                }
            }
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event){

        if(corePlugin.getPlayersManager().getInLogin().containsKey(event.getPlayer()) && !event.getMessage().startsWith("/pin")){

            event.setCancelled(true);

            sendCancelActionMessage(event.getPlayer());

        }
    }


    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){

        if(corePlugin.getPlayersManager().getInLogin().containsKey(event.getPlayer())){

            event.setCancelled(true);

            sendCancelActionMessage(event.getPlayer());

        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){

        if(corePlugin.getPlayersManager().getInLogin().containsKey(event.getPlayer())){

            event.setCancelled(true);

            sendCancelActionMessage(event.getPlayer());

        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){

        if(corePlugin.getPlayersManager().getInLogin().containsKey(event.getPlayer())){

            event.setCancelled(true);

            sendCancelActionMessage(event.getPlayer());

        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event){

        if(event.getEntity() instanceof Player) {

            if (corePlugin.getPlayersManager().getInLogin().containsKey((Player) event.getEntity())) {

                event.setCancelled(true);

                sendCancelActionMessage(((Player) event.getEntity()).getPlayer());

            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){

        if (corePlugin.getPlayersManager().getInLogin().containsKey(event.getWhoClicked())) {

            event.getWhoClicked().closeInventory();
            event.setCancelled(true);

            sendCancelActionMessage(((Player) event.getWhoClicked()));

        }
    }

    private void sendCancelActionMessage(Player player){

        player.sendMessage(corePlugin.getConfiguration().getString("PREFIX") + corePlugin.getConfiguration().getString("CANCEL-ACTION"));

        corePlugin.getTitle()
                .sendTitle(corePlugin.getConfiguration().getBoolean("TITLE.ACTIVE"),
                        player,
                        corePlugin.getConfiguration().getString("TITLE.PREFIX"),
                        corePlugin.getConfiguration().getString(corePlugin.getConfiguration().getString("CANCEL-ACTION")));

    }
}

