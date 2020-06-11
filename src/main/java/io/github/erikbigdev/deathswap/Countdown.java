package io.github.erikbigdev.deathswap;

import java.util.Collection;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
	public Countdown(DeathSwap ds) {
		this.ds = ds;
	}
	
	DeathSwap ds;
	
	public int i = 10;

	@Override
	public void run() {
		if(i > 0) {
			Bukkit.getServer().broadcastMessage("�4�lSWAPPING IN " + i);
			i--;
			return;
		}
		else if(i <= 0){
			Bukkit.getServer().broadcastMessage("�4�oSWAP!");
			Location loc1 = ds.p1.getLocation();
			Location loc2 = ds.p2.getLocation();
			
			PlayerInventory Inv1 = ds.p1.getInventory();
			int food = ds.p1.getFoodLevel();
			float exhaustion = ds.p1.getExhaustion();
			float saturation = ds.p1.getSaturation();
			int exp = ds.p1.getTotalExperience();
			
			Collection<PotionEffect> effects1 = ds.p1.getActivePotionEffects();
			Collection<PotionEffect> effects2 = ds.p2.getActivePotionEffects();
			
			ds.p1.getInventory().setContents(ds.p2.getInventory().getContents());
			ds.p2.getInventory().setContents(Inv1.getContents());
			
			ds.p1.updateInventory();
			ds.p2.updateInventory();
			
			ds.p1.setFoodLevel(ds.p2.getFoodLevel());
			ds.p2.setFoodLevel(food);
			
			ds.p1.setExhaustion(ds.p2.getExhaustion());
			ds.p2.setExhaustion(exhaustion);
			
			ds.p1.setSaturation(ds.p2.getSaturation());
			ds.p2.setSaturation(saturation);
			
			ds.p1.setTotalExperience(ds.p2.getTotalExperience());
			ds.p2.setTotalExperience(exp);
			
			for(PotionEffect effect : effects1) {
				ds.p1.removePotionEffect(effect.getType());
			}
			
			for(PotionEffect effect : effects2) {
				ds.p2.removePotionEffect(effect.getType());
			}
			
			ds.p1.addPotionEffects(effects2);
			ds.p2.addPotionEffects(effects1);
			
			Entity vehicle1 = null;
			Entity vehicle2 = null;
			
			if(ds.p1.isInsideVehicle()) {
				vehicle1 = ds.p1.getVehicle();
				ds.p1.leaveVehicle();
			}

			if(ds.p2.isInsideVehicle()) {
				vehicle2 = ds.p2.getVehicle();
				ds.p2.leaveVehicle();
			}
			
			if(vehicle1 != null)
				vehicle1.addPassenger(ds.p2);
			
			if(vehicle2 != null)
				vehicle2.addPassenger(ds.p1);
			
			ds.p1.teleport(loc2);
			ds.p2.teleport(loc1);

			ds.task.cancel();
			
			ds.task = new BukkitRunnable() {
				@Override
				public void run() {
					ds.countdown = new Countdown(ds);
					ds.countdown.runTaskTimer(JavaPlugin.getPlugin(DeathSwap.class), 0, 20);
				}
			};
			
			ds.task.runTaskLater(ds, 20*60*4+50*40);
			this.cancel();
		}
	}
}
