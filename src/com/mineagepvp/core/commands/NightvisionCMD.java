package com.mineagepvp.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.mineagepvp.core.api.Api;

public class NightvisionCMD extends MineageCommand {

	public NightvisionCMD() {
		super("nightvision", "mineage.nightvision", false);
	}

	public void execute(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		String message = ChatColor.translateAlternateColorCodes('&',
				plugin.getConfig().getString("nightvisionMessageEnable"));
		String message2 = ChatColor.translateAlternateColorCodes('&',
				plugin.getConfig().getString("nightvisionMessageDisable"));
		if (p.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
			p.sendMessage(Api.prefix + message2);
			p.removePotionEffect(PotionEffectType.NIGHT_VISION);
			return;
		} else {
			p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 2));
			p.sendMessage(Api.prefix + message);
			return;
		}
	}

}
