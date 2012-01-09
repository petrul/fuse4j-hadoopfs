package fuse4j.hadoopfs.gui;

import javax.swing.JDialog;

import fuse4j.hadoopfs.config.Configuration;

public class ConfigurationDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	Configuration configuration;
	
	public ConfigurationDialog() {
		this.configuration = Configuration.loadOrCreateDefaults();
		this.getContentPane().add(new ConfigurationPanel(this.configuration));
	}
}
