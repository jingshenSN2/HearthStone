package sn2.hearthstone.storage.data;

public class CooldownData {
	private int cooldown = 0;
	
	public CooldownData() {
		this.cooldown = 0;
	}
	
	public CooldownData(int cooldown) {
		this.cooldown = cooldown;
	}
	
	public void tick() {
		if (this.cooldown > 0)
			this.cooldown--;
	}

	public int getCoolDown() {
		return cooldown;
	}
}
