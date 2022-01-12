package shagejack.industrimania.foundation.tileEntity.behaviour;

import shagejack.industrimania.foundation.tileEntity.TileEntityBehaviour;

public class BehaviourType<T extends TileEntityBehaviour> {

	private String name;

	public BehaviourType(String name) {
		this.name = name;
	}

	public BehaviourType() {
		this("");
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return super.hashCode() * 31 * 493286711; // Better hash table distribution
	}
}
