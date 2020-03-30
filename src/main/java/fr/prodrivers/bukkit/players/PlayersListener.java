package fr.prodrivers.bukkit.players;

import fr.prodrivers.bukkit.commons.storage.SQLProvider;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

public class PlayersListener implements Listener {
	@EventHandler
	public void onPlayerJoin( PlayerJoinEvent event ) {
		final Player player = event.getPlayer();

		new Thread( () -> {
			fr.prodrivers.bukkit.players.models.Player.updateInformations( player );
		}).start();
	}

	@EventHandler
	public void onPlayerQuit( PlayerQuitEvent event ) {
		final Player player = event.getPlayer();

		new Thread( () -> {
			fr.prodrivers.bukkit.players.models.Player.markLeaved( player );
		}).start();
	}
}