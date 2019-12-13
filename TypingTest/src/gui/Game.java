package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.eclipse.swt.SWT;

import java.awt.Font;

public class Game extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblWord;
	private JProgressBar progressBar;
	private int seconds;
	private Timer timer; // Me decanto por el Timer de swing ya que el método restart() es esencial en mi
						 // programa

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game frame = new Game();
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
	public Game() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 465, 559);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblWord = new JLabel("Word");
		lblWord.setFont(new Font("Georgia", Font.PLAIN, 18));
		lblWord.setBounds(205, 117, 42, 29);
		lblWord.setText("word");
		contentPane.add(lblWord);

		progressBar = new JProgressBar();
		progressBar.setBounds(126, 34, 200, 200);
		progressBar.setForeground(Color.DARK_GRAY);
		progressBar.setOpaque(false);
		progressBar.setBorderPainted(false);
		progressBar.setUI(new ProgressCircleUI());
		progressBar.setValue(100);
		progressBar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				barStateHandler(e);
			}
		});
		contentPane.add(progressBar);

		seconds = 5;
		timer = new Timer(seconds * 10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				barProgressHandler();
			}
		});
		timer.start();
	}

	public void barProgressHandler() {
		progressBar.setValue(progressBar.getValue() - 1);
	}

	public void barStateHandler(ChangeEvent e) {
		JProgressBar bar = (JProgressBar) e.getSource();
		if (bar.getValue() <= 0) {
			timer.stop();
		}
	}
}
