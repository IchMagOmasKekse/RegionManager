package me.ichmagomaskekse.de;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

public class GSManager {
	/*
	 * Beinhaltet alle GS Instanzen die geladen wurden
	 */
	private static HashMap<String, GS> gses = new HashMap<String, GS>();
	
	private BukkitRunnable updater = null;
	
	public GSManager() {
		
		updater = new BukkitRunnable() {
			
			@Override
			public void run() {
				if(gses.isEmpty() == false) {
					for(GS gs : gses.values()) {
						gs.update();
					}
				}
			}
		};
		updater.runTaskTimer(RS.getInstance(), 0, 20l);
		
	}
	
	/*
	 * loadAllGS() lädt alle Grundstücke
	 */
	public static void loadAllGS() {
		File path = new File(RS.home_path+"GS/");
		File[] files = path.listFiles();
		String name = "";
		if(files == null || files.length == 0) return;
		for(File file : files) {
			if(file.isFile() && file.getName().endsWith(".yml") && file.getName().contains("plot_")) {
				name = file.getName().replace(".yml", "");
				loadGS(name);
				if(RS.debug)RS.broadcastMessage(true, "GS mit der ID §a"+name+"§r wurde geladen");
			}
		}
	}
	
	/*
	 * saveAllGS() speichert alle GS Daten.
	 * Diese Methode MUSS in der onDisable() Methode der Main Klasse RS.java verwendet werden!
	 */
	public static boolean saveAllGS() {
		for(String id : gses.keySet()) {
			GS gs = gses.get(id);
			saveGS(gs);
		}
		gses.clear(); //gses wird geleert um RAM-Speicher frei zu geben
		return true;
	}
	
	/*
	 * Lädt alle Grundstücke neu.
	 */
	public static boolean reloadAllGS() {
		saveAllGS();
		loadAllGS();
		return true;
	}
	
	/*
	 * Speichert alle Geänderten Daten aus der GS Instanz in die Datei.
	 * ACHTUNG
	 * Diese Methode muss stetig aktualisiert werden, sobald es neue Einstellmöglichkeiten beim GS gibt!
	 * Ebenso muss die readProperties() Method ein der GS.java Klasse stetig aktualisiert werden!
	 */
	public static boolean saveGS(GS gs) {
		File file = new File(RS.home_path+"GS/"+gs.getId()+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set("Region.Creator.UUID", gs.getCreatorUUID());
		cfg.set("Region.Creator.Name", gs.getCreatorName());
		cfg.set("Region.ID", gs.getId());
		cfg.set("Region.Nickname", gs.getNickname());
		cfg.set("Region.is Owned", gs.isOwned());
		cfg.set("Region.Owner", gs.getOwnerUUID().toString());
		cfg.set("Region.Admins", gs.getAdminsAsStringlist());
		cfg.set("Region.Member", gs.getMemberAsStringlist());
		cfg.set("Region.Settings.Link", gs.getSettingsLink().getLink());
		cfg.set("Region.Settings.Link Blocks", gs.getSettingsLink().getBlocks());
		cfg.set("Region.Settings.Link Blocksize", gs.getSettingsLink().getBlocksize());
		cfg.set("Region.Village", gs.getVillageName());
		
		cfg.set("Region.Price.money1", gs.getPrice1());
		cfg.set("Region.Price.money2", gs.getPrice2());
		
		cfg.set("Region.Sign.World", gs.getSignLocation().getWorld().getName());
		cfg.set("Region.Sign.X", gs.getSignLocation().getX());
		cfg.set("Region.Sign.Y", gs.getSignLocation().getY());
		cfg.set("Region.Sign.Z", gs.getSignLocation().getZ());
		
		cfg.set("Region.Spawn.World", gs.getSpawn().getWorld().getName());
		cfg.set("Region.Spawn.X", gs.getSpawn().getX());
		cfg.set("Region.Spawn.Y", gs.getSpawn().getY());
		cfg.set("Region.Spawn.Z", gs.getSpawn().getZ());
		cfg.set("Region.Spawn.Yaw", gs.getSpawn().getYaw());
		cfg.set("Region.Spawn.Pitch", gs.getSpawn().getPitch());
		
		cfg.set("Region.Pos1.World", gs.getRegion().getCorner1().getWorld().getName());
		cfg.set("Region.Pos1.X", gs.getRegion().getCorner1().getX());
		cfg.set("Region.Pos1.Y", gs.getRegion().getCorner1().getY());
		cfg.set("Region.Pos1.Z", gs.getRegion().getCorner1().getZ());
		
		cfg.set("Region.Pos2.World", gs.getRegion().getCorner7().getWorld().getName());
		cfg.set("Region.Pos2.X", gs.getRegion().getCorner7().getX());
		cfg.set("Region.Pos2.Y", gs.getRegion().getCorner7().getY());
		cfg.set("Region.Pos2.Z", gs.getRegion().getCorner7().getZ());
		
		try {
			cfg.save(file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * Lädt eine Instanz eines Grundstückes oder gibt die geladene zurpck,
	 * wenn eine vorhanden ist.
	 */
	public static GS loadGS(String id) {
		if(isLoaded(id) == false) {
			gses.put(id,  new GS(id));
		}
		return getGS(id);
	}
	 
	/*
	 * Gibt zurück, ob eine GS Instanz bereits geladen wurde.
	 */
	public static boolean isLoaded(String id) {
		return gses.containsKey(id);
	}
	
	/*
	 * Gibt die geladene Instanz eines GRundstückes wieder oder lädt eine neue Instanz,
	 * wenn noch keine geladenen vorhanden ist.
	 */
	public static GS getGS(String id) {
		if(isLoaded(id) == false)  gses.put(id, new GS(id));
		return gses.get(id);
	}
	
	/*
	 * Gibt alle registrierten IDs zurück
	 */
	public static ArrayList<String> getAllIDs() {
		File dir = new File(RS.home_path+"GS/");
		File[] files = dir.listFiles();
		FileConfiguration cfg = null;
		ArrayList<String> ids = new ArrayList<String>();
		for(File file : files) {
			if(file.isFile() && file.getName().endsWith(".yml")) {
				cfg = YamlConfiguration.loadConfiguration(file);
				ids.add(cfg.getString("Region.ID"));
			}
		}
		return ids;
	}
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, GS> getLoadedGSes() {
		return (HashMap<String, GS>) gses.clone();
	}
	
}
