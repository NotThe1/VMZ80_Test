package codeSupport;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

import ioSystem.IOController;
import ioSystem.ttyZ80.TTYZ80;
import utilities.hdNumberBox.HDNumberBox;


public class DriveDevices {

	private JFrame frmTemplate;
	private JButton btnOne;
	private JButton btnTwo;
	private JButton btnThree;
	private JButton btnFour;
	private JSplitPane splitPane1;

	private AppLogger log = AppLogger.getInstance();
	private JTextPane txtLog;
	private JPopupMenu popupLog; 
	private AdapterLog logAdaper = new AdapterLog();
	private StyledDocument localScreen; 

private IOController ioc = IOController.getInstance();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DriveDevices window = new DriveDevices();
					window.frmTemplate.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}//try
			}//run
		});
	}// main

	/* Standard Stuff */
	
	private void doBtnOne(){
		byte fromDevice =0x00;
		try {
			fromDevice = ioc.byteFromDevice(TTYZ80.IN);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		addScreenByte(fromDevice);
	}//doBtnOne
	
	private void doBtnTwo(){
//		byte b;
//		while ( ioc.byteFromDevice(TTYZ80.STATUS) != 0x01){
//			b= ioc.byteFromDevice(TTYZ80.IN);
//			ioc.byteToDevice(TTYZ80.OUT, b);
//		}//while
		
	}//doBtnTwo
	
	private void doBtnThree(){
		byte sourceByte = (byte) hdnByteOut.getValue();
		ioc.byteToDevice(TTYZ80.OUT, sourceByte);
	}//doBtnThree
	
	private void doBtnFour(){
		byte[] sourceBytes = txtSource.getText().getBytes();
		for( byte b:sourceBytes) {
			ioc.byteToDevice(TTYZ80.OUT, b);
		}//

	}//doBtnFour
	
	//---------------------------------------------------------
	
	private void doFileNew(){
		
	}//doFileNew
	private void doFileOpen(){
		
	}//doFileOpen
	private void doFileSave(){
		
	}//doFileSave
	private void doFileSaveAs(){
		
	}//doFileSaveAs
	private void doFilePrint(){
		
	}//doFilePrint
	private void doFileExit(){
		appClose();
		System.exit(0);
	}//doFileExit
	
	private void clearScreen() {
		try {
			localScreen.remove(0, localScreen.getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//try
	}//clearScreen
	
	private void addScreenString(String value) {
		try {
			localScreen.insertString(localScreen.getLength(), value, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//try
	}//addScreenString
	
	private void addScreenByte(byte value) {
		char c = (char) ((byte) value);
		addScreenString(Character.toString(c));
	}//addScreenByte


	private void appClose() {
		Preferences myPrefs =  Preferences.userNodeForPackage(DriveDevices.class).node(this.getClass().getSimpleName());
		Dimension dim = frmTemplate.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frmTemplate.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);
		myPrefs.putInt("Divider", splitPane1.getDividerLocation());
		myPrefs.putInt("Divider2", splitPane2.getDividerLocation());
		
		myPrefs.putInt("AddressOut", hdnOutputAddress.getValue());
		myPrefs.putInt("AddressIn", hdnIntputAddress.getValue());
		myPrefs.putInt("AddressStatus", hdnStatusAddress.getValue());
		myPrefs.putInt("ByteOut", hdnByteOut.getValue());
		
		myPrefs = null;
		ioc.close();
	}//appClose

	private void appInit() {
		Preferences myPrefs =  Preferences.userNodeForPackage(DriveDevices.class).node(this.getClass().getSimpleName());
		frmTemplate.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
		frmTemplate.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane1.setDividerLocation(myPrefs.getInt("Divider", 400));
		splitPane2.setDividerLocation(myPrefs.getInt("Divider2", 250));
		
		initNumberBoxes( myPrefs);
		
		myPrefs = null;
		
		txtLog.setText(EMPTY_STRING);

		log.setDoc(txtLog.getStyledDocument());
		log.addInfo("Starting....");
		
		localScreen = txtLocalScreen.getStyledDocument();
	}// appInit
	
	private void initNumberBoxes(Preferences myPrefs) {
		hdnOutputAddress.setValue(myPrefs.getInt("AddressOut", 0XEC));
		hdnOutputAddress.setMaxValue(0xFF);
		hdnOutputAddress.setMinValue(0x00);
		
		hdnIntputAddress.setValue(myPrefs.getInt("AddressIn", 0XEC));
		hdnIntputAddress.setMaxValue(0xFF);
		hdnIntputAddress.setMinValue(0x00);

		
		hdnStatusAddress.setValue(myPrefs.getInt("AddressStatus", 0XED));
		hdnStatusAddress.setMaxValue(0xFF);
		hdnStatusAddress.setMinValue(0x00);
		
		hdnByteOut.setValue(myPrefs.getInt("ByteOut", 0X00));
		hdnByteOut.setMaxValue(0xFF);
		hdnByteOut.setMinValue(0x00);

	}//initNumberBoxes

	public DriveDevices() {
		initialize();
		appInit();
	}// Constructor
	
	private void doLogClear() {
		log.clear();
	}// doLogClear

	private void doLogPrint() {

		Font originalFont = txtLog.getFont();
		try {
			// textPane.setFont(new Font("Courier New", Font.PLAIN, 8));
			txtLog.setFont(originalFont.deriveFont(8.0f));
			MessageFormat header = new MessageFormat("Identic Log");
			MessageFormat footer = new MessageFormat(new Date().toString() + "           Page - {0}");
			txtLog.print(header, footer);
			// textPane.setFont(new Font("Courier New", Font.PLAIN, 14));
			txtLog.setFont(originalFont);
		} catch (PrinterException e) {
			e.printStackTrace();
		} // try

	}// doLogPrint



	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTemplate = new JFrame();
		frmTemplate.setTitle("Drive Devices    0.0");
		frmTemplate.setBounds(100, 100, 450, 300);
		frmTemplate.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTemplate.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				appClose();
			}
		});
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 25, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, Double.MIN_VALUE };
		frmTemplate.getContentPane().setLayout(gridBagLayout);
		
		JPanel panelForButtons = new JPanel();
		GridBagConstraints gbc_panelForButtons = new GridBagConstraints();
		gbc_panelForButtons.anchor = GridBagConstraints.NORTH;
		gbc_panelForButtons.insets = new Insets(0, 0, 5, 0);
		gbc_panelForButtons.fill = GridBagConstraints.HORIZONTAL;
		gbc_panelForButtons.gridx = 0;
		gbc_panelForButtons.gridy = 0;
		frmTemplate.getContentPane().add(panelForButtons, gbc_panelForButtons);
		GridBagLayout gbl_panelForButtons = new GridBagLayout();
		gbl_panelForButtons.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelForButtons.rowHeights = new int[]{0, 0};
		gbl_panelForButtons.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panelForButtons.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelForButtons.setLayout(gbl_panelForButtons);
		
		btnOne = new JButton("Get 1 character");
		btnOne.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnOne = new GridBagConstraints();
		gbc_btnOne.insets = new Insets(0, 0, 0, 5);
		gbc_btnOne.gridx = 0;
		gbc_btnOne.gridy = 0;
		panelForButtons.add(btnOne, gbc_btnOne);
		btnOne.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnOne.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doBtnOne();
			}
		});
		btnOne.setMaximumSize(new Dimension(0, 0));
		btnOne.setPreferredSize(new Dimension(100, 20));
		
		btnTwo = new JButton("Full Duplex");
		btnTwo.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnTwo = new GridBagConstraints();
		gbc_btnTwo.insets = new Insets(0, 0, 0, 5);
		gbc_btnTwo.gridx = 1;
		gbc_btnTwo.gridy = 0;
		panelForButtons.add(btnTwo, gbc_btnTwo);
		btnTwo.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnTwo();
			}
		});
		btnTwo.setPreferredSize(new Dimension(100, 20));
		btnTwo.setMaximumSize(new Dimension(0, 0));
		
		btnThree = new JButton("Send Byte");
		btnThree.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnThree = new GridBagConstraints();
		gbc_btnThree.insets = new Insets(0, 0, 0, 5);
		gbc_btnThree.gridx = 3;
		gbc_btnThree.gridy = 0;
		panelForButtons.add(btnThree, gbc_btnThree);
		btnThree.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnThree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnThree();
			}
		});
		btnThree.setPreferredSize(new Dimension(100, 20));
		btnThree.setMaximumSize(new Dimension(0, 0));
		
		btnFour = new JButton("Send All Chars");
		btnFour.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnFour = new GridBagConstraints();
		gbc_btnFour.gridx = 4;
		gbc_btnFour.gridy = 0;
		panelForButtons.add(btnFour, gbc_btnFour);
		btnFour.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnFour.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnFour();
			}
		});
		btnFour.setPreferredSize(new Dimension(100, 20));
		btnFour.setMaximumSize(new Dimension(0, 0));
		
		splitPane1 = new JSplitPane();
		GridBagConstraints gbc_splitPane1 = new GridBagConstraints();
		gbc_splitPane1.insets = new Insets(0, 0, 5, 0);
		gbc_splitPane1.fill = GridBagConstraints.BOTH;
		gbc_splitPane1.gridx = 0;
		gbc_splitPane1.gridy = 1;
		frmTemplate.getContentPane().add(splitPane1, gbc_splitPane1);
		
		JPanel panelLeft = new JPanel();
		splitPane1.setLeftComponent(panelLeft);
		GridBagLayout gbl_panelLeft = new GridBagLayout();
		gbl_panelLeft.columnWidths = new int[]{0, 0};
		gbl_panelLeft.rowHeights = new int[]{0, 0};
		gbl_panelLeft.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelLeft.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelLeft.setLayout(gbl_panelLeft);
		
		splitPane2 = new JSplitPane();
		splitPane2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		GridBagConstraints gbc_splitPane2 = new GridBagConstraints();
		gbc_splitPane2.fill = GridBagConstraints.BOTH;
		gbc_splitPane2.gridx = 0;
		gbc_splitPane2.gridy = 0;
		panelLeft.add(splitPane2, gbc_splitPane2);
		
		JPanel panelTop = new JPanel();
		splitPane2.setLeftComponent(panelTop);
		GridBagLayout gbl_panelTop = new GridBagLayout();
		gbl_panelTop.columnWidths = new int[]{0, 0, 0, 0, 0, 0};
		gbl_panelTop.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panelTop.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panelTop.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panelTop.setLayout(gbl_panelTop);
		
		Component verticalStrut_2 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_2 = new GridBagConstraints();
		gbc_verticalStrut_2.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_2.gridx = 0;
		gbc_verticalStrut_2.gridy = 0;
		panelTop.add(verticalStrut_2, gbc_verticalStrut_2);
		
		JLabel lblNewLabel_1 = new JLabel("Output Address");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panelTop.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		hdnOutputAddress = new HDNumberBox ();
		hdnOutputAddress.setPreferredSize(new Dimension(40, 25));
		hdnOutputAddress.setDecimalDisplay(false);
		GridBagConstraints gbc_hdnOutputAddress = new GridBagConstraints();
		gbc_hdnOutputAddress.anchor = GridBagConstraints.WEST;
		gbc_hdnOutputAddress.insets = new Insets(0, 0, 5, 5);
		gbc_hdnOutputAddress.gridx = 1;
		gbc_hdnOutputAddress.gridy = 1;
		panelTop.add(hdnOutputAddress, gbc_hdnOutputAddress);
		
		JLabel lblNewLabel_3 = new JLabel("Byte Out:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 2;
		panelTop.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		hdnByteOut = new HDNumberBox();
		hdnByteOut.setDecimalDisplay(false);
		hdnByteOut.setMaximumSize(new Dimension(40, 25));
		hdnByteOut.setMinimumSize(new Dimension(40, 25));
		hdnByteOut.setPreferredSize(new Dimension(40, 25));
		GridBagConstraints gbc_hdnByteOut = new GridBagConstraints();
		gbc_hdnByteOut.anchor = GridBagConstraints.WEST;
		gbc_hdnByteOut.insets = new Insets(0, 0, 5, 5);
		gbc_hdnByteOut.fill = GridBagConstraints.VERTICAL;
		gbc_hdnByteOut.gridx = 2;
		gbc_hdnByteOut.gridy = 2;
		panelTop.add(hdnByteOut, gbc_hdnByteOut);
//		GridBagLayout gbl_hdnByteOut = new GridBagLayout();
//		gbl_hdnByteOut.columnWidths = new int[]{0};
//		gbl_hdnByteOut.rowHeights = new int[]{0};
//		gbl_hdnByteOut.columnWeights = new double[]{Double.MIN_VALUE};
//		gbl_hdnByteOut.rowWeights = new double[]{Double.MIN_VALUE};
//		hdnByteOut.setLayout(gbl_hdnByteOut);
		
//		GridBagLayout gbl_hdnOutputAddress = new GridBagLayout();
//		gbl_hdnOutputAddress.columnWidths = new int[]{0};
//		gbl_hdnOutputAddress.rowHeights = new int[]{0};
//		gbl_hdnOutputAddress.columnWeights = new double[]{Double.MIN_VALUE};
//		gbl_hdnOutputAddress.rowWeights = new double[]{Double.MIN_VALUE};
//		hdnOutputAddress.setLayout(gbl_hdnOutputAddress);
		
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 4;
		panelTop.add(verticalStrut, gbc_verticalStrut);
		
		JLabel lblCharactersOut = new JLabel("Characters Out:");
		GridBagConstraints gbc_lblCharactersOut = new GridBagConstraints();
		gbc_lblCharactersOut.insets = new Insets(0, 0, 5, 5);
		gbc_lblCharactersOut.anchor = GridBagConstraints.EAST;
		gbc_lblCharactersOut.gridx = 1;
		gbc_lblCharactersOut.gridy = 4;
		panelTop.add(lblCharactersOut, gbc_lblCharactersOut);
		
		txtSource = new JTextField();
		txtSource.setText("ABCDEFGHIJKLMNOPQRSTUV");
		GridBagConstraints gbc_txtSource = new GridBagConstraints();
		gbc_txtSource.anchor = GridBagConstraints.WEST;
		gbc_txtSource.gridwidth = 3;
		gbc_txtSource.insets = new Insets(0, 0, 5, 0);
		gbc_txtSource.gridx = 2;
		gbc_txtSource.gridy = 4;
		panelTop.add(txtSource, gbc_txtSource);
		txtSource.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("InputAddress");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 5;
		panelTop.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		hdnIntputAddress = new HDNumberBox();
		hdnIntputAddress.setPreferredSize(new Dimension(40, 25));
		hdnIntputAddress.setDecimalDisplay(false);

		GridBagConstraints gbc_hdnIntputAddress = new GridBagConstraints();
		gbc_hdnIntputAddress.anchor = GridBagConstraints.NORTHWEST;
		gbc_hdnIntputAddress.insets = new Insets(0, 0, 5, 5);
		gbc_hdnIntputAddress.gridx = 1;
		gbc_hdnIntputAddress.gridy = 5;
		panelTop.add(hdnIntputAddress, gbc_hdnIntputAddress);
//		GridBagLayout gbl_hdnIntputAddress = new GridBagLayout();
//		gbl_hdnIntputAddress.columnWidths = new int[]{0};
//		gbl_hdnIntputAddress.rowHeights = new int[]{0};
//		gbl_hdnIntputAddress.columnWeights = new double[]{Double.MIN_VALUE};
//		gbl_hdnIntputAddress.rowWeights = new double[]{Double.MIN_VALUE};
//		hdnIntputAddress.setLayout(gbl_hdnIntputAddress);
		
		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_1.gridx = 0;
		gbc_verticalStrut_1.gridy = 6;
		panelTop.add(verticalStrut_1, gbc_verticalStrut_1);
		
		JLabel lblStatusAddress = new JLabel("Status Address");
		GridBagConstraints gbc_lblStatusAddress = new GridBagConstraints();
		gbc_lblStatusAddress.insets = new Insets(0, 0, 0, 5);
		gbc_lblStatusAddress.gridx = 0;
		gbc_lblStatusAddress.gridy = 7;
		panelTop.add(lblStatusAddress, gbc_lblStatusAddress);
		
		hdnStatusAddress = new HDNumberBox();
		hdnStatusAddress.setPreferredSize(new Dimension(40, 25));
		hdnStatusAddress.setDecimalDisplay(false);
		GridBagConstraints gbc_hdnStatusAddress = new GridBagConstraints();
		gbc_hdnStatusAddress.insets = new Insets(0, 0, 0, 5);
		gbc_hdnStatusAddress.anchor = GridBagConstraints.NORTHWEST;
		gbc_hdnStatusAddress.gridx = 1;
		gbc_hdnStatusAddress.gridy = 7;
		panelTop.add(hdnStatusAddress, gbc_hdnStatusAddress);
//		GridBagLayout gbl_hdnStatusAddress = new GridBagLayout();
//		gbl_hdnStatusAddress.columnWidths = new int[]{0};
//		gbl_hdnStatusAddress.rowHeights = new int[]{0};
//		gbl_hdnStatusAddress.columnWeights = new double[]{Double.MIN_VALUE};
//		gbl_hdnStatusAddress.rowWeights = new double[]{Double.MIN_VALUE};
//		hdnStatusAddress.setLayout(gbl_hdnStatusAddress);
//		
		JPanel panelBottom = new JPanel();
		splitPane2.setRightComponent(panelBottom);
		GridBagLayout gbl_panelBottom = new GridBagLayout();
		gbl_panelBottom.columnWidths = new int[]{0, 0};
		gbl_panelBottom.rowHeights = new int[]{0, 0};
		gbl_panelBottom.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelBottom.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelBottom.setLayout(gbl_panelBottom);
		
		JScrollPane scrollPaneForScreen = new JScrollPane();
		GridBagConstraints gbc_scrollPaneForScreen = new GridBagConstraints();
		gbc_scrollPaneForScreen.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneForScreen.gridx = 0;
		gbc_scrollPaneForScreen.gridy = 0;
		panelBottom.add(scrollPaneForScreen, gbc_scrollPaneForScreen);
		
		lblForScreen = new JLabel("New label");
		lblForScreen.setHorizontalAlignment(SwingConstants.CENTER);
		lblForScreen.setFont(new Font("Tahoma", Font.PLAIN, 15));
		scrollPaneForScreen.setColumnHeaderView(lblForScreen);
		
		txtLocalScreen = new JTextPane();
		txtLocalScreen.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() >1) {
					clearScreen();
				}//if
			}//mouseClicked
		});
		scrollPaneForScreen.setViewportView(txtLocalScreen);
		splitPane2.setDividerLocation(300);
		
		JPanel panelForLog = new JPanel();
		splitPane1.setRightComponent(panelForLog);
		GridBagLayout gbl_panelForLog = new GridBagLayout();
		gbl_panelForLog.columnWidths = new int[]{0, 0};
		gbl_panelForLog.rowHeights = new int[]{0, 0};
		gbl_panelForLog.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelForLog.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelForLog.setLayout(gbl_panelForLog);
		
		JScrollPane scrollPaneForLog = new JScrollPane();
		GridBagConstraints gbc_scrollPaneForLog = new GridBagConstraints();
		gbc_scrollPaneForLog.fill = GridBagConstraints.BOTH;
		gbc_scrollPaneForLog.gridx = 0;
		gbc_scrollPaneForLog.gridy = 0;
		panelForLog.add(scrollPaneForLog, gbc_scrollPaneForLog);
		
		txtLog = new JTextPane();
		scrollPaneForLog.setViewportView(txtLog);
		
		 popupLog = new JPopupMenu();
		addPopup(txtLog, popupLog);

		JMenuItem popupLogClear = new JMenuItem("Clear Log");
		popupLogClear.setName(PUM_LOG_CLEAR);
		popupLogClear.addActionListener(logAdaper);
		popupLog.add(popupLogClear);

		JSeparator separator = new JSeparator();
		popupLog.add(separator);

		JMenuItem popupLogPrint = new JMenuItem("Print Log");
		popupLogPrint.setName(PUM_LOG_PRINT);
		popupLogPrint.addActionListener(logAdaper);
		popupLog.add(popupLogPrint);


		
		JLabel lblNewLabel = new JLabel("Application Log");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(new Color(0, 128, 0));
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 14));
		scrollPaneForLog.setColumnHeaderView(lblNewLabel);
		splitPane1.setDividerLocation(400);
		
		JPanel panelStatus = new JPanel();
		panelStatus.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_panelStatus = new GridBagConstraints();
		gbc_panelStatus.fill = GridBagConstraints.BOTH;
		gbc_panelStatus.gridx = 0;
		gbc_panelStatus.gridy = 2;
		frmTemplate.getContentPane().add(panelStatus, gbc_panelStatus);

		JMenuBar menuBar = new JMenuBar();
		frmTemplate.setJMenuBar(menuBar);
		
		JMenu mnuFile = new JMenu("File");
		menuBar.add(mnuFile);
		
		JMenuItem mnuFileNew = new JMenuItem("New");
		mnuFileNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileNew();
			}
		});
		mnuFile.add(mnuFileNew);
		
		JMenuItem mnuFileOpen = new JMenuItem("Open...");
		mnuFileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileOpen();
			}
		});
		mnuFile.add(mnuFileOpen);
		
		JSeparator separator99 = new JSeparator();
		mnuFile.add(separator99);
		
		JMenuItem mnuFileSave = new JMenuItem("Save...");
		mnuFileSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileSave();
			}
		});
		mnuFile.add(mnuFileSave);
		
		JMenuItem mnuFileSaveAs = new JMenuItem("Save As...");
		mnuFileSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileSaveAs();
			}
		});
		mnuFile.add(mnuFileSaveAs);
		
		JSeparator separator_2 = new JSeparator();
		mnuFile.add(separator_2);
		
		JMenuItem mnuFilePrint = new JMenuItem("Print...");
		mnuFilePrint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFilePrint();
			}
		});
		mnuFile.add(mnuFilePrint);
		
		
		JSeparator separator_1 = new JSeparator();
		mnuFile.add(separator_1);
		
		JMenuItem mnuFileExit = new JMenuItem("Exit");
		mnuFileExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doFileExit();
			}
		});
		mnuFile.add(mnuFileExit);

		

	}// initialize
	private static final String PUM_LOG_PRINT = "popupLogPrint";
	private static final String PUM_LOG_CLEAR = "popupLogClear";

	static final String EMPTY_STRING = "";
	private JSplitPane splitPane2;
	private HDNumberBox hdnOutputAddress;
	private HDNumberBox hdnIntputAddress;
	private HDNumberBox hdnStatusAddress;
	private HDNumberBox hdnByteOut;
	private JLabel lblForScreen;
	private JTextField txtSource;
	private JTextPane txtLocalScreen;
	
	//////////////////////////////////////////////////////////////////////////
	
	class AdapterLog implements ActionListener {// , ListSelectionListener
		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String name = ((Component) actionEvent.getSource()).getName();
			switch (name) {
			case PUM_LOG_PRINT:
				doLogPrint();
				break;
			case PUM_LOG_CLEAR:
				doLogClear();
				break;
			}// switch
		}// actionPerformed
	}// class AdapterAction

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				} // if popup Trigger
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}// addPopup

}// class GUItemplate