package fuse4j.hadoopfs.gui;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;

import fuse4j.hadoopfs.config.Configuration;

public class ConfigurationDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	Configuration configuration;
	
	public ConfigurationDialog() {
		this.configuration = Configuration.loadOrCreateDefaults();
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.getContentPane().add(new JButton("hi"));
	}
}
