package com.mineagepvp.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.mineagepvp.core.api.Api;

public class CpcCMD extends MineageCommand {

	public CpcCMD() {
		super("cpc", "mineage.clearchat.private", false);
		}

	
	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender; 
		String message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("privateClearChatMessage"));
		for (int i = 0; i < 70; i++) {
			p.sendMessage(" ");
		}
		p.sendMessage(Api.prefix + message);
		return;
	}

}
