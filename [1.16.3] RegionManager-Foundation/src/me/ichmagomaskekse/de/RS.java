package me.ichmagomaskekse.de;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.ichmagomaskekse.de.atlas.IDAtlas;
import me.ichmagomaskekse.de.commands.RegionCommands;
import me.ichmagomaskekse.de.events.PlayerInteract;
import me.ichmagomaskekse.de.village.VillageManager;

public class RS extends JavaPlugin {
	/*
	 * RS steht für RegionSystem
	 */
	
	public static String home_path = "plugins/RegionManager/";
	private static RS rs = null;
	public static RS getInstance() {return rs;}
	private static Random ran = new Random();
	public static IDAtlas idatlas = null;
	public static String noPerm = "§cDu hast kein Recht dazu!";
	public static boolean debug = false;
	public static final String none_uuid = "00d1a130-2e7e-4dd5-b2f2-5457ed0406c3";
	
	/* Temporäre API-Eibindung */
	public static ServerSystem ss = ServerSystem.getInstance();
	
	@Override
	public void onEnable() {
		rs = this;
		/* Initialisierung */
		preInit();  //Bevor das Plugin interne Initialisierungen durchführt
		init();     //Währenddessen das Plugin interne Initialisierungen durchführt
		postInit(); //Nachdem das Plugin interne Initialisierungen durchgeführt hat
		/* --------------- */
		
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		
		shutdown();
		
		super.onDisable();
	}
	
	/*
	 * Speichert alle Fortschritte und Daten, bevor das Plugin disabled wird.
	 */
	private void shutdown() {
		
		GSManager.saveAllGS();
		
	}
	
	private void preInit() {
		/*
		 * Systemrelevante Dateien werden erstellt.
		 */
		
		idatlas = new IDAtlas();
	}
	
	private void init() {
		/*
		 * Systmerelevante Einstellungen werden geladen.
		 */
	}
	
	private void postInit() {
		/*
		 * Instanzen werden erstellt.
		 * Events werden registriert.
		 */
		
		new PlayerInteract();
		
		this.getCommand("gs").setExecutor(new RegionCommands());
		
		new GSManager();
		GSManager.loadAllGS();
		
		new VillageManager(false);
	}
	
	public static String getRandomID() {
		//TODO: Ein 'Belegete-Id-Atlas' muss erstellt werden
		String id = "plot_"+ran.nextInt(10000);
		while(IDAtlas.idIsRegistered(id)) id = "plot_"+ran.nextInt(10000);
		return id;
	}
	
	public static void broadcastMessage(boolean onlyOps, String... s) {
		for(String msg : s) {
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(onlyOps) { if(p.isOp()) p.sendMessage(msg); }
				else p.sendMessage(msg);
			}
		}
	}
	
	
	private static boolean alwaysFalse = false;
	private static boolean alwaysTrue = true;
	public static boolean hasPermission(Player p, String permission) {
		if(p.hasPermission(permission) == false || (alwaysFalse == true && alwaysTrue == false)) {
			p.sendMessage(noPerm);
			return false;
		}else return true;
	}
	
}
