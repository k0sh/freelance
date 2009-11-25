/*
 * A simple Arkanoid clone.
 *
 * (c) 2007, Michal Januszewski <spock@gentoo.org>
 *
 * This file is subject to the terms and conditions of the GNU General Public
 * License v2.
 *
 */

import java.awt.*;
import java.awt.geom.*;
import java.lang.Math;
import java.util.Random;

public class Ball
{
    public Point2D.Float v, r, pr;	/* velocity, position, previous position */
	public static int diam = 8;		/* diameter */
    public boolean stuck;			/* true if the ball is stuck to the paddle */

    public Ball(float x, float y) {
        stuck = true;
        r = new Point2D.Float(x, y);
		v = new Point2D.Float(2.0f, -3.0f);
		reset();
    }

    /* Set an initial velocity */
    public void reset() {
        float i;
		Random gen = new Random();

		/* Whether the ball will start going to the left or to right
		 * is decided at random. */
		if (gen.nextInt(2) == 0) {
		    i = -1f;
		} else {
		    i = 1f;
		}

		v.x = i * 2.0f;
		v.y = -3.0f;
		v = normalize(v);
		v.x = 2.0f*v.x;
		v.y = 2.0f*v.y;
   		pr = r;
	}

    public Point2D.Float step() {
        return new Point2D.Float(r.x + v.x, r.y + v.y);
    }

	/* Moves the ball to a new position. Takes care of clearing
	 * the background at the point where the ball was previously
	 * located. */
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

	/* Normalizes a 2D vector. Used for velocity calculations. */
    public Point2D.Float normalize(Point2D.Float p) {
		float s = (float)Math.sqrt((double)(p.x * p.x + p.y * p.y));
		Point2D.Float r = p;
		r.x /= s;
		r.y /= s;
		return r;
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.white);
        g.fillOval((int)r.x-diam/2, (int)r.y-diam/2, diam, diam);
    }

    public void clear(Graphics2D g) {
        g.clearRect((int)pr.x - diam/2, (int)pr.y - diam/2, diam, diam);
    }
}
