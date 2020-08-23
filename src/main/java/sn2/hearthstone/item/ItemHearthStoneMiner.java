package sn2.hearthstone.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import sn2.hearthstone.HearthStone;
import sn2.hearthstone.storage.data.PositionData;

public class ItemHearthStoneMiner extends ItemHearthStoneBase{

	public ItemHearthStoneMiner(int maxCooldown, int stoneType) {
		super(maxCooldown, stoneType, -1);
	}

	public boolean isUnderSky(PlayerEntity player) {
      return player.world.isSkyVisible(player.getBlockPos());
	}

	@Override
	public boolean canRecord(ServerWorld world) {
		return false;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		super.inventoryTick(stack, world, entity, slot, selected);
		if (this.cooldown > 0 || world.isClient)
			return;
		ServerPlayerEntity player = (ServerPlayerEntity) entity;
		if (!this.isUnderSky(player))
			return;
		PositionData posData = new PositionData(player.getServerWorld().getRegistryKey(),
			player.getBlockPos(), player.yaw, player.pitch);
		HearthStone.posManager.put(player.getUuidAsString(), stoneType, posData);
	}
}
