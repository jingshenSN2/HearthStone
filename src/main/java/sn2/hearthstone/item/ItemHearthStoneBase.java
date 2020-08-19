package sn2.hearthstone.item;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
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
	private int stoneType;
	
	
	public ItemHearthStoneBase(int maxCooldown, int stoneType) {
		super(new Settings().maxCount(1).group(ItemGroup.TOOLS));
		this.maxCooldown = maxCooldown;
		this.stoneType = stoneType;
	}
	
	/*
	 *    protected boolean isInDaylight() {
      if (this.world.isDay() && !this.world.isClient) {
         float f = this.getBrightnessAtEyes();
         BlockPos blockPos = this.getVehicle() instanceof BoatEntity ? (new BlockPos(this.getX(), (double)Math.round(this.getY()), this.getZ())).up() : new BlockPos(this.getX(), (double)Math.round(this.getY()), this.getZ());
         if (f > 0.5F && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.world.isSkyVisible(blockPos)) {
            return true;
         }
      }

      return false;
   }
	 */

	public void teleport(PlayerEntity player, ServerWorld world, BlockPos pos) {
		if (!world.getRegistryKey().equals(player.world.getRegistryKey()))
			player.moveToWorld(world);
		player.teleport(pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (world.isClient) 
			return;
		int cooldown = HearthStone.cooldownManager.get(entity.getUuidAsString(), this.stoneType).getCoolDown();
		CompoundTag tag = new CompoundTag();
		tag.putInt("cooldown", cooldown);
		stack.setTag(tag);
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
		CompoundTag tag = stack.getTag();
		int cooldown;
		if (tag == null)
			cooldown = 0;
		else 
			cooldown = tag.getInt("cooldown");
		if (cooldown > 0) {
			int minutes = (int) (cooldown / 1200);
			int seconds = (int) (cooldown / 20 - (minutes * 60));
			tooltip.add(new TranslatableText("Cooldown: " + minutes + " minutes" + seconds + "seconds"));
		}
		else 
			tooltip.add(new TranslatableText("Ready!"));
	}
}
