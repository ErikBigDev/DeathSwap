package io.github.erikbigdev.deathswap;

import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(p1 != event.getEntity())
					p.sendTitle("§6§l"+p1.getPlayerListName()+" WON!", "§bGG", 10, 90, 20);
				else
					p.sendTitle("§6§l"+p2.getPlayerListName()+" WON!", "§bGG", 10, 90, 20);
			}
			started = false;
			timer.cancel();
			
			loc1.add(10000, 0, 10000);
			loc1.subtract(10000, 0, 10000);
		}
	}
	
	Timer timer = new Timer();
	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			try {
				Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN 10");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN 9");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN 8");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN 7");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN 6");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN 5");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN 4");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN 3");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN 2");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("§4§lSWAPPING IN 1");
				Thread.currentThread().sleep(1000);
				Bukkit.getServer().broadcastMessage("§4§oSWAP!");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Location loc = p1.getLocation();
			
			p1.teleport(p2);
			p2.teleport(loc);
			
			Date date = new Date();
			//date.setTime(date.getTime()+(1000*60*4+1000*30)+(new Random().nextInt(40)+1)*1000);
			date.setTime(date.getTime()+(1000*60*4+50*1000));
			
			
			timer.schedule(task, date);
		}
	};
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("start") && !started) {
			Date date = new Date();
			//ate.setTime(date.getTime()+(1000*60*4+1000*30)+(new Random().nextInt(40)+1)*1000);
			date.setTime(date.getTime()+(1000*60*4+50*1000));
			timer.schedule(task, date);
			
			p1.setHealth(0.0d);
			p2.setHealth(0.0d);
			
			p1.teleport(loc1);
			p2.teleport(loc2);
			
			p1.setInvulnerable(true);
			p2.setInvulnerable(true);
			
			try {
				Thread.currentThread().sleep(6000);
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
