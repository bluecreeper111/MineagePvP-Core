package com.mineagepvp.core.events;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import com.mineagepvp.core.Main;

import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand.EnumClientCommand;

public class AutoRespawn implements Listener {
	
	private Main plugin;
	
	public AutoRespawn(Main pl) {
		plugin = pl;
	}
	@EventHandler
	public void die(PlayerDeathEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			 Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		            @Override
		            public void run() {
		                PacketPlayInClientCommand packet = new PacketPlayInClientCommand();
		                try {
		                    Field a = PacketPlayInClientCommand.class.getDeclaredField("a");
		                    a.setAccessible(true);
		                    a.set(packet, EnumClientCommand.PERFORM_RESPAWN);
		                } catch (NoSuchFieldException e) {
		                    e.printStackTrace();
		                } catch (SecurityException e) {
		                    e.printStackTrace();
		                } catch (IllegalArgumentException e) {   
		                    e.printStackTrace();
		                } catch (IllegalAccessException e) {
		                    e.printStackTrace();
		                }
		                ((CraftPlayer) p).getHandle().playerConnection.a(packet);
		            }
		        }, 2L);
			}
		}

}
