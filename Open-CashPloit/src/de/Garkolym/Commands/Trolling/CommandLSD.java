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
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.Garkolym.Start;
import de.Garkolym.Commands.Command;
import de.Garkolym.Commands.CommandCategory;
import de.Garkolym.Events.ITick;
import net.minecraft.server.v1_9_R1.PacketPlayOutGameStateChange;

/**
 * Try it on yourself ^^
 * 
 * @author Kage0x3B
 */
public class CommandLSD extends Command implements ITick, Listener {
	private Random random;

	private List<Player> victims = new ArrayList<Player>();

	public CommandLSD() {
		super("lsd", CommandCategory.TROLLING);

		random = new Random();

		Start.instance.eventManager.tickListener.add(this);
		Bukkit.getPluginManager().registerEvents(this, Start.instance);
	}

	@Override
	public void onCommand(Player p, ArrayList<String> args) {
		if(args.size() >= 1) {
			Player victim = Bukkit.getPlayer(args.get(0));

			if(victim != null) {
				if(!victims.contains(victim)) {
					victims.add(victim);
					victim.setPlayerTime(6000, false);

					p.sendMessage(Start.instance.prefix + ChatColor.GREEN + "Für " + victim.getDisplayName() + " wurde der LSD-Modus aktiviert!");
				} else {
					victims.remove(victim);

					victim.removePotionEffect(PotionEffectType.CONFUSION);
					victim.removePotionEffect(PotionEffectType.SPEED);
					victim.removePotionEffect(PotionEffectType.BLINDNESS);
					victim.resetPlayerTime();

					p.sendMessage(Start.instance.prefix + ChatColor.RED + "Für " + victim.getDisplayName() + " wurde der LSD-Modus deaktiviert!");
				}
			} else {
				p.sendMessage(Start.instance.prefix + ChatColor.RED + "Der Spieler \"" + args.get(0) + "\" konnte nicht gefunden werden!");
			}
		} else {
			p.sendMessage(Start.instance.prefix + ChatColor.RED + "Gib als erstes Argument bitte einen Spielernamen ein!");
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent event) {
		event.getPlayer().removePotionEffect(PotionEffectType.CONFUSION);
		event.getPlayer().removePotionEffect(PotionEffectType.SPEED);
		event.getPlayer().removePotionEffect(PotionEffectType.BLINDNESS);

		victims.remove(event.getPlayer());
	}

	int ticks = 0;

	@Override
	public void onTick() {
		ticks++;

		for(Player player : new ArrayList<Player>(victims)) {
			if(!player.isOnline()) {
				continue;
			}

			if(!player.hasPotionEffect(PotionEffectType.CONFUSION)) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));
			}

			if(!player.hasPotionEffect(PotionEffectType.SPEED) && ticks % 20 == 0 && random.nextInt(1) == 0) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3, random.nextInt(8), false, false));
			}

			if(!player.hasPotionEffect(PotionEffectType.BLINDNESS) && ticks % 60 == 0 && random.nextInt(2) == 0) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, random.nextInt(60) + 20, random.nextInt(10) + 1, false, false));
			}

			Bukkit.getScheduler().runTask(Start.instance, new Runnable() {
				@Override
				public void run() {
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutGameStateChange(7, random.nextFloat()));
				}
			});

		}
	}
}