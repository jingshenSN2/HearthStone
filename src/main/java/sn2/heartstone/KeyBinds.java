package sn2.heartstone;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyBinds {

	public static KeyBinding preview = new KeyBinding("key.placementpreview.preview", // The translation key of the
			// keybinding's name
			InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
			GLFW.GLFW_KEY_LEFT_ALT, // The keycode of the key
			"category.placementpreview.key" // The translation key of the keybinding's category.
			);

	public static void init() {
		KeyBindingHelper.registerKeyBinding(preview);
	}
}
