CREATE KEYSPACE wifi_addressing
	WITH REPLICATION = {
		'class':'SimpleStrategy',
		'replication_factor': 1
	};

Use wifi_addressing;

CREATE TABLE addresses (MAC text PRIMARY KEY, 
			SSID text,
			chnl int,
			rssi int,
			type text,
            occurances bigint 
			);
		
