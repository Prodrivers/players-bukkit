package fr.prodrivers.bukkit.players;

import fr.prodrivers.bukkit.commons.storage.SQLProvider;
import fr.prodrivers.bukkit.players.models.Models;
import io.ebean.EbeanServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class PlayersPlugin extends JavaPlugin implements org.bukkit.event.Listener {
	public static PlayersPlugin plugin;
	public static EbeanServer database = null;

	public static final Logger logger = Logger.getLogger( "Minecraft" );

	@Override
	public void onDisable() {
		PluginDescriptionFile plugindescription = this.getDescription();

		for( Player player : getServer().getOnlinePlayers() ) {
			fr.prodrivers.bukkit.players.models.Player.markLeaved( player );
		}

		logger.info( plugindescription.getName() + " has been disabled!" );
	}

	@Override
	public void onEnable() {
		PluginDescriptionFile plugindescription = this.getDescription();
		Models.populate();

		if( plugin == null )
			plugin = this;

		getServer().getPluginManager().registerEvents( new PlayersListener(), this );

		database = SQLProvider.getEbeanServer( Models.ModelsList );
		if( database == null ) {
			logger.severe( "[ParkourAddon] ProdriversCommons SQL Provider not available, plugin is unable to start. Please check ProdriversCommons errors." );
			throw new InstantiationError( "SQL provider unavailable" );
		}

		if( !setupDatabase() ) {
			throw new InstantiationError( "Database wrongly initialized" );
		}

		logger.info( plugindescription.getName() + " has been enabled!" );
	}

	private boolean setupDatabase() {
		if( database == null )
			return false;
		try {
			for( Class<?> modelClass : Models.ModelsList ) {
				database.find( modelClass ).findCount();
			}
			return true;
		} catch( RuntimeException ex ) {
			logger.info( "Installing database for " + getDescription().getName() + " due to first time usage" );
			try {
				database.createSqlUpdate( Utils.INIT_TABLES_SCRIPT ).execute();
				return true;
			} catch( RuntimeException rex ) {
				logger.severe( "Error while installing the database " + rex.getLocalizedMessage() );
				logger.severe( "Please manually execute the installation SQL script:\n" + Utils.INIT_TABLES_SCRIPT );
				rex.printStackTrace();
			}
		}
		return false;
	}
}
