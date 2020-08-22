package sn2.hearthstone.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class ItemHearthStoneWorld extends ItemHearthStoneBase{

	private RegistryKey<World> worldKey;
	private int maxDist;
	private BlockPos recordPos;
	
	public ItemHearthStoneWorld(int maxCooldown, int stoneType, RegistryKey<World> worldKey, int maxDist) {
		super(maxCooldown, stoneType);
		this.worldKey = worldKey;
		this.maxDist = maxDist;
	}
	
	
	@Override
	public boolean canTeleport(ServerWorld world, PlayerEntity player, BlockPos pos) {
		return this.worldKey == world.getRegistryKey() && (recordPos.isWithinDistance(pos, this.maxDist) || this.maxDist == -1);
	}
}
