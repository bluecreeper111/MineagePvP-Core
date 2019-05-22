package com.mineagepvp.core.commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.mineagepvp.core.Main;
import com.mineagepvp.core.api.Api;

import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.api.exception.PlayerDataNotLoadedException;
import net.milkbowl.vault.economy.EconomyResponse;

public class SellwandCMD extends MineageCommand implements Listener {

	public SellwandCMD() {
		super("sellwand", "mineage.sellwand.give", true);
	}

	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
			Player tar = Bukkit.getPlayerExact(args[1]);
			if (tar == null) {
				sender.sendMessage(Api.prefix + "§cThat player was not found!");
				return;
			}
			ItemStack hoe = new ItemStack(Material.GOLD_HOE, 1);
			ItemMeta hoem = hoe.getItemMeta();
			hoem.setDisplayName("§c§lSellWand");
			ArrayList<String> lore = new ArrayList<>();
			lore.add("§7Left click to automatically sell chest contents!");
			hoem.setLore(lore);
			hoe.setItemMeta(hoem);
			tar.getInventory().addItem(hoe);
			sender.sendMessage(Api.prefix + "§aSell want given to §2" + tar.getName());
		} else {
			sender.sendMessage(Api.prefix + "§cIncorrect syntax! Try /sellwand give <player>");
            return;
		}

	}
	
	@EventHandler
	public void use(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		Block b = e.getClickedBlock();
		ItemStack item = p.getItemInHand();
		if (item.getType() == Material.GOLD_HOE && item.getItemMeta().getDisplayName().equals("§c§lSellWand")) {
			if (b.getType() == Material.CHEST && e.getAction() == Action.LEFT_CLICK_BLOCK) {
				Chest chest = (Chest) b.getState();
				for (ItemStack items : chest.getBlockInventory().getContents()) {
					if (items == null || items.getType() == Material.AIR) { continue; }
					try {
						double sell = ShopGuiPlusApi.getItemStackPriceSell(p, items);
						EconomyResponse bal = Main.getEconomy().depositPlayer(p, sell);
						if (bal.transactionSuccess()) {
							p.sendMessage(Api.prefix + "§aSold §2" + items.getAmount() + "§a " + items.getType().toString().toLowerCase() + " for §2$" + Double.toString(sell));
							chest.getBlockInventory().removeItem(items);
						} else {
							continue;
						}
					} catch (PlayerDataNotLoadedException e1) {
						e1.printStackTrace();
						p.sendMessage(Api.prefix + "§cAn error occured.");
					}
				}
			}
		}
	}

}
