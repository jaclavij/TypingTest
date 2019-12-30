package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import data.Words;
import logic.Client;

public class GamePanel extends JFrame {

	private static final long serialVersionUID = 1L;
	private Difficulty difficulty;
	private Language language;
	private double seconds;
	private boolean isRunning = false;
	private int score = 0;
	private Integer[] highScores = new Integer[] { 0, 0, 0, 0 };

	private JPanel contentPane;
	private JLabel lblWord;
	private JProgressBar progressBar;
	private Timer timer; // Me decanto por el Timer de swing ya que el método restart() es esencial en mi
							// programa
	private JPanel panelGame;
	private JTextField txtInput;
	private JLabel lblScore;
	private JLabel lblHighScore;
	private JLabel lblTypePlayTo;
	private JTabbedPane tabbedPane;
	private JPanel panelOptions;
	private JPanel panelLanguage;
	private JPanel panelDifficulty;
	private JRadioButton radioButton;
	private JRadioButton radioButton_1;
	private JRadioButton radioButton_2;
	private JRadioButton radioButton_3;
	private JRadioButton radioButton_4;
	private JRadioButton radioButton_5;
	private JPanel panelTop;
	private JTable table;
	private JPanel panelSubmit;
	private JLabel lblEasyScore;
	private JLabel lblMediumScore;
	private JLabel lblHardScore;
	private JLabel lblGodScore;
	private JButton btSubmit;
	private JSeparator separator_1;
	private JLabel lblUsername;
	private JTextField txtUsername;
	private JLabel lblYouCanSubmit;
	private JComboBox<String> comboBox;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GamePanel frame = new GamePanel(Difficulty.GOD, Language.ENG);
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
	public GamePanel(Difficulty dif, Language lang) {
		difficulty = dif;

		switch (difficulty) {
		case EASY:
			seconds = 8;
			break;
		case MEDIUM:
			seconds = 5;
			break;
		case HARD:
			seconds = 3;
			break;
		case GOD:
			seconds = 1.5;
			break;
		default:
			break;
		}
		language = lang;
		Words.load();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("TypingTest");
		;
		setLocationRelativeTo(null);
		setBounds(100, 100, 465, 559);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);

		panelGame = new JPanel();
		tabbedPane.addTab("TypingTest", null, panelGame, null);
		panelGame.setPreferredSize(new Dimension(450, 450));
		panelGame.setLayout(null);

