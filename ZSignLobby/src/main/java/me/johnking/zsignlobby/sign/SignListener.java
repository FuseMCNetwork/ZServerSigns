package me.johnking.zsignlobby.sign;

import me.johnking.zsignlobby.Main;
import me.michidk.DKLib.utils.BungeeCord;
import net.fusemc.zcore.rankSystem.Rank;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by Marco on 04.10.2014.
 */
public class SignListener implements Listener {

    public SignUpdater scheduler;

    public SignListener (SignUpdater scheduler) {
        this.scheduler = scheduler;
    }

    @EventHandler
    public void onSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        if(!player.isOp()) {
            return;
        }
        if(!event.getLine(0).equalsIgnoreCase("[SL]")) {
            return;
        }
        if(event.getLine(1).equals("")) {
            player.sendMessage(ChatColor.RED + "Second line must be the gamemodename!");
            return;
        }
        if(event.getLine(2).equals("")) {
            player.sendMessage(ChatColor.RED + "Third line must be the type (normal, vip)");
            return;
        }
        ServerSignType type = scheduler.getTypeFromName(event.getLine(1));
        if(type == null) {
            player.sendMessage(ChatColor.RED + "Invalid gamemodename!");
            return;
        }
        boolean vip = false;
        if(event.getLine(2).equalsIgnoreCase("vip")) {
            vip = true;
        } else if (!event.getLine(2).equalsIgnoreCase("normal")) {
            player.sendMessage(ChatColor.RED + "Invalid signtype!");
            return;
        }
        scheduler.addServerSign(event.getBlock().getLocation(), type, vip);
        player.sendMessage(ChatColor.GREEN + "Sign successfully registered!");
    }

    @EventHandler
    public void onSignClicked(PlayerInteractEvent e) {
        if(e.getAction() == Action.PHYSICAL) {
            return;
        }
        if(e.getClickedBlock() == null) {
            return;
        }
        if(!(e.getClickedBlock().getState() instanceof Sign)) {
            return;
        }
        Sign sign = (Sign) e.getClickedBlock().getState();
        ServerSign serverSign = scheduler.getServerSign(sign.getLocation());
        if(serverSign == null) {
            return;
        }
        if(e.getPlayer().isOp() && e.getPlayer().getGameMode() == GameMode.CREATIVE && e.getPlayer().getItemInHand().getType() == Material.STICK) {
            scheduler.removeServerSign(serverSign);
            e.getPlayer().sendMessage(ChatColor.GREEN + "Sign successfully unregistered!");
            return;
        }
        e.setCancelled(true);
        if(!serverSign.isDisplayed()) {
            return;
        }
        if(serverSign.isVip()) {
            if(!Rank.PREMIUM.isRank(e.getPlayer())){
                e.getPlayer().sendMessage("\u00A76Du bist kein Premium! Kaufe \u00A7lPremium\u00A76 auf \u00A7bshop.fusemc.net");
                return;
            }
        }
        BungeeCord.connect(Main.getInstance(), serverSign.getServerID(), e.getPlayer());
    }
}
