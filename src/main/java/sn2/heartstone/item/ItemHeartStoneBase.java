package sn2.heartstone.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolMaterial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sn2.heartstone.HeartStone;
import sn2.heartstone.data.PositionData;

public class ItemHeartStoneBase extends Item {
	
	public ItemHeartStoneBase(ToolMaterial material) {
		super(new Settings().maxCount(1).group(ItemGroup.TOOLS).maxDamage(material.getDurability()));
	}

	public void teleport(PlayerEntity player, ServerWorld world, BlockPos pos) {
		if (!world.getRegistryKey().equals(player.world.getRegistryKey()))
			player.moveToWorld(world);
		player.teleport(pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		if (world.isClient)
		    return ActionResult.PASS;
		ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
		if (player.isSneaking()) {
			PositionData posData = new PositionData(player.getUuidAsString(), player.getServerWorld().getRegistryKey(), player.getBlockPos(), player.yaw, player.pitch);
			HeartStone.playerDateManager.put(player, posData);
		}
		else {
			HeartStone.playerDateManager.get(player).teleport(player);
		}
	    return ActionResult.PASS;
	}

}
