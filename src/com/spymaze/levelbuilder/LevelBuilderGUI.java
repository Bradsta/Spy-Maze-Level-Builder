package com.spymaze.levelbuilder;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.spymaze.levelbuilder.component.LevelLabel;
import com.spymaze.levelbuilder.component.LevelTester;
import com.spymaze.levelbuilder.sprite.CharacterSprite;
import com.spymaze.levelbuilder.sprite.Direction;
import com.spymaze.levelbuilder.sprite.Sprite;
import com.spymaze.levelbuilder.sprite.TileSprite;
import com.spymaze.levelbuilder.utility.Utility;



@SuppressWarnings("serial")
public class LevelBuilderGUI extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private final ButtonGroup buttonGroup_1 = new ButtonGroup();
	private final LevelBuilderGUI lbg;
	
	private static int missionNumber = 1;
	
	public static void main(String args[]) {
		missionNumber = Integer.parseInt(JOptionPane.showInputDialog("Please enter which mission number to use sprites from? (Example: 1)"));
		
		Sprite.loadSprites(missionNumber);
		TileSprite.loadSprites(missionNumber);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LevelBuilderGUI frame = new LevelBuilderGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public LevelBuilderGUI() {
		setResizable(false);
		
		lbg = this;
		
		try {
			for (final LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels())
				if (System.getProperty("os.name").contains("indow") && "Windows".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				} else if (!System.getProperty("os.name").contains("indow") && "Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
		} catch (final UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		} catch (final InstantiationException e) {
			e.printStackTrace();
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}
		setTitle("SpyMaze Level Builder");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1350, 950);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(210, 11, 2, 390);
		contentPane.add(separator);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 190, 390);
		contentPane.add(tabbedPane);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Options", null, panel_1, null);
		panel_1.setLayout(null);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 67, 165, 2);
		panel_1.add(separator_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(222, 11, 1112, 900);
		contentPane.add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		
		final LevelLabel ml = new LevelLabel();
		scrollPane.setViewportView(ml);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ml.tileSprites != null) {
					TileSprite[][] added = new TileSprite[(Integer.parseInt((String) comboBox.getSelectedItem()) / 64)][ml.tileSprites[0].length];
					
					if (added.length != ml.tileSprites.length) {
						for (int x=0; x<added.length; x++) {
							for (int y=0; y<added[x].length; y++) {
								if (x >= ml.tileSprites.length) {
									added[x][y] = TileSprite.DEV_TILE;
								} else {
									added[x][y] = ml.tileSprites[x][y];
								}
							}
						}
						
						ml.tileSprites = added;
					}
					
					ml.setIcon(new ImageIcon(new BufferedImage(added.length * 64, added[0].length * 64, BufferedImage.TYPE_INT_RGB)));
				}
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"512", "576", "640", "704", "768", "832", "896", "960", "1024", "1088", "1152", "1216", "1280", "1344", "1408"}));
		comboBox.setBounds(66, 11, 109, 20);
		panel_1.add(comboBox);
		
		JLabel lblWidth = new JLabel("Width: ");
		lblWidth.setBounds(10, 17, 46, 14);
		panel_1.add(lblWidth);
		
		JLabel lblHeight = new JLabel("Height:");
		lblHeight.setBounds(10, 42, 46, 14);
		panel_1.add(lblHeight);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (ml.tileSprites != null) {
					TileSprite[][] added = new TileSprite[ml.tileSprites.length][(Integer.parseInt((String) comboBox_1.getSelectedItem()) / 64)];
					
					if (added[0].length != ml.tileSprites[0].length) {
						for (int x=0; x<added.length; x++) {
							for (int y=0; y<added[x].length; y++) {
								if (y >= ml.tileSprites[x].length) {
									added[x][y] = TileSprite.DEV_TILE;
								} else {
									added[x][y] = ml.tileSprites[x][y];
								}
							}
						}
						
						ml.tileSprites = added;
					}
					
					ml.setIcon(new ImageIcon(new BufferedImage(added.length * 64, added[0].length * 64, BufferedImage.TYPE_INT_RGB)));
				}
			}
		});
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"512", "576", "640", "704", "768", "832", "896", "960", "1024", "1088", "1152", "1216", "1280", "1344", "1408"}));
		comboBox_1.setBounds(66, 39, 109, 20);
		panel_1.add(comboBox_1);
		
		JButton btnNewButton = new JButton("Create New Level");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ml.paintMap = true;
				ml.tileSprites = new TileSprite[(Integer.parseInt((String) comboBox.getSelectedItem()) / 64)][(Integer.parseInt((String) comboBox_1.getSelectedItem()) / 64)];
				ml.charSprites = new ArrayList<CharacterSprite>();
				
				for (int x=0; x<ml.tileSprites.length; x++) {
					for (int y=0; y<ml.tileSprites[x].length; y++) {
						ml.tileSprites[x][y] = TileSprite.DEV_TILE;
					}
				}
				
				ml.setIcon(new ImageIcon(new BufferedImage((Integer.parseInt((String) comboBox.getSelectedItem())),
						(Integer.parseInt((String) comboBox_1.getSelectedItem())), BufferedImage.TYPE_INT_RGB))); // To set the size of the label to draw on
				
				setTitle("SpyMaze Level Builder");
				
				ml.startUsed = false;
				ml.endUsed = false;
			}
		});
		btnNewButton.setBounds(10, 80, 165, 23);
		panel_1.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Save Level");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ml.tileSprites == null) {
					JOptionPane.showMessageDialog(null, "Cannot save, you haven't created a map!");
					return;
				}
				
				try {
					JFileChooser fc = new JFileChooser();
					//fc.setFileFilter(new AMLFilter());
					fc.setCurrentDirectory(new File("./levels/"));
					fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					fc.showSaveDialog(lbg);
					String fileName = fc.getSelectedFile().getAbsolutePath();
					
					if (fileName == null) return;
					
					if (!fileName.toLowerCase().contains(".aml"))
						fileName = fileName + ".aml";
					File f = new File(fileName);
					if (f.exists())
						f.delete();
					f.createNewFile();
					FileWriter fStream = new FileWriter(fileName, true);
					BufferedWriter out = new BufferedWriter(fStream);
					out.write(Utility.encryptString(("Dimensions:" + ml.tileSprites.length + " " + ml.tileSprites[0].length), '1') + "\r\n");
					out.write(Utility.encryptString("Spy Maze Level", '1') + "\r\n");
					for (int x=0; x<ml.tileSprites.length; x++) {
						for (int y=0; y<ml.tileSprites[x].length; y++) {
							out.write(Utility.encryptString("tile x:" + x + " y:" + y + " file:" + ml.tileSprites[x][y].f.toString() , '1') + "\r\n");
						}
					}
					for (int i=0; i<ml.charSprites.size(); i++) {
						if (i == ml.charSprites.size()-1) {
							out.write(Utility.encryptString("char x:" + ml.charSprites.get(i).xLoc + " y:" + ml.charSprites.get(i).yLoc + 
											" dir:" + ml.charSprites.get(i).direction + " file" + ml.charSprites.get(i).f.toString(), '1'));
						} else {
							out.write(Utility.encryptString("char x:" + ml.charSprites.get(i).xLoc + " y:" + ml.charSprites.get(i).yLoc + 
							" dir:" + ml.charSprites.get(i).direction + " file" + ml.charSprites.get(i).f.toString(), '1') + "\r\n");
						}
					}
					out.close();
					fStream.close();
					
					setTitle("SpyMaze Level Builder (" + fileName + ")");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(10, 138, 165, 23);
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("Load Level");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					JFileChooser fc = new JFileChooser();
					fc.setFileFilter(new AMLFilter());
					fc.setCurrentDirectory(new File("./levels/"));
					fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
					fc.showOpenDialog(lbg);
					
					if (fc.getSelectedFile() != null) {
						String fileName = fc.getSelectedFile().getAbsolutePath();
						if (!fileName.toLowerCase().contains(".aml"))
							return;
						FileReader fStream = new FileReader(fileName);
						BufferedReader br = new BufferedReader(fStream);
						String nextLine = null;
						
						ml.charSprites.clear();
						
						while ((nextLine = br.readLine()) != null) {
							String dec = Utility.encryptString(nextLine, '1');
							System.out.println(dec);
							if (dec.contains("Dimensions")) {
								ml.tileSprites = new TileSprite[Integer.parseInt(dec.replace("Dimensions:", "").split(" ")[0])]
								                                [Integer.parseInt(dec.replace("Dimensions:", "").split(" ")[1])];
							} else if (dec.contains("tile")) {
								TileSprite used = null;
								
								if (dec.contains("\\tile.png")) used = TileSprite.TILE;
								else if (dec.contains("\\unreachabletile.png")) used = TileSprite.UNREACHABLE;
								else if (dec.contains("\\developmentTile.png")) used = TileSprite.DEV_TILE;
								else if (dec.contains("\\starttile.png")) {
									ml.startUsed = true;
									used = TileSprite.START_TILE;
								} else {
									ml.endUsed = true;
									used = TileSprite.END_TILE;
								}
								
								ml.tileSprites[Integer.parseInt(Utility.parse("x:", " ", dec))][Integer.parseInt(Utility.parse("y:", " ", dec))] = used;
							} else if (dec.contains("char")) {
								File spriteLoc = Sprite.ENEMY.f;
								Direction dir = null;
								String directionParsed = Utility.parse("dir:", " ", dec);
								
								for (Direction d : Direction.values()) {
									if (d.toString().equals(directionParsed)) {
										dir = d;
										break;
									}
								}
								
								ml.charSprites.add(new CharacterSprite(spriteLoc, dir, Integer.parseInt(Utility.parse("x:", " ", dec)),
										Integer.parseInt(Utility.parse("y:", " ", dec)), 0, 0));
							}
						}
						
						br.close();
						
						setTitle("SpyMaze Level Builder (" + fileName + ")");
						
						ml.paintMap = true;
						
						comboBox.setSelectedItem("" + (ml.tileSprites.length * 64));
						comboBox_1.setSelectedItem("" + (ml.tileSprites[0].length * 64));
						
						ml.setIcon(new ImageIcon(new BufferedImage(ml.tileSprites.length * 64, ml.tileSprites[0].length * 64, BufferedImage.TYPE_INT_RGB)));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		btnNewButton_2.setBounds(10, 109, 165, 23);
		panel_1.add(btnNewButton_2);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Tiles", null, panel_2, null);
		panel_2.setLayout(null);
		
		JToggleButton tglbtnNewToggleButton = new JToggleButton("");
		tglbtnNewToggleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { // tile
				ml.currentCharSprite = null;
				ml.currentSprite = TileSprite.TILE;
			}
		});
		buttonGroup.add(tglbtnNewToggleButton);
		tglbtnNewToggleButton.setIcon(new ImageIcon("./sprites/mission " + missionNumber + " sprites/ground/tile.png"));
		tglbtnNewToggleButton.setBounds(106, 11, 69, 73);
		panel_2.add(tglbtnNewToggleButton);
		
		JToggleButton tglbtnNewToggleButton_1 = new JToggleButton("");
		tglbtnNewToggleButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { // unreachable
				ml.currentCharSprite = null;
				ml.currentSprite = TileSprite.UNREACHABLE;
			}
		});
		buttonGroup.add(tglbtnNewToggleButton_1);
		tglbtnNewToggleButton_1.setIcon(new ImageIcon("./sprites/mission " + missionNumber + " sprites/ground/unreachabletile.png"));
		tglbtnNewToggleButton_1.setBounds(10, 95, 69, 73);
		panel_2.add(tglbtnNewToggleButton_1);
		
		JToggleButton tglbtnNewToggleButton_5 = new JToggleButton("");
		buttonGroup.add(tglbtnNewToggleButton_5);
		tglbtnNewToggleButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { // Start
				ml.currentCharSprite = null;
				ml.currentSprite = TileSprite.START_TILE;
			}
		});
		tglbtnNewToggleButton_5.setIcon(new ImageIcon("./sprites/mission " + missionNumber + " sprites/ground/starttile.png"));
		tglbtnNewToggleButton_5.setBounds(10, 11, 69, 73);
		panel_2.add(tglbtnNewToggleButton_5);
		
		JToggleButton tglbtnNewToggleButton_6 = new JToggleButton("");
		buttonGroup.add(tglbtnNewToggleButton_6);
		tglbtnNewToggleButton_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { // end
				ml.currentCharSprite = null;
				ml.currentSprite = TileSprite.END_TILE;
			}
		});
		tglbtnNewToggleButton_6.setIcon(new ImageIcon("./sprites/mission " + missionNumber + " sprites/ground/endtile.png"));
		tglbtnNewToggleButton_6.setBounds(106, 95, 69, 73);
		panel_2.add(tglbtnNewToggleButton_6);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("People", null, panel_3, null);
		panel_3.setLayout(null);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Northern path");
		rdbtnNewRadioButton.setEnabled(false);
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ml.currentCharSprite != null) ml.currentCharSprite.direction = Direction.NORTH;
			}
		});
		buttonGroup_1.add(rdbtnNewRadioButton);
		rdbtnNewRadioButton.setSelected(true);
		rdbtnNewRadioButton.setBounds(6, 113, 109, 23);
		panel_3.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("Southern path");
		rdbtnNewRadioButton_1.setEnabled(false);
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ml.currentCharSprite != null) ml.currentCharSprite.direction = Direction.SOUTH;
			}
		});
		buttonGroup_1.add(rdbtnNewRadioButton_1);
		rdbtnNewRadioButton_1.setBounds(6, 139, 109, 23);
		panel_3.add(rdbtnNewRadioButton_1);
		
		JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("Eastern path");
		rdbtnNewRadioButton_2.setEnabled(false);
		rdbtnNewRadioButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ml.currentCharSprite != null) ml.currentCharSprite.direction = Direction.EAST;
			}
		});
		buttonGroup_1.add(rdbtnNewRadioButton_2);
		rdbtnNewRadioButton_2.setBounds(6, 165, 109, 23);
		panel_3.add(rdbtnNewRadioButton_2);
		
		JRadioButton rdbtnNewRadioButton_3 = new JRadioButton("Western path");
		rdbtnNewRadioButton_3.setEnabled(false);
		rdbtnNewRadioButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ml.currentCharSprite != null) ml.currentCharSprite.direction = Direction.WEST;
			}
		});
		buttonGroup_1.add(rdbtnNewRadioButton_3);
		rdbtnNewRadioButton_3.setBounds(6, 191, 109, 23);
		panel_3.add(rdbtnNewRadioButton_3);
		
		final JRadioButton[] dirButtons = { rdbtnNewRadioButton, rdbtnNewRadioButton_1, rdbtnNewRadioButton_2, rdbtnNewRadioButton_3 };
		
		final JToggleButton tglbtnNewToggleButton_2 = new JToggleButton("");
		tglbtnNewToggleButton_2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				for (int i=0; i<dirButtons.length; i++) {
					if (tglbtnNewToggleButton_2.isSelected()) {
						dirButtons[i].setEnabled(true);
					} else {
						dirButtons[i].setEnabled(false);
					}
				}
			}
		});
		tglbtnNewToggleButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {				
				for (int i=0; i<dirButtons.length; i++) {
					if (dirButtons[i].isSelected()) {
						ml.currentSprite = null;
						ml.currentCharSprite = new CharacterSprite(Sprite.ENEMY.f, Direction.values()[i], -1, -1, 0, 0); // xLoc/yLoc unknown for now.
						break;
					}
				}
			}
		});
		buttonGroup.add(tglbtnNewToggleButton_2);
		tglbtnNewToggleButton_2.setIcon(new ImageIcon("./sprites/mission " + missionNumber + " sprites/enemy/enemy.png"));
		tglbtnNewToggleButton_2.setBounds(10, 11, 69, 73);
		panel_3.add(tglbtnNewToggleButton_2);
		
		JLabel lblSetEnemyPath = new JLabel("Set enemy path:");
		lblSetEnemyPath.setBounds(10, 92, 80, 14);
		panel_3.add(lblSetEnemyPath);
		
		JLabel lblClickSpotTo = new JLabel("Click spot to erase after clicking button");
		lblClickSpotTo.setBounds(10, 446, 190, 14);
		contentPane.add(lblClickSpotTo);
		
		JToggleButton tglbtnNewToggleButton_3 = new JToggleButton("Erase Objects");
		tglbtnNewToggleButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ml.currentSprite = null;
				ml.currentCharSprite = null;
			}
		});
		buttonGroup.add(tglbtnNewToggleButton_3);
		tglbtnNewToggleButton_3.setBounds(10, 412, 190, 23);
		contentPane.add(tglbtnNewToggleButton_3);
		
		JToggleButton tglbtnTestMap = new JToggleButton("Test Map");
		tglbtnTestMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean startTile = false;
				boolean endTile = false;
				
				for (int x=0; x<ml.tileSprites.length; x++) {
					for (int y=0; y<ml.tileSprites[x].length; y++) {
						if (ml.tileSprites[x][y] == TileSprite.DEV_TILE) {
							JOptionPane.showMessageDialog(null, "Cannot test, there are still development tiles!");
							return;
						}
						
						if (ml.tileSprites[x][y] == TileSprite.START_TILE) startTile = true;
						if (ml.tileSprites[x][y] == TileSprite.END_TILE) endTile = true;
					}
				}
				
				if (!startTile) {
					JOptionPane.showMessageDialog(null, "Cannot test, there isn't a start tile!");
					return;
				}
				
				if (!endTile) {
					JOptionPane.showMessageDialog(null, "Cannot test, there isn't a next level tile!");
					return;
				}
				
				
				new Thread(new LevelTester(ml.tileSprites, ml.charSprites)).start();
			}
		});
		tglbtnTestMap.setBounds(10, 471, 190, 23);
		contentPane.add(tglbtnTestMap);
	}
	
	private class AMLFilter extends javax.swing.filechooser.FileFilter {
		
		public boolean accept(File file) {
			String filename = file.getName();
			
			return filename.endsWith(".aml") || file.isDirectory();
		}

		public String getDescription() {
			return "SpyMaze Level File";
		}
		
	}
}
