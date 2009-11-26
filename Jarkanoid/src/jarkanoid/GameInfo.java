package jarkanoid;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameInfo extends JPanel {

	private JLabel labelStatus;
	
	public GameInfo() {
		labelStatus = new JLabel("Arkanoid");
		labelStatus.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		add(labelStatus);
	}
	
	public void setScore(int score) {
		
	}
	
	public void setTicks(int ticks) {
		
	}
	
	public void setStatus(String text) {
		labelStatus.setText(text);
	}

}
