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

/*
 * A canvas to show some simple information about the game:
 * current level, # of balls left and the score.
 */
public class infoCanvas extends Canvas
{
    Image offscr = null;
    mainCanvas game;

    static Font fnMain = new Font("dialog", Font.PLAIN, 20);
    static String txScore = "Score: ";
    static String txBalls = "Balls: ";
    static String txLevel = "Level: ";

    public infoCanvas(mainCanvas game)
    {
		setBackground(Color.black);
		this.game = game;
    }

    public Dimension getPreferredSize() {
		/* Our preferred width is the same as the width of the
		 * main board. */
		return new Dimension(game.getPreferredSize().width, 30);
    }

    public void paintStuff(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension d = getSize();
        Rectangle bnd = g2d.getClipBounds();

        g2d.clearRect(bnd.x, bnd.y, bnd.width, bnd.height);
		g2d.setFont(fnMain);
		g2d.setColor(Color.yellow);
		g2d.drawString(txLevel + game.level, 5, 22);
		g2d.drawString(txBalls + game.balls, 125, 22);
		g2d.drawString(txScore + game.score, 275, 22);
    }

    public void paint(Graphics g) {
        update(g);
    }

    public void update(Graphics g) {
        Dimension d = getSize();
        if (offscr == null) {
            offscr = createImage(d.width, d.height);
        }

        Graphics g2 = offscr.getGraphics();

        g2.setClip(0, 0, d.width, d.height);

		paintStuff(g2);

        g.drawImage(offscr, 0, 0, null);
        g2.dispose();
        return;
    }

    public void invalidate() {
        super.invalidate();
        offscr = null;
    }
}
