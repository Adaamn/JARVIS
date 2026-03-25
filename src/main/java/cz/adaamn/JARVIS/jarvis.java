package cz.adaamn.JARVIS;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;

public final class jarvis extends JavaPlugin implements Listener, TabCompleter {
    String PREFIX = "§e§lJARVIS: §7";

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        registraceCommandu("sinfo");
        registraceCommandu("kdeje");
        registraceCommandu("nickos");
        registraceCommandu("kdoje");
        registraceCommandu("jarvis");
        getCommand("jarvis").setTabCompleter(this);
        getCommand("kdoje").setTabCompleter(this);
        getCommand("kdeje").setTabCompleter(this);
        registraceCommandu("portni");
        getCommand("portni").setTabCompleter(this);

        getLogger().info("JARVIS is enabled.");
    }

    private void registraceCommandu(String name) {
        if (getCommand(name) != null) {
            getCommand(name).setExecutor(this);
        }
    }

    private final java.util.HashMap<java.util.UUID, Long> dashCooldown = new java.util.HashMap<>();

    @Override
    public void onDisable() {
        getLogger().info("JARVIS is disabled.");
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("");
        player.sendMessage(" §e➤ §7Napis §e/jarvis <prikaz> §7abys byl ironman");
        player.sendMessage("");

        player.getInventory().clear();
        player.getInventory().setHeldItemSlot(0);
        player.getInventory().setItem(8, new ItemStack(Material.ENDER_CHEST));
    }

    @EventHandler
    public void random(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (event.getBedEnterResult() != PlayerBedEnterEvent.BedEnterResult.OK) {
            return;
        }
        String msg = "&e&lALERT: &e" + player.getName() + " &7jde spat";
        Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PHANTOM_AMBIENT, 1.0f, 1.0f);
    }


    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(PREFIX + "Mazej do postele!");
    }

