package discoveryscheduler.persistence;

import java.util.Properties;
import java.io.*;

public class DiscoverySchedulerImportConfig {
	private static Properties config = new Properties();
	private static final String CONFIG_FILE = "discoveryscheduler/config/discoverySchedulerConfig.properties";
	
	public void loadConfig() {
		ClassLoader classLoader = getClass().getClassLoader();
		File configFile = new File(classLoader.getResource(CONFIG_FILE).getFile());
		try {
		    FileReader reader = new FileReader(configFile);
		    config.load(reader);
		 
		    reader.close();
		} catch (FileNotFoundException ex) {
			System.out.print("Config file at " + CONFIG_FILE + " not found");
		} catch (IOException ex) {
			System.out.print("Config file io exception");
		}
	}
	
	public static Properties getConfig() {
		return config;
	}

}
