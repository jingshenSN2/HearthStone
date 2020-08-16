package sn2.hearthstone.item;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import sn2.hearthstone.HearthStone;

public class ItemRegistry {

	public static final Item WOODEN_SLAB_HOOKER = new ItemHearthStoneBase(ToolMaterials.WOOD);

	public static void init() {
		Registry.register(Registry.ITEM, new Identifier(HearthStone.MODID, "wooden_slab_hooker"),
				WOODEN_SLAB_HOOKER);
	}
}
