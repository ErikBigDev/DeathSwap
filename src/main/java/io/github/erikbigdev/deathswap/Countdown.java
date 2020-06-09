package io.github.erikbigdev.deathswap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Countdown extends BukkitRunnable {
	public Countdown(DeathSwap ds) {
		this.ds = ds;
	}
	
	DeathSwap ds;
	
	public int i = 10;
	
	Location loc1;
	Location loc2;
	
	@Override
	public void run() {
		if(i > 0) {
			Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN " + i);
			i--;
			return;
		}
		else if(i <= 0){
			Bukkit.getServer().broadcastMessage("§4§oSWAP!");
			loc1 = ds.p1.getLocation();
			loc2 = ds.p2.getLocation();

			ds.p1.teleport(ds.loc2);
			ds.p2.teleport(ds.loc1);

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
