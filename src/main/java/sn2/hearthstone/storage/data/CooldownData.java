package sn2.hearthstone.storage.data;

public class CooldownData {
	private int cooldown = 0;
	private int stoneType;
	
	public CooldownData(int stoneType) {
		this.cooldown = 0;
		this.stoneType = stoneType;
	}
	
	public CooldownData(int cooldown, int stoneType) {
		this.cooldown = cooldown;
		this.stoneType = stoneType;
	}
	
	public void tick() {
		if (this.cooldown > 0)
			this.cooldown--;
	}

	public int getCoolDown() {
		return cooldown;
	}
	
	public int getStoneType() {
		return stoneType;
	}
}
