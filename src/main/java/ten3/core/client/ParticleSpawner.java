package ten3.core.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.world.level.Level;

@SuppressWarnings("all")
public class ParticleSpawner {
    public static DustParticleOptions RANGE = DustParticleOptions.REDSTONE;

    public static void spawnClt(DustParticleOptions data, double x, double y, double z, double spread) {

        Level world = Minecraft.getInstance().level;
        if(world != null)
        world.addParticle(data, x, y, z, spread, spread, spread);

    }

}