		if (language == Language.ENG) {
			lblWord = new JLabel("play");
			lblWord.setBounds(202, 128, 48, 31);
		} else if (language == Language.ESP) {
			lblWord = new JLabel("jugar");
			lblWord.setBounds(196, 128, 61, 31);
		}
		lblWord.setFont(new Font("Quicksand Medium", Font.PLAIN, 24));
		lblWord.addPropertyChangeListener("text", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				lblWordChangeHandler();
			}
		});
		panelGame.add(lblWord);

		timer = new Timer((int) seconds * 10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barProgressHandler();
			}
		});

		progressBar = new JProgressBar();
		progressBar.setBounds(126, 43, 200, 200);
		progressBar.setForeground(Color.DARK_GRAY);
		progressBar.setOpaque(false);
		progressBar.setBorderPainted(false);
		progressBar.setUI(new ProgressCircleUI());
		progressBar.setValue(100);
		progressBar.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				barStateHandler(e);
			}
		});
		panelGame.add(progressBar);

		txtInput = new JTextField();
		txtInput.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		txtInput.setBounds(138, 266, 175, 31);
		txtInput.setColumns(10);
		txtInput.getDocument().addDocumentListener(new DocumentListener() {
			public void removeUpdate(DocumentEvent e) {
				inputUpdateHandler();
			}

			public void insertUpdate(DocumentEvent e) {
				inputUpdateHandler();
			}

			public void changedUpdate(DocumentEvent e) {
				inputUpdateHandler();
			}
		});
		panelGame.add(txtInput);

		lblScore = new JLabel("Score: " + score);
		lblScore.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		lblScore.setBounds(148, 308, 72, 22);
		lblScore.addPropertyChangeListener("text", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				scoresChangeHandler(evt);
			}
		});
		panelGame.add(lblScore);

		lblHighScore = new JLabel("High Score: ");
		lblHighScore.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		lblHighScore.setBounds(148, 341, 105, 22);
		lblHighScore.addPropertyChangeListener("text", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				scoresChangeHandler(evt);
			}
		});
		panelGame.add(lblHighScore);

		lblTypePlayTo = new JLabel("Type play to restart");
		lblTypePlayTo.setFont(new Font("Quicksand Medium", Font.PLAIN, 12));
		lblTypePlayTo.setBounds(170, 196, 113, 15);
		lblTypePlayTo.setVisible(false);
		panelGame.add(lblTypePlayTo);

		panelOptions = new JPanel();
		tabbedPane.addTab("Options", null, panelOptions, null);
		panelOptions.setLayout(null);

		ButtonGroup difButtons = new ButtonGroup();

		panelDifficulty = new JPanel();
		panelDifficulty.setBorder(new LineBorder(new Color(0, 0, 0)));
		panelDifficulty.setBounds(34, 120, 385, 63);
		panelDifficulty.setBorder(BorderFactory.createTitledBorder("Difficulty"));
		panelOptions.add(panelDifficulty);

		radioButton = new JRadioButton("Easy");
		radioButton.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		panelDifficulty.add(radioButton);
		difButtons.add(radioButton);

		radioButton_1 = new JRadioButton("Medium");
		radioButton_1.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		panelDifficulty.add(radioButton_1);
		difButtons.add(radioButton_1);

		radioButton_2 = new JRadioButton("Hard");
		radioButton_2.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		panelDifficulty.add(radioButton_2);
		difButtons.add(radioButton_2);

		radioButton_3 = new JRadioButton("GOD");
		radioButton_3.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		panelDifficulty.add(radioButton_3);
		difButtons.add(radioButton_3);
		for (Enumeration<AbstractButton> buttons = difButtons.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			button.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					difButtonSelectionHandler(e);
				}
			});
			if (button.getText().equalsIgnoreCase(difficulty.toString())) {
				button.setSelected(true);
			}
		}

		panelLanguage = new JPanel();
		panelLanguage.setBounds(34, 206, 385, 63);
		panelLanguage.setBorder(BorderFactory.createTitledBorder("Words language"));
		panelOptions.add(panelLanguage);

		ButtonGroup langGroup = new ButtonGroup();
		radioButton_4 = new JRadioButton("English");
		radioButton_4.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		panelLanguage.add(radioButton_4);
		langGroup.add(radioButton_4);

		radioButton_5 = new JRadioButton("Spanish");
		radioButton_5.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		panelLanguage.add(radioButton_5);
		langGroup.add(radioButton_5);

		panelTop = new JPanel();
		tabbedPane.addTab("Top Scores", null, panelTop, null);
		panelTop.setLayout(null);

		panelSubmit = new JPanel();
		panelSubmit.setBorder(new TitledBorder(
				new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
				"Your Top Scores", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelSubmit.setBounds(10, 11, 433, 100);
		GridBagLayout gbl_panelSubmit = new GridBagLayout();
		gbl_panelSubmit.columnWidths = new int[] { 105, 105, 105, 105, 0 };
		gbl_panelSubmit.rowHeights = new int[] { 32, 0, 32, 0, 0, 0 };
		gbl_panelSubmit.columnWeights = new double[] { 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panelSubmit.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panelSubmit.setLayout(gbl_panelSubmit);
		panelTop.add(panelSubmit);

		JLabel lblEasy = new JLabel("Easy");
		lblEasy.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		GridBagConstraints gbc_lblEasy = new GridBagConstraints();
		gbc_lblEasy.fill = GridBagConstraints.VERTICAL;
		gbc_lblEasy.insets = new Insets(0, 0, 5, 5);
		gbc_lblEasy.gridx = 0;
		gbc_lblEasy.gridy = 0;
		panelSubmit.add(lblEasy, gbc_lblEasy);

		JLabel lblMedium = new JLabel("Medium");
		lblMedium.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		GridBagConstraints gbc_lblMedium = new GridBagConstraints();
		gbc_lblMedium.fill = GridBagConstraints.VERTICAL;
		gbc_lblMedium.insets = new Insets(0, 0, 5, 5);
		gbc_lblMedium.gridx = 1;
		gbc_lblMedium.gridy = 0;
		panelSubmit.add(lblMedium, gbc_lblMedium);

		JLabel lblHard = new JLabel("Hard");
		lblHard.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		GridBagConstraints gbc_lblHard = new GridBagConstraints();
		gbc_lblHard.fill = GridBagConstraints.VERTICAL;
		gbc_lblHard.insets = new Insets(0, 0, 5, 5);
		gbc_lblHard.gridx = 2;
		gbc_lblHard.gridy = 0;
		panelSubmit.add(lblHard, gbc_lblHard);

		JLabel lblGod = new JLabel("GOD");
		lblGod.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		GridBagConstraints gbc_lblGod = new GridBagConstraints();
		gbc_lblGod.fill = GridBagConstraints.VERTICAL;
		gbc_lblGod.insets = new Insets(0, 0, 5, 0);
		gbc_lblGod.gridx = 3;
		gbc_lblGod.gridy = 0;
		panelSubmit.add(lblGod, gbc_lblGod);

		separator_1 = new JSeparator();
		separator_1.setForeground(Color.DARK_GRAY);
		separator_1.setBackground(Color.DARK_GRAY);
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 4;
		gbc_separator_1.insets = new Insets(0, 0, 5, 0);
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 1;
		panelSubmit.add(separator_1, gbc_separator_1);

		lblEasyScore = new JLabel(String.valueOf(highScores[0]));
		lblEasyScore.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		GridBagConstraints gbc_lblEasyScore = new GridBagConstraints();
		gbc_lblEasyScore.insets = new Insets(0, 0, 5, 5);
		gbc_lblEasyScore.gridx = 0;
		gbc_lblEasyScore.gridy = 2;
		panelSubmit.add(lblEasyScore, gbc_lblEasyScore);

		lblMediumScore = new JLabel(String.valueOf(highScores[1]));
		lblMediumScore.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		GridBagConstraints gbc_lblMediumScore = new GridBagConstraints();
		gbc_lblMediumScore.insets = new Insets(0, 0, 5, 5);
		gbc_lblMediumScore.gridx = 1;
		gbc_lblMediumScore.gridy = 2;
		panelSubmit.add(lblMediumScore, gbc_lblMediumScore);

		lblHardScore = new JLabel(String.valueOf(highScores[2]));
		lblHardScore.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		GridBagConstraints gbc_lblHardScore = new GridBagConstraints();
		gbc_lblHardScore.insets = new Insets(0, 0, 5, 5);
		gbc_lblHardScore.gridx = 2;
		gbc_lblHardScore.gridy = 2;
		panelSubmit.add(lblHardScore, gbc_lblHardScore);

		lblGodScore = new JLabel(String.valueOf(highScores[3]));
		lblGodScore.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		GridBagConstraints gbc_lblGodScore = new GridBagConstraints();
		gbc_lblGodScore.insets = new Insets(0, 0, 5, 0);
		gbc_lblGodScore.gridx = 3;
		gbc_lblGodScore.gridy = 2;
		panelSubmit.add(lblGodScore, gbc_lblGodScore);

		JSeparator separator = new JSeparator();
		separator.setBackground(Color.DARK_GRAY);
		separator.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator.gridwidth = 4;
		gbc_separator.insets = new Insets(0, 0, 5, 0);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 3;
		panelSubmit.add(separator, gbc_separator);

		scoreTableSetup(difficulty);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(183, 211, 260, 231);
		panelTop.add(scrollPane);
		scrollPane.setViewportView(table);

		lblUsername = new JLabel("Username");
		lblUsername.setBounds(20, 154, 89, 23);
		panelTop.add(lblUsername);
		lblUsername.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));

		txtUsername = new JTextField();
		txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		txtUsername.setBounds(119, 149, 196, 32);
		panelTop.add(txtUsername);
		txtUsername.setColumns(10);

		btSubmit = new JButton("Submit");
		btSubmit.setBounds(325, 153, 89, 25);
		panelTop.add(btSubmit);
		btSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				submitButtonHandler();
			}
		});
		btSubmit.setFont(new Font("Tahoma", Font.PLAIN, 13));

		lblYouCanSubmit = new JLabel("You can send your scores by typing a username and clicking Submit!");
		lblYouCanSubmit.setBounds(20, 124, 423, 14);
		panelTop.add(lblYouCanSubmit);

		comboBox = new JComboBox<>();
		comboBox.setBounds(20, 242, 131, 22);
		comboBox.addItem("Easy");
		comboBox.addItem("Medium");
		comboBox.addItem("Hard");
		comboBox.addItem("GOD");
		comboBox.setSelectedIndex(difficulty.ordinal());
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				comboSelectionHandler(e);
			}
		});
		panelTop.add(comboBox);

		JLabel lblDifficulty = new JLabel("Difficulty:");
		lblDifficulty.setFont(new Font("Quicksand Medium", Font.PLAIN, 18));
		lblDifficulty.setBounds(20, 211, 101, 23);
		panelTop.add(lblDifficulty);

		for (Enumeration<AbstractButton> buttons = langGroup.getElements(); buttons.hasMoreElements();) {
			AbstractButton button = buttons.nextElement();
			button.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					langButtonSelectionHandler(e);
				}
			});
			if (button.getText().equalsIgnoreCase(language.toString())) {
				button.setSelected(true);
			}
		}

		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				txtInput.requestFocus();
			}
		});
	}

	public void barProgressHandler() {
		float[] hsbvals = new float[3];
		Color.RGBtoHSB((int) ((100 - progressBar.getValue()) * 2.55), 0, 0, hsbvals);
		progressBar.setForeground(Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2]));
		progressBar.setValue(progressBar.getValue() - 1);
	}

	// En esto evento realizo todas las sentencias necesarias cada vez que se
	// termina una ronda
	public void barStateHandler(ChangeEvent e) {
		if (progressBar.getValue() <= 0) {

			timer.stop();
			isRunning = false;
			lblWord.setText("Time's up");
			lblTypePlayTo.setVisible(true);
			if (score > highScores[difficulty.ordinal()]) {
				highScores[difficulty.ordinal()] = score;
				lblHighScore.setText("High Score: " + highScores[difficulty.ordinal()]);
				switch (difficulty) {
				case EASY:
					lblEasyScore.setText(String.valueOf(highScores[0]));
					break;
				case MEDIUM:
					lblMediumScore.setText(String.valueOf(highScores[1]));
					break;
				case HARD:
					lblHardScore.setText(String.valueOf(highScores[2]));
					break;
				case GOD:
					lblGodScore.setText(String.valueOf(highScores[3]));
					break;
				default:
					break;
				}
			}
		}
	}

	public void inputUpdateHandler() {
		// Utilizo esta estructura debido a que este método se ejecutará al cambiar el
		// texto de txtInput, por lo que se genera un lock sobre este objeto. Por ello,
		// no puedo hacer txtInput.setText("") durante la ejecución del evento. Con esta
		// solución, lanzaré un hilo que lo haga después de liberarse el lock.
		Runnable doUpdate = new Runnable() {
			public void run() {
				if (!isRunning && (txtInput.getText().equalsIgnoreCase("play")
						|| txtInput.getText().equalsIgnoreCase("jugar"))) {
					isRunning = true;
					progressBar.setValue(100);
					score = 0;
					lblScore.setText("Score: " + score);
					lblTypePlayTo.setVisible(false);
					txtInput.setText("");
					lblWord.setText(Words.getRandom(language));
					timer.restart();
				}
				if (isRunning && txtInput.getText().equalsIgnoreCase(lblWord.getText())) {
					progressBar.setValue(100);
					score++;
					lblScore.setText("Score: " + score);
					txtInput.setText("");
					lblWord.setText(Words.getRandom(language));
					timer.restart();
				}
			}
		};
		SwingUtilities.invokeLater(doUpdate);
	}

	public void lblWordChangeHandler() {
		lblWord.setSize(lblWord.getPreferredSize());
		lblWord.setLocation((panelGame.getWidth() - lblWord.getWidth()) / 2,
				progressBar.getLocation().y + (progressBar.getHeight() - lblWord.getHeight()) / 2);
	}

	public void scoresChangeHandler(PropertyChangeEvent evt) {
		JLabel lbl = (JLabel) evt.getSource();
		lbl.setSize(lbl.getPreferredSize());
	}

	public void difButtonSelectionHandler(ItemEvent e) {
		if (isRunning)
			progressBar.setValue(0);
		if (e.getStateChange() == ItemEvent.SELECTED) {
			JRadioButton bt = (JRadioButton) e.getItem();
			switch (bt.getText()) {
			case "Easy":
				difficulty = Difficulty.EASY;
				seconds = 8;
				break;
			case "Medium":
				difficulty = Difficulty.MEDIUM;
				seconds = 5;
				break;
			case "Hard":
				difficulty = Difficulty.HARD;
				seconds = 3;
				break;
			case "GOD":
				difficulty = Difficulty.GOD;
				seconds = 1.5;
				break;
			default:
				break;
			}
			timer.setDelay((int) (seconds * 10));
			lblHighScore.setText("High Score: " + highScores[difficulty.ordinal()]);
			if (comboBox != null)
				comboBox.setSelectedIndex(difficulty.ordinal());
		}
	}

	public void langButtonSelectionHandler(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			JRadioButton bt = (JRadioButton) e.getItem();
			switch (bt.getText()) {
			case "English":
				language = Language.ENG;
				if (lblWord.getText().equals("jugar"))
					lblWord.setText("play");
				break;
			case "Spanish":
				language = Language.ESP;
				if (lblWord.getText().equals("play"))
					lblWord.setText("jugar");
				break;
			default:
				break;
			}
		}
	}

	public void submitButtonHandler() {
		Client.sendScores(txtUsername.getText(), highScores);
		scoreTableSetup(difficulty);
	}

	public void scoreTableSetup(Difficulty dif) {
		ConcurrentHashMap<String, Integer> firstTable = Client.getTable(dif.name());
		if (firstTable != null) {
			Map<String, Integer> orderedTable = firstTable.entrySet().stream()
					.sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(Collectors
							.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
			String columnNames[] = { "Username", "Score" };
			Object[][] data = new Object[orderedTable.size()][2];
			int i = 0;
			for (Entry<String, Integer> entry : orderedTable.entrySet()) {
				data[i][1] = entry.getValue();
				data[i][0] = entry.getKey();
				i++;
			}
			if (table == null) {
				table = new JTable();
				table.setFocusable(false);
				table.setRowSelectionAllowed(false);
				table.setShowVerticalLines(false);
				table.getTableHeader().setReorderingAllowed(false);
				table.setBounds(119, 206, 334, 247);
			}
			table.setModel(new DefaultTableModel(data, columnNames) {
				private static final long serialVersionUID = 1L;

				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		}
	}

	public void comboSelectionHandler(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String item = (String) e.getItem();
			scoreTableSetup(Difficulty.valueOf(item.toUpperCase()));
		}
	}
}
