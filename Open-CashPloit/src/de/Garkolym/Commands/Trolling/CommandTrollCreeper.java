/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 OlfillasOdikno
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package de.Garkolym.Commands.Trolling;

import java.util.ArrayList;
import java.util.HashMap;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerMoveEvent;


import de.Garkolym.Start;
import de.Garkolym.Commands.Command;
import de.Garkolym.Commands.CommandCategory;

public class CommandTrollCreeper extends Command implements Listener{

	
	private HashMap<Player, Location> vics = new HashMap<Player,Location>();
	private HashMap<Player, Player> ps = new HashMap<Player,Player>();
	private HashMap<Entity,Player> ep = new HashMap<Entity,Player>();
	public CommandTrollCreeper() {
		super("trollcreeper", CommandCategory.TROLLING);
        Bukkit.getPluginManager().registerEvents(this, Start.instance);
	}

	@Override
	public void onCommand(Player p, ArrayList<String> args) {
		if (args.size() == 1) {
			
			Player victim = Bukkit.getPlayer(args.get(0));
			if(victim != null){
				if (!this.vics.containsKey(victim)) {
					this.vics.put(victim,victim.getLocation());
				    p.sendMessage(Start.instance.prefix + ChatColor.GREEN + "Der Spieler wird bald besuch bekommen :D");
				    ps.put(victim, p);
				    
				}else{
					this.vics.remove(victim);
				    p.sendMessage(Start.instance.prefix + ChatColor.RED + "Der Spieler wird keinen TrollCreeper bekommen!");
				}
			}else{
				p.sendMessage(Start.instance.prefix + ChatColor.RED + "Der Spieler ist nicht online!");
			}			

	}else{
		p.sendMessage(Start.instance.prefix + ChatColor.RED + "Bitte gib nach dem Command nur den Namen des Spielers an!");
	}
		
	}
	
	
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		if(vics.containsKey(e.getPlayer())){
			
			if(e.getPlayer().getLocation().getYaw()+160 <= vics.get(e.getPlayer()).getYaw() || e.getPlayer().getLocation().getYaw()-160 >= vics.get(e.getPlayer()).getYaw()){
				Location l = genLoc(e.getPlayer().getLocation());
				Entity en = e.getPlayer().getWorld().spawnEntity(l, EntityType.CREEPER);		
				vics.remove(e.getPlayer());
				ep.put(en, ps.get(e.getPlayer()));
				
			}
		}
	}
	
	
	public Location genLoc(Location loc){
		float yaw = loc.getYaw();
		if(yaw < 0){
			yaw+=360;
		}
		if(yaw >0 && yaw <= 45){
			loc= new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()-1);
		}else if(yaw >45 && yaw <= 135){
			loc= new Location(loc.getWorld(), loc.getX()+1, loc.getY(), loc.getZ());
		}else if(yaw >135 && yaw <= 225){
			loc= new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()+1);
		}else if(yaw >225 && yaw <= 315){
			loc= new Location(loc.getWorld(), loc.getX()-1, loc.getY(), loc.getZ());
		}else if(yaw >315 && yaw <= 360){
			loc= new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ()-1);

		}
		return loc;	
	}
	
	@EventHandler
	public void onEntityExplosion(EntityExplodeEvent e){
		if(ep.containsKey(e.getEntity())){
				e.setCancelled(true);
				e.getEntity().getWorld().createExplosion(e.getLocation(), 10F);
				ep.get(e.getEntity()).sendMessage(Start.instance.prefix + ChatColor.GREEN + "Der TrollCreeper ist explodiert!");;
				ep.remove(e.getEntity());
		}
	}
	


}
