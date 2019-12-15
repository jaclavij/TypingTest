package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import data.Words;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class GamePanel extends JFrame {

	private static final long serialVersionUID = 1L;
	private Difficulty difficulty;
	private Language language;
	private double seconds;
	private boolean isRunning = false;
	private int score = 0;
	private int[] highScores = new int[] {0,0,0,0};

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
	private JPanel panelCompete;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GamePanel frame = new GamePanel(Difficulty.HARD, Language.ESP);
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
		
		panelCompete = new JPanel();
		tabbedPane.addTab("Compete", null, panelCompete, null);
		panelCompete.setLayout(null);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"New column", "New column"
			}
		));
		table.setBounds(132, 25, 171, 252);
		panelCompete.add(table);

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

	public void barStateHandler(ChangeEvent e) {
		if (progressBar.getValue() <= 0) {
			timer.stop();
			isRunning = false;
			lblWord.setText("Time's up");
			lblTypePlayTo.setVisible(true);
			if (score > highScores[difficulty.ordinal()]) {
				highScores[difficulty.ordinal()] = score;
				lblHighScore.setText("High Score: " + highScores[difficulty.ordinal()]);
			}
			System.out.println(highScores[0] + " " + highScores[1] + " " + highScores[2] + " " + highScores[3]);
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
			timer.setDelay((int) seconds * 10);
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
}
