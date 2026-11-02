package ru.orbital.portalblock;

import org.bukkit.configuration.file.FileConfiguration;

public record PluginConfig(
        boolean enabled,
        String message,
        long messageCooldownMillis,
        boolean blockMovement,
        boolean pushOut
) {

    public static PluginConfig load(FileConfiguration config) {
        long cooldownSeconds = Math.max(0L, config.getLong("message-cooldown-seconds", 5L));
        return new PluginConfig(
                config.getBoolean("enabled", true),
                config.getString("message", ""),
                cooldownSeconds * 1000L,
                config.getBoolean("block-movement", true),
                config.getBoolean("push-out", true)
        );
    }
}
