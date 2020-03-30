package fr.prodrivers.bukkit.players.models;

import fr.prodrivers.bukkit.commons.storage.SQLProvider;
import fr.prodrivers.bukkit.players.PlayersPlugin;
import fr.prodrivers.bukkit.players.Utils;

import io.ebean.EbeanServer;
import io.ebean.annotation.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;

@Entity
@Table( name = "players" )
public class Player {
	@Column( length = 16, name="playeruuid", nullable = false )
	@NotNull
	@Getter
	@Setter
	byte[] playerUniqueId;

	@Column( length = 180, nullable = false )
	@NotNull
	@Getter
	@Setter
	String name;

	@Column( nullable = false, name="lastSeen" )
	@NotNull
	@Getter
	@Setter
	public Date lastSeen = new Date();

	@Column( columnDefinition = "boolean default 0 not null" )
	@NotNull
	@Getter
	@Setter
	boolean connected;

	@Column( columnDefinition = "integer default 0 not null" )
	@NotNull
	@Getter
	@Setter
	int parkoins;

	@Column( columnDefinition = "integer default 0 not null", name="parkourLevel" )
	@NotNull
	@Getter
	@Setter
	int parkourLevel;

	public static Player retrieve( EbeanServer server, UUID playerUniqueId ) {
		return server.find( Player.class ).where().eq( "playeruuid", Utils.getBytesFromUniqueId( playerUniqueId ) ).findOne();
	}

	public static void markLeaved( org.bukkit.entity.Player player ) {
		Player dbPlayer = fr.prodrivers.bukkit.players.models.Player.retrieve( PlayersPlugin.database, player.getUniqueId() );
		System.out.println(dbPlayer);
		if( dbPlayer != null ) {
			try {
				PreparedStatement query = SQLProvider.getConnection().prepareStatement( "UPDATE players SET connected = ? WHERE playeruuid = ?;" );
				query.setBoolean( 1, false );
				query.setBytes( 2, Utils.getBytesFromUniqueId( player.getUniqueId() ) );
				query.executeUpdate();
			} catch( SQLException e ) {
				PlayersPlugin.logger.log( Level.SEVERE, "Error on set player " + player.getName() + " as not connected", e );
			}
		}
	}
	
	public static void updateInformations( org.bukkit.entity.Player player ) {
		Player dbPlayer = retrieve( PlayersPlugin.database, player.getUniqueId() );

		if( dbPlayer == null ) {
			try {
				PreparedStatement query = SQLProvider.getConnection().prepareStatement( "INSERT INTO players (playeruuid, name, lastSeen, connected) VALUES (?, ?, ?, ?)" );
				query.setBytes( 1, Utils.getBytesFromUniqueId( player.getUniqueId() ) );
				query.setString( 2, player.getName() );
				query.setTimestamp( 3, new java.sql.Timestamp( ( new Date() ).getTime() ) );
				query.setBoolean( 4, true );
				query.executeUpdate();
			} catch( SQLException e ) {
				e.printStackTrace();
			}
		} else {
			try {
				PreparedStatement query = SQLProvider.getConnection().prepareStatement( "UPDATE players SET name = ?, lastSeen = ?, connected = ? WHERE playeruuid = ?;" );
				query.setString( 1, player.getName() );
				query.setTimestamp( 2, new java.sql.Timestamp( ( new Date() ).getTime() ) );
				query.setBoolean( 3, true );
				query.setBytes( 4, Utils.getBytesFromUniqueId( player.getUniqueId() ) );
				query.executeUpdate();
			} catch( SQLException e ) {
				e.printStackTrace();
			}
			}
	}
}
