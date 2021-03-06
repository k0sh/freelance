package jarkanoid;

import java.awt.*;
import java.awt.geom.*;
import java.lang.Math;
import java.util.Random;

public class Ball
{
    public Point2D.Float v, r, pr;	/* скорость, позиция, предыдущая позиция */
	public static int diam = 8;		/* диаметр */
    public boolean stuck;			/* true если мячик приклеен к каретке */

    public Ball(float x, float y) {
        stuck = true;
        r = new Point2D.Float(x, y);
		v = new Point2D.Float(2.0f, -3.0f);
		reset();
    }

    /* Сброс и установка начальной скорости */
    public void reset() {
        float i;
		Random gen = new Random();

		// Определяем в какую сторону первоначально полетит мячик
		if (gen.nextInt(2) == 0) {
		    i = -1f;
		} else {
		    i = 1f;
		}

		// Вычисляем скорость
		v.x = i * 2.0f;
		v.y = -3.0f;
		v = normalize(v);
		v.x = 2.0f*v.x;
		v.y = 2.0f*v.y;
   		pr = r;
	}

    /* Возвращает координаты следующей позиции мяча */
    public Point2D.Float step() {
        return new Point2D.Float(r.x + v.x, r.y + v.y);
    }

    /* Перемещает мяч в новую позицию. И очищает фон в предыдущей
     * позиции.
     */
    public void moveTo(Point2D.Float np, Image buf) {
		pr = r;
		r = np;

		if (buf != null) {
			Graphics2D g = (Graphics2D) buf.getGraphics();
			clear(g);
			paint(g);
			g.dispose();
		}
    }

	/* Нормализация вектора. Используется для вычисления скорости. */
    public Point2D.Float normalize(Point2D.Float p) {
		float s = (float)Math.sqrt((double)(p.x * p.x + p.y * p.y));
		Point2D.Float r = p;
		r.x /= s;
		r.y /= s;
		return r;
    }

    /* Рисует мяч */
    public void paint(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillOval((int)r.x-diam/2, (int)r.y-diam/2, diam, diam);
    }

    /* Очищает фон */
    public void clear(Graphics2D g) {
        g.clearRect((int)pr.x - diam/2, (int)pr.y - diam/2, diam, diam);
    }
}
