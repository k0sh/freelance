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

interface Block
{
	/* All blocks have this standard size. */
	static Dimension size = new Dimension(21, 15);

    public void paint(Graphics2D g);

    /* Returns true if the block remains on the board. */
    public boolean hit();

	/* Returns the number of points scored for hitting the block. */
    public int score();
}

class SimpleBlock implements Block
{
    Color c;
    int x, y;

    public SimpleBlock(Color c, int x, int y) {
		this.c = c;
		this.x = x;
		this.y = y;
	}

	public void paint(Graphics2D g) {
		g.setColor(this.c);
		g.fillRect(x + 1, y + 1, size.width - 2, size.height - 2);
    }

    public boolean hit() {
		/* The block always disappears after a single hit. */
		return false;
    }

    public int score() {
		return 10;
    }
}

class DoubleBlock implements Block
{
    Color c, cb;
    int hits, x, y;

    public DoubleBlock(Color c, int x, int y) {
		this.c = c;
		this.cb = c.darker();
		this.hits = 2;
		this.x = x;
		this.y = y;
    }

    public void paint(Graphics2D g) {
		g.setColor(this.c);
		g.fillRect(x + 1, y + 1, size.width - 2, size.height - 2);
		g.setColor(this.cb);
		g.fillRect(x + 3, y + 3, size.width - 6, size.height - 6);
    }

    public boolean hit() {
		hits--;
		if (hits == 0) {
		    return false;
		} else {
		    return true;
		}
    }

    public int score() {
		/* Score 5 points after the first hit and 20
		 * points after the block is destroyed. */
		if (hits > 0) {
		    return 5;
		} else {
		    return 20;
		}
    }
}

class TripleBlock implements Block
{
    Color c, cb;
    int hits, x, y;

    public TripleBlock(Color c, int x, int y) {
		this.c = c;
		this.cb = c.darker();
		this.hits = 2;
		this.x = x;
		this.y = y;
    }

    public void paint(Graphics2D g) {
		g.setColor(this.cb);
		g.fillRect(x + 1, y + 1, size.width - 2, size.height - 2);
		g.setColor(this.c);
		g.fillRect(x + 3, y + 3, size.width - 6, size.height - 6);
    }

    public boolean hit() {
		hits--;
		if (hits == 0) {
		    return false;
		} else {
		    return true;
		}
    }

    public int score() {
		if (hits == 0) {
		    return 75;
		} else {
		    return 0;
		}
    }
}
