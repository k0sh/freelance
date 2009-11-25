package jarkanoid;

import java.awt.*;

/* 
 * Класс описывающий блок
 */
public class Block {
	/* Координаты блока */
	int x, y;
	
	/* Размер блока */
	public static Dimension size = new Dimension(21, 15);
	
	public Block(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/* Рисует блок */
	public void paint(Graphics2D g) {
		g.setColor(Color.BLUE);
		g.fillRect(x+1, y+1, size.width-2, size.height-2);
	}
	

}

