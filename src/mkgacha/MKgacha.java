package mkgacha;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
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
			  sender.sendMessage(" /mkgacha set ガチャ名  => セッティングはこれ一つで！");
			  sender.sendMessage(" /mkgacha list  => ガチャリスト");
			  sender.sendMessage(" /mkgacha を /mg にしても機能します！");
			  sender.sendMessage("=======§a§kaaa§6§l====v1.3.2====§a§kaaa§r=======");
			  return true;
			  } else if(args.length == 1) {
					if(args[0].equalsIgnoreCase("reload")) {
				        getServer().getPluginManager().disablePlugin(this);
				        getServer().getPluginManager().enablePlugin(this);
						sender.sendMessage(prefix+"§a再起動完了。");
						return true;
				    }
					if(args[0].equalsIgnoreCase("list")) {
						sender.sendMessage(prefix+"[ガチャリスト]");
						for (String key : config1.getConfigurationSection("gacha").getKeys(false)) {
							sender.sendMessage(prefix+key);
						}
					}
			  } else if(args.length == 2) {
				if(args[0].equalsIgnoreCase("set")) {
					if(config1.contains("gacha."+args[1]) == false){
						sender.sendMessage(prefix+"§4そのガチャは存在しません！");
						return true;
					}
					Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "§6§l[§a§lMKgachaSET§6§l]");
					 ItemStack b = new ItemStack(Material.PAPER);
					 ItemMeta c =b.getItemMeta();
					 c.setDisplayName(args[1]);
					 List<String> k = new ArrayList<String>();
					 k.add("§5クリックするとお金モードと");
					 k.add("§5アイテムモードを切り替えます。");
					 k.add("§5現在: "+config1.getString("gacha."+args[1]+".hand.item_money_1"));
					 k.add("§4注意! 切り替えるとアイテムも");
					 k.add("§4設定金額も初期化されます！気を付けてください!");
					 c.setLore(k);
					 b.setItemMeta(c);
					 inv.setItem(53, b);
					 ItemStack d = new ItemStack(Material.REDSTONE_BLOCK);
					 ItemMeta e = d.getItemMeta();
					 e.setDisplayName("§4§lガチャ削除");
					 d.setItemMeta(e);
					 inv.setItem(52, d);
					 ItemStack f = null;
					 if(config1.contains("gacha."+args[1]+".hand.item_money_1")==false) {
						 config1.set("gacha."+args[1]+".hand.item_money_1","item");
						 saveConfig();
					 }
					 if(config1.getString("gacha."+args[1]+".hand.item_money_1").equals("item")==true) {
					 f = config1.getItemStack("gacha."+args[1]+".hand.item_money");
					 }else {
				     f = new ItemStack(Material.BARRIER);
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
				if(args[0].equalsIgnoreCase("config")) {
					if(config1.contains("gacha."+args[1]) == false){
						sender.sendMessage(prefix+"§4そのガチャは存在しません！");
						return true;
					}
					Inventory inv = Bukkit.createInventory((InventoryHolder)null, 54, "§6§l[§a§lMKgachaCONFIG§6§l]");
					 ItemStack b = new ItemStack(Material.PAPER);
					 ItemMeta c =b.getItemMeta();
					 c.setDisplayName(args[1]);
					 b.setItemMeta(c);
					 inv.setItem(53, b);
					if(config1.contains("gacha."+args[1]+".move")==false) {
						config1.set("gacha."+args[1]+".move","true");
						saveConfig();
					}
					if(config1.getString("gacha."+args[1]+".move").equals("true")) {
					 ItemStack a = new ItemStack(Material.EMERALD_BLOCK);
					 ItemMeta o =a.getItemMeta();
					 o.setDisplayName("§a§l稼働中");
					 a.setItemMeta(o);
					 inv.setItem(11, a);
					}else {
						 ItemStack a = new ItemStack(Material.REDSTONE_BLOCK);
						 ItemMeta o =a.getItemMeta();
						 o.setDisplayName("§4§l停止中");
						 a.setItemMeta(o);
						 inv.setItem(11, a);
					}
					if(config1.contains("gacha." + args[1] +".winitem")==true) {
						ItemStack item2 = config1.getItemStack("gacha." + args[1] +".winitem");
						inv.setItem(13, item2);
					}
					p.openInventory(inv);
				}
			  }
		return true;
	}
	private HashMap<Player,String> playerState;
	private HashMap<Player,String> onchat;
	private HashMap<String,String> onchat2;
	HashMap<String,ItemStack> gachaItems = new HashMap<>();
	public static FileConfiguration config1;
	String prefix = "§6§l[§a§lMKgacha§6§l]§r";
	@Override
	public void onDisable() {
		saveConfig();
		reloadConfig();
		getLogger().info("===============MKgacha===============");
		getLogger().info("see you!");
		getLogger().info("===============v1.3.2===============");
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
        onchat = new HashMap<>();
        onchat2 = new HashMap<>();
        getCommand("mkgacha").setExecutor(this);
        getCommand("mg").setExecutor(this);
		getLogger().info("===============MKgacha===============");
		getLogger().info("最新の更新: https://youtu.be/T__pfDN6zH0");
		getLogger().info("v1.3.0 最新要素: /mkgacha set ガチャ名");
		getLogger().info("困った時は /mkgacha");
		getLogger().info("===============v1.3.2===============");
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
				   if(config1.contains("gacha." + id + ".move")==true) {
				   if(config1.getString("gacha." + id + ".move") == "false") {
					     p.sendMessage(prefix+"§4このガチャは現在稼働が停止されています！");
					     return;
				    }
				   }else {
					     p.sendMessage(prefix+"§4エラー: 設定を行ってください。 /mkgacha config "+id);
					     return;
				   }
	                if(!e.getPlayer().hasPermission("mkgacha.use")){
	                    e.getPlayer().sendMessage(prefix + "§4あなたにガチャを引く権限はありません！");
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
	                	p.sendMessage(prefix+"§e§l$"+b+"§a支払いました。");
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
                  if(config1.contains("gacha." + id +".winitem")==true) {
                  ItemStack item = inv.getItem(13);
                  ItemStack item2 = config1.getItemStack("gacha." + id +".winitem");
                  if(item.getAmount() == item2.getAmount()){
                	  if(item.getType() == item2.getType()){
                		  if(item2.getItemMeta() == null || item.getItemMeta().toString().equalsIgnoreCase(item2.getItemMeta().toString())){
                         	        if(inv.getItem(13).getItemMeta().getDisplayName() == null) {
                         		     Bukkit.broadcastMessage(prefix+"§e§l"+p.getName().toString()+"さんが"+inv.getItem(13).getType().toString()+"§eを§e§l"+inv.getItem(13).getAmount()+"§e個当てました！おめでとう！");
                             	    }else{
                             		 Bukkit.broadcastMessage(prefix+"§6§l"+p.getName().toString()+"さんが"+inv.getItem(13).getItemMeta().getDisplayName().toString()+"§eを§e§l"+inv.getItem(13).getAmount()+"§e個当てました！おめでとう！");
                             	    }
                                	for (Player player : Bukkit.getOnlinePlayers()) {
                     	            	player.getWorld().playSound(player.getLocation(),Sound.ENTITY_ENDERDRAGON_DEATH, 1.0F, 2.0F);
                                	}
            	                    World world = e.getClickedBlock().getWorld();
            	                    int x3 = e.getClickedBlock().getX();
            	                    int y3 = e.getClickedBlock().getY();
            	                    int z3 = e.getClickedBlock().getZ();
            	                    Location location = new Location(world, x3, y3, z3);
            	   	                Firework fireWork = (Firework) p.getWorld().spawnEntity(location, EntityType.FIREWORK);
            	   	                FireworkMeta fwMeta = fireWork.getFireworkMeta();
            	   	                fwMeta.addEffect(FireworkEffect.builder().flicker(false).trail(true).with(Type.BALL).with(Type.BALL_LARGE).with(Type.STAR).withColor(Color.ORANGE).withColor(Color.YELLOW).withFade(Color.PURPLE).withFade(Color.RED).build());
            	   	                fireWork.setFireworkMeta(fwMeta);
                                    playerState.put(p, "done");
                                    gachaItems.clear();
                                    return;
                		  }
                	  }
                  }
                  }
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
        if (e.getClickedInventory().getName().equals("§6§l[§a§lMKgacha§6§l]")==true) {
            if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){
                return;
            }
            e.setCancelled(true);
            return;
        }else if (e.getClickedInventory().getName().equals("§6§l[§a§lMKgachaSET§6§l]")==true) {
            if(e.getCurrentItem() == null){
                return;
            }
            if(e.getCurrentItem().getType()==Material.BARRIER && e.getSlot()==51) {
            	Player p = (Player)e.getWhoClicked();
            	onchat.put(p, "taiki");
            	onchat2.put("MK", e.getClickedInventory().getItem(53).getItemMeta().getDisplayName());
            	p.closeInventory();
            	e.getWhoClicked().sendMessage(prefix+"§a設定したい数字をチャットに打ち込んでください！");
            	e.setCancelled(true);
            	return;
            }
            if(e.getSlot()==53) {
            	Player p = (Player)e.getWhoClicked();
            	p.closeInventory();
            	e.getWhoClicked().sendMessage(prefix+"§4料金モードとアイテムモードを切り替えました。");
            	e.getWhoClicked().sendMessage(prefix+"§4再度インベントリを開くことで反映されます。");
				if(config1.getString("gacha."+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+".hand.item_money_1").equals("item")==true) {
            	config1.set("gacha."+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+".hand.item_money_1","money");
				}else {
				config1.set("gacha."+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+".hand.item_money_1","item");
				}
				config1.set("gacha."+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+".hand.item_money",null);
				saveConfig();
            	e.setCancelled(true);
            	return;
            }
            if(e.getSlot()==52) {
            	config1.set("gacha."+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName(), null);
            	saveConfig();
            	e.getWhoClicked().sendMessage(prefix+"§4"+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+"ガチャを削除しました。");
            	e.getInventory().setItem(53,null);
            	e.getWhoClicked().closeInventory();
            	return;
            }
            return;
        }else if(e.getClickedInventory().getName().equals("§6§l[§a§lMKgachaCONFIG§6§l]")==true){
        	Player p = (Player)e.getWhoClicked();
            if(e.getCurrentItem() == null){
                return;
            }
            if(e.getCurrentItem().getType()==Material.EMERALD_BLOCK && e.getSlot()==11) {
            	e.setCancelled(true);
				 ItemStack a = new ItemStack(Material.REDSTONE_BLOCK);
				 ItemMeta b =a.getItemMeta();
				 b.setDisplayName("§4§l停止中");
				 a.setItemMeta(b);
				 e.getInventory().setItem(11, a);
				 config1.set("gacha."+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+".move", "false");
				 saveConfig();
				 p.sendMessage(prefix+"§4"+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+"ガチャを稼働停止しました。");
				 return;
            }else if(e.getCurrentItem().getType()==Material.REDSTONE_BLOCK && e.getSlot()==11) {
            	e.setCancelled(true);
				 ItemStack a = new ItemStack(Material.EMERALD_BLOCK);
				 ItemMeta o =a.getItemMeta();
				 o.setDisplayName("§a§l稼働中");
				 a.setItemMeta(o);
				 e.getInventory().setItem(11, a);
				 config1.set("gacha."+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+".move", "true");
				 saveConfig();
				 p.sendMessage(prefix+"§a"+e.getClickedInventory().getItem(53).getItemMeta().getDisplayName()+"ガチャを稼働しました。");
				 return;
            }
        	return;
        }else {
        	return;
        }
    }
    @EventHandler
    public void onSign(SignChangeEvent e){
            if(e.getLine(0).equalsIgnoreCase("[MKgacha]")){
                if(!e.getPlayer().hasPermission("mkgacha.config")){
                    e.getPlayer().sendMessage(prefix + "§4あなたにガチャを作成する権限はありません!");
                    e.getBlock().breakNaturally();
                    return;
                }
            	if(config1.contains("gacha."+e.getLine(1)) == false){
                    e.getPlayer().sendMessage(prefix+"§a§lガチャが存在しないため作成しました！");
                    config1.set("gacha."+e.getLine(1)+".hand", "a");
                    saveConfig();
                }
                e.setLine(0,"§b===============");
                if(e.getLine(2).isEmpty()==false) {
                e.setLine(2, e.getLine(2).replace("&", "§"));
                e.setLine(3,"§b===============");
                }else {
                	 e.setLine(2,"§b===============");
                }
                e.getPlayer().sendMessage(prefix+"§a§l看板を登録しました。");

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
    	if(e.getInventory().getName().equals("§6§l[§a§lMKgachaSET§6§l]")==true) {
        if(e.getInventory().getItem(53)==null) {
        	return;
        }
    	String id = e.getInventory().getItem(53).getItemMeta().getDisplayName().toString();
    	if(e.getInventory().getItem(51)!=null) {
         if(e.getInventory().getItem(51).getType()==Material.BARRIER) {
         }else {
    	  config1.set("gacha." + id +".hand.item_money", e.getInventory().getItem(51));
    	  config1.set("gacha."+id+".hand.item_money_1","item");
         }
    	}else {
    		e.getPlayer().sendMessage(prefix+"§4handの設定が消えています！もう一度設定することをお勧めします！");
    		config1.set("gacha." + id +".hand.item_money", null);
    	}
    	config1.set("gacha."+id+".item", null);
    	saveConfig();
    	for(int i = 1; i < 50; i++) {
    		int f = i - 1;
    		if(e.getInventory().getItem(f)!=null) {
    			for(int d = 1; d < 50; d++) {
    			 if(config1.contains("gacha."+id+".item."+d)==false) {
    			 config1.set("gacha."+id+".item."+d, e.getInventory().getItem(f));
                 break;
    			 }
    			}
    		}else {
    			config1.set("gacha."+id+".item."+i, null);
    		}
    	}
    	saveConfig();
    	e.getPlayer().sendMessage(prefix + "§a設定を保存しました。");
    	return;
    	}else if(e.getInventory().getName().equals("§6§l[§a§lMKgachaCONFIG§6§l]")==true) {
    		String id = e.getInventory().getItem(53).getItemMeta().getDisplayName().toString();
    		if(e.getInventory().getItem(13)!=null) {
    			config1.set("gacha." + id +".winitem", e.getInventory().getItem(13));
        	}else {
        		config1.set("gacha." + id +".winitem", null);
        	}
        	saveConfig();
        	e.getPlayer().sendMessage(prefix + "§a設定を保存しました。");
    		return;
    	}else {
    		return;
    	}
    }
	@EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
		Player p = (Player)e.getPlayer();
		String m = e.getMessage();
        if (!onchat.isEmpty()) {
            if (onchat.get(p) != null && onchat.get(e.getPlayer()).equalsIgnoreCase("taiki")) {
				int i = 0;
				try{
                i = Integer.parseInt(m);
				}catch (NumberFormatException f){
				  p.sendMessage(prefix+"§4数字で入力してください。");
				  onchat.put(p, "done");
				  onchat2.clear();
				  return;
				}
				String a =onchat2.get("MK");
				config1.set("gacha."+a+".hand.item_money", i);
				config1.set("gacha."+a+".hand.item_money_1", "money");
				p.sendMessage(prefix+"§a料金を設定しました。");
				  onchat.put(p, "done");
				  onchat2.clear();
				  e.setCancelled(true);
				saveConfig();
            }

        }
	}
    }

}
