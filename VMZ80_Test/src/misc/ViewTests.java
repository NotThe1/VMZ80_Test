package misc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
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

import codeSupport.AppLogger;
import codeSupport.Z80.Register;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import hardware.View.V_CCR;
import hardware.View.V_IndexRegisters;
import hardware.View.V_PrimaryRegisters;
import hardware.View.V_ProgramRegisters;

public class ViewTests {

	private JFrame frmTemplate;
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
		frmTemplate.setSize(1028, 539);
		frmTemplate.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane1.setDividerLocation(myPrefs.getInt("Divider", 250));
		myPrefs = null;

		txtLog.setText(EMPTY_STRING);

		log.setDoc(txtLog.getStyledDocument());
		log.info("Starting....");

		wrs.setReg(Register.A, (byte) 0X01);
		wrs.setReg(Register.B, (byte) 0X02);
		wrs.setReg(Register.C, (byte) 0X03);
		wrs.setReg(Register.D, (byte) 0X04);
		wrs.setReg(Register.E, (byte) 0X05);
		wrs.setReg(Register.H, (byte) 0X06);
		wrs.setReg(Register.L, (byte) 0X07);
		wrs.setReg(Register.F, (byte) 0X01);

		wrs.setReg(Register.Ap, (byte) 0X10);
		wrs.setReg(Register.Bp, (byte) 0X20);
		wrs.setReg(Register.Cp, (byte) 0X30);
		wrs.setReg(Register.Dp, (byte) 0X40);
		wrs.setReg(Register.Ep, (byte) 0X50);
		wrs.setReg(Register.Hp, (byte) 0X60);
		wrs.setReg(Register.Lp, (byte) 0X70);
		wrs.setReg(Register.Fp, (byte) 0XD7);
		Thread t_PrimayRegisters = new Thread(panel_V_PrimayRegisters);
		t_PrimayRegisters.run();

		wrs.setProgramCounter(0x0100);
		wrs.setStackPointer(0x1111);
		Thread t_ProgramRegisters = new Thread(panel_V_ProgramRegisters);
		t_ProgramRegisters.run();

		wrs.setIX(0xA5A5);
		wrs.setIY(0x5A5A);
		Thread t_IndexRegisters = new Thread(panel_V_IndexRegisters);
		t_IndexRegisters.run();

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

