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
import java.beans.PropertyVetoException;
import java.text.MessageFormat;
import java.util.Date;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
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
import codeSupport.Z80;
import codeSupport.Z80.Register;
import disks.DiskControlUnit;
import disks.diskPanel.V_IF_DiskPanel;
import hardware.ConditionCodeRegister;
import hardware.WorkingRegisterSet;
import hardware.View.V_IF_CCR;
import hardware.View.V_IF_IndexRegisters;
import hardware.View.V_IF_PrimaryRegisters;
import hardware.View.V_IF_ProgramRegisters;
import hardware.View.V_IF_SpecialRegisters;

public class ViewTests {

	private JFrame frmTemplate;
	private JSplitPane splitPane1;

	private AppLogger log = AppLogger.getInstance();
	private JTextPane txtLog;
	private JPopupMenu popupLog;
	private AdapterLog logAdaper = new AdapterLog();

	ConditionCodeRegister ccr = ConditionCodeRegister.getInstance();
	WorkingRegisterSet wrs = WorkingRegisterSet.getInstance();
	private V_IF_PrimaryRegisters ifPrimaryRegisters;
	private V_IF_ProgramRegisters ifProgramRegisters;
	private V_IF_IndexRegisters ifIndexRegisters;
	private V_IF_SpecialRegisters ifSpecialRegisters;
	private V_IF_CCR ifCCR;
	
	private V_IF_DiskPanel ifDiskPanel;	
	
	DiskControlUnit dcu = DiskControlUnit.getInstance();
	
//	File[] mountedDisks = new File[4];
	
//	private static RoundIcon redIcon = new RoundIcon(Color.RED);
//	private static RoundIcon grayIcon = new RoundIcon(Color.GRAY);


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

	
	private void initAllRegisters() {
		wrs.setReg(Register.A, (byte) 0X01);
		wrs.setReg(Register.B, (byte) 0X02);
		wrs.setReg(Register.C, (byte) 0X03);
		wrs.setReg(Register.D, (byte) 0X04);
		wrs.setReg(Register.E, (byte) 0X05);
		wrs.setReg(Register.H, (byte) 0X06);
		wrs.setReg(Register.L, (byte) 0X07);
		wrs.setReg(Register.F, (byte) 0X0C3);	//SZ....NC

		wrs.setReg(Register.Ap, (byte) 0X10);
		wrs.setReg(Register.Bp, (byte) 0X20);
		wrs.setReg(Register.Cp, (byte) 0X30);
		wrs.setReg(Register.Dp, (byte) 0X40);
		wrs.setReg(Register.Ep, (byte) 0X50);
		wrs.setReg(Register.Hp, (byte) 0X60);
		wrs.setReg(Register.Lp, (byte) 0X70);
		wrs.setReg(Register.Fp, (byte) 0XD7);

		wrs.setProgramCounter(0x0100);
		wrs.setStackPointer(0x1111);

		wrs.setIX(0xA5A5);
		wrs.setIY(0x5A5A);
		
		wrs.setReg(Z80.Register.I, (byte)0xF0);
		wrs.setReg(Z80.Register.R, (byte)0x0F);
		wrs.setIFF1(true);
		wrs.setIFF2(false);
		
		readAllRegisters();
	}//initAllRegisters
	
	private void readAllRegisters() {
		Thread t_PrimayRegistersIF = new Thread(ifPrimaryRegisters);
		t_PrimayRegistersIF.run();

		Thread t_ProgramRegistersIF = new Thread(ifProgramRegisters);
		t_ProgramRegistersIF.run();

		Thread t_IndexRegistersIF = new Thread(ifIndexRegisters);
		t_IndexRegistersIF.run();
		
		Thread t_SpecialRegistersIF = new Thread(ifSpecialRegisters);
		t_SpecialRegistersIF.run();
		
		Thread t_CCR = new Thread(ifCCR);
		t_CCR.start();
		
	}//readAllRegisters
	
