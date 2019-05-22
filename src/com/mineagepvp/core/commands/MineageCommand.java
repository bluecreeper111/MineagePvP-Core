package com.mineagepvp.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.mineagepvp.core.api.Api;

public abstract class MineageCommand implements CommandExecutor {
	
	private final String command;
	private final String permission;
	private final boolean console;
	public static JavaPlugin plugin;
	
	public MineageCommand(String command, String permission, boolean console) {
		this.command = command;
		this.permission = permission;
		this.console = console;
		plugin.getCommand(command).setExecutor(this);
	}
	public final static void registerCommands(JavaPlugin pl) {
		plugin = pl;
		new TrashCMD();
		new BottleCMD();
		new JellylegsCMD();
		new NightvisionCMD();
		new AnvilCMD();
		new CraftTntCMD();
		new FreezeCMD();
		new ClearchatCMD();
		new CpcCMD();
		new PotCMD();
		new ReaperCMD();
		new SellwandCMD();
		new ChunkloaderCMD();
	}
	public abstract void execute(CommandSender sender, Command cmd, String label, String[] args);
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!cmd.getName().equalsIgnoreCase(command)) {
			return true;
		}
		if (!(sender instanceof Player) && !console) {
			sender.sendMessage(Api.prefix + "§cOnly players can run that command!");
			return true;
		}
		if (!sender.hasPermission(permission)) {
			Api.noPermission(sender);
			return true;
		}
		execute(sender, cmd, label, args);
		return true;
	}

}
