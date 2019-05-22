package com.mineagepvp.core.events;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.mineagepvp.core.Main;

import net.minecraft.server.v1_8_R3.EntityLiving;

public class Reaper extends BukkitRunnable implements Listener {
	
	private Main plugin;
	
	public Reaper(Main pl) {
		plugin = pl;
	}
	
	public void spawnReaper(Location loc) {
		int health = plugin.getConfig().getInt("reaperHealth") * 2; 
		LivingEntity wraith = (LivingEntity) loc.getWorld().spawnEntity(loc, EntityType.PIG_ZOMBIE);
		EntityLiving cwraith = ((CraftLivingEntity)wraith).getHandle();
		ItemStack hand = new ItemStack(Material.DIAMOND_HOE, 1);
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 1);
		ItemStack chest = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lam = (LeatherArmorMeta)chest.getItemMeta();
		lam.setColor(Color.BLACK);
		chest.setItemMeta(lam);
		cwraith.setEquipment(0, CraftItemStack.asNMSCopy(hand));
		cwraith.setEquipment(4, CraftItemStack.asNMSCopy(head));
		cwraith.setEquipment(3, CraftItemStack.asNMSCopy(chest));
		wraith.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
		wraith.setCustomName("wraith");
		cwraith.setHealth(health);
	}
	
	@EventHandler
	public void spawn(CreatureSpawnEvent e) {
		int chance = plugin.getConfig().getInt("reaperSpawnPercentage");
		Random random = new Random();
		if (e.getLocation().getWorld().getEnvironment() == World.Environment.NETHER && e.getEntityType() == EntityType.PIG_ZOMBIE) {
			double gen = random.nextInt(100) + 1;
			if (chance < gen) {
				spawnReaper(e.getLocation());
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void kill(EntityDeathEvent e) {
		if (e.getEntityType() == EntityType.PIG_ZOMBIE &&  e.getEntity().getCustomName() != null && e.getEntity().getCustomName().equals("wraith")) {
			ArrayList<ItemStack> drops = new ArrayList<>();
			for (String d : plugin.getConfig().getStringList("reaperDrops")) {
				String[] drop = d.split(",");
				if (drop.length == 2) {
					ItemStack item = new ItemStack(Material.getMaterial(drop[0].toUpperCase().trim()), Integer.parseInt(drop[1].trim()));
					drops.add(item);
				} else if (drop.length == 3) {
					ItemStack item = new ItemStack(Material.getMaterial(drop[0].toUpperCase().trim()), Integer.parseInt(drop[1].trim()), (short) Short.parseShort(drop[2].trim()));
					drops.add(item);
				} else { continue; }
			}
			e.getDrops().clear();
			e.getDrops().addAll(drops);
		}
	}
	@EventHandler
	public void launch(EntityExplodeEvent e) {
		if (e.getEntityType() == EntityType.FIREBALL) {
			e.setCancelled(true);
		}
	}
	
	public void run() {
		for (World world : Bukkit.getWorlds()) {
			for (Entity en : world.getEntities()) {
				if (en.getType() == EntityType.PIG_ZOMBIE && en.getCustomName() != null && en.getCustomName().equals("wraith")) {
					LivingEntity entity = (LivingEntity) en;
					entity.launchProjectile(Fireball.class);
				}
			}
		}
	}

}
