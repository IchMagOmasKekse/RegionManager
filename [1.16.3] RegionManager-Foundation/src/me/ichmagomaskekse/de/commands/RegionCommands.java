package me.ichmagomaskekse.de.commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.GS;
import me.ichmagomaskekse.de.RS;
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
				if(cmd.getName().equalsIgnoreCase("gs") && RS.hasPermission(p, "region.cmd")) {
					sendCommandInfo(p);
				}
				break;
			case 1:
				if(args[0].equalsIgnoreCase("tp") && RS.hasPermission(p, "region.tp")) {
//					p.sendMessage("TODO: Grundstücke müssen erstmal claimbar sein");
					p.sendMessage(SessionManager.createRandomSettingsLink());
				}else if(args[0].equalsIgnoreCase("mksel") && RS.hasPermission(p, "region.makeselection")) {
					if(SessionManager.hasSession(p) && SessionManager.getSession(p).isReadyToSave()) {
						if(SessionManager.saveSelection(p)) p.sendMessage("§aDeine Selektion wurde gespeichert(§f"+SessionManager.getSession(p).getID()+"§a)");
						else p.sendMessage("§cDeine Selektion konnte nicht gespeichert werden");
						//TODO:
					}else p.sendMessage("§cDu musst §fzwei Positionen §cund §feine Position des Verkaufsschildes §cmarkieren um eine Selektion speichern zu können!");
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
		p.sendMessage("X§b/gs tp §7Zum GS teleportieren");
		p.sendMessage("X§b/gs help §7Zeigt Command-Infos");
		p.sendMessage("X§b/gs mksel §7Speichert eine Selection");
		p.sendMessage("X§b/gs set id §7Setzt eine ID");
		p.sendMessage("X§b/gs set owner §7Setzt einen owner");
		p.sendMessage("X§b/gs set cancel §7Session beenden");
	}
	
}
