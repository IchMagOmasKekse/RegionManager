package me.ichmagomaskekse.de.session;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.ichmagomaskekse.de.RS;

public class SessionManager {
	
	/*
	 * Der SessionManager handhabt jede PlayerSession.
	 * Vom SessionManager erhält man die jeweilige Session des Players und kann sie dementsprechend verwalten.
	 */
	
	/* Settings-Link Properties */
	private static Random random = new Random();
	private static int amount_blocks = 3; //MUSS MINDESTENS 1 SEIN!
	private static int size_of_block = 10; //MUSS MINDESTENS 6 SEIN!
	private static String seperator = "-";
	/* ----- */
	private static HashMap<Player, PlayerSession> sessions = new HashMap<Player, PlayerSession>();
	public static final Material wand = Material.NETHERITE_AXE;
	
	public SessionManager() { }
	
	public static boolean removeSession(Player host) {
		if(hasSession(host)) sessions.remove(host);
		else return false;
		return true;
	}
	
	/*
	 * newSession registriert eine neue PlayerSession für einen Spieler.
	 */
	public static PlayerSession newSession(Player host) {
		if(hasSession(host) == false) {
			sessions.put(host, new PlayerSession(RS.getRandomID(), host));
			return sessions.get(host);
		}else return sessions.get(host);
	}
	
	
	public static boolean hasSession(Player p) {
		return sessions.containsKey(p);
	}
	
	public static PlayerSession getSession(Player host) {
		if(hasSession(host) == false) newSession(host); 
		return sessions.get(host);
	}
	
	public static Location getSignLocation(String id) {
		File file = new File(RS.home_path+"GS/"+id+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		return new Location(Bukkit.getWorld(cfg.getString("Region.Sign.World")), cfg.getDouble("Region.Sign.X"), cfg.getDouble("Region.Sign.Y"), cfg.getDouble("Region.Sign.Z"));
	}
	
	public static boolean saveSelection(Player host) {
		if(hasSession(host) == false) return false;
		
		PlayerSession session = getSession(host);
		
		File file = new File(RS.home_path+"GS/"+session.getID()+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		
		cfg.set("Region.Creator.UUID", session.getHost().getUniqueId().toString());
		cfg.set("Region.Creator.Name", session.getHost().getName());
		cfg.set("Region.ID", session.getID());
		cfg.set("Region.Nickname", "none");
		cfg.set("Region.is Owned", false);
		cfg.set("Region.Owner", "none");
		cfg.set("Region.Admins", "none");
		cfg.set("Region.Member", "[]");
		cfg.set("Region.Settings.Link", createRandomSettingsLink());
		cfg.set("Region.Settings.Link Blocks", amount_blocks);
		cfg.set("Region.Settings.Link Blocksize", size_of_block);
		cfg.set("Region.Village", "none");
		
		cfg.set("Region.Price.money1", 100.0d);
		cfg.set("Region.Price.money2", 100.0d);
		
		cfg.set("Region.Sign.World", session.getSignLoc().getWorld().getName());
		cfg.set("Region.Sign.X", session.getSignLoc().getX());
		cfg.set("Region.Sign.Y", session.getSignLoc().getY());
		cfg.set("Region.Sign.Z", session.getSignLoc().getZ());
		
		cfg.set("Region.Spawn.World", session.getSpawn().getWorld().getName());
		cfg.set("Region.Spawn.X", session.getSpawn().getX());
		cfg.set("Region.Spawn.Y", session.getSpawn().getY());
		cfg.set("Region.Spawn.Z", session.getSpawn().getZ());
		cfg.set("Region.Spawn.Yaw", session.getSpawn().getYaw());
		cfg.set("Region.Spawn.Pitch", session.getSpawn().getPitch());
		
		cfg.set("Region.Pos1.World", session.getPos1().getWorld().getName());
		cfg.set("Region.Pos1.X", session.getPos1().getX());
		cfg.set("Region.Pos1.Y", session.getPos1().getY());
		cfg.set("Region.Pos1.Z", session.getPos1().getZ());
		
		cfg.set("Region.Pos2.World", session.getPos2().getWorld().getName());
		cfg.set("Region.Pos2.X", (session.getPos2().getX() + 1));
		cfg.set("Region.Pos2.Y", session.getPos2().getY());
		cfg.set("Region.Pos2.Z", (session.getPos2().getZ() + 1));
		
		
		try {
			cfg.save(file);
			removeSession(host);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static String createRandomSettingsLink() {
		return getRandomCode(amount_blocks, size_of_block);
	}

	/* Generiert einen zufälligen Code, welcher beliebig anpassbar ist */
	public static String getRandomCode(int blocks, int blocksize) {
		if(blocks < 1) blocks = 1;
		if(blocksize < 6) blocksize = 6;
		
		String link = "";
		
		for(int i = 0; i != blocks; i++) {
			for(int ii = 0; ii != blocksize; ii++) {
				if(random.nextBoolean()) {
					//Buchstabe
					if(random.nextBoolean()) {
						//Großbuchstabe
						switch(random.nextInt(26)) {
						case  0: link = link + "A"; break;
						case  1: link = link + "B"; break;
						case  2: link = link + "C"; break;
						case  3: link = link + "D"; break;
						case  4: link = link + "E"; break;
						case  5: link = link + "F"; break;
						case  6: link = link + "G"; break;
						case  7: link = link + "H"; break;
						case  8: link = link + "I"; break;
						case  9: link = link + "J"; break;
						case 10: link = link + "K"; break;
						case 11: link = link + "L"; break;
						case 12: link = link + "M"; break;
						case 13: link = link + "N"; break;
						case 14: link = link + "O"; break;
						case 15: link = link + "P"; break;
						case 16: link = link + "Q"; break;
						case 17: link = link + "R"; break;
						case 18: link = link + "S"; break;
						case 19: link = link + "T"; break;
						case 20: link = link + "U"; break;
						case 21: link = link + "V"; break;
						case 22: link = link + "W"; break;
						case 23: link = link + "X"; break;
						case 24: link = link + "Y"; break;
						case 25: link = link + "Z"; break;
						}
					}else {
						switch(random.nextInt(26)) {
						case  0: link = link + "a"; break;
						case  1: link = link + "b"; break;
						case  2: link = link + "c"; break;
						case  3: link = link + "d"; break;
						case  4: link = link + "e"; break;
						case  5: link = link + "f"; break;
						case  6: link = link + "g"; break;
						case  7: link = link + "h"; break;
						case  8: link = link + "i"; break;
						case  9: link = link + "j"; break;
						case 10: link = link + "k"; break;
						case 11: link = link + "l"; break;
						case 12: link = link + "m"; break;
						case 13: link = link + "n"; break;
						case 14: link = link + "o"; break;
						case 15: link = link + "p"; break;
						case 16: link = link + "q"; break;
						case 17: link = link + "r"; break;
						case 18: link = link + "s"; break;
						case 19: link = link + "t"; break;
						case 20: link = link + "u"; break;
						case 21: link = link + "v"; break;
						case 22: link = link + "w"; break;
						case 23: link = link + "x"; break;
						case 24: link = link + "y"; break;
						case 25: link = link + "z"; break;
						}
						//Kleinbuchstabe
						
					}
				}else {
					//Zahl
					link = link + ""+random.nextInt(10);
				}
			}
			
			if((i == (blocks - 1)) == false) link = link+seperator;
		}
		return link;
	}
	
}
