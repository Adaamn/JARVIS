package cz.adaamn.serverInfo;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public final class ServerInfo extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        registraceCommandu("sinfo");
        registraceCommandu("jarvis");
        registraceCommandu("suitup");
        registraceCommandu("shoot");
        getLogger().info("SInfo is enabled.");
    }

    private void registraceCommandu(String name) {
        if (getCommand(name) != null) {
            getCommand(name).setExecutor(this);
        }
    }

    private final java.util.HashMap<java.util.UUID, Long> dashCooldown = new java.util.HashMap<>();

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


        if (command.getName().equalsIgnoreCase("jarvis")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Jarvis funguje pro lidi, ne pro konzoli!");
            return true;
            }

            Player player = (Player) sender;

            if (player.getAllowFlight()) {
                player.setFlying(false);
                player.setAllowFlight(false);
                String zprava = "§e§lJARVIS: §7Hráč §e" + player.getName() + " §7přestal létat.";
                Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', zprava));
            }
            else {
                player.setAllowFlight(true);
                player.setFlying(true);
                player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
                player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation(), 50);
                player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 50);
                player.sendMessage(" ");
                player.sendMessage("§7[§c!§7] §7Připojuju se do NASA...");
                player.sendMessage("§7[§c!§7] §7Prohledávám Mikyho harddrive...");
                player.sendMessage("§7[§c!§7] §7Nalezeno 21 fotek chodidel...");
                player.sendMessage("§7[§c!§7] §7Uloženo 21 neznámých fotek na C:/Users/Adam...");
                player.sendMessage("§7[§c!§7] §7Hackuju Danovo škodovku...");
                player.sendMessage(" ");
                String zprava = "§e§lJARVIS: §7Hráč §e" + player.getName() + " §7začal létat.";
                Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', zprava));
            }
            return true;
        }



        if (command.getName().equalsIgnoreCase("suitup")) {
            if (!(sender instanceof Player)){
                sender.sendMessage("Tohle muze jenom hrac!");
                return true;
            }

            Player player = (Player) sender;

            if (player.getInventory().getChestplate() == null) {

                player.setAllowFlight(true);
                player.getInventory().setHelmet(new ItemStack(Material.GOLDEN_HELMET));
                player.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                player.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
                player.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
                player.sendMessage("§e§lJARVIS: §7Nasazuju oblek..");

                player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 1.0f, 1.0f);
                player.spawnParticle(Particle.FLAME, player.getLocation(), 50);

                player.setHealth(20.0);
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 99999, 4));
            }
            else {
                player.setAllowFlight(false);
                player.getInventory().setHelmet(null);
                player.getInventory().setChestplate(null);
                player.getInventory().setLeggings(null);
                player.getInventory().setBoots(null);
                player.sendMessage("§e§lJARVIS: §7Sundavam oblek..");

                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
                player.removePotionEffect(PotionEffectType.ABSORPTION);
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("shoot")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Tohle může jenom hráč!");
                return true;
            }
            Player player = (Player) sender;


            if (player.getInventory().getChestplate() != null) {
                SmallFireball strela = player.launchProjectile(SmallFireball.class);
                strela.setIsIncendiary(false);
                player.playSound(player.getLocation(), Sound.ENTITY_GHAST_SHOOT, 1.0f, 1.0f);

            } else {
                sender.sendMessage("§e§lJARVIS: §7Musíš mít oblek.");
            }
            return true;
        }


        return false;
    }


    @EventHandler
    public void DoubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.CREATIVE) {
            if (player.getInventory().getChestplate() != null) {

                event.setCancelled(true);
                player.setFlying(false);

                long casTed = System.currentTimeMillis();
                int cooldownCas = 2000;

                if (dashCooldown.containsKey(player.getUniqueId())) {
                    long posledniDash = dashCooldown.get(player.getUniqueId());

                    long ubehlo = casTed - posledniDash;

                    if (ubehlo < cooldownCas) {
                        // long zbyva = (cooldownCas - ubehlo) / 1000;
                        // player.sendMessage("§e§lJARVIS: §7Klidek bejcku jeste " + zbyva + "s");
                        return;
                    }
                }

                dashCooldown.put(player.getUniqueId(), casTed);

                Vector dash = player.getLocation().getDirection().multiply(1.2);
                dash.setY(0.7);
                player.setVelocity(dash);
                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);

            }
        }
    }
}