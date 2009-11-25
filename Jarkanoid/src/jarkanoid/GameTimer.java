package jarkanoid;

/*
 * Класс Таймера в потоке которого
 * выполняеттся передвижение.
 */
public class GameTimer extends Thread {
	/* Задержка */
	private int delay;
	
	/* Игровое поле */
	private GameField p;
	
	public GameTimer(GameField p, int delay) {
		this.p = p;
		this.delay = delay;
	}
	
	/* Интерфейсный метод класса Thread */
	public void run() {
		try {
			while (!interrupted()) {
				p.step();
				sleep(delay);			
			}
		} catch (InterruptedException e) {}
	}
}

