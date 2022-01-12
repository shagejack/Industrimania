package shagejack.industrimania.content.schematics;

public interface ISpecialEntityItemRequirement {

	default ItemRequirement getRequiredItems() {
		return ItemRequirement.INVALID;
	}
	
}
