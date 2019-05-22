package com.mineagepvp.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ClearchatCMD extends MineageCommand {

	public ClearchatCMD() {
		super("clearchat", "mineage.clearchat.global", true);
		}

	
	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		for (int i = 0; i < 70; i++) {
			Bukkit.broadcastMessage(" ");
		}
		return;
	}

}
