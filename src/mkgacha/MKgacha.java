package mkgacha;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;


public class MKgacha extends JavaPlugin {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("mkgacha.config")){
            sender.sendMessage(prefix + "§4あなたにガチャを作る権限はありません！");
            return true;
        }
		Player p = (Player)sender;
		if(args.length == 0) {
			  sender.sendMessage("======="+prefix+"=======");
			  sender.sendMessage(" /mkgacha reload ==> MKgachaのconfigをreload");
			  sender.sendMessage(" /mkgacha add ガチャ名  => ガチャにアイテムを追加");
			  sender.sendMessage(" /mkgacha hand ガチャ名  => ガチャの必要アイテムを設定");
			  sender.sendMessage(" /mkgacha check ガチャ名  => ガチャに登録されているアイテム表示&クリックで削除");
			  sender.sendMessage("=======§a§kaaa§6§l====v1.2.0====§a§kaaa§r=======");
			  return true;
			  } else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("reload")) {
				        getServer().getPluginManager().disablePlugin(this);
				        getServer().getPluginManager().enablePlugin(this);
						sender.sendMessage(prefix+"§a再起動完了。");
						return true;
				    }				
			  } else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("check")) {
					if(config1.contains("gacha."+args[1]) == false){
						sender.sendMessage(prefix+"§4そのガチャは存在しません！");
						return true;
					}
					Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "§6§l[§a§lMKgachaSET§6§l]");
					 ItemStack b = new ItemStack(Material.PAPER);
					 ItemMeta c =b.getItemMeta();
					 c.setDisplayName(args[1]);
					 b.setItemMeta(c);
					 inv.setItem(53, b);
					 ItemStack d = new ItemStack(Material.REDSTONE_BLOCK);
					 ItemMeta e = d.getItemMeta();
					 e.setDisplayName("§4§lガチャ削除");
					 d.setItemMeta(e);
					 inv.setItem(52, d);
					 ItemStack f = null;
					 if(config1.getString("gacha."+args[1]+".hand.item_money_1").equals("item")==true) {
					 f = config1.getItemStack("gacha."+args[1]+".hand.item_money");
					 }else {
				     f = new ItemStack(Material.PAPER);
				     int g = config1.getInt("gacha."+args[1]+".hand.item_money");
				     ItemMeta h = f.getItemMeta();
				     h.setDisplayName(g+"");
				     f.setItemMeta(h);
				     }
					 inv.setItem(51, f);
					for(int i = 1; i <= 50; i++){
				     if(config1.contains("gacha."+args[1]+".item."+i)==true) {
					 ItemStack a =config1.getItemStack("gacha."+args[1]+".item."+i);
					 inv.setItem(i-1, a);
				     }else {
				     }
					}
					p.openInventory(inv);
					return true;
				}
				if(args[0].equalsIgnoreCase("add")) {
					if(config1.contains("gacha."+args[1]) == false){
						sender.sendMessage(prefix+"§4そのガチャは存在しません！");
						return true;
					}
					if(p.getInventory().getItemInMainHand().getAmount() == 0) {
						sender.sendMessage(prefix+"§4手にアイテムを持ってください！");
						return true;
					}
					for(int i = 1; i <= 50; i++){
				    if(config1.contains("gacha."+args[1]+".item."+i)==false) {
					config1.set("gacha."+args[1]+".item."+i, p.getInventory().getItemInMainHand());
					sender.sendMessage(prefix+"§aアイテムを追加しました。");
					saveConfig();
					return true;
				    }
					}
					sender.sendMessage(prefix+"§4アイテム数がいっぱいです！");
					return true;
				}
				if(args[0].equalsIgnoreCase("hand")) {
					if(config1.contains("gacha."+args[1]) == false){
						sender.sendMessage(prefix+"§4そのガチャは存在しません！");
						return true;
					}
					if(p.getInventory().getItemInMainHand().getAmount() == 0) {
						sender.sendMessage(prefix+"§4手にアイテムを持ってください！");
						return true;
					}
					config1.set("gacha."+args[1]+".hand.item_money", p.getInventory().getItemInMainHand());
					config1.set("gacha."+args[1]+".hand.item_money_1", "item");
					sender.sendMessage(prefix+"§a必要アイテムを設定しました。");
					saveConfig();
			        getServer().getPluginManager().disablePlugin(this);
			        getServer().getPluginManager().enablePlugin(this);
					return true;
				}
			  } else if(args.length == 3) {
				    if(args[0].equalsIgnoreCase("hand")) {
						if(config1.contains("gacha."+args[1]) == false){
							sender.sendMessage(prefix+"§4そのガチャは存在しません！");
							return true;
						}
						int i = 0;
						try{
                        i = Integer.parseInt(args[2]);
						}catch (NumberFormatException e){
						  sender.sendMessage(prefix+"§4数字で入力してください。");
						  return true;
						}
						config1.set("gacha."+args[1]+".hand.item_money", i);
						config1.set("gacha."+args[1]+".hand.item_money_1", "money");
						sender.sendMessage(prefix+"料金を設定しました。");
						saveConfig();
					}
			  }
		return true;
	}
	private HashMap<Player,String> playerState;
	HashMap<String,ItemStack> gachaItems = new HashMap<>();
	public static FileConfiguration config1;
	String prefix = "§6§l[§a§lMKgacha§6§l]§r";
	@Override
	public void onDisable() {
		saveConfig();
		reloadConfig();
		getLogger().info("===============MKgacha===============");
		getLogger().info("see you!");
		getLogger().info("===============v1.2.0===============");
		super.onDisable();
	}

	@Override
	public void onEnable() {
	    saveDefaultConfig();
	    FileConfiguration config = getConfig();
        config1 = config;
        new signclick(this);
        new Vault(this);
        playerState = new HashMap<>();
        getCommand("mkgacha").setExecutor(this);
		getLogger().info("===============MKgacha===============");
		getLogger().info("最新の更新: https://youtu.be/T__pfDN6zH0");
		getLogger().info("v1.1.1 最新要素: /mkgacha check ガチャ名");
		getLogger().info("困った時は /mkgacha");
		getLogger().info("===============v1.2.0===============");
		super.onEnable();
	}
    public class signclick implements Listener{
    public signclick(MKgacha plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	@EventHandler
    public void onInteract(PlayerInteractEvent e) {
		Player p = (Player)e.getPlayer();
    	Plugin pl = MKgacha.this;
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getClickedBlock().getState() instanceof Sign) {
            	if (((Sign) e.getClickedBlock().getState()).getLine(0).contains("§b===============")) {
            		String id = ((Sign) e.getClickedBlock().getState()).getLine(1);
				   if(config1.contains("gacha." + id) == false) {
				     p.sendMessage(prefix+"§4エラー: その名前のガチャはありません!");
				     return;
				     }
				   if(config1.contains("gacha." + id + ".item.1") == false) {
					     p.sendMessage(prefix+"§4エラー: 設定が終了していません！");
					     return;
					     }
				   if(config1.contains("gacha." + id + ".hand.item_money_1") == false) {
					     p.sendMessage(prefix+"§4エラー: 設定が終了していません！");
					     return;
					     }
	                if (!playerState.isEmpty()) {
	                    if (playerState.get(p) != null && playerState.get(e.getPlayer()).equalsIgnoreCase("rolling")) {
	                        p.sendMessage(prefix+"§4エラー:ガチャは1度に１回しかできません!");
	                        return;
	                    }

	                }
	                if(config1.getString("gacha."+id+".hand.item_money_1").equals("item")==true) {
	                ItemStack i = config1.getItemStack("gacha." + id +".hand.item_money");
	                int am = i.getAmount();
	                ItemStack hand = p.getInventory().getItemInMainHand();
	                if(hand.getAmount() < i.getAmount()){
	                	if(i.getItemMeta().getDisplayName() == null) {
	                	p.sendMessage(prefix+"§4エラー: §e§l"+i.getType().toString()+"§4が§e§l"+i.getAmount()+"§4個必要です！");
	                	}else{
	                	p.sendMessage(prefix+"§4エラー: §e§l"+i.getItemMeta().getDisplayName().toString()+"§4が§e§l"+i.getAmount()+"§4個必要です！");
	                	}
	                	return;
	                }
	                if(hand.getType() != i.getType()){
	                	if(i.getItemMeta().getDisplayName() == null) {
	                	p.sendMessage(prefix+"§4エラー: §e§l"+i.getType().toString()+"§4が§e§l"+i.getAmount()+"§4個必要です！");
	                	}else{
	                	p.sendMessage(prefix+"§4エラー: §e§l"+i.getItemMeta().getDisplayName().toString()+"§4が§e§l"+i.getAmount()+"§4個必要です！");
	                	}
	                	return;
	                }
	                if(i.getItemMeta() == null || hand.getItemMeta().toString().equalsIgnoreCase(i.getItemMeta().toString())){
	                hand.setAmount(hand.getAmount() - am);
	                if(i.getItemMeta() == null) {
	                	p.sendMessage(prefix+"§e§l"+i.getType().toString()+"§aを§e§l"+i.getAmount()+"§a個支払いました。");
	                }else if(i.getItemMeta().getDisplayName() == null) {
                	p.sendMessage(prefix+"§e§l"+i.getType().toString()+"§aを§e§l"+i.getAmount()+"§a個支払いました。");
                	}else{
                	p.sendMessage(prefix+i.getItemMeta().getDisplayName().toString()+"§aを§e§l"+i.getAmount()+"§a個支払いました。");
                	}
	                }else {
	                	if(i.getItemMeta().getDisplayName() == null) {
	                	p.sendMessage(prefix+"§4エラー: §e§l"+i.getType().toString()+"§4が§e§l"+i.getAmount()+"§4個必要です！");
	                	}else{
	                	p.sendMessage(prefix+"§4エラー: §e§l"+i.getItemMeta().getDisplayName().toString()+"§4が§e§l"+i.getAmount()+"§4個必要です！");
	                	}
	                	return;	
	                }
	                }else if(config1.getString("gacha."+id+".hand.item_money_1").equals("money")==true) {
	                	double a = Vault.economy.getBalance(p);
	                	double b = config1.getDouble("gacha."+id+".hand.item_money");
	                	if(a<b) {
	                		p.sendMessage(prefix+"§4エラー: §e§l$"+b+"必要です！");
	                		return;
	                	}
	                	Vault.economy.withdrawPlayer(p, b);
	                	p.sendMessage(prefix+"§a$"+b+"支払いました。");
	                }
	                for (String key : config1.getConfigurationSection("gacha."+id+".item").getKeys(false)) {
	                	gachaItems.put(key,config1.getItemStack("gacha."+id+".item."+ key));
	                }
					Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, "§6§l[§a§lMKgacha§6§l]");
					ItemStack blackGlass = new ItemStack(Material.STAINED_GLASS_PANE,1, (byte) 15);
					ItemStack eGlass = new ItemStack(Material.STAINED_GLASS_PANE,1, (byte) 4);
					playerState.put(p, "rolling");
				  inv.setItem(0, blackGlass);
				  inv.setItem(1, blackGlass);
	              inv.setItem(2, blackGlass);
	              inv.setItem(3, blackGlass);
	              inv.setItem(4, blackGlass);
	              inv.setItem(5, blackGlass);
	              inv.setItem(6, blackGlass);
	              inv.setItem(7, blackGlass);
	              inv.setItem(8, blackGlass);
	              inv.setItem(9, blackGlass);
	              inv.setItem(10, blackGlass);
	              inv.setItem(11, blackGlass);
	              inv.setItem(12, eGlass);
	              inv.setItem(14, eGlass);
	              inv.setItem(15, blackGlass);
	              inv.setItem(16, blackGlass);
	              inv.setItem(17, blackGlass);
	              inv.setItem(18, blackGlass);
	              inv.setItem(19, blackGlass);
	              inv.setItem(20, blackGlass);
	              inv.setItem(21, blackGlass);
	              inv.setItem(22, blackGlass);
	              inv.setItem(23, blackGlass);
	              inv.setItem(24, blackGlass);
	              inv.setItem(25, blackGlass);
	              inv.setItem(26, blackGlass);
	      		  p.openInventory(inv);
	              BukkitScheduler scheduler = getServer().getScheduler();
	              int t = 0;
	              for (int i1 = 1; i1<= 50; i1++){
	            	   t = t + 2;
	                  scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
	                    	@Override
	                    	public void run() {
	                    		p.getWorld().playSound(p.getLocation(),Sound.UI_BUTTON_CLICK,1.0F, 2.0F);
	                    		Random rnd = new Random();
	                    		String[] keys = gachaItems.keySet().toArray(new String[gachaItems.size()]);
	                    		ItemStack value = gachaItems.get(keys[rnd.nextInt(keys.length)]);
	            				inv.setItem(13, value);
	                        }
	                    }, t);
	              }
	              scheduler.scheduleSyncDelayedTask(pl, new Runnable() {
                  	@Override
                  	public void run() {
                  p.getInventory().addItem(inv.getItem(13));
                  p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 2.0F);
              	if(inv.getItem(13).getItemMeta().getDisplayName() == null) {
              	p.sendMessage(prefix+"§e§l"+inv.getItem(13).getType().toString()+"§eが§e§l"+inv.getItem(13).getAmount()+"§e個当たりました！");
              	}else{
              	p.sendMessage(prefix+"§r"+inv.getItem(13).getItemMeta().getDisplayName().toString()+"§eが§e§l"+inv.getItem(13).getAmount()+"§e個当たりました！");
              	}
                  playerState.put(p, "done");
                  gachaItems.clear();
                  	}
	              }, t+1);

	              return;
            	}
            }
        }
	}
    @EventHandler
    public void OnClick(InventoryClickEvent e){
    	if(e.getInventory().getLocation() != null) {
    		  return;
    		}
        if (e.getClickedInventory().getName().contains("§6§l[§a§lMKgacha§6§l]")) {
            if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){
                return;
            }
            e.setCancelled(true);
        }else if (e.getClickedInventory().getName().contains("§6§l[§a§lMKgachaSET§6§l]")) {
            if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){
                return;
            }
            if(e.getSlot()==53||e.getSlot()==51) {
            	e.setCancelled(true);
            	return;
            }
            if(e.getSlot()==52) {
            	config1.set("gacha."+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName(), null);
            	e.getWhoClicked().sendMessage(prefix+"§4"+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+"ガチャを削除しました。");
            	e.getWhoClicked().closeInventory();
            	return;
            }
            int a =e.getSlot();
            e.getClickedInventory().setItem(a, null);
            config1.set("gacha."+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+".item."+a+1, null);
            e.getWhoClicked().sendMessage(prefix+"§4"+a+1+"番を削除しました。");
            saveConfig();
            return;
        }else {
        	return;
        }
    }
    @EventHandler
    public void onSign(SignChangeEvent e){
            if(e.getLine(0).equalsIgnoreCase("[MKgacha]")){
            	if(config1.contains("gacha."+e.getLine(1)) == false){
                    e.getPlayer().sendMessage(prefix+"§a§lガチャが存在しないため作成しました！");
                    config1.set("gacha."+e.getLine(1)+".hand", "a");
                    saveConfig();
                }
                e.setLine(0,"§b===============");
                e.setLine(2,"§b===============");
                e.getPlayer().sendMessage("§a§l看板を登録しました");

            }
    }
    @EventHandler
    public void onSignbreak(BlockBreakEvent e){
            if(e.getBlock().getType()!=Material.SIGN) {
            	return;
            }
            String a = ((Sign) e.getBlock()).getLine(0);
            if(a!="§b===============") {
            	return;
            }
            if(!e.getPlayer().hasPermission("mkgacha.config")){
                e.getPlayer().sendMessage(prefix + "§4あなたにガチャを壊す権限はありません!");
                e.setCancelled(true);
                return;
            }
            e.getPlayer().sendMessage(prefix + "§4ガチャ看板を破壊しました。");
            
    }
    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e){
    }
    }

}
