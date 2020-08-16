package sn2.heartstone.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import sn2.heartstone.PreviewPlayerEntity;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity implements PreviewPlayerEntity {
	@Shadow
	public abstract void sendMessage(Text message, boolean actionBar);

	private boolean isPreview = false;

	@Override
	public boolean isPreview() {
		return isPreview;
	}

	@Override
	public void setPreview() {
		isPreview = !isPreview;
		MutableText ON = new TranslatableText("message.player.preview.on")
				.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(152 + 251 * 256 + 152 * 256 * 256)));
		MutableText OFF = new TranslatableText("message.player.preview.off")
				.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(34 + 34 * 256 + 178 * 256 * 256)));
		this.sendMessage(new TranslatableText("message.player.preview", isPreview ? ON : OFF), true);
	}
}
