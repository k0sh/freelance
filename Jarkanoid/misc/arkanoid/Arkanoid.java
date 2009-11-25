/*
 * A simple Arkanoid clone.
 *
 * (c) 2007, Michal Januszewski <spock@gentoo.org>
 *
 * This file is subject to the terms and conditions of the GNU General Public
 * License v2.
 *
 * Created on January 8, 2007, 7:15 PM
 * Last update on January 14, 2007, 4:32 PM
 */

import java.awt.event.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.lang.Math;
import javax.swing.*;
import java.util.Random;

class Paddle
{
    public int pos;
    public static Dimension size = new Dimension(40, 5);

    public Paddle(int pos) {
        this.pos = pos;
    }

    public void paint(Graphics2D g, int y) {
        g.setColor(Color.yellow);
        g.fillRect(pos - size.width/2, y, size.width, size.height);
    }
}

class mainCanvas extends Canvas implements MouseMotionListener, MouseListener, KeyListener
{
    Image offscr = null;
    Timer timer = null;
    infoCanvas ic = null;
    Label lInfo = null;
    boolean paused = true;
    boolean repaintBlocks = false;	/* set to true to repaint the whole gaming area
									   when this object is rendered */

    Paddle paddle;
    Ball ball;

    static int frameThickness = 10;
    static int blocksPerRow = 20;
    static int blocksPerCol = 20;
    static Dimension fieldSize = new Dimension(
								    frameThickness * 2 + blocksPerRow * Block.size.width,
								    frameThickness * 2 + blocksPerCol * Block.size.height + 50);
	/* Colors used for the gradient forming the frame around the board. */
    static Color clGr1 = new Color(255, 0, 0);
    static Color clGr2 = new Color(255, 255, 180);

    int level, score, balls;
    int blockCnt = 0;
	int speed = 0;
	int ticks = 0;

    static String lev1[] = {
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",	/* 0 */
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",
	".. 3b 3b 3b 3b 3b 3b 3b 3b 3b 3b 3b 3b 3b 3b 3b 3b 3b 3b ..",
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",
	".. 2g 1y 1y 1y 1y 2g .. .. 2g 2g .. .. 2g 1y 1y 1y 1y 2g ..",
	".. 2g 1y 1y 1y 1y 2g .. .. 2g 2g .. .. 2g 1y 1y 1y 1y 2g ..",  /* 5 */
	".. .. 2g 1y 1y 2g .. .. 2g 1y 1y 2g .. .. 2g 1y 1y 2g .. ..",
	".. .. 2g 1y 1y 2g .. .. 2g 1y 1y 2g .. .. 2g 1y 1y 2g .. ..",
	".. .. .. 2g 2g .. .. 2g 1y 1y 1y 1y 2g .. .. 2g 2g .. .. ..",
	".. .. .. 2g 2g .. .. 2g 1y 1y 1y 1y 2g .. .. 2g 2g .. .. ..",
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",	/* 10 */
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",
	".. 1r 1r 1r .. 1r .. .. .. .. 1r .. .. 1r .. 1r .. 1r 1r ..",
	".. 1r .. 1r .. 1r .. .. .. 1r .. 1r .. 1r .. 1r .. 1r 1r ..",
	".. 1r 1r 1r .. 1r .. .. .. 1r .. 1r .. .. 1r .. .. 1r 1r ..",
	".. 1r .. .. .. 1r .. .. .. 1r 1r 1r .. .. 1r .. .. .. .. ..",	/* 15 */
	".. 1r .. .. .. 1r 1r 1r .. 1r .. 1r .. .. 1r .. .. 1r 1r ..",
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",
    };

