package io.github.erikbigdev.deathswap;

import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public final class DeathSwap extends JavaPlugin implements Listener{
	
	public void onEnable() {
		instance = this;
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		
		loc1 = new Location(Bukkit.getWorld("world"), 20000, 170, 20000);
		loc2 = new Location(Bukkit.getWorld("world"), -20000, 170, -20000);
	}
	
	static DeathSwap instance;
	
	Player p1;
	Player p2;
	
	Location loc1;
	Location loc2;
	
	boolean started = false;

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(p1 == null)
			p1 = event.getPlayer();
		else
			p2 = event.getPlayer();
	}

	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if(started) {
			if(p1 != event.getEntity()) {
				p1.sendTitle("§6§l"+p1.getPlayerListName()+" WON!", "§bGG", 10, 90, 20);
				p2.sendTitle("§6§l"+p1.getPlayerListName()+" WON!", "§bGG", 10, 90, 20);
			}
			else {
				p1.sendTitle("§6§l"+p2.getPlayerListName()+" WON!", "§bGG", 10, 90, 20);
				p2.sendTitle("§6§l"+p2.getPlayerListName()+" WON!", "§bGG", 10, 90, 20);
			}
			started = false;
			
			countdown.cancel();
			task.cancel();
			
			loc1.add(10000, 0, 10000);
			loc2.subtract(10000, 0, 10000);
		}
	}
	
	BukkitRunnable countdown;
	
	BukkitRunnable task;
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("start") && !started) {
			if(task != null)
				task.cancel();
			task = new BukkitRunnable() {
				@Override
				public void run() {
					countdown = new Countdown(instance);
					countdown.runTaskTimer(JavaPlugin.getPlugin(DeathSwap.class), 0, 20);
				}
			};
			task.runTaskLater(this, 20*60*4+50*40);
			
			Bukkit.getWorld("world").setGameRuleValue("doImmediateRespawn", "true");
			
			p1.setStatistic(Statistic.TIME_SINCE_REST, 0);
			p1.setStatistic(Statistic.TIME_SINCE_REST, 0);
			
			p1.setHealth(20.0d);
			p2.setHealth(20.0d);
			
			p1.setSaturation(5.0f);
			p2.setSaturation(5.0f);
			
			p1.setExhaustion(0.0f);
			p2.setExhaustion(0.0f);
			
			p1.setInvulnerable(true);
			p2.setInvulnerable(true);
			
			p1.teleport(loc1);
			p2.teleport(loc2);
			
			p1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 255), true);
			p1.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*5, 255), true);
			
			p2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 255), true);
			p2.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20*5, 255), true);
			
			new BukkitRunnable() {
				@Override
				public void run() {
					p1.setInvulnerable(false);
					p2.setInvulnerable(false);
					Bukkit.getServer().broadcastMessage("§2§oSTART!");
				}
			}.runTaskLater(this, 20*5);
			
			started = true;
			
			return true;
		}
				
		return false;
	}
	
}