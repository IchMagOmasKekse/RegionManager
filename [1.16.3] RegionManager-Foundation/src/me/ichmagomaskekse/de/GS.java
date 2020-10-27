package me.ichmagomaskekse.de;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class GS {
	
	private String id = "none";
	private String nickname = "none";
	private UUID creator_uuid = null;
	private String creator_name = "none";
	private boolean isOwned = false;
	private UUID owner_uuid = null;
	private ArrayList<UUID> admins = new ArrayList<UUID>();
	//private HashMap<UUID, MemberPermissions> member = new HashMap<UUID, MemberPermissions>();
	private ArrayList<UUID> member = new ArrayList<UUID>();
	private SettingsLink setlink = null;
	private double price1 = 0.0D;
	private double price2 = 0.0D;
	private Location sign_loc;
	private Region region = null;
	
	public GS(String id) {
		this.id = id;
		readProperties();
	}
	
	public boolean readProperties() {
		File file = new File(RS.home_path+"GS/"+id+".yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		if(file.exists() == false) return false;
		
		nickname = cfg.getString("Region.Nickname");
		if(cfg.getString("Region.Owner").equalsIgnoreCase("none") == false)
			creator_uuid = UUID.fromString(cfg.getString("Region.Creator.UUID"));
		creator_name = cfg.getString("Region. Creator.Name");
		isOwned = cfg.getBoolean("Region.is Owned");
		if(cfg.getString("Region.Owner").equalsIgnoreCase("none") == false)
			owner_uuid = UUID.fromString(cfg.getString("Region.Owner"));
		for(String s : cfg.getStringList("Region.Admins")) admins.add(UUID.fromString(s));
		for(String s : cfg.getStringList("Region.Member")) member.add(UUID.fromString(s));
		setlink = new SettingsLink(cfg.getString("Region.Settings.Link"), cfg.getInt("Region.Settings.Link Blocks"), cfg.getInt("Region.Settings.Link Blocksize"));		
		price1 = cfg.getDouble("Region.Price.money1");
		price2 = cfg.getDouble("Region.Price.money2");
		sign_loc = new Location(Bukkit.getWorld(cfg.getString("Region.Sign.World")), cfg.getDouble("Region.Sign.X"), cfg.getDouble("Region.Sign.Y"), cfg.getDouble("Region.Sign.Z"));
		region = new Region(new Location(Bukkit.getWorld(cfg.getString("Region.Pos1.World")),
						cfg.getDouble("Region.Pos1.X"),
						cfg.getDouble("Region.Pos1.Y"),
						cfg.getDouble("Region.Pos1.Z")),
				new Location(Bukkit.getWorld(cfg.getString("Region.Pos2.World")),
						cfg.getDouble("Region.Pos2.X"),
						cfg.getDouble("Region.Pos2.Y"),
						cfg.getDouble("Region.Pos2.Z")));
		
		return true;
	}
	/* --------------- */
	
	/*
	 * löscht alle Entities auf einem GS.
	 * Bypass beinhaltet die EntityTypes, die dabei verschont bleiben.
	 * Spieler werden niemals gelöscht. Daher müssen sie nicht in der bypass List eingetragen werden.
	 */
	public void killAllEntity() {
		killAllEntity(null);
	}
	public void killAllEntity(ArrayList<EntityType> filter) {
		/*
		 * filter beinhaltet EntityTypes die NICHT gelöscht werden.
		 * 
		 * Ebenfalls nicht gelöscht werden Entities, die eine von den folgenden Bedingungen erfüllen:
		 * - Sind kein Spieler
		 * - Besitzen ein Nickname
		 * - Sind im Filter enthalten
		 */
		int i = 0;
		boolean check = false;
		for(Entity e : getRegion().getWorld().getEntities()) {
			if(e instanceof Player == false) {
				if(filter != null && filter.contains(e.getType())) {
					check = false;
				}else check = true;
				
				if(check) {					
					if(e.getCustomName() == null) {
						if(getRegion().isIn(e.getLocation())) {
							e.remove();
							i++;
						}
					}
				}
			}
			check = false;
		}
		RS.broadcastMessage(true, "§aEs wurden §f"+i+" Entities §agelöscht!");
	}
	
	/* --------------- */
	public String getId() { return id; }
	public String getNickname() { return nickname; }
	public UUID getCreatorUUID() { return creator_uuid; }
	public String getCreatorName() { return creator_name; }
	public boolean isOwned() { return isOwned; }
	public UUID getOwnerUUID() { return owner_uuid; }
	public ArrayList<UUID> getAdmins() { return admins; }
	public ArrayList<UUID> getMember() { return member; }
	public SettingsLink getSettingsLink() { return setlink; }
	public double getPrice1() { return price1; }
	public double getPrice2() { return price2; }
	public Location getSignLocation() { return sign_loc.clone(); }
	public Region getRegion() { return region; }



	@SuppressWarnings("unused")
	public static class MemberPermissions {
		
		        //       Perm. | Perm.-level
		private HashMap<String,    Integer> permissions = new HashMap<String, Integer>();
		private Player player = null;
		private SettingsLink setlink = null;
		
		public MemberPermissions(Player player, SettingsLink setlink) {
			this.player = player;
			this.setlink = setlink;
		}
		
	}
	
	public static class SettingsLink {
		
		private String link = "";
		private int blocks = 0;
		private int blocksize = 0;
		
		public SettingsLink(String link, int blocks, int blocksize) {
			this.link = link;
			this.blocks = blocks; 
			this.blocksize = blocksize;
		}

		public String getLink() {
			return link;
		}

		public int getBlocks() {
			return blocks;
		}

		public int getBlocksize() {
			return blocksize;
		}
		
		
	}
	
}
