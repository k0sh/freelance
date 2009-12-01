package jarkanoid;

import java.awt.*;
import javax.swing.*;

/* Класс MainFrame является главным
 * окном приложения
 */
public class MainFrame extends JFrame {

	public MainFrame() {
		setTitle("JArkanoid");
			
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		/* Панель отображает текущий статус игры */		
		GameInfo statusPanel = new GameInfo();
		c.add(BorderLayout.SOUTH, statusPanel);
		
		/* Главное игровое поле */
		GameField mainPanel = new GameField();
		mainPanel.setStatusPanel(statusPanel);
		c.add(BorderLayout.CENTER, mainPanel);
		
		/* Устанавливаем размеры главного окна */
		Dimension size = new Dimension(448, 427);
		setPreferredSize(size);
		setMinimumSize(size);
		setMaximumSize(size);
				
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


