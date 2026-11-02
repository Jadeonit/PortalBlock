package ru.orbital.portalblock;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public final class PortalListener implements Listener {

    private final PortalBlockPlugin plugin;

    public PortalListener(PortalBlockPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Stops the player at the portal edge, before the client shows the
     * purple portal overlay.
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (!plugin.config().enabled() || !plugin.config().blockMovement()) {
            return;
        }
        if (plugin.canBypass(player)) {
            return;
        }

        Location to = event.getTo();
        Location from = event.getFrom();
        if (to == null || !PortalUtil.intersectsPortal(to) || PortalUtil.intersectsPortal(from)) {
            return;
        }

        event.setTo(event.getFrom());
        plugin.notifyBlocked(player);
    }

    /**
     * Fallback: cancels the actual nether teleport (for plugin teleports,
     * players already inside a portal on login, etc.).
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPortal(PlayerPortalEvent event) {
        if (!plugin.config().enabled() || event.getCause() != TeleportCause.NETHER_PORTAL) {
            return;
        }
        if (plugin.canBypass(event.getPlayer())) {
            return;
        }

        event.setCancelled(true);
        plugin.notifyBlocked(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.forget(event.getPlayer());
    }
}
