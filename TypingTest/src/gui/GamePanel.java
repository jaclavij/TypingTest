package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import data.Words;

public class GamePanel extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String LANG = "ESP";
	private int seconds = 5;
	private boolean isRunning = false;
	private JPanel contentPane;
	private JLabel lblWord;
	private JProgressBar progressBar;
	private Timer timer; // Me decanto por el Timer de swing ya que el método restart() es esencial en mi
							// programa
	private JPanel panel;
	private JTextField txtInput;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GamePanel frame = new GamePanel();
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
	public GamePanel() {
		Words.load();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 465, 559);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));

		panel = new JPanel();
		panel.setPreferredSize(new Dimension(450, 450));
		contentPane.add(panel);
		panel.setLayout(null);
		if (LANG.equals("ENG")) {
			lblWord = new JLabel("play");
			lblWord.setBounds(207, 128, 37, 31);
		} else if (LANG.equals("ESP")) {
			lblWord = new JLabel("jugar");
			lblWord.setBounds(207, 128, 46, 31);
		}
		lblWord.setFont(new Font("Gadugi", Font.PLAIN, 20));
		lblWord.addPropertyChangeListener("text", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				lblWord.setSize(lblWord.getPreferredSize());
				lblWord.setLocation((contentPane.getWidth() - lblWord.getWidth()) / 2,
						progressBar.getLocation().y + (progressBar.getHeight() - lblWord.getHeight()) / 2);
			}
		});
		panel.add(lblWord);

		progressBar = new JProgressBar();
		progressBar.setBounds(126, 43, 200, 200);
		panel.add(progressBar);
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

		timer = new Timer(seconds * 10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barProgressHandler();
			}
		});

		txtInput = new JTextField();
		txtInput.setFont(new Font("Gadugi", Font.PLAIN, 18));
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
//				inputUpdateHandler();
			}
		});
		panel.add(txtInput);

		pack();
	}

	public void barProgressHandler() {
		progressBar.setValue(progressBar.getValue() - 1);
	}

	public void barStateHandler(ChangeEvent e) {
		if (progressBar.getValue() <= 0) {
			timer.stop();
			lblWord.setText("LOSER");
		}
	}

	protected void inputUpdateHandler() {
		// Utilizo esta estructura debido a que este método se ejecutará al cambiar el
		// texto de txtInput, por lo que genera un lock sobre este objeto. Por ello no
		// puedo hacer txtInput.setText("") durante la ejecución del evento. Con esta
		// solución, lanzaré el hilo después de liberarse el lock.
		Runnable doUpdate = new Runnable() {
			public void run() {
				if (!isRunning && (txtInput.getText().equalsIgnoreCase("play")
						|| txtInput.getText().equalsIgnoreCase("jugar"))) {
					isRunning = true;
					txtInput.setText("");
					lblWord.setText(Words.getRandom(LANG));
					timer.start();
				}
				if (isRunning && txtInput.getText().equalsIgnoreCase(lblWord.getText())) {
					progressBar.setValue(100);
					txtInput.setText("");
					lblWord.setText(Words.getRandom(LANG));
					timer.restart();
				}
			}
		};
		SwingUtilities.invokeLater(doUpdate);
	}
}
