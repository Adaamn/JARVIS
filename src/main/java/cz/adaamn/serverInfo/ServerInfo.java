package cz.adaamn.serverInfo;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerInfo extends JavaPlugin {

    @Override
    public void onEnable() {
        registraceCommandu("sinfo");
        registraceCommandu("adam");
        registraceCommandu("zpravicka");
        getLogger().info("SInfo is enabled.");
    }

    private void registraceCommandu(String name) {
        if (getCommand(name) != null) {
            getCommand(name).setExecutor(this);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("SInfo is disabled.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("sinfo")) {

            if (!sender.hasPermission("sinfo.use")) {
                sender.sendMessage("§cYou dont have permission to use this plugin");
                return true;
            }

            int online = getServer().getOnlinePlayers().size();
            int maxOnline = getServer().getMaxPlayers();
            int plugins = getServer().getPluginManager().getPlugins().length;


            sender.sendMessage(" ");
            sender.sendMessage("§7Server version: §e" + getServer().getVersion());
            sender.sendMessage("§7Online players: §e" + online + "§7/§e" + maxOnline);
            sender.sendMessage("§7Plugins: §e" + plugins);
            sender.sendMessage(" ");

            return true;
        }

        String PREFIX = "§e§lSERVER: ";
        if (command.getName().equalsIgnoreCase("adam")) {

            String finalniVystup = "%luckperms_prefix%§7" + sender.getName() + "§7 used §e/adam §7command";

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                // Pokud je sender hráč, PAPI může doplnit věci jako %player_health%
                Player player = (sender instanceof Player) ? (Player) sender : null;
                finalniVystup = PlaceholderAPI.setPlaceholders(player, finalniVystup);
            }

            finalniVystup = ChatColor.translateAlternateColorCodes('&', finalniVystup);

            Bukkit.getServer().broadcastMessage(finalniVystup);
            return true;
        }


    /*
        if (command.getName().equalsIgnoreCase("zpravicka")) {
            if (args.length == 0) {
                sender.sendMessage("§cMusíš napsat nějakou zprávu!");
                return true;
            }
            String zprava = String.join(" ", args);
            String finalniVystup = "%luckperms_prefix%" + sender.getName() + " §7§lANNOUNCED: §e" + zprava;

            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
                // Pokud je sender hráč, PAPI může doplnit věci jako %player_health%
                Player player = (sender instanceof Player) ? (Player) sender : null;
                finalniVystup = PlaceholderAPI.setPlaceholders(player, finalniVystup);
            }

            finalniVystup = ChatColor.translateAlternateColorCodes('&', finalniVystup);

            Bukkit.getServer().broadcastMessage(finalniVystup);
            return true;
        }
    */


        return false;
    }
}