package sn2.hearthstone.storage.data;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class PositionData {
	private RegistryKey<World> worldKey;
	private long poslong;
	private float yaw;
	private float pitch;
	
	public PositionData() {
	}
	
	public PositionData(RegistryKey<World> worldKey, BlockPos pos, float yaw, float pitch) {
		this.worldKey = worldKey;
		this.poslong = pos.asLong();
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public void teleport(ServerPlayerEntity player) {
		player.teleport(getWorld(player), getPos().getX(), getPos().getY(), getPos().getZ(), getYaw(), getPitch());
	}
	
	public String getWorldKey() { return worldKey.getValue().toString(); }
	
	public ServerWorld getWorld(ServerPlayerEntity player) {	return player.getServer().getWorld(worldKey); }
	
	public BlockPos getPos() { return BlockPos.fromLong(getPoslong()); }

	public long getPoslong() { return poslong; }

	public float getYaw() { return yaw; }

	public float getPitch() { return pitch;	}	
}
