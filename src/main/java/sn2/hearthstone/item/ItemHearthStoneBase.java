package sn2.hearthstone.item;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import sn2.hearthstone.HearthStone;
import sn2.hearthstone.storage.data.CooldownData;
import sn2.hearthstone.storage.data.PositionData;

public class ItemHearthStoneBase extends Item {
	
	private int maxCooldown;
	protected int cooldown;
	protected int stoneType;
	
	
	public ItemHearthStoneBase(int maxCooldown, int stoneType) {
		super(new Settings().maxCount(1).group(ItemGroup.TOOLS));
		this.maxCooldown = maxCooldown;
		this.cooldown = 0;
		this.stoneType = stoneType;
	}
	
	public boolean isUnderSky(PlayerEntity player) {
      if (!player.world.isClient)
    	  return false;
      BlockPos blockPos = player.getVehicle() instanceof BoatEntity ? 
     		 (new BlockPos(player.getX(), (double)Math.round(player.getY()), player.getZ())).up() : 
     			 new BlockPos(player.getX(), (double)Math.round(player.getY()), player.getZ());
      return player.world.isSkyVisible(blockPos);
	}
	
	public void teleport(PlayerEntity player, ServerWorld world, BlockPos pos) {
		if (!canTeleport(world, player, pos))
			return;
		if (!world.getRegistryKey().equals(player.world.getRegistryKey()))
			player.moveToWorld(world);
		player.teleport(pos.getX(), pos.getY(), pos.getZ());
	}
	
	public boolean canTeleport(ServerWorld world, PlayerEntity player, BlockPos pos) {
		return true;
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (world.isClient) 
			return;
		this.cooldown = HearthStone.cooldownManager.get(entity.getUuidAsString(), this.stoneType).getCoolDown();
	}

	@Override
	 public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
	      ItemStack itemStack = user.getStackInHand(hand);
		if (!world.isClient) {
			ServerPlayerEntity player = (ServerPlayerEntity) user;
			PositionData posData = null;
			if (player.isSneaking()) {
				posData = new PositionData(player.getUuidAsString(), player.getServerWorld().getRegistryKey(),
					player.getBlockPos(), player.yaw, player.pitch, 0);
				HearthStone.posManager.put(player.getUuidAsString(), stoneType, posData);
			} else {
				posData = HearthStone.posManager.get(player.getUuidAsString(), stoneType);
				if (posData != null) {
					posData.teleport(player);
					HearthStone.cooldownManager.put(player.getUuidAsString(), stoneType, new CooldownData(maxCooldown, stoneType));
				}
			}
		}
		return TypedActionResult.method_29237(itemStack, world.isClient());
	   }
	
	@Override
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if (this.cooldown > 0) {
			int minutes = (int) (cooldown / 1200);
			int seconds = (int) (cooldown / 20 - (minutes * 60));
			tooltip.add(new TranslatableText("Cooldown: " + minutes + " minutes" + seconds + "seconds"));
		}
		else 
			tooltip.add(new TranslatableText("Ready!"));
	}
}
