package fr.kro.race;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.nashoba24.wolvmc.WolvMC;
import fr.nashoba24.wolvmc.WolvMCAPI;
import fr.nashoba24.wolvmc.events.WolvMCInitEffectsEvent;

public class Mage implements Listener {

	public static boolean enable = true;
	private static WolvMCAPI wmc = new WolvMCAPI();
	private static boolean baguette = true;
	private static String bID = "Mage Baguette";
	private static String biID = "Mage Baguette Glace";
	private static String bfID = "Mage Baguette Feu";
	private static String baID = "Mage Baguette Air";
	private static int strenght = 3;
	private static int jump = 4;
	private static int speed = 4;
	private static int firer = 2;
	private static String notMage = ChatColor.DARK_RED + "Tu n'es pas Mage !";
	private static HashMap<UUID, Long> stormc = new HashMap<>();
	private static HashMap<UUID, Long> stormcbug = new HashMap<>();
	private static int stormtps = 60;
	private static HashMap<UUID, Long> speedc = new HashMap<>();
	private static HashMap<UUID, Long> speedbug = new HashMap<>();
	private static int speedtps = 120;
	private static HashMap<UUID, Long> feuc = new HashMap<>();
	private static HashMap<UUID, Long> feubug = new HashMap<>();
	private static int feutps = 20;

