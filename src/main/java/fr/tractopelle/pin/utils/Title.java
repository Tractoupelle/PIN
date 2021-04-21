package fr.tractopelle.pin.utils;

import fr.tractopelle.pin.CorePlugin;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Title {

    private final CorePlugin corePlugin;

    public Title(CorePlugin corePlugin) {
        this.corePlugin = corePlugin;
    }

    public void sendTitle(boolean b , Player player, String title, String subtitle) {

        if(b) {

            PlayerConnection connection = (((CraftPlayer) player).getHandle()).playerConnection;

            PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null,
                    corePlugin.getConfiguration().getInt("TITLE.FADE-IN"), corePlugin.getConfiguration().getInt("TITLE.STAY"), corePlugin.getConfiguration().getInt("TITLE.FADE-OUT"));
            connection.sendPacket(packetPlayOutTimes);
            if (subtitle != null) {
                subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
                IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
                PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);

                connection.sendPacket(packetPlayOutSubTitle);
            }
            if (title != null) {
                title = title.replaceAll("%player%", player.getDisplayName());
                IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
                PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);

                connection.sendPacket(packetPlayOutTitle);

            }
        }
    }
}