/*
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory() == player.getInventory()) {
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.BOOK) {
                event.setCancelled(true);
            }

            if (event.getCursor() != null && event.getCursor().getType() == Material.BOOK) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.BOOK) {
            event.setCancelled(true);
        }
    }

*/



    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("kdeje")) {
            if (args.length==0) {
                sender.sendMessage(PREFIX + "§7Musis zadat jmeno");
            }
            else {
                Player player = Bukkit.getPlayerExact(args[0]);
                if (player != null) {
                    if (player.getName().equals(sender.getName())) {
                        sender.sendMessage("§7Nachazis se na " + player.getWorld().getName() + ": §e"
                                + (int) player.getLocation().getX() + " "
                                + (int) player.getLocation().getY() + " "
                                + (int) player.getLocation().getZ());
                    } else {
                    Bukkit.getServer().broadcastMessage("§e" + sender.getName() + " §7hleda " + args[0] + ", ten se schovava: " + player.getWorld().getName() + ": §e"
                            + (int) player.getLocation().getX() + " "
                            + (int) player.getLocation().getY() + " "
                            + (int) player.getLocation().getZ());
                    }
                } else {
                    sender.sendMessage("§e" + args[0] + " §7neni na serveru");
                }
            }
            return true;
        }


        if (command.getName().equalsIgnoreCase("portni")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Tohle může jenom hráč!");
                return true;
            }

            Player player1 = (Player) sender;

            if (args.length == 1) {
                if (args[0].equals(player1.getName())) {
                    player1.sendMessage(PREFIX + "Nemůžeš se teleportovat sám k sobě");
                    return true;
                }
                Player player2 = Bukkit.getPlayerExact(args[0]);
                if (player2 != null) {
                    player1.teleport(player2);
                    player1.sendMessage(PREFIX + "Teleportuju tě k §e" + args[0]);
                    player2.sendMessage(PREFIX + "Teleportoval se na tebe §e" + player1.getName());
                } else {
                    player1.sendMessage(PREFIX + "§e" + args[0] + " §7neni na serveru");
                }
            } else if (args.length == 2) {
                if (args[0].equals(args[1])) {
                    player1.sendMessage(PREFIX + "Nemůžeš hráče teleportovat sám k sobě");
                    return true;
                }
                Player player2 = Bukkit.getPlayerExact(args[0]);
                Player player3 = Bukkit.getPlayerExact(args[1]);
                if (player2 != null && player3 != null) {
                    player2.teleport(player3);
                    player2.sendMessage(PREFIX + "Teleportuju tě k §e" + args[1]);
                    player3.sendMessage(PREFIX + "Teleportoval se na tebe §e" + args[0]);
                } else {
                    player1.sendMessage(PREFIX + "Někdo z hráčů neni na serveru");
                }
            } else {
                player1.sendMessage(PREFIX + "Špatně zadané argumenty");
            }
            return true;
        }


        if (command.getName().equalsIgnoreCase("nickos")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Tohle může jenom hráč!");
                return true;
            }

            Player player = (Player) sender;

            if (args.length==0) {
                player.sendMessage(PREFIX + "Musíš zadat nick!");
                return true;
            } else {
                if (player.getDisplayName().equals(args[0])) {
                    player.sendMessage(PREFIX + "Tenhle nick už máš");
                } else {
                    player.setDisplayName(args[0]);
                    player.setCustomName(args[0]);
                    player.setCustomNameVisible(true);
                    player.sendMessage(PREFIX + "Nickname změněn na " + "§e" + args[0]);
                }
            }
            return true;
        }


        if (command.getName().equalsIgnoreCase("kdoje")) {
            if (args.length == 0) {
                sender.sendMessage(PREFIX + "Musíš zadat jméno");
            }
            else {
                Player nevimbro = Bukkit.getPlayerExact(args[0]);
                if (nevimbro == null) {
                    sender.sendMessage(PREFIX + "§e" + args[0] + " §7neni na serveru");
                }
                else {
                    sender.sendMessage(" ");
                    sender.sendMessage("§7Name: §e" + nevimbro.getName());
                    sender.sendMessage("§7HP: §e" + (int)nevimbro.getHealth());
                    sender.sendMessage("§7Gamemode: §e" + nevimbro.getGameMode());
                    sender.sendMessage("§7Lokace: §e" + nevimbro.getWorld().getName() + " §7- §e" + (int)nevimbro.getLocation().getX() + " " + (int)nevimbro.getLocation().getY() + " " + (int)nevimbro.getLocation().getZ());
                    sender.sendMessage("§7Ping: §e" + nevimbro.getPing());
                    sender.sendMessage(" ");

                }
            }
            return true;
        }



        if (command.getName().equalsIgnoreCase("sinfo")) {

            if (!sender.hasPermission("sinfo.use")) {
                sender.sendMessage("§cNemáš právo použít tenhle příkaz!");
                return true;
            }

            int online = getServer().getOnlinePlayers().size();
            int maxOnline = getServer().getMaxPlayers();
            int plugins = getServer().getPluginManager().getPlugins().length;


            sender.sendMessage(" ");
            sender.sendMessage("§7Server version: §e" + getServer().getVersion());
            sender.sendMessage("§7Online players: §e" + online + "§7/§e" + maxOnline);
            sender.sendMessage("§7Plugins: §e" + plugins);
            sender.sendMessage("§7Version: §e" + getServer().getMinecraftVersion());
            sender.sendMessage(" ");

            return true;
        }


        if (command.getName().equalsIgnoreCase("jarvis")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Tohle může jenom hráč!");
                return true;
            }

            if (args.length == 0) {
                sender.sendMessage(PREFIX + "Musíš napsat argument..");
                return true;
            }

            if (args[0].equalsIgnoreCase("start")) {

                Player player = (Player) sender;

                player.playSound(player.getLocation(), Sound.BLOCK_METAL_FALL, 1.0f, 1.0f);
                player.sendMessage(" ");
                /*
                player.sendMessage(PREFIX + "Connecting to Stark Industries drones..");
                player.sendMessage(PREFIX + "Connected to Stark Industries drones..");
                player.sendMessage(PREFIX + "All systems operational..");
                */

                player.sendMessage(" ");

                Location locY = player.getLocation();

                locY.setY(locY.getY() + 100);
                player.teleport(locY);

                return true;
            }

            if (args[0].equalsIgnoreCase("fly")) {

                Player player = (Player) sender;

                if (player.getAllowFlight()) {
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    String zprava = "§e§lJARVIS: §7Hráč §e" + player.getName() + " §7přestal létat.";
                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', zprava));
                } else {
                    player.setAllowFlight(true);
                    player.playSound(player.getLocation(), Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
                    player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
                    player.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, player.getLocation(), 50);
                    player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 50);
                    /*
                    player.sendMessage(" ");
                    player.sendMessage("§7[§c!§7] §7Připojuju se do NASA...");
                    player.sendMessage("§7[§c!§7] §7Prohledávám Mikyho harddrive...");
                    player.sendMessage("§7[§c!§7] §7Nalezeno 21 fotek chodidel...");
                    player.sendMessage("§7[§c!§7] §7Uloženo 21 neznámých fotek na C:/Users/Adam...");
                    player.sendMessage("§7[§c!§7] §7Hackuju Danovo škodovku...");
                    player.sendMessage(" ");
                    */
                    String zprava = "§e§lJARVIS: §7Hráč §e" + player.getName() + " §7začal létat.";

                    Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', zprava));
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("suitup")) {

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
                } else {
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


            if (args[0].equalsIgnoreCase("shoot")) {
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


            if (args[0].equalsIgnoreCase("scan")) {
                Player player = (Player) sender;
                int pocet = 0;
                for (Entity e : player.getNearbyEntities(100, 100, 100)) {
                    if (e instanceof Player) {

                        player.sendMessage(PREFIX + "Byl nalezen hráč §e" + e.getName() + "§7, na §eX:" + (int)e.getLocation().getX() + " Y:" + (int)e.getLocation().getY() + " Z:" +(int)e.getLocation().getZ());
                        pocet++;
                    }
                }

                if (pocet == 0) {
                    player.sendMessage(PREFIX + "Žádný hráč v okolí");
                }
                return true;
            }

        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("jarvis")) {
            if (args.length == 1) {
                return List.of("start", "fly", "suitup", "shoot", "scan");
            }
        }

        if (command.getName().equalsIgnoreCase("kdoje")) {
            return null;
        }

        if (command.getName().equalsIgnoreCase("kdeje")) {
            return null;
        }

        if (command.getName().equalsIgnoreCase("portni")) {
            return null;
        }

        return List.of();
    }




    @EventHandler
    public void DoubleJump(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.CREATIVE) {
            if (player.getInventory().getChestplate() != null) {

                event.setCancelled(true);
                player.setFlying(false);

                long casTed = System.currentTimeMillis();
                int cooldownCas = 1500;

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
                dash.setY(1);
                player.setVelocity(dash);
                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);

            }
        }
    }
}