	@EventHandler
	public void onEffects(WolvMCInitEffectsEvent e) {
		Player p = e.getPlayer();
		if (e.getRace().equals("mage")) {
			if (WolvMC.hasFinishMission("mage.1", p.getName())) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2147483647, strenght - 1));
			}
			if (WolvMC.hasFinishMission("mage.3", p.getName())) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 2147483647, jump - 1));
			}
		}
	}

	@EventHandler
	public void onCraftBaguette(CraftItemEvent e) {
		Player p = (Player) e.getWhoClicked();
		ItemStack i = e.getRecipe().getResult();
		if (isMageBaguette(i) || isMageBaguetteGlace(i) || isMageBaguetteFeu(i)
				|| isMageBaguetteAir(i) && WolvMC.getRace(p.getName()).equals("mage")) {
			WolvMCAPI.addNumberToPlayerMission(p.getName(), "mage.1", (double) 1);
			return;
		} else {
			p.sendMessage(notMage);
			e.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onPluie(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.isSneaking() && wmc.getRace(p).equals("mage") && e.hasItem() && isMageBaguette(e.getItem())) {
			if (e.getAction().toString().contains("LEFT_CLICK") || e.getAction().toString().contains("RIGHT_CLICK")) {
				int Cooldowntime = stormtps;
				if (stormc.containsKey(p.getUniqueId())) {
					Long Bug = stormcbug.get(p.getUniqueId());
					if (System.currentTimeMillis() - Bug < 20) {
						return;
					}
					long secondesleft = ((stormc.get(p.getUniqueId()) / 1000) + Cooldowntime)
							- (System.currentTimeMillis() / 1000);
					if (secondesleft > 0) {
						stormcbug.put(p.getUniqueId(), System.currentTimeMillis());
						p.sendMessage(WolvMCAPI.getCooldownMessage((int) secondesleft));
						return;
					}
				}
				stormc.put(p.getUniqueId(), System.currentTimeMillis());
				stormcbug.put(p.getUniqueId(), System.currentTimeMillis());
				e.setCancelled(true);
			}
			if (e.getAction().toString().contains("LEFT_CLICK")) {
				p.getWorld().setStorm(true);
				p.sendMessage(ChatColor.GREEN + "La pluie a été mise.");
				WolvMCAPI.addNumberToPlayerMission(p, "mage.2", (double) 1);
				return;
			} else if (e.getAction().toString().contains("RIGHT_CLICK")) {
				if (!WolvMCAPI.hasFinishMission(p, "mage.2")) {
					p.sendMessage(ChatColor.DARK_RED + "Tu n'as pas fini la mission 2.");
					return;
				}
				p.getWorld().setStorm(false);
				p.sendMessage(ChatColor.GREEN + "Le beau temps a été mis.");
				WolvMCAPI.addNumberToPlayerMission(p, "mage.3", (double) 1);
				return;
			}
		}
	}

	@EventHandler
	public void onGel(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.isSneaking() && wmc.getRace(p).equals("mage") && e.hasItem() && isMageBaguetteGlace(e.getItem())) {
			if (e.getAction().toString().contains("LEFT_CLICK")) {
				int Cooldowntime = speedtps;
				if (speedc.containsKey(p.getUniqueId())) {
					Long Bug = speedbug.get(p.getUniqueId());
					if (System.currentTimeMillis() - Bug < 20) {
						return;
					}
					long secondesleft = ((speedc.get(p.getUniqueId()) / 1000) + Cooldowntime)
							- (System.currentTimeMillis() / 1000);
					if (secondesleft > 0) {
						speedbug.put(p.getUniqueId(), System.currentTimeMillis());
						p.sendMessage(WolvMCAPI.getCooldownMessage((int) secondesleft));
						return;
					}
				}
				speedc.put(p.getUniqueId(), System.currentTimeMillis());
				speedbug.put(p.getUniqueId(), System.currentTimeMillis());
				e.setCancelled(true);
			}
			if (e.getAction().toString().contains("LEFT_CLICK")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, speed - 1));
				p.sendMessage(ChatColor.GREEN + "Speed IV pour 20 secondes!");
				return;
			}
		}

	}

	@EventHandler
	public void onFeu(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.isSneaking() && wmc.getRace(p).equals("mage") && e.hasItem() && isMageBaguetteFeu(e.getItem())) {
			if (e.getAction().toString().contains("RIGHT_CLICK")) {
				int Cooldowntime = feutps;
				if (feuc.containsKey(p.getUniqueId())) {
					Long Bug = feubug.get(p.getUniqueId());
					if (System.currentTimeMillis() - Bug < 20) {
						return;
					}
					long secondesleft = ((feuc.get(p.getUniqueId()) / 1000) + Cooldowntime)
							- (System.currentTimeMillis() / 1000);
					if (secondesleft > 0) {
						feubug.put(p.getUniqueId(), System.currentTimeMillis());
						p.sendMessage(WolvMCAPI.getCooldownMessage((int) secondesleft));
						return;
					}
				}
				feuc.put(p.getUniqueId(), System.currentTimeMillis());
				feubug.put(p.getUniqueId(), System.currentTimeMillis());
				e.setCancelled(true);
			}
			if (e.getAction().toString().contains("LEFT_CLICK")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 18000, firer - 1));
				p.sendMessage(ChatColor.GREEN + "Fire Resistance 2 pour 15 minutes!");
				return;
			}
			if (e.getAction().toString().contains("RIGHT_CLICK")) {
				p.getWorld().spawnEntity(p.getLocation(), EntityType.FIREBALL);
				p.sendMessage(ChatColor.GREEN + "Tu a fait spawn une fireball!");
				return;
			}
		}
	}

	@EventHandler
	public void onAir(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (p.isSneaking() && wmc.getRace(p).equals("mage") && e.hasItem() && isMageBaguetteAir(e.getItem())) {
			if (e.getAction().toString().contains("LEFT_CLICK")) {
				p.setAllowFlight(true);
				p.sendMessage(ChatColor.GREEN+"Fly Activer");
			}
			if (e.getAction().toString().contains("RIGHT_CLICK")) {
				p.setAllowFlight(false);
				p.sendMessage(ChatColor.GREEN+"Fly Desactiver");
			}
		}
	}

	public static void initMage() {
		WolvMC.addMission("mage.3", (double) 50, "mage", "Mettre le beau temps %goal% fois", "Tu à jump 4.");
		WolvMC.addMission("mage.1", (double) 10, "mage", "Crafter %goal% fois la baguette du mage", "Tu à force 3.");
		WolvMC.addMission("mage.2", (double) 50, "mage", "Mettre la pluie %goal% fois",
				"Tu peut mettre le beau temps.");
		WolvMCAPI.addRace("mage", ChatColor.DARK_BLUE + "Mage", new ItemStack(Material.ENCHANTMENT_TABLE));
		WolvMC.getPlugin(WolvMC.class).getLogger().fine("Mage Class loaded!");

		if (baguette) {
			ItemStack b = new ItemStack(Material.STICK);
			ItemMeta bM = b.getItemMeta();

			bM.setDisplayName(ChatColor.BLUE + "BAGUETTE DU MAGE (BASE)");
			bM.addEnchant(Enchantment.LOOT_BONUS_MOBS, 4, true);
			bM.setLore(Arrays.asList(bID));
			b.setItemMeta(bM);

			ShapedRecipe rb = new ShapedRecipe(b);
			rb.shape(" D ", " S ", " D ");
			rb.setIngredient('D', Material.DIAMOND);
			rb.setIngredient('S', Material.STICK);
			Bukkit.addRecipe(rb);

			ItemStack bi = new ItemStack(Material.STICK);
			ItemMeta biM = bi.getItemMeta();

			biM.setDisplayName(ChatColor.AQUA + "BAGUETTE DU MAGE (GLACE)");
			biM.addEnchant(Enchantment.LOOT_BONUS_MOBS, 4, true);
			biM.addEnchant(Enchantment.FROST_WALKER, 2, true);
			biM.setLore(Arrays.asList(biID));
			bi.setItemMeta(biM);

			ShapedRecipe rbi = new ShapedRecipe(bi);
			rbi.shape("GGG", "GBG", "GGG");
			rbi.setIngredient('B', Material.STICK);
			rbi.setIngredient('G', Material.ICE);
			Bukkit.addRecipe(rbi);

			ItemStack bf = new ItemStack(Material.BLAZE_ROD);
			ItemMeta bfM = bf.getItemMeta();

			bfM.setDisplayName(ChatColor.RED + "BAGUETTE DU MAGE (FEU)");
			bfM.addEnchant(Enchantment.LOOT_BONUS_MOBS, 4, true);
			bfM.addEnchant(Enchantment.FIRE_ASPECT, 4, true);
			bfM.setLore(Arrays.asList(bfID));
			bf.setItemMeta(bfM);

			ShapedRecipe rbf = new ShapedRecipe(bf);
			rbf.shape("MMM", "MBM", "MMM");
			rbf.setIngredient('B', Material.STICK);
			rbf.setIngredient('M', Material.MAGMA);
			Bukkit.addRecipe(rbf);

			ItemStack ba = new ItemStack(Material.END_ROD);
			ItemMeta baM = ba.getItemMeta();

			baM.setDisplayName(ChatColor.WHITE + "BAGUETTE DU MAGE (AIR)");
			baM.addEnchant(Enchantment.LOOT_BONUS_MOBS, 4, true);
			baM.addEnchant(Enchantment.KNOCKBACK, 2, true);
			baM.setLore(Arrays.asList(baID));
			ba.setItemMeta(baM);

			ShapedRecipe rba = new ShapedRecipe(ba);
			rba.shape("VVV", "VBV", "VVV");
			rba.setIngredient('B', Material.STICK);
			rba.setIngredient('V', Material.GLASS);
			Bukkit.addRecipe(rba);
		}
	}

	private static boolean isMageBaguette(ItemStack i) {
		return (i.hasItemMeta() && i.getItemMeta() != null && i.getItemMeta().hasLore()
				&& i.getItemMeta().getLore() != null && i.getItemMeta().getLore().size() == 1
				&& i.getItemMeta().getLore().get(0).equals(bID));
	}

	private static boolean isMageBaguetteGlace(ItemStack d) {
		return (d.hasItemMeta() && d.getItemMeta() != null && d.getItemMeta().hasLore()
				&& d.getItemMeta().getLore() != null && d.getItemMeta().getLore().size() == 1
				&& d.getItemMeta().getLore().get(0).equals(biID));
	}

	private static boolean isMageBaguetteFeu(ItemStack f) {
		return (f.hasItemMeta() && f.getItemMeta() != null && f.getItemMeta().hasLore()
				&& f.getItemMeta().getLore() != null && f.getItemMeta().getLore().size() == 1
				&& f.getItemMeta().getLore().get(0).equals(bfID));
	}

	private static boolean isMageBaguetteAir(ItemStack a) {
		return (a.hasItemMeta() && a.getItemMeta() != null && a.getItemMeta().hasLore()
				&& a.getItemMeta().getLore() != null && a.getItemMeta().getLore().size() == 1
				&& a.getItemMeta().getLore().get(0).equals(baID));
	}
}
