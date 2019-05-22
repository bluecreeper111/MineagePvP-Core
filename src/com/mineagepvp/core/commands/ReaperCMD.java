package com.mineagepvp.core.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineagepvp.core.Main;
import com.mineagepvp.core.api.Api;
import com.mineagepvp.core.events.Reaper;

public class ReaperCMD extends MineageCommand {
	
	public ReaperCMD() {
		super("reaper", "mineage.reaper.spawn", false);
	}

	
	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		Reaper wmanager = new Reaper((Main)plugin);
		if (args.length == 1 && args[0].equalsIgnoreCase("spawn")) {
			wmanager.spawnReaper(p.getLocation());
			p.sendMessage(Api.prefix + "§aReaper spawned.");
		} else {
			p.sendMessage(Api.prefix + "§cIncorrect command syntax! Use /reaper [spawn]");
		}
		
	}
	

}
