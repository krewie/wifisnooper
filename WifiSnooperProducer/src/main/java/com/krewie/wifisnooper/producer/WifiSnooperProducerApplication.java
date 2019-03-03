package com.krewie.wifisnooper.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.support.GenericMessage;

@SpringBootApplication
public class WifiSnooperProducerApplication {

	public static void main(String[] args) throws InterruptedException {

	ConfigurableApplicationContext context = SpringApplication.run(WifiSnooperProducerApplication.class, args);

	Producer producer = context.getBean(Producer.class);

	//String jsonString = "{\"TYPE\":\"Device\",\"SSID\":\"comhem_C0E\",\"MAC\":\"68ab1e1ae5e3\",\"CHNL\":\"3\",\"RSSI\":\"-70\"}";
		while(true) {
			Thread.sleep(10000);
			producer.send(new Device("Device", Randomizer.randomIdentifier(), Randomizer.randomIdentifier(), "3", "50"));
			producer.send(new Device("Device", Randomizer.randomIdentifier(), Randomizer.randomIdentifier(), "3", "50"));
			producer.send(new Device("Device", Randomizer.randomIdentifier(), Randomizer.randomIdentifier(), "3", "50"));
		}
	}
}
