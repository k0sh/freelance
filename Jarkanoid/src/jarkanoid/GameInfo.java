package jarkanoid;

import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class GameInfo extends JPanel {

	private JLabel labelStatus, labelScore, labelTicks;
	
	public GameInfo() {
		
		/* Создаем рамку с вогнутыми краями */
		Border border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
		
		labelStatus = new JLabel("JArkanoid");
		labelStatus.setBorder(border);
		labelScore = new JLabel("Score: -");
		labelScore.setBorder(border);
		labelTicks = new JLabel("Time: -");
		labelTicks.setBorder(border);
		
		/* Устанавливаем менеджер компановки */
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		add(labelStatus);
		add(labelScore);
		add(labelTicks);
	}
	
	public void setScore(int score) {
		labelScore.setText("Score: " + Integer.toString(score));
	}
	
	public void setTicks(int ticks) {
		labelTicks.setText("Time: " + Integer.toString(ticks));
	}
	
	public void setStatus(String text) {
		labelStatus.setText(text);
	}

}
