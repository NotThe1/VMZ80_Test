package misc;

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
import java.text.MessageFormat;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import codeSupport.AppLogger;
import codeSupport.Z80.Register;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import hardware.View.V_PrimaryRegisters;
import hardware.View.ViewCCR;

public class ViewTests {

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

	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewTests window = new ViewTests();
					window.frmTemplate.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} // try
			}// run
		});
	}// main

	/* Standard Stuff */

	private void doBtnOne() {

	}// doBtnOne

	private void doBtnTwo() {

	}// doBtnTwo

	private void doBtnThree() {

	}// doBtnThree

	private void doBtnFour() {

	}// doBtnFour

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

	private void setRegisterDisplay(boolean mainRegs) {

	}

	private void appClose() {
		Preferences myPrefs = Preferences.userNodeForPackage(ViewTests.class).node(this.getClass().getSimpleName());
		Dimension dim = frmTemplate.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frmTemplate.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);
		myPrefs.putInt("Divider", splitPane1.getDividerLocation());
		myPrefs = null;
	}// appClose

	private void appInit() {
		Preferences myPrefs = Preferences.userNodeForPackage(ViewTests.class).node(this.getClass().getSimpleName());
		frmTemplate.setSize(587, 539);
		frmTemplate.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane1.setDividerLocation(myPrefs.getInt("Divider", 250));
		myPrefs = null;

		txtLog.setText(EMPTY_STRING);

		log.setDoc(txtLog.getStyledDocument());
		log.info("Starting....");
		
		wrs.setReg(Register.A, (byte)0X01);
		wrs.setReg(Register.B, (byte)0X02);
		wrs.setReg(Register.C, (byte)0X03);
		wrs.setReg(Register.D, (byte)0X04);
		wrs.setReg(Register.E, (byte)0X05);
		wrs.setReg(Register.H, (byte)0X06);
		wrs.setReg(Register.L, (byte)0X07);
		wrs.setReg(Register.F, (byte)0X01);

		wrs.setReg(Register.Ap, (byte)0X10);
		wrs.setReg(Register.Bp, (byte)0X20);
		wrs.setReg(Register.Cp, (byte)0X30);
		wrs.setReg(Register.Dp, (byte)0X40);
		wrs.setReg(Register.Ep, (byte)0X50);
		wrs.setReg(Register.Hp, (byte)0X60);
		wrs.setReg(Register.Lp, (byte)0X70);
		wrs.setReg(Register.Fp, (byte)0XD7);
		Thread t_PrimayRegisters = new Thread(panel_V_PrimayRegisters);
		t_PrimayRegisters.run();

	}// appInit

	public ViewTests() {
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
		frmTemplate.setTitle("View Tests    1.0");
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
		gbl_panelForButtons.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panelForButtons.rowHeights = new int[] { 0, 0 };
		gbl_panelForButtons.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelForButtons.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panelForButtons.setLayout(gbl_panelForButtons);

		btnOne = new JButton("Button 1");
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

		btnTwo = new JButton("Button 2");
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

		btnThree = new JButton("Button 3");
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

		btnFour = new JButton("Button 4");
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
		gbl_panelLeft.columnWidths = new int[] { 0, 0 };
		gbl_panelLeft.rowHeights = new int[] { 0, 0 };
		gbl_panelLeft.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelLeft.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelLeft.setLayout(gbl_panelLeft);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		GridBagConstraints gbc_tabbedPane = new GridBagConstraints();
		gbc_tabbedPane.fill = GridBagConstraints.BOTH;
		gbc_tabbedPane.gridx = 0;
		gbc_tabbedPane.gridy = 0;
		panelLeft.add(tabbedPane, gbc_tabbedPane);

		JPanel panelPrimaryRegisters = new JPanel();
		panelPrimaryRegisters.setBackground(new Color(255, 0, 0));
		tabbedPane.addTab("Primary Regs", null, panelPrimaryRegisters, null);
		GridBagLayout gbl_panelPrimaryRegisters = new GridBagLayout();
		gbl_panelPrimaryRegisters.columnWidths = new int[] { 200, 0 };
		gbl_panelPrimaryRegisters.rowHeights = new int[] { 0, 0, 0 };
		gbl_panelPrimaryRegisters.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_panelPrimaryRegisters.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		panelPrimaryRegisters.setLayout(gbl_panelPrimaryRegisters);

		panel_V_PrimayRegisters = new V_PrimaryRegisters();
		panel_V_PrimayRegisters.setMaximumSize(new Dimension(0, 0));
		GridBagConstraints gbc_panel_V_PrimayRegisters = new GridBagConstraints();
		gbc_panel_V_PrimayRegisters.anchor = GridBagConstraints.WEST;
		gbc_panel_V_PrimayRegisters.fill = GridBagConstraints.VERTICAL;
		gbc_panel_V_PrimayRegisters.gridx = 0;
		gbc_panel_V_PrimayRegisters.gridy = 1;
		panelPrimaryRegisters.add(panel_V_PrimayRegisters, gbc_panel_V_PrimayRegisters);
		GridBagLayout gbl_panel_V_PrimayRegisters = new GridBagLayout();
		gbl_panel_V_PrimayRegisters.columnWidths = new int[]{0};
		gbl_panel_V_PrimayRegisters.rowHeights = new int[]{0};
		gbl_panel_V_PrimayRegisters.columnWeights = new double[]{Double.MIN_VALUE};
		gbl_panel_V_PrimayRegisters.rowWeights = new double[]{Double.MIN_VALUE};
		panel_V_PrimayRegisters.setLayout(gbl_panel_V_PrimayRegisters);

		JPanel panelCCR = new JPanel();
		tabbedPane.addTab("CCR", null, panelCCR, null);
		GridBagLayout gbl_panelCCR = new GridBagLayout();
		gbl_panelCCR.columnWidths = new int[] { 0, 214, 0 };
		gbl_panelCCR.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelCCR.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelCCR.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panelCCR.setLayout(gbl_panelCCR);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 0;
		gbc_horizontalStrut.gridy = 0;
		panelCCR.add(horizontalStrut, gbc_horizontalStrut);

		ViewCCR panelViewCCR = new ViewCCR();
		panelViewCCR.setSize(new Dimension(0, 0));
		panelViewCCR.setMinimumSize(new Dimension(190, 54));
		GridBagConstraints gbc_panelViewCCR = new GridBagConstraints();
		gbc_panelViewCCR.anchor = GridBagConstraints.WEST;
		gbc_panelViewCCR.insets = new Insets(0, 0, 5, 0);
		gbc_panelViewCCR.fill = GridBagConstraints.VERTICAL;
		gbc_panelViewCCR.gridx = 1;
		gbc_panelViewCCR.gridy = 1;
		panelCCR.add(panelViewCCR, gbc_panelViewCCR);
		GridBagLayout gbl_panelViewCCR = new GridBagLayout();
		gbl_panelViewCCR.columnWidths = new int[] { 0 };
		gbl_panelViewCCR.rowHeights = new int[] { 0 };
		gbl_panelViewCCR.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_panelViewCCR.rowWeights = new double[] { Double.MIN_VALUE };
		panelViewCCR.setLayout(gbl_panelViewCCR);

		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 2;
		panelCCR.add(verticalStrut, gbc_verticalStrut);

		cbSign = new JCheckBox("Sign");
		GridBagConstraints gbc_cbSign = new GridBagConstraints();
		gbc_cbSign.insets = new Insets(0, 0, 5, 0);
		gbc_cbSign.anchor = GridBagConstraints.WEST;
		gbc_cbSign.gridx = 1;
		gbc_cbSign.gridy = 3;
		panelCCR.add(cbSign, gbc_cbSign);

		cbZero = new JCheckBox("Zero");
		GridBagConstraints gbc_cbZero = new GridBagConstraints();
		gbc_cbZero.anchor = GridBagConstraints.WEST;
		gbc_cbZero.insets = new Insets(0, 0, 5, 0);
		gbc_cbZero.gridx = 1;
		gbc_cbZero.gridy = 4;
		panelCCR.add(cbZero, gbc_cbZero);

		cbHalfCarry = new JCheckBox("Half Carry");
		GridBagConstraints gbc_cbHalfCarry = new GridBagConstraints();
		gbc_cbHalfCarry.anchor = GridBagConstraints.WEST;
		gbc_cbHalfCarry.insets = new Insets(0, 0, 5, 0);
		gbc_cbHalfCarry.gridx = 1;
		gbc_cbHalfCarry.gridy = 5;
		panelCCR.add(cbHalfCarry, gbc_cbHalfCarry);

		cbParity = new JCheckBox("Parity");
		GridBagConstraints gbc_cbParity = new GridBagConstraints();
		gbc_cbParity.anchor = GridBagConstraints.WEST;
		gbc_cbParity.insets = new Insets(0, 0, 5, 0);
		gbc_cbParity.gridx = 1;
		gbc_cbParity.gridy = 6;
		panelCCR.add(cbParity, gbc_cbParity);

		cbNFlag = new JCheckBox("N Flag");
		GridBagConstraints gbc_cbNFlag = new GridBagConstraints();
		gbc_cbNFlag.anchor = GridBagConstraints.WEST;
		gbc_cbNFlag.insets = new Insets(0, 0, 5, 0);
		gbc_cbNFlag.gridx = 1;
		gbc_cbNFlag.gridy = 7;
		panelCCR.add(cbNFlag, gbc_cbNFlag);

		cbCarry = new JCheckBox("Carry");
		GridBagConstraints gbc_cbCarry = new GridBagConstraints();
		gbc_cbCarry.anchor = GridBagConstraints.WEST;
		gbc_cbCarry.insets = new Insets(0, 0, 5, 0);
		gbc_cbCarry.gridx = 1;
		gbc_cbCarry.gridy = 8;
		panelCCR.add(cbCarry, gbc_cbCarry);

		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_1.gridx = 1;
		gbc_verticalStrut_1.gridy = 10;
		panelCCR.add(verticalStrut_1, gbc_verticalStrut_1);

		JButton btnNewButton = new JButton("Update CCR");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ccr.setSignFlag(cbSign.isSelected() ? true : false);
				ccr.setZeroFlag(cbZero.isSelected() ? true : false);
				ccr.setHFlag(cbHalfCarry.isSelected() ? true : false);
				ccr.setPvFlag(cbParity.isSelected() ? true : false);
				ccr.setNFlag(cbNFlag.isSelected() ? true : false);
				ccr.setCarryFlag(cbCarry.isSelected() ? true : false);
				Thread t = new Thread(panelViewCCR);
				t.start();
			}// actionPerformed
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.WEST;
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 11;
		panelCCR.add(btnNewButton, gbc_btnNewButton);

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
		splitPane1.setDividerLocation(250);

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

	private static final String MAIN = "Main";
	private static final String AUXILARY = "Auxilary";
	private static final String[] REGS_MAIN = new String[] { "A", "B", "C", "D", "E", "H", "L", "F" };
	private static final String[] REGS_AUX = new String[] { "A'", "B'", "C'", "D'", "E'", "H'", "L'", "F'" };

	private static final String PUM_LOG_PRINT = "popupLogPrint";
	private static final String PUM_LOG_CLEAR = "popupLogClear";

	static final String EMPTY_STRING = "";
	private JCheckBox cbSign;
	private JCheckBox cbZero;
	private JCheckBox cbHalfCarry;
	private JCheckBox cbParity;
	private JCheckBox cbNFlag;
	private JCheckBox cbCarry;
	private V_PrimaryRegisters panel_V_PrimayRegisters;

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