    static String lev2[] = {
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",	/* 0 */
	".. .. .. .. .. .. .. .. .. 2r 2r .. .. .. .. .. .. .. .. ..",
	".. .. .. .. .. .. .. .. 2r 2r 2r 2r .. .. .. .. .. .. .. ..",
	".. .. .. .. .. .. .. 2r 2r .. .. 2r 2r .. .. .. .. .. .. ..",
	".. .. .. .. .. .. 2r 2r .. .. .. .. 2r 2r .. .. .. .. .. ..",
	".. .. .. .. .. 2r 2r .. .. .. .. .. .. 2r 2r .. .. .. .. ..",  /* 5 */
	".. .. .. .. .. 2r 2r 2r 2r 2r 2r 2r 2r 2r 2r .. .. .. .. ..",
	".. .. .. .. .. 1g .. .. .. .. .. .. .. .. 1b .. .. .. .. ..",
	".. .. .. .. 1g 1g 1g .. .. .. .. .. .. 1b 1b 1b .. .. .. ..",
	".. .. .. 1g 1g .. 1g 1g .. .. .. .. 1b 1b .. 1b 1b .. .. ..",
	".. .. 1g 1g .. .. .. 1g 1g .. .. 1b 1b .. .. .. 1b 1b .. ..",  /* 10 */
	".. 1g 1g .. .. .. .. .. 1g 1g 1b 1b .. .. .. .. .. 1b 1b ..",
	".. 1g 1g 1g 1g 1g 1g 1g 1g 1g 1b 1b 1b 1b 1b 1b 1b 1b 1b ..",
	".. 1y 1y 1y 1y 1y.1y 1y 1y 1y 1y 1y 1y 1y 1y 1y 1y 1y 1y ..",
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",  /* 15 */
	".. 2y 2y 2y 2y .. .. .. 3y 3y 3y 3y .. .. .. 2y 2y 2y 2y ..",
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",
	".. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. .. ..",
    };

    Block[][] blocks;

    public mainCanvas() {
        paddle = new Paddle(fieldSize.width/2);
        ball = new Ball(fieldSize.width/2,
				        fieldSize.height - frameThickness - paddle.size.height - Ball.diam/2);

		/* Hide the mouse cursor. */
		int[] pixels = new int[16 * 16];
		Image cursorImage = Toolkit.getDefaultToolkit().createImage(new MemoryImageSource(16, 16, pixels, 0, 16));
		Cursor hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage, new Point(0,0), "invisible");
		setCursor(hiddenCursor);

		setBackground(Color.gray);
        addMouseMotionListener(this);
        addMouseListener(this);
		addKeyListener(this);

		blocks = new Block[blocksPerRow][blocksPerCol];
		timer = new Timer(this, 10);
		timer.start();

