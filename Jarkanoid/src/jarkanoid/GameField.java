package jarkanoid;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.*;

public class GameField extends JPanel implements MouseMotionListener, MouseListener {
	
	private Image offscr;
	private GameTimer timer;
	private JLabel lblStatus;

	/* Флаг паузы */
	private boolean paused = true;
	/* Флаг перересовки блоков */
	private boolean repaintBlocks = false;

	/* Теущий счет */
	private int score, balls;
	
	/* Реальное количество блоков */
	private int blockCnt = 0;
	
	/* Ширина рамки */
	private static int frameThickness = 10;
	
	/* Количество блоков в ряду/в столбце */
	private static int blocksPerRow = 20;
	private static int blocksPerCol = 20;
	/* Размер игрового поля */
	private Dimension fieldSize = new Dimension(
				frameThickness * 2 + blocksPerRow * Block.size.width,
				frameThickness * 2 + blocksPerCol * Block.size.height + 50);
	
	private Paddle paddle;
	private Ball ball;

	/* Массив блоков */
	private Block[][] blocks;
	
	public GameField() {

		/* Устанавливаем фон */
		setBackground(Color.GRAY);
		
		/* Добавляем обработчики движения мыши */
		addMouseMotionListener(this);
		addMouseListener(this);
		
		/* Создаем игровые элементы: каретку, мяч и блоки */
		paddle = new Paddle(fieldSize.width/2);
		ball   = new Ball(
				fieldSize.width/2, 
				fieldSize.height-frameThickness-Paddle.size.height-Ball.diam/2);
		blocks = new Block[blocksPerRow][blocksPerCol];		

		/* Создаем и запускаем таймер */
		timer = new GameTimer(this, 10);
		timer.start();
		
		resetGame();
	}
	
	/* Инициализирует массив блоков */
	private void initBlocks() {
		int i, j;		
        int t = frameThickness;

		blockCnt = 0;
		for (i = 0; i < blocksPerCol; i++) {

			for (j = 0; j < blocksPerRow; j++) {
				/* Второй ряд заполняем блоками */
				if (i == 2) {
					blocks[j][i] = new Block(
									t + j * Block.size.width,
									t + i * Block.size.height
									);
					blockCnt++;
				} else {
					blocks[j][i] = null;
				}
			}
		}	
	}
	
	/* Инициализирует игру */
	private void initGame() {
		/* Переносим мяч на каретку */
		ball.stuck = true;
		ball.moveTo(new Point2D.Float(paddle.pos,
					    fieldSize.height - frameThickness - Paddle.size.height - Ball.diam/2), offscr);
		ball.reset();
			
		initBlocks();
		
		repaintBlocks = true;
	}

	/* Сброс игры в начальное положение */
	private void resetGame() {
		score = 0;
		balls = 3;
		initGame();
		paused = false;
	}
	
