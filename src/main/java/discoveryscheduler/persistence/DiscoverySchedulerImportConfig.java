package discoveryscheduler.persistence;

import java.util.Properties;
import java.io.*;

public class DiscoverySchedulerImportConfig {
	private static Properties config = new Properties();;
	
	public DiscoverySchedulerImportConfig() {
		ClassLoader classLoader = getClass().getClassLoader();
		File configFile = new File(classLoader.getResource("discoveryscheduler/config/discoverySchedulerConfig.properties").getFile());
		try {
		    FileReader reader = new FileReader(configFile);
		    config.load(reader);
		 
		    reader.close();
		} catch (FileNotFoundException ex) {
			System.out.print("Config file not found exception");
		} catch (IOException ex) {
			System.out.print("Config file io exception");
		}
	}
	
	public static Properties getConfig() {
		return config;
	}

}
