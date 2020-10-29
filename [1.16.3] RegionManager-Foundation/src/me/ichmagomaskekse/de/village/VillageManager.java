package me.ichmagomaskekse.de.village;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ichmagomaskekse.de.RS;
import me.ichmagomaskekse.de.Region;
import me.ichmagomaskekse.de.session.SessionManager;

@SuppressWarnings("unused")
public class VillageManager {
	
	private static HashMap<String, Village> villages = new HashMap<String, Village>();
	
	public VillageManager(boolean createBlanko) {
		loadVillages();
		if(createBlanko) generateBlankoVillage(SessionManager.getRandomCode(1, 6));
	}
	
	/*
	 * loadVillages() lädt alle Dörfer aus den Dateien und erstellt
	 * Instanzen die in der HashMap 'villages' gespeichert werden.
	 */
	private static void loadVillages() {
		File dir = new File(RS.home_path+"Villages/");
		File[] files = dir.listFiles();
		FileConfiguration cfg = null;
		for(File file : files) {
			if(file.isFile() && file.getName().endsWith(".yml")) {
				cfg = YamlConfiguration.loadConfiguration(file);
				villages.put(cfg.getString("Village.Name"), new Village(cfg.getString("Village.Name")));
			}
		}
	}
	
	public static Village loadVillage(String name) {
		return new Village(name);
	}
	
	public static Village getVillage(String name) {
		if(isLoaded(name) == false) loadVillage(name);
		return villages.get(name);
	}
	
	public static boolean isLoaded(String name) {
		return villages.containsKey(name);
	}
	
	public static void generateBlankoVillage(String name) {
		File file = new File(RS.home_path+"Villages/"+name+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.set("Village.Name", name);
		cfg.set("Village.Type", VillageType.WILDERNESS.toString());
		cfg.set("Village.Settings Link", "Run /gs setlink to get a new Settings Link");
		
		cfg.set("Village.Pos1.World", "world");
		cfg.set("Village.Pos1.X", "0");
		cfg.set("Village.Pos1.Y", "0");
		cfg.set("Village.Pos1.Z", "0");
		
		cfg.set("Village.Pos2.World", "world");
		cfg.set("Village.Pos2.X", "0");
		cfg.set("Village.Pos2.Y", "0");
		cfg.set("Village.Pos2.Z", "0");
		
		cfg.set("Village.Spawn.World", "world");
		cfg.set("Village.Spawn.X", "0.5");
		cfg.set("Village.Spawn.Y", "0.5");
		cfg.set("Village.Spawn.Z", "0.5");
		
		try { cfg.save(file); } catch (IOException e) { e.printStackTrace(); }
	}
	
	public static class Village {
		
		private VillageType type = VillageType.WILDERNESS;
		private Region region;
		private String name = "";
		
		public Village(String name) {
			this.name = name;
			RS.broadcastMessage(true, "Village §a"+name+" §fwurde geladen");
			//TODO: Arbeit an Village beendet um die Hovermessage von /gs list weiterzumachen.
		}
		
		public void save() {
			
		}
		
		public String getName() { return name; }
		public String getCodename() { return type.getCodename(); }
		public String getDisplayname() { return type.getDisplayname(); }
		
	}
	
	public static enum VillageType {
		
		UNDEFINED(   "undefined",  "UNDEFINIERT",  "none",                             0),
		WILDERNESS(  "wilderness", "Wildnis",      "3S60FHOKI1-ul9pM75xzg-1503647qsd", 1),
		STADT(       "city",       "Stadt",        "SjV31M61m5-qI4rf01050-9Au6101g79", 2),
		MODERN(      "modern",     "Modern",       "r1L0532CA5-gjvK0EkV0w-CWBqo968A3", 3),
		FREEBUILD(   "freebuild",  "Freies Bauen", "54967d2x1I-WEKEL3V482-9q6Enkx268", 4),
		MEDIVAL(     "medival",    "Mittelalter",  "mn7863o09y-3aR5Vm7v9l-2G2oklwR91", 5),
		DUBAI(       "dubai",      "Dubai",        "3Nz406G973-d0K55j166a-21fTW24nMe", 6);
		
		String codename, displayname, setlink;
		int id;
		
		VillageType(String codename, String displayname, String setlink, int id) {
			this.codename = codename;
			this.displayname = displayname;
			this.setlink = setlink;
			this.id = id;
		}

		public String getCodename() { return codename; }
		public String getDisplayname() { return displayname; }
		public String getSetlink() { return setlink; }
		public int getId() { return id; }
	}
	
}