	/* Передвигает мяч */
	public void step() {
		if (paused) {
			return;
		}

		Point2D.Float p = ball.step();
        int w = fieldSize.width;
        int h = fieldSize.height;
		int t = frameThickness;

		/* Не перемещать мяч если он приклеен к каретке */
		if (ball.stuck) {
		    return;
		}

		/* Левая граница */
		if (p.x <= t + Ball.diam/2) {
			p.x = t + Ball.diam/2;
		    ball.v.x = -ball.v.x;
		}

		/* Правая граница */
		if (p.x >= w - t - Ball.diam/2) {
			p.x = w - t - Ball.diam/2;
			ball.v.x = -ball.v.x;
		}

		/* Верхняя граница */
		if (p.y <= t + Ball.diam/2) {
			p.y = t + Ball.diam/2;
			ball.v.y = -ball.v.y;
		}

		/* Нижняя граница -- проверяем попал ли
		 * мяч в каретку или нет */
		if (p.y >= h - t - Ball.diam/2 - Paddle.size.height) {
			p.y = h - t - Ball.diam/2 - Paddle.size.height;

			if ((p.x >= paddle.pos - Paddle.size.width/2 - Ball.diam + 1) &&
				(p.x <= paddle.pos + Paddle.size.width/2 + Ball.diam - 1)) {

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
				 * theta  - угол падения
				 * theta' - угол отражения
				 *
				 *                      paddle.pos
				 *                          v
				 * |----|----|----|----|----|----|----|----|----|----|
				 *  ---------           +++++++++           ---------
				 *
				 *
				 */
				if (p.x < paddle.pos - (Paddle.size.width * 0.3f)) {
					theta = theta * 0.9f;
					if (theta > (1f/16f) * Math.PI && theta < (15f/32f) * Math.PI) {
						ball.v.y = -l * (float)Math.sin(theta);
						ball.v.x = -l * (float)Math.cos(theta);
					}
				} else if ((p.x > paddle.pos - (Paddle.size.width * 0.1f)) && 
					   (p.x < paddle.pos + (Paddle.size.width * 0.1f))) {
					theta = theta * 1.1f;
					if (theta > (1f/16f) * Math.PI && theta < (15f/32f) * Math.PI) {
						ball.v.y = -l * (float)Math.sin(theta);

						if (p.x <= paddle.pos) {
							ball.v.x = -l * (float)Math.cos(theta);
						} else {
							ball.v.x = l * (float)Math.cos(theta);
						}
					}
				} else if (p.x > paddle.pos + (Paddle.size.width * 0.3f)) {
					theta = theta * 0.9f;
					if (theta > (1f/16f) * Math.PI && theta < (15f/32f) * Math.PI) {
						ball.v.y = -l * (float)Math.sin(theta);
						ball.v.x = l * (float)Math.cos(theta);
					}
				}
			} else {

				balls--;
				ball.reset();

				/* Game over -- Если у игрока закончились мячи */
				if (balls < 0) {
					paused = true;
					updateMessage();
					balls = 0;
					repaint();

					/* Ждем 5 секунд */
					try {
						Thread.sleep(5000);
					}
					catch (InterruptedException e) {}
					resetGame();
				}
				
				/* Устанавливаем мяч на каретку */
				ball.stuck = true;
				ball.moveTo(new Point2D.Float(paddle.pos, ball.r.y), offscr);
				repaint();
				return;
			}
		}

		float hbdm = Ball.diam/2;
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
				blocks[idx1.x][idx1.y] = null;
				blockCnt--;
				repaintBlocks = true;
				score += 10;
				hit += 1;
			}
		}

		/* Upper right corner */
		idx2 = getBlockIdx(p.x + hbdm, p.y - hbdm);
		if (idx2.x > -1 && idx2.y > -1) {
			blk = blocks[idx2.x][idx2.y];
			if (blk != null) {
				if (!(idx2.x == idx1.x && idx2.y == idx1.y)) {
					blocks[idx2.x][idx2.y] = null;
					blockCnt--;
					repaintBlocks = true;
					score += 10;
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
					blocks[idx3.x][idx3.y] = null;
					blockCnt--;
					repaintBlocks = true;
					score += 10;
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
					blocks[idx4.x][idx4.y] = null;
					blockCnt--;
					repaintBlocks = true;
					score += 10;
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
			resetGame();
		}
	}

	/* Gets the inner offset of a ball's corner inside a
	 * single block. */
    private Point getInnerOffset(float x, float y) {
		Point p = new Point();
		p.x = ((int)x - frameThickness) % Block.size.width;
		p.y = ((int)y - frameThickness) % Block.size.height;
		return p;
    }

	/* Gets the block coordinates corresponding to the
	 * physical coordinates of the specified point. */
    private Point getBlockIdx(float x, float y) {
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
	
	/* Обнавляет надпись статуса игры */
    private void updateMessage() {
		if (balls < 0) {
			lblStatus.setText("Game over!");
			return;
		}

		if (blockCnt == 0) {
			lblStatus.setText("Congratulations!");
			return;
		}
    }
	
	public void setStatusLabel(JLabel l) {
		this.lblStatus = l;
	}
	
    public void paintBlocks(Graphics2D g) {
		int i, j;

		for (i = 0; i < blocksPerCol; i++) {
			for (j = 0; j < blocksPerRow; j++) {
				if (blocks[j][i] != null) {
					blocks[j][i].paint(g);
				}
			}
		}
    }

    /* Рисует рамку вокруг игрового поля */
    public void paintFrame(Graphics2D g) {
        Paint p = g.getPaint();
        int w = fieldSize.width;
        int h = fieldSize.height;
        int t = frameThickness;
      
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, w, t);
        g.fillRect(0, 0, t, h);
        g.fillRect(w-t, 0, t, h);

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
        paddle.paint(g2d, d.height - frameThickness - Paddle.size.height);
        ball.paint(g2d);
    }

    public void paintUpdated(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = getSize();

		g2d.clearRect(frameThickness, 
				d.height - frameThickness - Paddle.size.height - Ball.diam,
			 	d.width - frameThickness*2, Paddle.size.height + Ball.diam);
		ball.clear(g2d);

    	if (repaintBlocks) {
			g2d.clearRect(frameThickness, frameThickness,
						  d.width - frameThickness*2, d.height - frameThickness*2);
			paintBlocks(g2d);
			repaintBlocks = false;
		}

        paddle.paint(g2d, d.height - frameThickness - Paddle.size.height);
		ball.paint(g2d);
	}
    
	public synchronized void paint(Graphics g) {
		update(g);
	}
	
	public synchronized void update(Graphics g) {
		Graphics2D g2;
		Dimension d = getSize();
		
		if (offscr == null) {
			offscr = createImage(d.width, d.height);
			g2 = (Graphics2D) offscr.getGraphics();
			g2.setClip(0, 0, d.width, d.height);
			paintAll(g2);
		} else {
			g2 = (Graphics2D) offscr.getGraphics();
			g2.setClip(0, 0, d.width, d.height);
			paintUpdated(g2);
		}
		g.drawImage(offscr, 0, 0, null);
		g2.dispose();
	}
	
	public void invalidate() {
		super.invalidate();
		offscr = null;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
        mouseMoved(e);	
	}

	@Override
	public void mouseMoved(MouseEvent e) {
        int x = e.getX();

		if (paused) {
			return;
		}

		/* Move the paddle if the mouse pointer isn't outside of the game board. */
		if (x < frameThickness + Paddle.size.width/2) {
            paddle.pos = frameThickness + Paddle.size.width/2;
        } else if (x > fieldSize.width - frameThickness - Paddle.size.width/2) {
            paddle.pos = fieldSize.width - frameThickness - Paddle.size.width/2;
        } else {
            paddle.pos = x;
        }

        /* Is the ball stuck to the paddle? If so, reposition it so
         * that it follows the paddle's movement. */
		if (ball.stuck) {
			ball.moveTo(new Point2D.Float(paddle.pos,
					    fieldSize.height - frameThickness - Paddle.size.height - Ball.diam/2), offscr);
        }
        repaint();
	
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (paused) {
			return;
		}
		ball.stuck = false;
	}

	public Dimension getPreferredSize() {
        return fieldSize;
    }
}
