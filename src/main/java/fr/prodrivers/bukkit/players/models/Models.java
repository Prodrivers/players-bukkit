package fr.prodrivers.bukkit.players.models;

import java.util.ArrayList;
import java.util.List;

public class Models {
	public static List<Class<?>> ModelsList = new ArrayList<>();

	public static void populate() {
		ModelsList.clear();
		ModelsList.add( Player.class );
	}
}
