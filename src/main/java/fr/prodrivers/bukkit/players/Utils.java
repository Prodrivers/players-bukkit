package fr.prodrivers.bukkit.players;

import java.nio.ByteBuffer;
import java.util.UUID;

public class Utils {
	public static String INIT_TABLES_SCRIPT = "create table players\n" +
			"(\n" +
			"\tplayeruuid BINARY(16) not null,\n" +
			"\tname varchar(180) not null,\n" +
			"\tlastSeen datetime not null,\n" +
			"\tconnected boolean default 0 not null,\n" +
			"\tparkoins int default 0 not null,\n" +
			"\tparkourLevel int default 0 not null\n" +
			");\n" +
			"\n" +
			"create unique index players_playeruuid_uindex\n" +
			"\ton players (playeruuid);\n" +
			"\n" +
			"alter table players\n" +
			"\tadd constraint players_pk\n" +
			"\t\tprimary key (playeruuid);\n" +
			"\n";

	public static byte[] getBytesFromUniqueId( UUID uniqueId ) {
		ByteBuffer bb = ByteBuffer.wrap( new byte[ 16 ] );
		bb.putLong( uniqueId.getMostSignificantBits() );
		bb.putLong( uniqueId.getLeastSignificantBits() );
		return bb.array();
	}

	public static UUID getUniqueIdFromBytes( byte[] uniqueId ) {
		return UUID.nameUUIDFromBytes( uniqueId );
	}
}
