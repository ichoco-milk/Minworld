package site.ichocomilk.minworld.internal;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.WorldData;

final class MinWorldData extends WorldData {

    public MinWorldData(String worldName) {
        super();
        this.a(worldName);
    }

    @Override
    public NBTTagCompound a(final NBTTagCompound nbttagcompound) {
        return new NBTTagCompound();
    }
}