		JButton btnNewButton_1 = new JButton("test Registers");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Thread t_ProgramRegisters = new Thread(panel_V_ProgramRegisters);
				t_ProgramRegisters.run();
				Thread t_IndexRegisters = new Thread(panel_V_IndexRegisters);
				t_IndexRegisters.run();
			}//actionPerformed
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 0;
		panelForButtons.add(btnNewButton_1, gbc_btnNewButton_1);

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
		gbl_panelLeft.columnWidths = new int[] { 100, 0 };
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
		gbl_panelPrimaryRegisters.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelPrimaryRegisters.rowWeights = new double[] { 0.0, 0.0, 0.0 };
		panelPrimaryRegisters.setLayout(gbl_panelPrimaryRegisters);

		V_CCR viewCCR = new V_CCR();
		viewCCR.setSize(new Dimension(0, 0));
		viewCCR.setMinimumSize(new Dimension(190, 54));
		GridBagConstraints gbc_viewCCR = new GridBagConstraints();
		gbc_viewCCR.anchor = GridBagConstraints.NORTHWEST;
		gbc_viewCCR.insets = new Insets(0, 0, 5, 0);
		gbc_viewCCR.gridx = 0;
		gbc_viewCCR.gridy = 0;
		panelPrimaryRegisters.add(viewCCR, gbc_viewCCR);
		viewCCR.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		panel_V_PrimayRegisters = new V_PrimaryRegisters();
		panel_V_PrimayRegisters.setPreferredSize(new Dimension(450, 90));
		panel_V_PrimayRegisters.setLayout(null);

		GridBagConstraints gbc_panel_V_PrimayRegisters = new GridBagConstraints();
		gbc_panel_V_PrimayRegisters.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_V_PrimayRegisters.gridx = 0;
		gbc_panel_V_PrimayRegisters.gridy = 1;
		panelPrimaryRegisters.add(panel_V_PrimayRegisters, gbc_panel_V_PrimayRegisters);

		panel_V_ProgramRegisters = new V_ProgramRegisters();
		panel_V_ProgramRegisters.setPreferredSize(new Dimension(240, 75));
		panel_V_ProgramRegisters.setLayout(null);

		GridBagConstraints gbc_panel_V_ProgramRegisters = new GridBagConstraints();
		gbc_panel_V_ProgramRegisters.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_V_ProgramRegisters.gridx = 0;
		gbc_panel_V_ProgramRegisters.gridy = 2;
		panelPrimaryRegisters.add(panel_V_ProgramRegisters, gbc_panel_V_ProgramRegisters);

		//
		panel_V_IndexRegisters = new V_IndexRegisters();
		panel_V_IndexRegisters.setPreferredSize(new Dimension(240, 75));
		panel_V_IndexRegisters.setLayout(null);

		GridBagConstraints gbc_panel_V_IndexRegisters = new GridBagConstraints();
		gbc_panel_V_IndexRegisters.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel_V_IndexRegisters.gridx = 0;
		gbc_panel_V_IndexRegisters.gridy = 3;
		panelPrimaryRegisters.add(panel_V_IndexRegisters, gbc_panel_V_IndexRegisters);
		//

		JPanel panelCCR = new JPanel();
		tabbedPane.addTab("CCR", null, panelCCR, null);
		GridBagLayout gbl_panelCCR = new GridBagLayout();
		gbl_panelCCR.columnWidths = new int[] { 200 };
		gbl_panelCCR.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelCCR.columnWeights = new double[] { 1.0 };
		gbl_panelCCR.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panelCCR.setLayout(gbl_panelCCR);

		V_CCR panelViewCCR = new V_CCR();
		// panelViewCCR.setSize(new Dimension(0, 0));
		panelViewCCR.setMinimumSize(new Dimension(10, 10));
		GridBagConstraints gbc_panelViewCCR = new GridBagConstraints();
		gbc_panelViewCCR.anchor = GridBagConstraints.WEST;
		gbc_panelViewCCR.insets = new Insets(0, 0, 5, 0);
		gbc_panelViewCCR.fill = GridBagConstraints.VERTICAL;
		gbc_panelViewCCR.gridx = 0;
		gbc_panelViewCCR.gridy = 1;
		panelCCR.add(panelViewCCR, gbc_panelViewCCR);
		GridBagLayout gbl_panelViewCCR = new GridBagLayout();
		gbl_panelViewCCR.columnWidths = new int[] { 0 };
		gbl_panelViewCCR.rowHeights = new int[] { 0 };
		gbl_panelViewCCR.columnWeights = new double[] { Double.MIN_VALUE };
		gbl_panelViewCCR.rowWeights = new double[] { Double.MIN_VALUE };
		panelViewCCR.setLayout(gbl_panelViewCCR);
		//
		Component verticalStrut = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.anchor = GridBagConstraints.WEST;
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut.gridx = 0;
		gbc_verticalStrut.gridy = 2;
		panelCCR.add(verticalStrut, gbc_verticalStrut);

		cbSign = new JCheckBox("Sign");
		GridBagConstraints gbc_cbSign = new GridBagConstraints();
		gbc_cbSign.insets = new Insets(0, 0, 5, 0);
		gbc_cbSign.anchor = GridBagConstraints.WEST;
		gbc_cbSign.gridx = 0;
		gbc_cbSign.gridy = 3;
		panelCCR.add(cbSign, gbc_cbSign);

		cbZero = new JCheckBox("Zero");
		GridBagConstraints gbc_cbZero = new GridBagConstraints();
		gbc_cbZero.anchor = GridBagConstraints.WEST;
		gbc_cbZero.insets = new Insets(0, 0, 5, 0);
		gbc_cbZero.gridx = 0;
		gbc_cbZero.gridy = 4;
		panelCCR.add(cbZero, gbc_cbZero);

		cbHalfCarry = new JCheckBox("Half Carry");
		GridBagConstraints gbc_cbHalfCarry = new GridBagConstraints();
		gbc_cbHalfCarry.anchor = GridBagConstraints.WEST;
		gbc_cbHalfCarry.insets = new Insets(0, 0, 5, 0);
		gbc_cbHalfCarry.gridx = 0;
		gbc_cbHalfCarry.gridy = 5;
		panelCCR.add(cbHalfCarry, gbc_cbHalfCarry);

		cbParity = new JCheckBox("Parity");
		GridBagConstraints gbc_cbParity = new GridBagConstraints();
		gbc_cbParity.anchor = GridBagConstraints.WEST;
		gbc_cbParity.insets = new Insets(0, 0, 5, 0);
		gbc_cbParity.gridx = 0;
		gbc_cbParity.gridy = 6;
		panelCCR.add(cbParity, gbc_cbParity);

		cbNFlag = new JCheckBox("N Flag");
		GridBagConstraints gbc_cbNFlag = new GridBagConstraints();
		gbc_cbNFlag.anchor = GridBagConstraints.WEST;
		gbc_cbNFlag.insets = new Insets(0, 0, 5, 0);
		gbc_cbNFlag.gridx = 0;
		gbc_cbNFlag.gridy = 7;
		panelCCR.add(cbNFlag, gbc_cbNFlag);

		cbCarry = new JCheckBox("Carry");
		GridBagConstraints gbc_cbCarry = new GridBagConstraints();
		gbc_cbCarry.anchor = GridBagConstraints.WEST;
		gbc_cbCarry.insets = new Insets(0, 0, 5, 0);
		gbc_cbCarry.gridx = 0;
		gbc_cbCarry.gridy = 8;
		panelCCR.add(cbCarry, gbc_cbCarry);

		Component verticalStrut_1 = Box.createVerticalStrut(20);
		GridBagConstraints gbc_verticalStrut_1 = new GridBagConstraints();
		gbc_verticalStrut_1.anchor = GridBagConstraints.WEST;
		gbc_verticalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_verticalStrut_1.gridx = 0;
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
		gbc_btnNewButton.gridx = 0;
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
		splitPane1.setDividerLocation(200);

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

		JSeparator separator99 = new JSeparator();
		mnuFile.add(separator99);

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
	private JCheckBox cbSign;
	private JCheckBox cbZero;
	private JCheckBox cbHalfCarry;
	private JCheckBox cbParity;
	private JCheckBox cbNFlag;
	private JCheckBox cbCarry;
	private V_PrimaryRegisters panel_V_PrimayRegisters;
	private V_ProgramRegisters panel_V_ProgramRegisters;
	private V_IndexRegisters panel_V_IndexRegisters;
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