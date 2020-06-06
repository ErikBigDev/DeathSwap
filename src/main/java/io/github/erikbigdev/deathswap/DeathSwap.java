package io.github.erikbigdev.deathswap;

import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
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

public final class DeathSwap extends JavaPlugin implements Listener{
	
	Player p1;
	Player p2;
	
	Location loc1 = new Location(Bukkit.getWorld("world"), 20000, 170, 20000);
	Location loc2 = new Location(Bukkit.getWorld("world"), -20000, 170, -20000);
	
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
				p1.sendTitle("�6�l"+p1.getPlayerListName()+" WON!", "�bGG", 10, 90, 20);
				p2.sendTitle("�6�l"+p1.getPlayerListName()+" WON!", "�bGG", 10, 90, 20);
			}
			else {
				p1.sendTitle("�6�l"+p2.getPlayerListName()+" WON!", "�bGG", 10, 90, 20);
				p2.sendTitle("�6�l"+p2.getPlayerListName()+" WON!", "�bGG", 10, 90, 20);
			}
			started = false;
			timer.cancel();
			
			loc1.add(10000, 0, 10000);
			loc1.subtract(10000, 0, 10000);
		}
	}
	
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			try {
				Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN 10");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN 9");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN 8");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN 7");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN 6");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN 5");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN 4");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN 3");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN 2");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN 1");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("�4�oSWAP!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			loc1 = p1.getLocation();
			loc2 = p2.getLocation();
			PlayerInventory Inv1 = p1.getInventory();
			int food = p1.getFoodLevel();
			float exhaustion = p1.getExhaustion();
			float saturation = p1.getSaturation();
			int exp = p1.getTotalExperience();
			
			Collection<PotionEffect> effects1 = p1.getActivePotionEffects();
			Collection<PotionEffect> effects2 = p2.getActivePotionEffects();
			////////////////////////////////////////////////////////////////////////////////////////
			//TODO: saveData() and loadData()
			////////////////////////////////////////////////////////////////////////////////////////
			p1.teleport(loc2);
			p2.teleport(loc1);
			
			p1.getInventory().setContents(p2.getInventory().getContents());
			p2.getInventory().setContents(Inv1.getContents());
			
			p1.updateInventory();
			p2.updateInventory();
			
			p1.setFoodLevel(p2.getFoodLevel());
			p2.setFoodLevel(food);
			
			p1.setExhaustion(p2.getExhaustion());
			p2.setExhaustion(exhaustion);
			
			p1.setSaturation(p2.getSaturation());
			p2.setSaturation(saturation);
			
			p1.setTotalExperience(p2.getTotalExperience());
			p2.setTotalExperience(exp);
			
			for(PotionEffect effect : effects1) {
				p1.removePotionEffect(effect.getType());
			}
			
			for(PotionEffect effect : effects2) {
				p2.removePotionEffect(effect.getType());
			}
			
			p1.addPotionEffects(effects2);
			p2.addPotionEffects(effects1);
			
			Entity vehicle1 = null;
			Entity vehicle2 = null;
			
			if(p1.isInsideVehicle()) {
				vehicle1 = p1.getVehicle();
				p1.leaveVehicle();
			}

			if(p2.isInsideVehicle()) {
				vehicle2 = p2.getVehicle();
				p2.leaveVehicle();
			}
			
			if(vehicle1 != null)
				vehicle1.addPassenger(p2);
			
			if(vehicle2 != null)
				vehicle2.addPassenger(p1);
			
			
			Date date = new Date();
			//date.setTime(date.getTime()+(1000*60*4+1000*30)+(new Random().nextInt(40)+1)*1000);
			date.setTime(date.getTime()+(1000*60*4+50*1000));
			
			
			timer.schedule(task, date);
		}
	};
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("start") && !started) {
			Date date = new Date();
			//ate.setTime(date.getTime()+(1000*60*4+1000*30)+(new Random().nextInt(40)+1)*1000);
			date.setTime(date.getTime()+(1000*60*4+50*1000));
			timer.schedule(task, date);
			
			Bukkit.getWorld("world").setGameRuleValue("doImmediateRespawn", "true");
			
			p1.setHealth(0.0d);
			p2.setHealth(0.0d);
			
			p1.setInvulnerable(true);
			p2.setInvulnerable(true);
			
			p1.teleport(loc1);
			p2.teleport(loc2);
			
			try {
				Thread.currentThread().sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			p1.setInvulnerable(false);
			p2.setInvulnerable(false);
			
			started = true;
			
			return true;
		}
				
		return false;
	}
	
}
