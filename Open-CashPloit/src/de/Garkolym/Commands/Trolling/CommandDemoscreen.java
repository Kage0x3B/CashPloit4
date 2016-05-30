/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Garkolym
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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.Garkolym.Start;
import de.Garkolym.Commands.Command;
import de.Garkolym.Commands.CommandCategory;
import net.minecraft.server.v1_9_R1.PacketPlayOutGameStateChange;


public class CommandDemoscreen extends Command implements Listener {
	//Von KayCrafterHD
	private ArrayList<Player> demoscreen = new ArrayList<Player>();
	
	public CommandDemoscreen() {
		super("demoscreen", CommandCategory.TROLLING);
		Bukkit.getPluginManager().registerEvents(this, Start.instance);
	}

	
	@Override
	public void onCommand(Player p, ArrayList<String> args) {
		if(args.size() == 1){
			try {
			Player vic = Bukkit.getPlayer(args.get(1));
			
			p.sendMessage(Start.instance.prefix + ChatColor.GREEN + "Dieser Spieler sieht jetzt ein Demoscreen!");
			if(!demoscreen.contains(vic)) {
				demoscreen.add(vic);
				((CraftPlayer)vic).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(5,0));
			} else {
				demoscreen.remove(vic);
				p.sendMessage(Start.instance.prefix + ChatColor.GREEN + "Dieser Spieler sieht jetzt kein Demoscreen mehr!");
				vic.closeInventory();
			}
			
			
			
			}catch(Exception ex) {
				p.sendMessage(Start.instance.prefix + ChatColor.RED + "Dieser Spieler ist nicht online!");
				Bukkit.broadcastMessage(ex.getMessage());
			}
		
		}
			if(args.size() != 1){
				p.sendMessage(Start.instance.prefix + ChatColor.RED + "Nutze +demoscreen <Player>!");
			}
			
		}
		@EventHandler
		public void onCloseInventory(final InventoryCloseEvent e){
			for(Player vic : this.demoscreen){
				boolean ds = true;
				while(ds){
				((CraftPlayer)vic).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(5,0));
				ds = false;
				}
		}
		}
		@EventHandler
		public void onMove(final PlayerMoveEvent e){
			for(Player vic : this.demoscreen){
				boolean ds = true;
				while(ds){
				((CraftPlayer)vic).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(5,0));
				ds = false;
				}
		}
			
		}
		@EventHandler
		public void onTeleport(final PlayerTeleportEvent e){
			for(Player vic : this.demoscreen){
				boolean ds = true;
				while(ds){
				((CraftPlayer)vic).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(5,0));
				ds = false;
				}
		}
		}
		@EventHandler
		public void onClickInv(final InventoryClickEvent e){
			for(Player vic : this.demoscreen){
				boolean ds = true;
				while(ds){
				e.setCancelled(true);
				vic.closeInventory();
				((CraftPlayer)vic).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(5,0));
				ds = false;
				}
		}
		}
		
	}

		

