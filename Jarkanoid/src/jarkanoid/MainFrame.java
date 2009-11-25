package jarkanoid;

import java.awt.*;
import javax.swing.*;

/* Класс MainFrame является главным
 * окном приложения
 */
public class MainFrame extends JFrame {

	public MainFrame() {
		setTitle("Arkanoid");
		setSize(450, 450);
			
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		/* Надпись, отображает текущий статус игры */
		JLabel l = new JLabel("Arkanoid");
		l.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			
		/* Панель для вывода текущих результатов игры */
		JPanel infoPanel = new JPanel();
		c.add(BorderLayout.NORTH, infoPanel);
			
		/* Главное игровое поле */
		GameField mainPanel = new GameField();
		mainPanel.setStatusLabel(l);
		c.add(BorderLayout.CENTER, mainPanel);
		
		JPanel statusPanel = new JPanel();
		statusPanel.add(l);		
		c.add(BorderLayout.SOUTH, statusPanel);
		
		
			
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame frame = new MainFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}
}


