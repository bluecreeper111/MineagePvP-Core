package com.mineagepvp.core.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import com.mineagepvp.core.api.Api;

public class PotCMD extends MineageCommand implements Listener {
	
	public PotCMD() {
		super("pot", "mineage.pot", false);
	}

	
	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		String m = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("potMessage"));
		HashMap<Short, Integer> pots = new HashMap<>();
		for (ItemStack item : p.getInventory().getContents()) {
			if (item == null || item.getType() == Material.AIR) { continue; }
			if (item.getType() == Material.POTION) {
				if (pots.get(item.getDurability()) == null) {
					pots.put(item.getDurability(), item.getAmount());
				} else {
					pots.put(item.getDurability(), pots.get(item.getDurability()) + item.getAmount());
				}
				p.getInventory().removeItem(item);
			}
		}
		for (Short type : pots.keySet()) {
			int amount = pots.get(type);
			while (amount > 64) {
				amount = amount - 64;
				ItemStack add = new ItemStack(Material.POTION, 64, type);
				p.getInventory().addItem(add);
				p.updateInventory();
			}
			ItemStack rest = new ItemStack(Material.POTION, amount, type);
			p.getInventory().addItem(rest);
			p.updateInventory();
			continue;
		}
		p.sendMessage(Api.prefix + m);
		return;
		
	}
	
	@EventHandler
	public void use(PlayerItemConsumeEvent e) {
		if (e.getItem().getType() == Material.POTION && (e.getItem().getAmount() > 1)) {
			e.setCancelled(true);
		}
	}

}
