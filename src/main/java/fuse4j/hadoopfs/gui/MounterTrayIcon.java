package fuse4j.hadoopfs.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.SwingWorker;

import fuse4j.hadoopfs.FuseHdfsClient;
import fuse4j.hadoopfs.config.Configuration;

public class MounterTrayIcon {
	State state = State.UNMOUNTED;
	
	   public static void main(String[] args) {
	        /* Use an appropriate Look and Feel */
//	        try {
//	        	String systemLookAndFeelClassName = UIManager.getSystemLookAndFeelClassName();
//	            UIManager.setLookAndFeel(systemLookAndFeelClassName);
//	            //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//	        } catch (UnsupportedLookAndFeelException ex) {
//	            ex.printStackTrace();
//	        } catch (IllegalAccessException ex) {
//	            ex.printStackTrace();
//	        } catch (InstantiationException ex) {
//	            ex.printStackTrace();
//	        } catch (ClassNotFoundException ex) {
//	            ex.printStackTrace();
//	        }
	        /* Turn off metal's use of bold fonts */
	        UIManager.put("swing.boldMetal", Boolean.FALSE);
	        //Schedule a job for the event-dispatching thread:
	        //adding TrayIcon.
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                createAndShowGUI();
	            }
	        });
	    }
	    
	    private static void createAndShowGUI() {
	        //Check the SystemTray support
	        if (!SystemTray.isSupported()) {
	            System.out.println("SystemTray is not supported");
	            return;
	        }
	        final PopupMenu popup = new PopupMenu();
	        final TrayIcon trayIcon =
	                new TrayIcon(createImage("mounter-systray.png", "tray icon"));
	        final SystemTray tray = SystemTray.getSystemTray();
	        
	        // Create a popup menu components
	        
	        MenuItem aboutItem = new MenuItem("About");
	        MenuItem exitItem = new MenuItem("Exit");
	        
	        MenuItem configurationItem = new MenuItem("Configuration...");
	        MenuItem mountItem = new MenuItem("Mount");
	        
	        //Add components to popup menu
	        
	        popup.add(configurationItem);
	        popup.add(mountItem);
	        popup.add(aboutItem);
	        popup.add(exitItem);
	        
	        trayIcon.setPopupMenu(popup);
	        trayIcon.setImageAutoSize(true);
	        trayIcon.setToolTip("HDFS Mounter");
	        
	        
	        
	        try {
	            tray.add(trayIcon);
	        } catch (AWTException e) {
	            System.out.println("TrayIcon could not be added.");
	            return;
	        }
	        
	        trayIcon.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                JOptionPane.showMessageDialog(null,
	                        "This dialog box is run from System Tray");
	            }
	        });
	        
	        configurationItem.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					final ConfigurationDialog dialog = new ConfigurationDialog();
					
					dialog.pack();
					dialog.setVisible(true);
					
				}
			});
	        
	        aboutItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                JOptionPane.showMessageDialog(null,
	                        "This dialog box is run from the About menu item");
	            }
	        });

	        mountItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	final Configuration conf = Configuration.loadOrCreateDefaults();
                    	makeSureDirectoryExists(conf.getMountPoint());
                    	SwingWorker<?, ?> worker = new SwingWorker() {

							@Override
							protected Object doInBackground() throws Exception {
								FuseHdfsClient.doMount(conf.getHdfsUrl(), conf.getUsername(), 
										new String[] {conf.getMountPoint(), "-f"});
								return null;
							}
                    		
                    	};
                    	worker.execute();
                        
                    }

					private void makeSureDirectoryExists(String mountPoint) {
						File dir = new File(mountPoint);
						if (!dir.exists()) 
							dir.mkdirs();
						else {
							if (dir.isFile())
								throw new IllegalArgumentException(String.format("mount point [%s] is a regular file, should point to a folder", mountPoint));
							if (dir.exists() && dir.list().length > 0)
								throw new IllegalArgumentException(String.format("mount point [%s] is a non-empty folder, should point to an empty folder", mountPoint));
							
						}
					}
                });
	        
//	        ActionListener listener = new ActionListener() {
//	            public void actionPerformed(ActionEvent e) {
//	                MenuItem item = (MenuItem)e.getSource();
//	                //TrayIcon.MessageType type = null;
//	                System.out.println(item.getLabel());
//	                if ("Error".equals(item.getLabel())) {
//	                    //type = TrayIcon.MessageType.ERROR;
//	                    trayIcon.displayMessage("Hdfs Mounter",
//	                            "This is an error message", TrayIcon.MessageType.ERROR);
//	                    
//	                } else if ("Warning".equals(item.getLabel())) {
//	                    //type = TrayIcon.MessageType.WARNING;
//	                    trayIcon.displayMessage("Sun TrayIcon Demo",
//	                            "This is a warning message", TrayIcon.MessageType.WARNING);
//	                    
//	                } else if ("Info".equals(item.getLabel())) {
//	                    //type = TrayIcon.MessageType.INFO;
//	                    trayIcon.displayMessage("Sun TrayIcon Demo",
//	                            "This is an info message", TrayIcon.MessageType.INFO);
//	                    
//	                } else if ("None".equals(item.getLabel())) {
//	                    //type = TrayIcon.MessageType.NONE;
//	                    trayIcon.displayMessage("Sun TrayIcon Demo",
//	                            "This is an ordinary message", TrayIcon.MessageType.NONE);
//	                }
//	            }
//	        };
	        
//	        errorItem.addActionListener(listener);
//	        warningItem.addActionListener(listener);
//	        infoItem.addActionListener(listener);
//	        noneItem.addActionListener(listener);
	        
	        exitItem.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                tray.remove(trayIcon);
	                System.exit(0);
	            }
	        });
	    }
	    
	    //Obtain the image URL
	    protected static Image createImage(String path, String description) {
	        URL imageURL = MounterTrayIcon.class.getClassLoader().getResource(path);
	        
	        if (imageURL == null) {
	            System.err.println("Resource not found: " + path);
	            return null;
	        } else {
	            return (new ImageIcon(imageURL, description)).getImage();
	        }
	    }
}
