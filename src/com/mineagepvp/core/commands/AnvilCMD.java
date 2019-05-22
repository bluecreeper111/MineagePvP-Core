package com.mineagepvp.core.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.mineagepvp.core.api.Api;

public class AnvilCMD extends MineageCommand {
	
	public AnvilCMD() {
		super("anvil", "mineage.anvil", false);
	}

	
	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		p.closeInventory();
		Inventory anvil = Bukkit.createInventory(null, InventoryType.ANVIL);
		p.openInventory(anvil);
		p.sendMessage(Api.prefix + "§aAnvil Opened.");
	}

}
