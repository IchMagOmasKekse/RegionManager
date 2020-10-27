package me.ichmagomaskekse.de.events;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.ichmagomaskekse.de.RS;
import me.ichmagomaskekse.de.session.PlayerSession;
import me.ichmagomaskekse.de.session.SessionManager;

public class PlayerInteract implements Listener {
		
	public PlayerInteract() {
		RS.getInstance().getServer().getPluginManager().registerEvents(this, RS.getInstance());
	}
	
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent e) {
		if(e.getHand() == EquipmentSlot.HAND) {			
			if(e.getPlayer().getInventory().getItemInMainHand() != null) {
				ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
				if(item.getType() == SessionManager.wand) {
					if(e.getPlayer().hasPermission("region.selection.create")) {
						Location pos = e.getClickedBlock().getLocation();
						if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
							
							SessionManager.newSession(e.getPlayer()).setUnprocessedPos1(pos);
							
							RS.broadcastMessage(true, "§7POS1 wurde gesetzt");
							e.setCancelled(true);					
						}else if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
							if(e.getPlayer().isSneaking()) {
								if(SessionManager.hasSession(e.getPlayer()) == false) return;
								SessionManager.getSession(e.getPlayer()).setSignLocation(e.getClickedBlock().getLocation());
								
								RS.broadcastMessage(true, "§7Das Verkaufsschild wurde erfasst");
								e.setCancelled(true);								
							}else {
								SessionManager.newSession(e.getPlayer()).setUnprocessedPos2(pos);
								
								RS.broadcastMessage(true, "§7POS2 wurde gesetzt");
								e.setCancelled(true);								
							}
						}
						
						PlayerSession session = SessionManager.getSession(e.getPlayer());
						if(session.hasBothPositions()) {						
							RS.broadcastMessage(true, "§7"+((int)session.getUnprocessedPos1().getX())+"/"+((int)session.getUnprocessedPos1().getY())+"/"+((int)session.getUnprocessedPos1().getZ()));
							RS.broadcastMessage(true, "§7"+((int)session.getUnprocessedPos2().getX())+"/"+((int)session.getUnprocessedPos2().getY())+"/"+((int)session.getUnprocessedPos2().getZ()));
						}
					}
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onMove(PlayerMoveEvent e) {
//		if(SessionManager.hasSession(e.getPlayer()) && SessionManager.getSession(e.getPlayer()).getRegion().isIn(e.getPlayer().getLocation())) RS.broadcastMessage(true, "Bist drin!");
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlace(BlockPlaceEvent e) {
		if(RS.hasPermission(e.getPlayer(), "region.build.everywhere") == false && SessionManager.hasSession(e.getPlayer()) &&
				SessionManager.getSession(e.getPlayer()).getRegion().isIn(e.getBlockPlaced().getLocation()) == false)
			e.setCancelled(true);
	}
}
