package sn2.heartstone.data;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import sn2.heartstone.HeartStone;

public class PositionData {
	public String uuid;
	public RegistryKey<World> worldKey;
	public long poslong;
	public float yaw;
	public float pitch;
	
	public PositionData(String uuid, RegistryKey<World> worldKey, BlockPos pos, float yaw, float pitch) {
		this.uuid = uuid;
		this.worldKey = worldKey;
		this.poslong = pos.asLong();
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public void updateToHere(ServerPlayerEntity player) {
		this.worldKey = player.world.getRegistryKey();
		this.poslong = player.getBlockPos().asLong();
		this.yaw = player.yaw;
		this.pitch = player.pitch;
	}
	
	public void teleport(ServerPlayerEntity player) {
		player.teleport(getWorld(player), getPos().getX(), getPos().getY(), getPos().getZ(), yaw, pitch);
	}
	
	public String getWorldKey() {
		return worldKey.getValue().toString();
	}
	
	public ServerWorld getWorld(ServerPlayerEntity player) {	return player.getServer().getWorld(worldKey); }
	
	public BlockPos getPos() { return BlockPos.fromLong(poslong); }
	
	
}
