package fuse4j.hadoopfs.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import javax.management.RuntimeErrorException;

import org.apache.log4j.Logger;

public class Configuration extends Properties {
	
	private static final long serialVersionUID = 1L;

	public Configuration(Properties props) {
		Enumeration<Object> keys = props.keys();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			String value = (String) props.get(key);
			this.put(key, value);
		}
	}
	
	/**
	 * properties that come with the distribution, in the jar
	 */
	public static Configuration getFactoryDefaults() {
		InputStream resourceAsStream = Configuration.class.getClassLoader().getResourceAsStream("fuse4j-hdfs.properties");
		Properties props = new Properties();
		try {
			props.load(resourceAsStream);
			return new Configuration(props);
		} catch (IOException e) {
			// this shouldn't happend
			throw new RuntimeException(e);
		}
	}
	
	public static File getDefaultUserConfigurationPath() {
		String userhome = System.getProperty("user.home");
		File confdir = new File(userhome, ".fuse4j-hdfs");
		File propfile = new File(confdir, "fuse4j-hdfs.properties");
		return propfile;
	}
	
	public static Configuration load(File path) throws IOException {
		Properties props = new Properties();
		props.load(new FileInputStream(path));
		LOG.info(String.format("loaded configuration from [%s]", path.getAbsoluteFile()));
		return new Configuration(props);
	}
	
	
	public static Configuration loadOrCreateDefaults() {
		File defaultUserConfigurationPath = getDefaultUserConfigurationPath();
		Configuration c;
		try {
			c = load(defaultUserConfigurationPath);
			return c;
		} catch (IOException e) {
			Configuration _c = getFactoryDefaults();
			try {
				_c.save(defaultUserConfigurationPath);
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}
		}
		// now it should work
		try {
			c = load(defaultUserConfigurationPath);
			return c;
		} catch (IOException e) {
			throw new RuntimeException("still couldn't load configuration from [" + defaultUserConfigurationPath + "], don't know what to do", e);
		}
	}
	
	public void save(File where) throws IOException {
		File parent = new File(where.getParent());
		if (! parent.exists()) {
			parent.mkdir();
		} else
			if (parent.isFile())
				throw new IOException("parent directory [" + parent + "] already exists and is a file. bummer.");
		
		this.store(new FileOutputStream(where), "fuse hdfs properties saved on " + new Date());
		LOG.info(String.format("saved properties to [%s]", where.getAbsoluteFile()));
	}
	
	static Logger LOG = Logger.getLogger(Configuration.class); 
}
