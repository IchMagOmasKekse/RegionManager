package me.ichmagomaskekse.de;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Chat {
	
	@SuppressWarnings("deprecation")
	public static boolean sendClickablePlotID(Player target, String id, boolean isLoaded) {
		/*
		 * CMD: gs open:plot_2942
		 */
		TextComponent message = null;
		if(isLoaded) message = new TextComponent(" §7- §b"+id);
		else message = new TextComponent(" §7- §7"+id);
		
		message.setColor(ChatColor.AQUA);
		message.setBold(true);
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gs open:"+id));
		if(isLoaded) message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("§aGS wurde bereits geladen.\n§fWähle eine ID um sie zu verwalten\n"+GSManager.getGS(id).getInfoStringForHoverring()).color(ChatColor.WHITE).italic(true).create()));
		else message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder("§cGS wurde noch nicht geladen.\n§fWähle eine ID um sie zu verwalten\n"+GSManager.getGS(id).getInfoStringForHoverring()).color(ChatColor.WHITE).italic(true).create()));
		
		target.spigot().sendMessage(message);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean sendHoverableMessage(Player target, String msg, String hovermsg, boolean bold, boolean italic) {
		msg = prepareString(msg);
		hovermsg = prepareString(hovermsg);
		TextComponent message = new TextComponent(msg);
		
		message.setItalic(italic);
		message.setBold(bold);
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(hovermsg).italic(italic).bold(bold).create()));
		
		target.spigot().sendMessage(message);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean sendCopyableMessage(Player target, String msg, String hovermsg, String copy, boolean bold, boolean italic) {
		msg = prepareString(msg);
		hovermsg = prepareString(hovermsg);
		TextComponent message = new TextComponent(msg);
		
		message.setItalic(italic);
		message.setBold(bold);
		message.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copy));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new ComponentBuilder(hovermsg).italic(italic).bold(bold).create()));
		
		target.spigot().sendMessage(message);
		return true;
	}
	
	/*
	 * Cleart den Chat für alle Spieler die nicht im Bypass drinnen sind.
	 */
	public static void clearChat(ArrayList<UUID> bypass) {
		TextComponent message = new TextComponent("");
		message.setColor(ChatColor.AQUA);
		for(int i = 0; i != 200; i++) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(bypass.contains(p.getUniqueId()) == false) {
					p.spigot().sendMessage(message);
				}
			}
		}
	}
	
	public static String prepareString(String s) {
		s = s.replace("{NEWLINE}", "\n");
		return s;
	}
	
}