	private void saveInternalFrameLocations(Preferences myPrefs) { //
		Point location = new Point();
		JInternalFrame[] internalFrames = desktopPane.getAllFrames();
		for(JInternalFrame internalFrame:internalFrames) {
			String key = internalFrame.getClass().getSimpleName();
			location = internalFrame.getLocation();
			myPrefs.putInt(key + ".x", location.x);
			myPrefs.putInt(key + ".y", location.y);
			myPrefs.putBoolean(key+ ".isIcon", internalFrame.isIcon());
			log.infof("x = %d, y = %d for frame %s%n", location.x,location.y,internalFrame.getClass().getSimpleName());
		}//for
	}//saveInternalFrameLocations
	
	private void getInternalFrameLocations(Preferences myPrefs){
		Point location = new Point();
		JInternalFrame[] internalFrames = desktopPane.getAllFrames();
		for(JInternalFrame internalFrame:internalFrames) {
			String key = internalFrame.getClass().getSimpleName();
			location.x = myPrefs.getInt(key +".x", 0);
			location.y = myPrefs.getInt(key +".y", 0);
			internalFrame.setLocation(location);
			try {
				internalFrame.setIcon(myPrefs.getBoolean(key + ".isIcon", false));
			} catch (PropertyVetoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			log.infof("x = %d, y = %d for frame %s%n", location.x,location.y,internalFrame.getClass().getSimpleName());
		}//for
	
	}//getInternalFrameLocations
//	private void doDiskpanelEvent(DiskPanelEvent diskPanelEvent) {
//
//		if (diskPanelEvent.isSelected()) {
//			mountDisk(diskPanelEvent.getDiskIndex());
//		}else {
//			dismountDisk(diskPanelEvent.getDiskIndex());			
//		}//if mount/dismount
//		
//		ifDiskPanel.updateDisks(mountedDisks);
//		
//		Thread t_ifDiskPanel = new Thread(ifDiskPanel);
//		t_ifDiskPanel.start();
//	}//doDiskpanelEvent
//	
//	private void mountDisk(int diskIndex) {
//		if(mountedDisks[diskIndex] !=null) {
//			String diskPath = mountedDisks[diskIndex].getAbsolutePath();
//			log.warnf("disk already mounted at index %d - %s%n", diskIndex,diskPath);
//			return;
//		}//if mounted already
//		
//		JFileChooser fc = FilePicker.getDiskPicker();
//		if (fc.showOpenDialog(frmTemplate) == JFileChooser.CANCEL_OPTION) {
//			log.info("Bailed out of the open");
//			return;
//		} // if
//
//		File selectedFile = fc.getSelectedFile();
//		
//		if (!selectedFile.exists()) {
//			log.info("Selected Disk does not exists");
//			return; // try again
//		} //if exists
//		
//		if (isDiskMounted(selectedFile)) {
//			log.warn("Disk already mounted");
//			return;
//		}// already mounted
//		
//		mountedDisks[diskIndex] = selectedFile;
//		log.infof("Mounted Disk - Index %d, Path: %s%n", diskIndex,mountedDisks[diskIndex].getAbsoluteFile());
//		return;		
//	}//mountDisk
//	
//	private void dismountDisk(int diskIndex) {
//		if (mountedDisks[diskIndex] ==null) {
//			log.warn("No Disk to Dismount");
//		}else {
//			log.infof("Dismounted Disk - Index %d, Path: %s%n", diskIndex,mountedDisks[diskIndex].getAbsoluteFile());
//
//			mountedDisks[diskIndex] =null;
//		}//if
//		
//	}//dismountDisk
//	
//	private boolean isDiskMounted(File newFile) {
//		boolean ans = false;
//		for (File file:mountedDisks) {
//			if (newFile.equals(file)) {
//				ans = true;
//				break;
//			}//if
//		}//for
//		return ans;
//	}//

	private void appClose() {
		Preferences myPrefs = Preferences.userNodeForPackage(ViewTests.class).node(this.getClass().getSimpleName());
		Dimension dim = frmTemplate.getSize();
		myPrefs.putInt("Height", dim.height);
		myPrefs.putInt("Width", dim.width);
		Point point = frmTemplate.getLocation();
		myPrefs.putInt("LocX", point.x);
		myPrefs.putInt("LocY", point.y);
		myPrefs.putInt("Divider", splitPane1.getDividerLocation());
		saveInternalFrameLocations(myPrefs);
		myPrefs = null;
	}// appClose

	private void appInit() {
		Preferences myPrefs = Preferences.userNodeForPackage(ViewTests.class).node(this.getClass().getSimpleName());
		frmTemplate.setSize(1028, 672);
		frmTemplate.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane1.setDividerLocation(myPrefs.getInt("Divider", 250));
		getInternalFrameLocations( myPrefs) ;
		myPrefs = null;

		txtLog.setText(EMPTY_STRING);

		log.setDoc(txtLog.getStyledDocument());
		log.info("Starting....");

		initAllRegisters();
		dcu.setDisplay(ifDiskPanel);
		
		

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

		JButton btnNewButton_1 = new JButton("Read  Registers");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				readAllRegisters();
			}//actionPerformed
		});
		panelForButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JButton btnNewButton_2 = new JButton("Init Registers");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initAllRegisters();
			}//actionPerformed
		});//ActionListener()
		panelForButtons.add(btnNewButton_2);
		panelForButtons.add(btnNewButton_1);
		
		JButton btnAddTwoDisks = new JButton("Add disks A & D");
			panelForButtons.add(btnAddTwoDisks);

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
		
		JPanel panelDeskTop = new JPanel();
		tabbedPane.addTab("Desktop", null, panelDeskTop, null);
		GridBagLayout gbl_panelDeskTop = new GridBagLayout();
		gbl_panelDeskTop.columnWidths = new int[]{0, 0};
		gbl_panelDeskTop.rowHeights = new int[]{0, 0};
		gbl_panelDeskTop.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panelDeskTop.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		panelDeskTop.setLayout(gbl_panelDeskTop);
		
		desktopPane = new JDesktopPane();
		desktopPane.setLayout(null);
		GridBagConstraints gbc_desktopPane = new GridBagConstraints();
		gbc_desktopPane.fill = GridBagConstraints.BOTH;
		gbc_desktopPane.gridx = 0;
		gbc_desktopPane.gridy = 0;
		panelDeskTop.add(desktopPane, gbc_desktopPane);
		
		ifPrimaryRegisters = new V_IF_PrimaryRegisters();
		desktopPane.add(ifPrimaryRegisters);
		ifPrimaryRegisters.setVisible(true);
		
		ifProgramRegisters = new V_IF_ProgramRegisters();
		desktopPane.add(ifProgramRegisters);
		ifProgramRegisters.setVisible(true);
		
		ifIndexRegisters = new V_IF_IndexRegisters();
		desktopPane.add(ifIndexRegisters);
		ifIndexRegisters.setVisible(true);
		
		ifSpecialRegisters = new V_IF_SpecialRegisters();
		desktopPane.add(ifSpecialRegisters);
		ifSpecialRegisters.setVisible(true);
		
		ifCCR = new V_IF_CCR();
		desktopPane.add(ifCCR);
		ifCCR.setVisible(true);
		
		ifDiskPanel = new V_IF_DiskPanel();

		desktopPane.add(ifDiskPanel);
		ifDiskPanel.setVisible(true);		

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
		splitPane1.setDividerLocation(300);

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
		
		JMenu mnuWindows = new JMenu("Windows");
		menuBar.add(mnuWindows);
		
		JMenuItem mnuWindowsPrimaryRegisters = new JMenuItem("PrimaryRegisters");
		mnuWindowsPrimaryRegisters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ifPrimaryRegisters.setVisible(!ifPrimaryRegisters.isVisible());
			}
		});
		mnuWindows.add(mnuWindowsPrimaryRegisters);

	}// initialize

	private static final String PUM_LOG_PRINT = "popupLogPrint";
	private static final String PUM_LOG_CLEAR = "popupLogClear";

	static final String EMPTY_STRING = "";
	private JDesktopPane desktopPane;

	//////////////////////////////////////////////////////////////////////////

	class AdapterLog implements ActionListener {// , ListSelectionListener
		/*   ActionListener   */
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