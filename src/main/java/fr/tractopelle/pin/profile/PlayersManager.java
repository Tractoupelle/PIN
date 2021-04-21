package fr.tractopelle.pin.profile;

import fr.tractopelle.pin.CorePlugin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayersManager {

    private final CorePlugin corePlugin;
    private Map<Player, Location> inLogin;
    private List<Profile> profileList;

    public PlayersManager(CorePlugin corePlugin) {
        this.corePlugin = corePlugin;
        this.inLogin = new HashMap<>();
        this.profileList = new ArrayList<>();
    }

    public Map<Player, Location> getInLogin() {
        return inLogin;
    }

    public void addInLogin(Player player, Location location) {
        this.inLogin.put(player, location);
    }

    public void endLogin(Player player) {
        this.inLogin.remove(player);
    }

    public void addInProfileList(Player player) { this.profileList.add(new Profile(player)); }

    public List<Profile> getProfileList() { return profileList; }

    public boolean containsProfileList(Player player) {

        return profileList.stream().anyMatch(profile -> profile.getPlayer().equals(player));

    }
}
