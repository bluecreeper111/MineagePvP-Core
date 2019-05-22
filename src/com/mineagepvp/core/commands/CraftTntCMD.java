package com.mineagepvp.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.mineagepvp.core.api.Api;

public class CraftTntCMD extends MineageCommand {

	public CraftTntCMD() {
		super("crafttnt", "mineage.crafttnt", false);
	}

	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		String message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tntMessage"));
		String none = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("tntNone"));
		double sand = 0;
		double gunpowder = 0;
		for (ItemStack item : p.getInventory().getContents()) {
			if (item == null || item.getType() == Material.AIR) {
				continue;
			}
			if (item.getType() == Material.SAND) {
				sand = sand + item.getAmount();
			}
			if (item.getType() == Material.SULPHUR) {
				gunpowder = gunpowder + item.getAmount();
			}
		}
		int tnt = 0;
		while ((sand / 4 >= 1) && gunpowder / 5 >= 1) {
			sand = sand - 4;
			gunpowder = gunpowder - 5;
			tnt = tnt + 1;
			p.getInventory().removeItem(new ItemStack(Material.SAND, 4));
			p.getInventory().removeItem(new ItemStack(Material.SULPHUR, 5));
			

		}
		p.updateInventory();
		int total = Integer.valueOf(tnt);
		while (tnt > 64) {
			tnt = tnt - 64;
			ItemStack add = new ItemStack(Material.TNT, 64);
			if (p.getInventory().firstEmpty() == -1) {
				p.getWorld().dropItem(p.getLocation(), add);
			} else {
				p.getInventory().addItem(add);
			}
		}
		ItemStack rest = new ItemStack(Material.TNT, tnt);
		if (tnt == 0) {
			p.sendMessage(Api.prefix + none);
			return;
		}
		if (p.getInventory().firstEmpty() == -1) {
			p.getWorld().dropItem(p.getLocation(), rest);
		} else {
			p.getInventory().addItem(rest);
		}
		p.updateInventory();
		p.sendMessage(Api.prefix + message.replaceAll("%tnt%", Integer.toString(total)));
		return;

	}

}
