package shagejack.industrimania.foundation.utility;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import shagejack.industrimania.Industrimania;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Lang {

	public static TranslatableComponent translate(String key, Object... args) {
		return createTranslationTextComponent(key, args);
	}

	public static TranslatableComponent createTranslationTextComponent(String key, Object... args) {
		return new TranslatableComponent(Industrimania.MOD_ID + "." + key, args);
	}

	public static void sendStatus(Player player, String key, Object... args) {
		player.displayClientMessage(createTranslationTextComponent(key, args), true);
	}

	public static List<Component> translatedOptions(String prefix, String... keys) {
		List<Component> result = new ArrayList<>(keys.length);
		for (String key : keys)
			result.add(translate(prefix + "." + key));

		return result;
	}

	public static String asId(String name) {
		return name.toLowerCase(Locale.ROOT);
	}
	
	public static String nonPluralId(String name) {
		String asId = asId(name);
		return asId.endsWith("s") ? asId.substring(0, asId.length() - 1) : asId;
	}

}
