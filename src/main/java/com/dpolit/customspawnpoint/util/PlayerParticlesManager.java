package com.dpolit.customspawnpoint.util;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class PlayerParticlesManager {

    public static void spawnParticles(Player player, Particle particle) {
        player.spawnParticle(particle, player.getEyeLocation(), 25);
    }
}
