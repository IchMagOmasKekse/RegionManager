package me.ichmagomaskekse.de.commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.Chat;
import me.ichmagomaskekse.de.GS;
import me.ichmagomaskekse.de.GSManager;
import me.ichmagomaskekse.de.RS;
import me.ichmagomaskekse.de.session.PlayerSession;
import me.ichmagomaskekse.de.session.SessionManager;

public class RegionCommands implements CommandExecutor {
	
	public RegionCommands() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		
		if(sender instanceof Player) {
			Player p = (Player) sender; 
			
			switch(args.length) {
			
			case 0:
				if(cmd.getName().equalsIgnoreCase("gs") && RS.hasPermission(p, "region.gs")) {
					sendCommandInfo(p);
				}
				break;
			case 1:
				if(args[0].contains("setlink") && RS.hasPermission(p, "region.setlink")) {
					String setlink = SessionManager.createRandomSettingsLink();
					Chat.sendCopyableMessage(p, "§e"+setlink, setlink, setlink, false, false);
					p.playSound(p.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0f, 0f);
				}else if(args[0].contains("help") && RS.hasPermission(p, "region.help")) {
					sendCommandInfo(p);
				}else if(args[0].contains("fill") && RS.hasPermission(p, "region.fill")) {
					if(SessionManager.hasSession(p)) SessionManager.getSession(p).getRegion().fill(Material.DIRT);
					else p.sendMessage("§cDu musst ein Plot bei §f/gs list §causwählen um es füllen zu können");
				}else if(args[0].contains("open") && RS.hasPermission(p, "region.open")) {
					SessionManager.getSession(p).setGS(GSManager.getGS(args[0].split(":")[1]), true);
					p.sendMessage("§bGS §f"+args[0].split(":")[1]+" §bwurde geladen. Du kannst es nun verwalten.");
				}else if(args[0].equalsIgnoreCase("list") && RS.hasPermission(p, "region.list")) {
					listGSes(p);
				}else if(args[0].equalsIgnoreCase("tp") && RS.hasPermission(p, "region.tp")) {
					if(SessionManager.hasSession(p)) {
						SessionManager.getSession(p).getGS().teleportToGS(p);
						p.playSound(p.getEyeLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 6f, 3f);
					}
				}else if(args[0].equalsIgnoreCase("mksel") && RS.hasPermission(p, "region.makeselection")) {
					if(SessionManager.hasSession(p) && SessionManager.getSession(p).isReadyToSave()) {
						
						PlayerSession session = SessionManager.getSession(p);
						
						if(SessionManager.saveSelection(p)) p.sendMessage("§aDeine Selektion wurde gespeichert(§f"+session.getID()+"§a)");
						else p.sendMessage("§cDeine Selektion konnte nicht gespeichert werden");
					}else p.sendMessage("§cDu musst §fzwei Positionen§c, §feine Position des Verkaufsschildes §cund einen §fSpawn §cmarkieren um eine Selektion speichern zu können!");
				}else if(args[0].equalsIgnoreCase("kill") && RS.hasPermission(p, "region.kill.entities")) {
					GS gs = new GS("plot_3956");
					
					gs.killAllEntity(new ArrayList<>(Arrays. asList(EntityType.COW)));
				}
				break;
			
			}
			
		}else sender.sendMessage("§cDieser Befehl ist nur für Spieler!");
		
		return false;
	}
	
	public void sendCommandInfo(Player p) {
		p.sendMessage(" --------- §bBefehls Information §f---------- ");
		if(p.isOp() || p.hasPermission("region.*") || p.hasPermission("region.command.admin"))
			p.sendMessage("§aAlle Spieler §cOP-Only");
		
		p.sendMessage(" §a/gs kill §7Entities auf GS töten");
		p.sendMessage(" §a/gs tp §7Zum GS teleportieren");
		p.sendMessage(" §a/gs help §7Zeigt Command-Infos");
		if(p.isOp() || p.hasPermission("region.*") || p.hasPermission("region.command.admin")) {			
			p.sendMessage(" §c/gs mksel §7Speichert eine Selection");
			p.sendMessage("X§c/gs set id §7Setzt eine ID");
			p.sendMessage("X§c/gs set owner §7Setzt einen owner");
			p.sendMessage("X§c/gs set cancel §7Session beenden");
			p.sendMessage(" §c/gs list §7Liste alle Grundstücke");
		}
	}
	
	public void listGSes(Player p) {
		Chat.sendHoverableMessage(p, "§bAlle registrierten Grundstücke:", "§fHalte deine Maus über eine ID um alle{NEWLINE}§frelevanten Infos über das GS zu erfahren.", false, false);
		
		for(String id : GSManager.getAllIDs()) {
			if(GSManager.getLoadedGSes().containsKey(id))  Chat.sendClickablePlotID(p, id, true);
		}
		for(String id : GSManager.getAllIDs()) {
			if(GSManager.getLoadedGSes().containsKey(id) == false)  Chat.sendClickablePlotID(p, id, false);
		}
			
			
	}
	
}
