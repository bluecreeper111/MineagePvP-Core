package com.mineagepvp.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.mineagepvp.core.api.Api;

public class TrashCMD extends MineageCommand {
	
	public TrashCMD() {
		super("trash", "mineage.trash", false);
	}

	
	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		String title = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("trashGuiTitle"));
		p.closeInventory();
		Inventory trash = Bukkit.createInventory(null, 27, title);
		p.openInventory(trash);
		p.sendMessage(Api.prefix + "§aTrash opened.");
		return;
	}
	

}
