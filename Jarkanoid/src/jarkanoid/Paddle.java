package jarkanoid;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

/*
 *  Класс описывающий каретку 
 */
public class Paddle {
	/* Текущяя позиция каретки */
	public int pos;	
	/* Размер каретки */
	public static Dimension size = new Dimension(40,5);
	
	public Paddle(int pos) {
		this.pos = pos;
	}
	
	/* Рисует каретку */
	public void paint(Graphics2D g, int y) {		
		g.setColor(Color.GREEN);
		g.fillRect(pos - size.width/2, y, size.width, size.height);
	}

}


