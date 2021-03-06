package codeSupport;
/*
 * 2018-11-16 Changed to Queue for I/O
 */

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
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import ioSystem.listDevice.GenericPrinter;
import ioSystem.terminals.TTYZ80;
import ioSystem.terminals.VT100;
import utilities.hdNumberBox.HDNumberBox;

public class DriveDevices {

	private JFrame frmTemplate;
	private JButton btnRead1Byte;
	private JButton btnReadStatus;
	private JButton btnSend1Byte;
	private JButton btnSendAll;
	private JSplitPane splitPane1;

	private AppLogger log = AppLogger.getInstance();
	private JTextPane txtLog;
	private JPopupMenu popupLog;
	private AdapterLog logAdaper = new AdapterLog();
	private StyledDocument localScreen;

	private IOController ioc = IOController.getInstance();
	private ApplicationAdapter applicationAdapter = new ApplicationAdapter();

	private byte consoleData;
	private byte consoleStatus;

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
				} // try
			}// run
		});
	}// main

	/* Standard Stuff */

	private void doRead1Byte() {
		byte fromDevice = 0x00;
		try {
			fromDevice = ioc.byteToCPU(consoleData);
			addScreenByte(fromDevice);
		} catch (NullPointerException npe) {
			log.warn("No data to read from device");
		} // try

	}// doBtnOne

	private void doReadAll() {
		byte fromDevice = 0x00;
		while ((ioc.byteToCPU(consoleStatus) & Z80.ASCII_MASK) != 0) {
			fromDevice = ioc.byteToCPU(consoleData);
			addScreenByte(fromDevice);
		} // while
		addScreenString(System.lineSeparator());

	}// doReadAll

	private void doReadStatus() {
		Byte statusValue = ioc.byteToCPU(consoleStatus);
		if ((statusValue & 0x7F) != (byte) 0x00) {
			addScreenByte(ioc.byteToCPU(consoleData));
		} // if

		hdStatusReturned.setValue(statusValue & Z80.BYTE_MASK);

	}// doReadStatus

	private void doSend1Byte() {
		if ((ioc.byteToCPU(consoleStatus) & TTYZ80.STATUS_OUT_READY) == TTYZ80.STATUS_OUT_READY) {
			byte sourceByte = (byte) hdnByteOut.getValue();
			ioc.byteFromCPU(consoleData, sourceByte);

		} else {
			addScreenString(" Device not ready for output");
		}
	}// doSend1Byte

	private void doSendAll(byte ioAddress, boolean newLine) {
		if (txtLocalScreen.getText().length() == 0)
			;
		byte[] sourceBytes = txtLocalScreen.getText().length() != 0 ? txtLocalScreen.getText().getBytes()
				: txtSource.getText().getBytes();
		for (byte b : sourceBytes) {
			if ((ioc.byteToCPU(consoleStatus) & TTYZ80.STATUS_OUT_READY) == TTYZ80.STATUS_OUT_READY) {
				ioc.byteFromCPU(ioAddress, b);
			} // if
		} // for
		if (newLine) {
			ioc.byteFromCPU(ioAddress, (byte) 0x0A);
		}

	}// doSendAll

	private void doSendEscapeSequences() {
		sendByte((byte) 0x1B);
		sendByte((byte) 0x5B);
		EscapeSequence es = (EscapeSequence) cbSequence.getSelectedItem();
		byte[] values = es.sequence;
		for (byte value : values) {
			sendByte(value);
		} // for each
	}// doSendEscapeSequences

	private void sendByte(byte value) {
		if ((ioc.byteToCPU(consoleStatus) & TTYZ80.STATUS_OUT_READY) == TTYZ80.STATUS_OUT_READY) {
			ioc.byteFromCPU(consoleData, value);
		} else {
			addScreenString(" Device not ready for output");
		} // if
	}// sendByte

	// ---------------------------------------------------------

	private void doFileNew() {

	}// doFileNew

	private void doFileOpen() {

	}// doFileOpen

	private void doFileSave() {

	}// doFileSave

	private void doFileSaveAs() {

	}// doFileSaveAs

	private void doFilePrint() {

	}// doFilePrint

	private void doFileExit() {
		appClose();
		System.exit(0);
	}// doFileExit

	private void clearScreen() {
		try {
			localScreen.remove(0, localScreen.getLength());
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // try
	}// clearScreen

	private void addScreenString(String value) {
		try {
			localScreen.insertString(localScreen.getLength(), value, null);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // try
	}// addScreenString

	private void addScreenByte(byte value) {
		char c = (char) ((byte) value);
		addScreenString(Character.toString(c));
	}// addScreenByte

	private void setConsole() {

		btnConsole.setText(btnConsole.getText().equals(TTY) ? CRT : TTY);
		SetAddresses();

	}// setConsole

	private void SetAddresses() {
		if (btnConsole.getText().equals(TTY)) {
			consoleData = (byte) 0xEC;
			consoleStatus = (byte) 0xED;
		} else {
			consoleData = (byte) 0x01;
			consoleStatus = (byte) 0x02;
		} // if
	}//

	private void appClose() {
		Preferences myPrefs = Preferences.userNodeForPackage(DriveDevices.class).node(this.getClass().getSimpleName());
		Dimension dim = frmTemplate.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frmTemplate.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);
		myPrefs.putInt("Divider", splitPane1.getDividerLocation());
		myPrefs.putInt("Divider2", splitPane2.getDividerLocation());

		// myPrefs.putInt("AddressOut", hdnOutputAddress.getValue());
		// myPrefs.putInt("AddressIn", hdnIntputAddress.getValue());
		// myPrefs.putInt("AddressStatus", hdnStatusAddress.getValue());
		// myPrefs.putInt("ByteOut", hdnByteOut.getValue());
		myPrefs.put("Console", btnConsole.getText());
		myPrefs = null;
		ioc.close();
	}// appClose

	private void appInit() {
		Preferences myPrefs = Preferences.userNodeForPackage(DriveDevices.class).node(this.getClass().getSimpleName());
		frmTemplate.setSize(myPrefs.getInt("Width", 761), myPrefs.getInt("Height", 693));
		frmTemplate.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane1.setDividerLocation(myPrefs.getInt("Divider", 400));
		splitPane2.setDividerLocation(myPrefs.getInt("Divider2", 250));

		initNumberBoxes(myPrefs);
		btnConsole.setText(myPrefs.get("Console", TTY));
		myPrefs = null;

		txtLog.setText(EMPTY_STRING);

		log.setDoc(txtLog.getStyledDocument());
		log.info("Starting....");

		localScreen = txtLocalScreen.getStyledDocument();
		SetAddresses();
		ioc.addDevice(new VT100("VT100", VT100.IN, VT100.OUT, VT100.STATUS));
		cbSequence.setModel(loadSequenceModel());
	}// appInit

	private ComboBoxModel<EscapeSequence> loadSequenceModel() {
		ComboBoxModel<EscapeSequence> cbm = new DefaultComboBoxModel<EscapeSequence>();

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("5;6f".getBytes(), "Move cursor to screen location [5;6f]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("17;28H".getBytes(), "Move cursor to screen location 17,28 [17;28H]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("A".getBytes(), "Move cursor up 1 row [A]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("3A".getBytes(), "Move cursor up 3 rows [3A]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("B".getBytes(), "Move cursor down 1 row [B]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("13B".getBytes(), "Move cursor down 13 rows [13B]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("C".getBytes(), "Move cursor right 1 columnl [C]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("12C".getBytes(), "Move cursor right 12 columns[12C]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("D".getBytes(), "Move cursor left 1 column[D]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("5D".getBytes(), "Move cursor left 5 columns [5D]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("H".getBytes(), "Move cursor to upper left corner [H]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("f".getBytes(), "Move cursor to upper left corner [f]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence(";H".getBytes(), "Move cursor to upper left corner [;H]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence(";f".getBytes(), "Move cursor to upper left corner [;f]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("K".getBytes(), "Clear line from cursor right [K]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("0K".getBytes(), "Clear line from cursor right [0K]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("2K".getBytes(), "Clear entire line [2K]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("2J".getBytes(), "Clear entire screen [2J]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("J".getBytes(), "Clear screen from cursor down [J]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("0J".getBytes(), "Clear screen from cursor down [0J]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("1K".getBytes(), "Clear line from cursor left [1K]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("1J".getBytes(), "Clear screen from cursor up [1J]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("?3h".getBytes(), "Set number of columns to 132 [?3h]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("?3l".getBytes(), "Set number of columns to 80 [?3l]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("0q".getBytes(), "Turn off all four leds [0q]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("1q".getBytes(), "Turn on LED #1 [1q]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("2q".getBytes(), "Turn on LED #2 [2q]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("3q".getBytes(), "Turn on LED #3 [3q]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("4q".getBytes(), "Turn on LED #4 [4q]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm).addElement(
				new EscapeSequence("What Follows is not Implemented".getBytes(), "What Follows is not Implemented"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("m".getBytes(), "* Turn off character attributes [m]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("0m".getBytes(), "* Turn off character attributes [0m]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("1m".getBytes(), "* Turn bold mode on [1m]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("2m".getBytes(), "* Turn low intensity mode on [2m]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("3m".getBytes(), "* Turn italisize mode on [3m]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("4m".getBytes(), "* Turn underline mode on [4m]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("5m".getBytes(), "* Turn blinking mode on[5m]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("6m".getBytes(), "* Turn slow blinking mode on[6m]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("7m".getBytes(), "* Turn reverse video on [7m]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("8m".getBytes(), "* Turn invisible text mode on [8m]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm).addElement(
				new EscapeSequence("0;1;2;3;4;5;6;7;8;0m".getBytes(), "All attributes 0,1,2,3,4,5,6,7,8,0"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("?7h".getBytes(), "* Set auto-wrap mode [?7h]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("?7l".getBytes(), "* Reset auto-wrap mode [?7l]"));

		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("g".getBytes(), "* Clear a tab at the current column [g]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("3g".getBytes(), "* Clear all tabs [3g]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("?5h".getBytes(), "* Set reverse video on screen [?5h]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("?5l".getBytes(), "* Set normal video on screen [?5l]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("20h".getBytes(), "* Set new line mode [20h]"));
		((DefaultComboBoxModel<EscapeSequence>) cbm)
				.addElement(new EscapeSequence("20l".getBytes(), "* Set line feed mode [20l]"));

		return cbm;
	}// getSequenceModel()

	private void initNumberBoxes(Preferences myPrefs) {

		hdnByteOut.setValue(myPrefs.getInt("ByteOut", 0X00));
		hdnByteOut.setMaxValue(0xFF);
		hdnByteOut.setMinValue(0x00);

	}// initNumberBoxes

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
		gbl_panelForButtons.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelForButtons.rowHeights = new int[] { 0, 0, 0 };
		gbl_panelForButtons.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panelForButtons.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelForButtons.setLayout(gbl_panelForButtons);

		btnConsole = new JButton(TTY);
		btnConsole.setActionCommand(TTY_CRT);
		btnConsole.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btnTTY_CON = new GridBagConstraints();
		gbc_btnTTY_CON.insets = new Insets(0, 0, 5, 5);
		gbc_btnTTY_CON.gridx = 0;
		gbc_btnTTY_CON.gridy = 0;
		panelForButtons.add(btnConsole, gbc_btnTTY_CON);

		btnRead1Byte = new JButton("Read 1 Byte");
		btnRead1Byte.setActionCommand(BTN_READ_1_BYTE);
		btnRead1Byte.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnOne = new GridBagConstraints();
		gbc_btnOne.insets = new Insets(0, 0, 5, 5);
		gbc_btnOne.gridx = 1;
		gbc_btnOne.gridy = 0;
		panelForButtons.add(btnRead1Byte, gbc_btnOne);
		btnRead1Byte.setAlignmentX(Component.RIGHT_ALIGNMENT);
		btnRead1Byte.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnRead1Byte.addActionListener(applicationAdapter);
		btnRead1Byte.setMaximumSize(new Dimension(0, 0));
		btnRead1Byte.setPreferredSize(new Dimension(100, 20));

		JButton btnReadAll = new JButton("Read All Bytes");
		btnReadAll.setActionCommand(BTN_READ_ALL);
		btnReadAll.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btnReadAll = new GridBagConstraints();
		gbc_btnReadAll.insets = new Insets(0, 0, 5, 5);
		gbc_btnReadAll.gridx = 2;
		gbc_btnReadAll.gridy = 0;
		panelForButtons.add(btnReadAll, gbc_btnReadAll);

		btnSend1Byte = new JButton("Send 1 Byte");
		btnSend1Byte.setActionCommand(BTN_SEND_1_BYTE);
		btnSend1Byte.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnThree = new GridBagConstraints();
		gbc_btnThree.insets = new Insets(0, 0, 5, 5);
		gbc_btnThree.gridx = 4;
		gbc_btnThree.gridy = 0;
		panelForButtons.add(btnSend1Byte, gbc_btnThree);
		btnSend1Byte.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnSend1Byte.addActionListener(applicationAdapter);
		btnSend1Byte.setPreferredSize(new Dimension(100, 20));
		btnSend1Byte.setMaximumSize(new Dimension(0, 0));

		btnSendAll = new JButton("Send ALL");
		btnSendAll.setActionCommand(BTN_SEND_ALL);
		btnSendAll.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnFour = new GridBagConstraints();
		gbc_btnFour.insets = new Insets(0, 0, 5, 5);
		gbc_btnFour.gridx = 5;
		gbc_btnFour.gridy = 0;
		panelForButtons.add(btnSendAll, gbc_btnFour);
		btnSendAll.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnSendAll.addActionListener(applicationAdapter);
		btnSendAll.setPreferredSize(new Dimension(100, 20));
		btnSendAll.setMaximumSize(new Dimension(0, 0));

		JButton btnSendALLnl = new JButton("Send ALL NL");
		btnSendALLnl.setActionCommand(BTN_SEND_ALL_NL);
		btnSendALLnl.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btnSendALLnl = new GridBagConstraints();
		gbc_btnSendALLnl.insets = new Insets(0, 0, 5, 5);
		gbc_btnSendALLnl.gridx = 6;
		gbc_btnSendALLnl.gridy = 0;
		panelForButtons.add(btnSendALLnl, gbc_btnSendALLnl);

		btnReadStatus = new JButton("Read Status");
		btnReadStatus.setActionCommand(BTN_READ_STATUS);
		btnReadStatus.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnTwo = new GridBagConstraints();
		gbc_btnTwo.insets = new Insets(0, 0, 5, 0);
		gbc_btnTwo.gridx = 8;
		gbc_btnTwo.gridy = 0;
		panelForButtons.add(btnReadStatus, gbc_btnTwo);
		btnReadStatus.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		btnReadStatus.addActionListener(applicationAdapter);
		btnReadStatus.setPreferredSize(new Dimension(100, 20));
		btnReadStatus.setMaximumSize(new Dimension(0, 0));

		JLabel lblLst = new JLabel("LST: ");
		GridBagConstraints gbc_lblLst = new GridBagConstraints();
		gbc_lblLst.insets = new Insets(0, 0, 0, 5);
		gbc_lblLst.gridx = 0;
		gbc_lblLst.gridy = 1;
		panelForButtons.add(lblLst, gbc_lblLst);

		JButton btnPrintLine = new JButton("Print Line");
		btnPrintLine.setActionCommand(BTN_PRINT_LINE);
		btnPrintLine.addActionListener(applicationAdapter);
		GridBagConstraints gbc_btnPrintLine = new GridBagConstraints();
		gbc_btnPrintLine.insets = new Insets(0, 0, 0, 5);
		gbc_btnPrintLine.gridx = 1;
		gbc_btnPrintLine.gridy = 1;
		panelForButtons.add(btnPrintLine, gbc_btnPrintLine);

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
		gbl_panelLeft.columnWidths = new int[] { 0, 0 };
		gbl_panelLeft.rowHeights = new int[] { 0, 0 };
		gbl_panelLeft.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelLeft.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
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
		gbl_panelTop.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panelTop.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelTop.columnWeights = new double[] { 0.0, 1.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelTop.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelTop.setLayout(gbl_panelTop);

		Component verticalStrut_2 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_2 = new GridBagConstraints();
		gbc_verticalStrut_2.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_2.gridx = 0;
		gbc_verticalStrut_2.gridy = 0;
		panelTop.add(verticalStrut_2, gbc_verticalStrut_2);

		JLabel lblNewLabel_3 = new JLabel("Byte Out:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 1;
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
		gbc_hdnByteOut.gridx = 1;
		gbc_hdnByteOut.gridy = 1;
		panelTop.add(hdnByteOut, gbc_hdnByteOut);

		JLabel lblStatusAddress = new JLabel("Status");
		GridBagConstraints gbc_lblStatusAddress = new GridBagConstraints();
		gbc_lblStatusAddress.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblStatusAddress.insets = new Insets(0, 0, 5, 5);
		gbc_lblStatusAddress.gridx = 2;
		gbc_lblStatusAddress.gridy = 1;
		panelTop.add(lblStatusAddress, gbc_lblStatusAddress);

		hdStatusReturned = new HDNumberBox();
		hdStatusReturned.setPreferredSize(new Dimension(40, 25));
		hdStatusReturned.setDecimalDisplay(false);
		GridBagConstraints gbc_hdStatusReturned = new GridBagConstraints();
		gbc_hdStatusReturned.insets = new Insets(0, 0, 5, 5);
		gbc_hdStatusReturned.anchor = GridBagConstraints.NORTHWEST;
		gbc_hdStatusReturned.gridx = 3;
		gbc_hdStatusReturned.gridy = 1;
		panelTop.add(hdStatusReturned, gbc_hdStatusReturned);

		JLabel lblCharactersOut = new JLabel("Characters Out:");
		GridBagConstraints gbc_lblCharactersOut = new GridBagConstraints();
		gbc_lblCharactersOut.insets = new Insets(0, 0, 5, 5);
		gbc_lblCharactersOut.anchor = GridBagConstraints.EAST;
		gbc_lblCharactersOut.gridx = 0;
		gbc_lblCharactersOut.gridy = 3;
		panelTop.add(lblCharactersOut, gbc_lblCharactersOut);

		txtSource = new JTextField();
		txtSource.setText("01234567890123456789012345678901234567890123456789012345678901234567890123456789");
		GridBagConstraints gbc_txtSource = new GridBagConstraints();
		gbc_txtSource.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtSource.gridwidth = 3;
		gbc_txtSource.insets = new Insets(0, 0, 5, 5);
		gbc_txtSource.gridx = 1;
		gbc_txtSource.gridy = 3;
		panelTop.add(txtSource, gbc_txtSource);
		txtSource.setColumns(10);

		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 4;
		panelTop.add(verticalStrut, gbc_verticalStrut);

		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut_1.gridx = 0;
		gbc_verticalStrut_1.gridy = 5;
		panelTop.add(verticalStrut_1, gbc_verticalStrut_1);

		cbSequence = new JComboBox<EscapeSequence>();
		GridBagConstraints gbc_cbSequence = new GridBagConstraints();
		gbc_cbSequence.gridwidth = 3;
		gbc_cbSequence.insets = new Insets(0, 0, 0, 5);
		gbc_cbSequence.fill = GridBagConstraints.HORIZONTAL;
		gbc_cbSequence.gridx = 0;
		gbc_cbSequence.gridy = 6;
		panelTop.add(cbSequence, gbc_cbSequence);

		JButton btnSequences = new JButton("Send");
		btnSequences.addActionListener(applicationAdapter);
		btnSequences.setActionCommand(BTN_ESCAPE_SEQUENCES);
		GridBagConstraints gbc_btnSequences = new GridBagConstraints();
		gbc_btnSequences.anchor = GridBagConstraints.NORTH;
		gbc_btnSequences.insets = new Insets(0, 0, 0, 5);
		gbc_btnSequences.gridx = 3;
		gbc_btnSequences.gridy = 6;
		panelTop.add(btnSequences, gbc_btnSequences);
		//
		JPanel panelBottom = new JPanel();
		splitPane2.setRightComponent(panelBottom);
		GridBagLayout gbl_panelBottom = new GridBagLayout();
		gbl_panelBottom.columnWidths = new int[] { 0, 0 };
		gbl_panelBottom.rowHeights = new int[] { 0, 0 };
		gbl_panelBottom.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelBottom.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
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
		txtLocalScreen.addMouseListener(applicationAdapter);
		scrollPaneForScreen.setViewportView(txtLocalScreen);
		splitPane2.setDividerLocation(300);

		JPanel panelForLog = new JPanel();
		splitPane1.setRightComponent(panelForLog);
		GridBagLayout gbl_panelForLog = new GridBagLayout();
		gbl_panelForLog.columnWidths = new int[] { 0, 0 };
		gbl_panelForLog.rowHeights = new int[] { 0, 0 };
		gbl_panelForLog.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelForLog.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
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
		mnuFileNew.setEnabled(false);
		mnuFileNew.setActionCommand(MNU_FILE_NEW);
		mnuFileNew.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileNew);

		JMenuItem mnuFileOpen = new JMenuItem("Open...");
		mnuFileOpen.setEnabled(false);
		mnuFileOpen.setActionCommand(MNU_FILE_OPEN);
		mnuFileOpen.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileOpen);

		JSeparator separator99 = new JSeparator();
		mnuFile.add(separator99);

		JMenuItem mnuFileSave = new JMenuItem("Save...");
		mnuFileSave.setEnabled(false);
		mnuFileSave.setActionCommand(MNU_FILE_SAVE);
		mnuFileSave.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileSave);

		JMenuItem mnuFileSaveAs = new JMenuItem("Save As...");
		mnuFileSaveAs.setEnabled(false);
		mnuFileSaveAs.setActionCommand(MNU_FILE_SAVE_AS);
		mnuFileSaveAs.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileSaveAs);

		JSeparator separator_2 = new JSeparator();
		mnuFile.add(separator_2);

		JMenuItem mnuFilePrint = new JMenuItem("Print...");
		mnuFilePrint.setEnabled(false);
		mnuFilePrint.setActionCommand(MNU_FILE_PRINT);
		mnuFilePrint.addActionListener(applicationAdapter);
		mnuFile.add(mnuFilePrint);

		JSeparator separator_1 = new JSeparator();
		mnuFile.add(separator_1);

		JMenuItem mnuFileExit = new JMenuItem("Exit");
		mnuFileExit.setActionCommand(MNU_FILE_EXIT);
		mnuFileExit.addActionListener(applicationAdapter);
		mnuFile.add(mnuFileExit);

	}// initialize

	private static final String PUM_LOG_PRINT = "popupLogPrint";
	private static final String PUM_LOG_CLEAR = "popupLogClear";

	static final String EMPTY_STRING = "";
	private JSplitPane splitPane2;
	private HDNumberBox hdnByteOut;
	private JLabel lblForScreen;
	private JTextField txtSource;
	private JTextPane txtLocalScreen;
	private HDNumberBox hdStatusReturned;

	//////////////////////////////////////////////////////////////////////////

	class ApplicationAdapter implements ActionListener, MouseListener {

		/* ActionListener */

		@Override
		public void actionPerformed(ActionEvent actionEvent) {
			String cmd = actionEvent.getActionCommand();
			switch (cmd) {
			case TTY_CRT:
				setConsole();
				break;
			case BTN_READ_1_BYTE:
				doRead1Byte();
				break;
			case BTN_READ_ALL:
				doReadAll();
				break;
			case BTN_SEND_1_BYTE:
				doSend1Byte();
				break;
			case BTN_SEND_ALL_NL:
				doSendAll(consoleData, true);
				break;
			case BTN_SEND_ALL:
				doSendAll(consoleData, false);
				break;
			case BTN_READ_STATUS:
				doReadStatus();
				break;

			case BTN_PRINT_LINE:
				doSendAll(GenericPrinter.OUT, true);
				break;

			case BTN_ESCAPE_SEQUENCES:
				doSendEscapeSequences();

			case MNU_FILE_NEW:
				doFileNew();
				break;
			case MNU_FILE_OPEN:
				doFileOpen();
				break;
			case MNU_FILE_SAVE:
				doFileSave();
				break;
			case MNU_FILE_SAVE_AS:
				doFileSaveAs();
				break;
			case MNU_FILE_PRINT:
				doFilePrint();
				break;
			case MNU_FILE_EXIT:
				doFileExit();
				break;

			default:
			}// swing
		}// action Performed

		@Override
		public void mouseClicked(MouseEvent mouseEvent) {
			if (mouseEvent.getClickCount() > 1) {
				clearScreen();
			} // if
		}// mouseClicked

		@Override
		public void mouseEntered(MouseEvent e) {
		}// mouseEntered

		@Override
		public void mouseExited(MouseEvent e) {
		}// mouseExited

		@Override
		public void mousePressed(MouseEvent e) {
		}// mousePressed

		@Override
		public void mouseReleased(MouseEvent e) {
		}// mouseReleased

	}// ApplicationAdapter

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

	static final String TTY = "TTY";
	static final String CRT = "CRT";
	static final String TTY_CRT = "TTY_CRT";

	static final String MNU_FILE_NEW = "mnuFileNew";
	static final String MNU_FILE_OPEN = "mnuFileOpen";
	static final String MNU_FILE_SAVE = "mnuFileSave";
	static final String MNU_FILE_SAVE_AS = "mnuFileSaveAs";
	static final String MNU_FILE_PRINT = "mnuFilePrint";
	static final String MNU_FILE_EXIT = "mnuFileExit";

	static final String BTN_READ_1_BYTE = "btnRead1Byte";
	static final String BTN_READ_ALL = "btnReadAll";
	static final String BTN_SEND_1_BYTE = "btnSend1Byte";
	static final String BTN_SEND_ALL = "btnSendAll";
	static final String BTN_SEND_ALL_NL = "btnSendAllnl";
	static final String BTN_READ_STATUS = "btnReadStatus";
	static final String BTN_PRINT_LINE = "btnPrintLine";

	static final String BTN_ESCAPE_SEQUENCES = "btnEscapeSequences";
	private JButton btnConsole;
	private JComboBox<EscapeSequence> cbSequence;

}// class GUItemplate