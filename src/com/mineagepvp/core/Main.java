package com.mineagepvp.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import com.mineagepvp.core.commands.BottleCMD;
import com.mineagepvp.core.commands.ChunkloaderCMD;
import com.mineagepvp.core.commands.FreezeCMD;
import com.mineagepvp.core.commands.JellylegsCMD;
import com.mineagepvp.core.commands.PotCMD;
import com.mineagepvp.core.commands.SellwandCMD;
import com.mineagepvp.core.commands.TrashCMD;
import com.mineagepvp.core.events.AntiAutoCannon;
import com.mineagepvp.core.events.AutoRespawn;
import com.mineagepvp.core.events.Chunkloaders;
import com.mineagepvp.core.events.LavaSponge;
import com.mineagepvp.core.events.NetherWater;
import com.mineagepvp.core.events.Reaper;

import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {

	public static Main instance;
	private static Economy econ;

	public void onEnable() {
		long time = getConfig().getInt("reaperFireballInterval") * 20L;
		TrashCMD.registerCommands(this);
		if (authenticate()) {
			return;
		}
		getLogger().info("MineagePvP Core Plugin has been enabled.");
		getLogger().info("Developed by bluecreeper111. (Exponential Services)");
		config();
		registerEvents();
		@SuppressWarnings("unused")
		BukkitTask run = new Reaper(this).runTaskTimer(this, time, time);
		@SuppressWarnings("unused")
		BukkitTask run2 = new Chunkloaders(this).runTaskTimer(this, 20L, 20L);
		if (!setupEconomy()) {
			getLogger().severe("Vault API could not be found!");
			getLogger().severe("Make sure you have vault installed!");
			Bukkit.getPluginManager().disablePlugin(this);
		}
		if (ChunkloaderCMD.chunks.getConfigurationSection("chunkloaders") != null) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					for (String b : ChunkloaderCMD.chunks.getConfigurationSection("chunkloaders").getKeys(false)) {
						double x = ChunkloaderCMD.chunks.getDouble("chunkloaders." + b + ".x");
						double y = ChunkloaderCMD.chunks.getDouble("chunkloaders." + b + ".y");
						double z = ChunkloaderCMD.chunks.getDouble("chunkloaders." + b + ".z");
						String world = ChunkloaderCMD.chunks.getString("chunkloaders." + b + ".world");
						Location add = new Location(Bukkit.getWorld(world), x, y, z);
						ChunkloaderCMD.chunkloaders.add(add);
					}
				}
			}, 10L);
		}
	}

	public void onDisable() {
		getLogger().info("MineagePvP Core Plugin has been disabled.");
		int number = 0;
		for (Location loc : ChunkloaderCMD.chunkloaders) {
			double x = loc.getX();
			double y = loc.getY();
			double z = loc.getZ();
			String world = loc.getWorld().toString();
			ChunkloaderCMD.chunks.set("chunkloaders." + Integer.toString(number) + ".x", x);
			ChunkloaderCMD.chunks.set("chunkloaders." + Integer.toString(number) + ".y", y);
			ChunkloaderCMD.chunks.set("chunkloaders." + Integer.toString(number) + ".z", z);
			ChunkloaderCMD.chunks.set("chunkloaders." + Integer.toString(number) + ".world", world);
			number++;
		}
		try {
			ChunkloaderCMD.chunks.save(ChunkloaderCMD.chunk);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void registerEvents() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new BottleCMD(), this);
		pm.registerEvents(new JellylegsCMD(), this);
		pm.registerEvents(new FreezeCMD(), this);
		pm.registerEvents(new AutoRespawn(this), this);
		pm.registerEvents(new PotCMD(), this);
		pm.registerEvents(new LavaSponge(this), this);
		pm.registerEvents(new Reaper(this), this);
		pm.registerEvents(new NetherWater(), this);
		pm.registerEvents(new AntiAutoCannon(this), this);
		pm.registerEvents(new SellwandCMD(), this);
		pm.registerEvents(new ChunkloaderCMD(), this);
	}

	public void config() {
		if (!new File(this.getDataFolder(), "config.yml").exists()) {
			saveResource("config.yml", false);
		}
		if (!ChunkloaderCMD.chunk.exists()) {
			try {
				ChunkloaderCMD.chunk.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public boolean authenticate() {
		try {
			URLConnection url = new URL("https://mineageauthen.000webhostapp.com/").openConnection();
			url.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			url.connect();
			BufferedReader r = new BufferedReader(
					new InputStreamReader(url.getInputStream(), Charset.forName("UTF-8")));
			StringBuilder s = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				s.append(line);
			}
			String value = s.toString();
			if (value.equalsIgnoreCase("false")) {
				Bukkit.getPluginManager().disablePlugin(this);
				getLogger().severe("------------------------------------------------------------------------------");
				getLogger().severe("Plugin author has denied access to use this plugin. Likely due too chargeback.");
				getLogger().severe("------------------------------------------------------------------------------");
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public static Economy getEconomy() {
		return econ;
	}

}
