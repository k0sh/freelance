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
	private GameInfo info;

	/* Флаг паузы */
	private boolean paused = true;

	/* Теущий счет и время игры */
	private int score;
	private int ticks;
	
	/* Ширина рамки */
	private static int frameThickness = 10;
	
	/* Размер игрового поля */
	private Dimension fieldSize = new Dimension(
				frameThickness * 2 + 420,
				frameThickness * 2 + 350);
	
	private Paddle paddle;
	private Ball ball;

	public GameField() {

		/* Устанавливаем фон */
		setBackground(Color.GRAY);
		
		/* Добавляем обработчики движения мыши */
		addMouseMotionListener(this);
		addMouseListener(this);
		
		/* Создаем игровые элементы: каретку, мяч*/
		paddle = new Paddle(fieldSize.width/2);
		ball   = new Ball(
				fieldSize.width/2, 
				fieldSize.height-frameThickness-Paddle.size.height-Ball.diam/2);

		/* Создаем и запускаем таймер */
		timer = new GameTimer(this, 10);
		timer.start();
		
		resetGame();
	}
	
	/* Инициализирует игру */
	private void initGame() {
		/* Переносим мяч на каретку */
		ball.stuck = true;
		ball.moveTo(new Point2D.Float(paddle.pos,
					    fieldSize.height - frameThickness - Paddle.size.height - Ball.diam/2), offscr);
		ball.reset();
	}

	/* Сброс игры в начальное положение */
	private void resetGame() {
		score = 0;
		ticks = 0;
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
		
		ticks++;

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
				
				score++;
				
			} else {
				/* Game over */

				ball.reset();
				paused = true;
				info.setStatus("Game over!");					
				repaint();

				/* Ждем 5 секунд */
				try {
					Thread.sleep(5000);
				}
				catch (InterruptedException e) {}
				resetGame();
				
				/* Устанавливаем мяч на каретку */
				ball.stuck = true;
				ball.moveTo(new Point2D.Float(paddle.pos, ball.r.y), offscr);
				repaint();
				return;
			}
		}
		
		/* Обнавляем статус */
		info.setScore(score);
		info.setTicks(ticks/100);

		/* Перемещаем мяч и отрисовываем поле */
		ball.moveTo(p, offscr);
		repaint();
	}
	
	public void setStatusPanel(GameInfo info) {
		this.info = info;
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

    /* Перерисовывает полностью все игровое поле */
    public void paintAll(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = getSize();

        Rectangle bnd = g2d.getClipBounds();
        g2d.setColor(Color.gray);
        g2d.clearRect(bnd.x, bnd.y, bnd.width, bnd.height);

        paintFrame(g2d);
        paddle.paint(g2d, d.height - frameThickness - Paddle.size.height);
        ball.paint(g2d);
    }

    /* Перерисовывает только изменившуюся часть игрового поля */
    public void paintUpdated(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Dimension d = getSize();

		g2d.clearRect(frameThickness, 
				d.height - frameThickness - Paddle.size.height - Ball.diam,
			 	d.width - frameThickness*2, Paddle.size.height + Ball.diam);
		ball.clear(g2d);

        paddle.paint(g2d, d.height - frameThickness - Paddle.size.height);
		ball.paint(g2d);
	}
    
    /* Переопределяем интерфейсный метод */
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

		/* Перемещаем каретку если она не выходит за пределы поля */
		if (x < frameThickness + Paddle.size.width/2) {
            paddle.pos = frameThickness + Paddle.size.width/2;
        } else if (x > fieldSize.width - frameThickness - Paddle.size.width/2) {
            paddle.pos = fieldSize.width - frameThickness - Paddle.size.width/2;
        } else {
            paddle.pos = x;
        }

        /* Если мяч прикреплен к каретке начинаем его движение. */
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
