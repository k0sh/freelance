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
		
		/* Панель отображает текущий статус игры */		
		GameInfo statusPanel = new GameInfo();
		c.add(BorderLayout.SOUTH, statusPanel);
		
		/* Главное игровое поле */
		GameField mainPanel = new GameField();
		mainPanel.setStatusPanel(statusPanel);
		c.add(BorderLayout.CENTER, mainPanel);
				
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


