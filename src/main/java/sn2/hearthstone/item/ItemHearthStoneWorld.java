package sn2.hearthstone.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class ItemHearthStoneWorld extends ItemHearthStoneBase{

	private RegistryKey<World> worldKey;
	
	public ItemHearthStoneWorld(int maxCooldown, int stoneType, int maxDist, RegistryKey<World> worldKey) {
		super(maxCooldown, stoneType, maxDist);
		this.worldKey = worldKey;
	}
	
	@Override
	public boolean canTeleport(ServerWorld world, PlayerEntity player, BlockPos pos) {
		return this.worldKey == world.getRegistryKey();
	}
}
