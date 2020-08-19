package sn2.hearthstone.item;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import sn2.hearthstone.HearthStone;

public class ItemRegistry {

	public static final Item SIMPLE_HEARTHSTONE = new ItemHearthStoneBase(20*60*20, 0);

	public static void init() {
		Registry.register(Registry.ITEM, new Identifier(HearthStone.MODID, "simple_hearthstone"),
				SIMPLE_HEARTHSTONE);
	}
}
