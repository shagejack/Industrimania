package shagejack.industrimania.content.misc.processing;

import shagejack.industrimania.Industrimania;
import shagejack.industrimania.foundation.utility.Lang;

public enum HeatCondition {

	NONE(0xffffff), HEATED(0xE88300), SUPERHEATED(0x5C93E8),

	;

	private int color;

	HeatCondition(int color) {
		this.color = color;
	}

	public String serialize() {
		return Lang.asId(name());
	}

	public String getTranslationKey() {
		return "recipe.heat_requirement." + serialize();
	}

	public static HeatCondition deserialize(String name) {
		for (HeatCondition heatCondition : values())
			if (heatCondition.serialize()
					.equals(name))
				return heatCondition;
		Industrimania.LOGGER.warn("Tried to deserialize invalid heat condition: \"" + name + "\"");
		return NONE;
	}

	public int getColor() {
		return color;
	}

}