		resetGame();
    }

    public void setInfoCanvas(infoCanvas ic)
    {
		this.ic = ic;
    }

    public void setInfoLabel(Label l)
    {
		this.lInfo = l;
    }

    void setLevel()
    {
		ball.stuck = true;
		ball.moveTo(new Point2D.Float(paddle.pos,
					    fieldSize.height - frameThickness - paddle.size.height - ball.diam/2), offscr);
		ball.reset();

		speed = 0;
		ticks = 0;

		if (level == 1) {
		    parseLevel(lev1);
		} else if (level == 2) {
		    parseLevel(lev2);
		} else {
			/* The third level is randomized. */
		    int i, j, n, x, y, c;
			int t = frameThickness;
		    Color[] cls = { Color.red, Color.green, Color.blue, Color.white, Color.yellow };

			/* Clear the board. */
		    for (i = 0; i < blocksPerRow; i++) {
				for (j = 0; j < blocksPerCol; j++) {
				    blocks[i][j] = null;
				}
			}

			/* We want the blocks to take 80% of the board. */
		    n = (blocksPerRow * blocksPerCol * 4) / 5;
		    Random gen = new Random();

		    for (i = 0; i < n; i++) {
				do {
				    x = gen.nextInt(blocksPerRow);
					y = gen.nextInt(blocksPerCol);
				} while (blocks[x][y] != null);

				j = gen.nextInt(2);
				c = gen.nextInt(cls.length);

				switch (j) {
				case 0:
				    blocks[x][y] = new SimpleBlock(cls[c],
										t + x * Block.size.width,
										t + y * Block.size.height);
				    break;
				case 1:
				    blocks[x][y] = new DoubleBlock(cls[c],
										t + x * Block.size.width,
										t + y * Block.size.height);
				    break;
				}
				blockCnt++;
			}
		}
        repaintBlocks = true;
    }

    void resetGame()
    {
		level = 1;
		score = 0;
		balls = 3;
		setLevel();
		paused = false;
    }

    void parseLevel(String[] lev) {
		int i, j;
        int t = frameThickness;

		blockCnt = 0;
		for (i = 0; i < blocksPerCol; i++) {
			String curr = lev[i];

			for (j = 0; j < blocksPerRow; j++) {
				if (curr.charAt(j*3) == '1') {
					Color c = decodeColor(curr.charAt(j*3+1));
					blocks[j][i] = new SimpleBlock(c,
									t + j * Block.size.width,
									t + i * Block.size.height);
					blockCnt++;
				} else if (curr.charAt(j*3) == '2') {
					Color c = decodeColor(curr.charAt(j*3+1));
					blocks[j][i] = new DoubleBlock(c,
									t + j * Block.size.width,
									t + i * Block.size.height);
					blockCnt++;
				} else if (curr.charAt(j*3) == '3') {
					Color c = decodeColor(curr.charAt(j*3+1));
					blocks[j][i] = new TripleBlock(c,
									t + j * Block.size.width,
									t + i * Block.size.height);
					blockCnt++;
				} else {
					blocks[j][i] = null;
				}
			}
		}
    }

    Color decodeColor(char c) {
		switch (c) {
		case 'b':
		    return Color.blue;
		case 'g':
		    return Color.green;
		case 'r':
		    return Color.red;
		case 'y':
			return Color.yellow;
		default:
			return Color.black;
		}
    }

    public Dimension getPreferredSize() {
        return fieldSize;
    }

	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {
		/* Pause or unpause the game. */
		if (e.getKeyChar() == 'p') {
			paused = !paused;
			updateMessage();
		}
	}

    public void mouseClicked(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {
		if (paused) {
			return;
		}
		ball.stuck = false;
		updateMessage();
    }

	public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    public void mouseMoved(MouseEvent e) {
        int x = e.getX();

		if (paused) {
			return;
		}

		/* Move the paddle if the mouse pointer isn't outside of the game board. */
		if (x < frameThickness + paddle.size.width/2) {
            paddle.pos = frameThickness + paddle.size.width/2;
        } else if (x > fieldSize.width - frameThickness - paddle.size.width/2) {
            paddle.pos = fieldSize.width - frameThickness - paddle.size.width/2;
        } else {
            paddle.pos = x;
        }

        /* Is the ball stuck to the paddle? If so, reposition it so
         * that it follows the paddle's movement. */
		if (ball.stuck) {
			ball.moveTo(new Point2D.Float(paddle.pos,
					    fieldSize.height - frameThickness - paddle.size.height - ball.diam/2), offscr);
        }
        repaint();
    }

    public void paintBlocks(Graphics2D g) {
		int i, j;
		int t = frameThickness;

		for (i = 0; i < blocksPerCol; i++) {
			for (j = 0; j < blocksPerRow; j++) {
				if (blocks[j][i] != null) {
					blocks[j][i].paint(g);
				}
			}
		}
    }

    public void paintFrame(Graphics2D g) {
        Paint p = g.getPaint();
        int w = fieldSize.width;
        int h = fieldSize.height;
        int t = frameThickness;

        /* Top */
        g.setPaint(new GradientPaint(0, 0, clGr1, 0, t, clGr2));
        g.fillRect(0, 0, w, t);

        /* Left */
        g.setPaint(new GradientPaint(0, 0, clGr1, t, 0, clGr2));
        g.fillRect(0, 0, t, h);

        /* Right */
        g.setPaint(new GradientPaint(w-t, 0, clGr2, w, 0, clGr1));
        g.fillRect(w-t, 0, t, h);

        /* Bottom */
        g.setPaint(new GradientPaint(0, h-t, clGr2, 0, h, clGr1));
        g.fillRect(0, h-t, w, t);

        /* Top-left */
        g.setPaint(new GradientPaint(0, 0, clGr1, t, t, clGr2));
        g.fillRect(0, 0, t, t);

        /* Top-right */
        g.setPaint(new GradientPaint(w, 0, clGr1, w-t, t, clGr2));
        g.fillRect(w-t, 0, t, t);

        /* Bottom-left */
        g.setPaint(new GradientPaint(t, h-t, clGr2, 0, h, clGr1));
        g.fillRect(0, h-t, t, t);

        /* Bottom-right */
        g.setPaint(new GradientPaint(w, h, clGr1, w-t, h-t, clGr2));
        g.fillRect(w-t, h-t, t, t);

        g.setPaint(p);
    }

    public void paintAll(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = getSize();

        Rectangle bnd = g2d.getClipBounds();
        g2d.setColor(Color.gray);
        g2d.clearRect(bnd.x, bnd.y, bnd.width, bnd.height);

        paintFrame(g2d);
		paintBlocks(g2d);
        paddle.paint(g2d, d.height - frameThickness - paddle.size.height);
        ball.paint(g2d);
    }

    public void paintUpdated(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = getSize();

		g2d.clearRect(frameThickness, d.height - frameThickness - paddle.size.height - Ball.diam,
			 	      d.width - frameThickness*2, paddle.size.height + Ball.diam);
		ball.clear(g2d);

    	if (repaintBlocks) {
			g2d.clearRect(frameThickness, frameThickness,
						  d.width - frameThickness*2, d.height - frameThickness*2);
			paintBlocks(g2d);
			repaintBlocks = false;
		}

        paddle.paint(g2d, d.height - frameThickness - paddle.size.height);
		ball.paint(g2d);
	}

    public void step() {
		if (paused) {
		    return;
		}

		ticks++;

		/* Increase the speed of the ball after every minute. */
		if (ticks > 6000) {
			ticks = 0;

			if (speed < 6) {
				speed++;
				ball.v.x *= 1.3;
				ball.v.y *= 1.3;
			}

		}

		Point2D.Float p = ball.step();
        int w = fieldSize.width;
        int h = fieldSize.height;
		int t = frameThickness;

		/* Don't move the ball if it's stuck to the paddle. */
		if (ball.stuck) {
		    return;
		}

		/* Left boundary */
		if (p.x <= t + ball.diam/2) {
			p.x = t + ball.diam/2;
		    ball.v.x = -ball.v.x;
		}

		/* Right boundary */
		if (p.x >= w - t - ball.diam/2) {
			p.x = w - t - ball.diam/2;
			ball.v.x = -ball.v.x;
		}

		/* Top boundary */
		if (p.y <= t + ball.diam/2) {
			p.y = t + ball.diam/2;
			ball.v.y = -ball.v.y;
		}

		/* Bottom boundary -- check whether the ball
		 * should bounce off the paddle. */
		if (p.y >= h - t - ball.diam/2 - paddle.size.height) {
			p.y = h - t - ball.diam/2 - paddle.size.height;

			if ((p.x >= paddle.pos - paddle.size.width/2 - ball.diam + 1) &&
				(p.x <= paddle.pos + paddle.size.width/2 + ball.diam - 1)) {

				ball.v.y = -ball.v.y;

				if (p.x > paddle.pos) {
					if (ball.v.x < 0) {
						ball.v.x = -ball.v.x;
					}
				} else {
					if (ball.v.x > 0) {
						ball.v.x = -ball.v.x;
					}
				}

				float l = (float)Math.sqrt(ball.v.x * ball.v.x + ball.v.y * ball.v.y);
				float theta = Math.abs((float)Math.atan(ball.v.y / ball.v.x));

				/*
				 * ..      |         ....
				 *   ..    |      ...
				 *     ..  |   ...
				 * theta ..|... theta'
				 * ----------------------
				 *
				 * theta  - angle of incidence
				 * theta' - angle of reflection
				 *
				 *                      paddle.pos
				 *                          v
				 * |----|----|----|----|----|----|----|----|----|----|
				 *  ---------           +++++++++           ---------
				 *
				 * Sections of the paddle marked with '-' ('+') decrease
				 * (increase) the angle of reflection by 10%.
				 *
				 */
				if (p.x < paddle.pos - (paddle.size.width * 0.3f)) {
					theta = theta * 0.9f;
					if (theta > (1f/16f) * Math.PI && theta < (15f/32f) * Math.PI) {
						ball.v.y = -l * (float)Math.sin(theta);
						ball.v.x = -l * (float)Math.cos(theta);
					}
				} else if ((p.x > paddle.pos - (paddle.size.width * 0.1f)) && 
					   (p.x < paddle.pos + (paddle.size.width * 0.1f))) {
					theta = theta * 1.1f;
					if (theta > (1f/16f) * Math.PI && theta < (15f/32f) * Math.PI) {
						ball.v.y = -l * (float)Math.sin(theta);

						if (p.x <= paddle.pos) {
							ball.v.x = -l * (float)Math.cos(theta);
						} else {
							ball.v.x = l * (float)Math.cos(theta);
						}
					}
				} else if (p.x > paddle.pos + (paddle.size.width * 0.3f)) {
					theta = theta * 0.9f;
					if (theta > (1f/16f) * Math.PI && theta < (15f/32f) * Math.PI) {
						ball.v.y = -l * (float)Math.sin(theta);
						ball.v.x = l * (float)Math.cos(theta);
					}
				}
			} else {
				balls--;
				ball.reset();
				speed = 0;
				ticks = 0;

				/* Game over -- the player has just lost his last ball! */
				if (balls < 0) {
					paused = true;
					updateMessage();
					balls = 0;
					repaint();

					/* Wait 5 secs before we continue. */
					try {
						Thread.sleep(5000);
					}
					catch (InterruptedException e) {}
					resetGame();
				}
				ball.stuck = true;
				ball.moveTo(new Point2D.Float(paddle.pos, ball.r.y), offscr);
				repaint();
				return;
			}
		}
		/* end of bottom boundary check */

		float hbdm = ball.diam/2;
		int hit = 0;
		Point idx1, idx2, idx3, idx4;
		Block blk;

		/* Process block hits. Take special care not to count hits
		 * twice (separately for two corners). */

		/* Upper left corner */
		idx1 = getBlockIdx(p.x - hbdm, p.y - hbdm);
		if (idx1.x > -1 && idx1.y > -1) {
			blk = blocks[idx1.x][idx1.y];
			if (blk != null) {
				if (!blk.hit()) {
					blocks[idx1.x][idx1.y] = null;
					blockCnt--;
					repaintBlocks = true;
				}
				score += blk.score();
				hit += 1;
			}
		}

		/* Upper right corner */
		idx2 = getBlockIdx(p.x + hbdm, p.y - hbdm);
		if (idx2.x > -1 && idx2.y > -1) {
			blk = blocks[idx2.x][idx2.y];
			if (blk != null) {
				if (!(idx2.x == idx1.x && idx2.y == idx1.y)) {
					if (!blk.hit()) {
						blocks[idx2.x][idx2.y] = null;
						blockCnt--;
						repaintBlocks = true;
					}
					score += blk.score();
				}
				hit += 2;
			}
		}

		/* Lower left corner */
		idx3 = getBlockIdx(p.x - hbdm, p.y + hbdm);
		if (idx3.x > -1 && idx3.y > -1) {
			blk = blocks[idx3.x][idx3.y];
			if (blk != null) {
				if (!(idx1.x == idx3.x && idx1.y == idx3.y)) {
					if (!blk.hit()) {
						blocks[idx3.x][idx3.y] = null;
						blockCnt--;
						repaintBlocks = true;
					}
					score += blk.score();
				}
				hit += 4;
			}
		}

		/* Lower right corner */
		idx4 = getBlockIdx(p.x + hbdm, p.y + hbdm);
		if (idx4.x > -1 && idx4.y > -1) {
			blk = blocks[idx4.x][idx4.y];
			if (blk != null) {
				if (!(idx3.x == idx4.x && idx3.y == idx4.y) &&
					!(idx2.x == idx4.x && idx2.y == idx4.y)) {
					if (!blk.hit()) {
						blocks[idx4.x][idx4.y] = null;
						blockCnt--;
						repaintBlocks = true;
					}
					score += blk.score();
				}
				hit += 8;
			}
		}

		/*
		 * Code for the corners of the ball's bounding rectangle:
		 *
		 *  1 --- 2
		 *  |     |
		 *  |     |
		 *  4 --- 8
		 *
		 */
		Point tp;
		boolean bx = false, by = false;


		/* Actually bounce the ball off the block(s) */
		switch(hit) {
		case 1:

			if (ball.v.x > 0 && ball.v.y < 0) {
				by = true;
			} else if (ball.v.x < 0 && ball.v.y > 0) {
				bx = true;
			} else {
				tp = getInnerOffset(p.x - hbdm, p.y - hbdm);
				tp.x = Block.size.width - tp.x;
				tp.y = Block.size.height - tp.y;

				if (tp.x >= tp.y && ball.v.y < 0) {
					by = true;
				}
				if (tp.y >= tp.x && ball.v.x < 0) {
					bx = true;
				}
			}
			break;
		case 2:
			if (ball.v.x < 0 && ball.v.y < 0) {
				by = true;
			} else if (ball.v.x > 0 && ball.v.y > 0) {
				bx = true;
			} else {
				tp = getInnerOffset(p.x + hbdm, p.y - hbdm);
				tp.y = Block.size.height - tp.y;

				if (tp.x >= tp.y && ball.v.y < 0) {
					by = true;
				}
				if (tp.y >= tp.x && ball.v.x > 0) {
					bx = true;
				}
			}
			break;

		case 4:
			if (ball.v.x < 0 && ball.v.y < 0) {
				bx = true;
			} else if (ball.v.x > 0 && ball.v.y > 0) {
				by = true;
			} else {
				tp = getInnerOffset(p.x - hbdm, p.y + hbdm);
				tp.x = Block.size.width - tp.x;

				if (tp.x >= tp.y && ball.v.y > 0) {
					by = true;
				}
				if (tp.y >= tp.x && ball.v.x < 0) {
					bx = true;
				}
			}
			break;

		case 8:
			if (ball.v.x > 0 && ball.v.y < 0) {
				bx = true;
			} else if (ball.v.x < 0 && ball.v.y > 0) {
				by = true;
			} else {
				tp = getInnerOffset(p.x + hbdm, p.y + hbdm);
				if (tp.x >= tp.y && ball.v.y > 0) {
					by = true;
				}
				if (tp.y >= tp.x && ball.v.x > 0) {
					bx = true;
				}
			}
			break;

		/*   o
		 * #####
		 * #####
		 */
		case 3:
			by = true;
			break;
		/*
		 * #####
		 * #####
		 *  o
		 */
		case 12:
			by = true;
			break;
		/*
		 *  #####
		 * o#####
		 *  #####
		 */
		case 5:
			bx = true;
			break;
		/*
		 * #####
		 * #####o
		 * #####
		 */
		case 10:
			bx = true;
			break;
		/*
		 * #####
		 * #####
		 *     o#####
		 *      #####
		 */
		case 9:
		/*
		 * ##########
		 * ##########
		 * #####o
		 * #####
		 */
		case 7:
		/*
		 * #########
		 * #########
		 *    o#####
		 *     #####
		 */
		case 11:
		/*
		 * #####
		 * #####o
		 * ##########
		 * ##########
		 */
		case 13:
		/*
		 *      #####
		 *     o#####
		 * ##########
		 * ##########
		 */
		case 14:
		/*
		 *      #####
		 *     o#####
		 * #####
		 * #####
		 *
		 */
		case 6:
			bx = true;
			by = true;
			break;
		default:
			break;
		}

		/*
		 * Since the movement of the ball happens in discrete steps,
		 * we have to take care to move the ball outside of the blocks
		 * after it bounces.
		 */
		if (hit != 0) {
			if (idx4.x == -1)
				idx4.x = blocksPerRow;

			if (idx4.y == -1)
				idx4.y = blocksPerCol;

			if (bx) {
				ball.v.x = -ball.v.x;

				if (ball.v.x < 0) {
					p.x = idx4.x * Block.size.width + t - hbdm;
				} else {
					p.x = idx4.x * Block.size.width + t + hbdm;
				}
			}

			if (by) {
				ball.v.y = -ball.v.y;

				if (ball.v.y < 0) {
					p.y = idx4.y * Block.size.height + t - hbdm;
				} else {
					p.y = idx4.y * Block.size.height + t + hbdm;
				}
			}
		}

		updateMessage();
		ball.moveTo(p, offscr);
		repaint();

		/* We've just finished a level! */
		if (blockCnt == 0) {
			level++;
			if (level > 3) {
				resetGame();
			} else {
				setLevel();
			}
		}
    }


	/* Gets the inner offset of a ball's corner inside a
	 * single block. */
    Point getInnerOffset(float x, float y) {
		Point p = new Point();
		p.x = ((int)x - frameThickness) % Block.size.width;
		p.y = ((int)y - frameThickness) % Block.size.height;
		return p;
    }

	/* Gets the block coordinates corresponding to the
	 * physical coordinates of the specified point. */
    Point getBlockIdx(float x, float y) {
		float t = frameThickness;
		int i, j;
		x -= t;
		y -= t;

		i = (int)(x/Block.size.width);
		j = (int)(y/Block.size.height);

		Point f = new Point(i, j);

		if (f.x >= blocksPerRow) {
			f.x = -1;
		}

		if (f.y >= blocksPerCol) {
			f.y = -1;
		}

		return f;
    }

    public void updateMessage() {
		if (balls < 0) {
			lInfo.setText("Game over!");
			return;
		}

		if (paused) {
			lInfo.setText("Game paused. Press 'p' to continue.");
			return;
		}

		if (ball.stuck) {
			lInfo.setText("Get ready!");
			return;
		}

		if (blockCnt < 10 && blockCnt > 0) {
			lInfo.setText("" + blockCnt + "...");
			return;
		}

		if (blockCnt == 0) {
			lInfo.setText("Congratulations!");
			return;
		}

		if ((blockCnt % 10 == 0) && blockCnt > 0) {
			lInfo.setText("Only " + blockCnt + " remaining!");
			return;
		} else {
			lInfo.setText("Keep on rockin'!");
			return;
		}
    }

    public synchronized void paint(Graphics g) {
        update(g);
    }

    public synchronized void update(Graphics g) {
        Dimension d = getSize();
		Graphics g2;

		/* If the image was invalidated, we need to repaint
		 * everything... */
        if (offscr == null) {
            offscr = createImage(d.width, d.height);
		    g2 = offscr.getGraphics();
            g2.setClip(0, 0, d.width, d.height);
			paintAll(g2);
		/* Otherwise, we only repaint what we have to, which
		 * usually will be the ball and (possibly) the blocks. */
		} else {
		    g2 = offscr.getGraphics();
			g2.setClip(0, 0, d.width, d.height);
		    paintUpdated(g2);
		}

		g.drawImage(offscr, 0, 0, null);
        g2.dispose();

		/* Repaint the information panel. */
        ic.repaint();
        return;
    }

    public void invalidate() {
        super.invalidate();
        offscr = null;
    }
}

class Timer extends Thread
{
    int delay;
    mainCanvas cnv;

    public Timer(mainCanvas cnv, int delay) {
        this.delay = delay;
        this.cnv = cnv;
    }

    public void run() {
        try {
            while (!interrupted()) {
                cnv.step();
                sleep(delay);
            }
        }
        catch (InterruptedException e) {}
    }
}

public class Arkanoid extends JApplet
{
    mainCanvas mc = null;
    infoCanvas ic = null;

    public void init() {
		Container c = getContentPane();
        c.setLayout(new BorderLayout());

        Label l = new Label("Welcome & have fun with the game!");
		l.setAlignment(Label.CENTER);

		mc = new mainCanvas();
		ic = new infoCanvas(mc);
		mc.setInfoCanvas(ic);
		mc.setInfoLabel(l);

		JPanel pm = new JPanel();
		JPanel pi = new JPanel();

		pi.setSize(ic.getPreferredSize());
        pm.setSize(mc.getPreferredSize());
		pi.add(ic);
		pm.add(mc);
        c.add("North", pi);
        c.add(pm);

        JPanel p = new JPanel();
        p.add(l);
        c.add("South", p);
    }
}
