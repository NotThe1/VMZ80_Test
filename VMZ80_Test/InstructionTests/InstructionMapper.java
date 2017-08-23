

import java.awt.Dimension;
import java.awt.EventQueue;
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
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import codeSupport.AppLogger;

public class InstructionMapper {
	AppLogger log = AppLogger.getInstance();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InstructionMapper window = new InstructionMapper();
					window.frmTemplate.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				} // try
			}// run
		});
	}// main

	/* Standard Stuff */

	private void doBtnOne() {// Map
		int opCodeValue = (int) spinnerOpCode.getValue();
		int page = (opCodeValue & 0B11000000) >> 6;
		spinnerPage.setValue(page);
		lblPage.setText(getBits(page).substring(1));
		int yyy = (opCodeValue & 0B00111000) >> 3;
		spinnerYYY.setValue(yyy);
		lblYYY.setText(getBits(yyy));
		int zzz = opCodeValue & 0B00000111;
		spinnerZZZ.setValue(zzz);
		lblZZZ.setText(getBits(zzz));

	}// doBtnOne
	
	private String getBits(int value) {
		String ans = "xxx";
		switch(value) {
		case 0:
			ans = "000";
			break;
		case 1:
			ans = "001";
			break;
		case 2:
			ans = "010";
			break;
		case 3:
			ans = "011";
			break;
		case 4:
			ans = "100";
			break;
		case 5:
			ans = "101";
			break;
		case 6:
			ans = "110";
			break;
		case 7:
			ans = "111";
			break;
		}//switch value
		return ans;
	}//getBits

	private void doBtnTwo() {// Combine
		int page = (int) spinnerPage.getValue();
		lblPage.setText(getBits(page).substring(1));

		int yyy = (int) spinnerYYY.getValue();
		lblYYY.setText(getBits(yyy));

		int zzz = (int) spinnerZZZ.getValue();
		lblZZZ.setText(getBits(zzz));
		
		int opCode = (page<<6) + (yyy<<3) + zzz;

		
		spinnerOpCode.setValue(opCode);
	}// doBtnTwo

	private void doBtnThree() { // All YYY
		int page = (int) spinnerPage.getValue();
		int pageInt = page << 6;
		int zzz = (int) spinnerZZZ.getValue();
		int opCode = 0;
		String bitsPage = getBits(page).substring(1);
		String bitsYYY = "xxx";
		String bitsZZZ = getBits(zzz);		
		String message = "opCode\tPage\tYYY\tZZZ";
		log.addWarning(message);
		
		for (int yyy = 0; yyy <8;yyy++) {
			opCode = pageInt + (yyy << 3) + zzz;
			bitsYYY = getBits(yyy);
			message = String.format("  %02X\t %s\t%s\t%s", opCode,bitsPage,bitsYYY,bitsZZZ);
			log.addInfo(message);
		}//for

	}// doBtnThree

	private void doBtnFour() {// All ZZZ
		int page = (int) spinnerPage.getValue();
		int pageInt = page << 6;
		int yyy = (int) spinnerYYY.getValue();
		int yyyInt = yyy<<3;
		int opCode = 0;
		String bitsPage = getBits(page).substring(1);
		String bitsYYY = getBits(yyy);
		String bitsZZZ = "XXX";		
		String message = "opCode\tPage\tYYY\tZZZ";
		log.addWarning(message);
		
		for (int zzz = 0; zzz <8;zzz++) {
			opCode = pageInt + yyyInt + zzz;
			bitsZZZ = getBits(zzz);
			message = String.format("  %02X\t %s\t%s\t%s", opCode,bitsPage,bitsYYY,bitsZZZ);
			log.addInfo(message);
		}//for


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

	private void appClose() {
		Preferences myPrefs = Preferences.userNodeForPackage(InstructionMapper.class)
				.node(this.getClass().getSimpleName());
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
		Preferences myPrefs = Preferences.userNodeForPackage(InstructionMapper.class)
				.node(this.getClass().getSimpleName());
		frmTemplate.setSize(761, 501);
		frmTemplate.setLocation(myPrefs.getInt("LocX", 100), myPrefs.getInt("LocY", 100));
		splitPane1.setDividerLocation(myPrefs.getInt("Divider", 250));
		myPrefs = null;
		log.setDoc(tpLog.getStyledDocument());
	}// appInit

	public InstructionMapper() {
		initialize();
		appInit();
	}// Constructor

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTemplate = new JFrame();
		frmTemplate.setTitle("Instruction Mapper");
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

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		frmTemplate.getContentPane().add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		btnOne = new JButton("Map");
		btnOne.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnOne = new GridBagConstraints();
		gbc_btnOne.insets = new Insets(0, 0, 0, 5);
		gbc_btnOne.gridx = 0;
		gbc_btnOne.gridy = 0;
		panel.add(btnOne, gbc_btnOne);
		btnOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doBtnOne();
			}
		});
		btnOne.setMaximumSize(new Dimension(0, 0));
		btnOne.setPreferredSize(new Dimension(100, 20));

		btnTwo = new JButton("Combine");
		btnTwo.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnTwo = new GridBagConstraints();
		gbc_btnTwo.insets = new Insets(0, 0, 0, 5);
		gbc_btnTwo.gridx = 1;
		gbc_btnTwo.gridy = 0;
		panel.add(btnTwo, gbc_btnTwo);
		btnTwo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnTwo();
			}
		});
		btnTwo.setPreferredSize(new Dimension(100, 20));
		btnTwo.setMaximumSize(new Dimension(0, 0));

		btnThree = new JButton("All YYY");
		btnThree.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnThree = new GridBagConstraints();
		gbc_btnThree.insets = new Insets(0, 0, 0, 5);
		gbc_btnThree.gridx = 2;
		gbc_btnThree.gridy = 0;
		panel.add(btnThree, gbc_btnThree);
		btnThree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doBtnThree();
			}
		});
		btnThree.setPreferredSize(new Dimension(100, 20));
		btnThree.setMaximumSize(new Dimension(0, 0));

		btnFour = new JButton("All ZZZ");
		btnFour.setMinimumSize(new Dimension(100, 20));
		GridBagConstraints gbc_btnFour = new GridBagConstraints();
		gbc_btnFour.anchor = GridBagConstraints.NORTH;
		gbc_btnFour.gridx = 3;
		gbc_btnFour.gridy = 0;
		panel.add(btnFour, gbc_btnFour);
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

		JPanel panelToParts = new JPanel();
		GridBagConstraints gbc_panelToParts = new GridBagConstraints();
		gbc_panelToParts.fill = GridBagConstraints.BOTH;
		gbc_panelToParts.gridx = 0;
		gbc_panelToParts.gridy = 0;
		panelLeft.add(panelToParts, gbc_panelToParts);
		GridBagLayout gbl_panelToParts = new GridBagLayout();
		gbl_panelToParts.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_panelToParts.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panelToParts.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_panelToParts.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panelToParts.setLayout(gbl_panelToParts);

		JLabel lblHexCode = new JLabel("Hex Code:");
		GridBagConstraints gbc_lblHexCode = new GridBagConstraints();
		gbc_lblHexCode.insets = new Insets(0, 0, 5, 5);
		gbc_lblHexCode.gridx = 1;
		gbc_lblHexCode.gridy = 1;
		panelToParts.add(lblHexCode, gbc_lblHexCode);

		spinnerOpCode = new Hex64KSpinner();
		// spinnerOpCode.setModel(new SpinnerNumberModel(0, 0, 256, 1));
		GridBagConstraints gbc_spinnerOpCode = new GridBagConstraints();
		gbc_spinnerOpCode.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerOpCode.gridx = 2;
		gbc_spinnerOpCode.gridy = 1;
		panelToParts.add(spinnerOpCode, gbc_spinnerOpCode);

		JLabel lblNewLabel = new JLabel("Page");
		lblNewLabel.setToolTipText("Page");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 3;
		panelToParts.add(lblNewLabel, gbc_lblNewLabel);

		JLabel lblYyy = new JLabel("YYY");
		GridBagConstraints gbc_lblYyy = new GridBagConstraints();
		gbc_lblYyy.insets = new Insets(0, 0, 5, 5);
		gbc_lblYyy.gridx = 2;
		gbc_lblYyy.gridy = 3;
		panelToParts.add(lblYyy, gbc_lblYyy);

		JLabel lblNewLabel_1 = new JLabel("ZZZ");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel_1.gridx = 4;
		gbc_lblNewLabel_1.gridy = 3;
		panelToParts.add(lblNewLabel_1, gbc_lblNewLabel_1);

		spinnerPage = new Hex64KSpinner();
		spinnerPage.setModel(new SpinnerNumberModel(0, 0, 3, 1));
		GridBagConstraints gbc_spinnerPage = new GridBagConstraints();
		gbc_spinnerPage.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerPage.gridx = 1;
		gbc_spinnerPage.gridy = 4;
		panelToParts.add(spinnerPage, gbc_spinnerPage);

		spinnerYYY = new Hex64KSpinner();
		spinnerYYY.setModel(new SpinnerNumberModel(0, 0, 7, 1));
		GridBagConstraints gbc_spinnerYYY = new GridBagConstraints();
		gbc_spinnerYYY.insets = new Insets(0, 0, 5, 5);
		gbc_spinnerYYY.gridx = 2;
		gbc_spinnerYYY.gridy = 4;
		panelToParts.add(spinnerYYY, gbc_spinnerYYY);

		spinnerZZZ = new Hex64KSpinner();
		spinnerZZZ.setModel(new SpinnerNumberModel(0, 0, 7, 1));
		GridBagConstraints gbc_spinnerZZZ = new GridBagConstraints();
		gbc_spinnerZZZ.insets = new Insets(0, 0, 5, 0);
		gbc_spinnerZZZ.gridx = 4;
		gbc_spinnerZZZ.gridy = 4;
		panelToParts.add(spinnerZZZ, gbc_spinnerZZZ);

		lblPage = new JLabel("00");
		GridBagConstraints gbc_lblPage = new GridBagConstraints();
		gbc_lblPage.insets = new Insets(0, 0, 5, 5);
		gbc_lblPage.gridx = 1;
		gbc_lblPage.gridy = 5;
		panelToParts.add(lblPage, gbc_lblPage);

		lblYYY = new JLabel("000");
		GridBagConstraints gbc_lblYYY = new GridBagConstraints();
		gbc_lblYYY.anchor = GridBagConstraints.SOUTH;
		gbc_lblYYY.insets = new Insets(0, 0, 5, 5);
		gbc_lblYYY.gridx = 2;
		gbc_lblYYY.gridy = 5;
		panelToParts.add(lblYYY, gbc_lblYYY);

		lblZZZ = new JLabel("000");
		GridBagConstraints gbc_lblZZZ = new GridBagConstraints();
		gbc_lblZZZ.insets = new Insets(0, 0, 5, 0);
		gbc_lblZZZ.gridx = 4;
		gbc_lblZZZ.gridy = 5;
		panelToParts.add(lblZZZ, gbc_lblZZZ);

		JPanel panelRight = new JPanel();
		splitPane1.setRightComponent(panelRight);
		GridBagLayout gbl_panelRight = new GridBagLayout();
		gbl_panelRight.columnWidths = new int[] { 0, 0 };
		gbl_panelRight.rowHeights = new int[] { 0, 0 };
		gbl_panelRight.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_panelRight.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		panelRight.setLayout(gbl_panelRight);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		panelRight.add(scrollPane, gbc_scrollPane);

		tpLog = new JTextPane();
		tpLog.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount()>1) {
					log.clear();
				}
			}
		});
		scrollPane.setViewportView(tpLog);

		lblLog = new JLabel("New label");
		lblLog.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane.setColumnHeaderView(lblLog);
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

		JSeparator separator = new JSeparator();
		mnuFile.add(separator);

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

	private JFrame frmTemplate;
	private JButton btnOne;
	private JButton btnTwo;
	private JButton btnThree;
	private JButton btnFour;
	private JSplitPane splitPane1;
	private JTextPane tpLog;
	private JLabel lblLog;
	private Hex64KSpinner spinnerOpCode;
	private Hex64KSpinner spinnerPage;
	private Hex64KSpinner spinnerYYY;
	private Hex64KSpinner spinnerZZZ;
	private JLabel lblPage;
	private JLabel lblYYY;
	private JLabel lblZZZ;

}// class GUItemplate