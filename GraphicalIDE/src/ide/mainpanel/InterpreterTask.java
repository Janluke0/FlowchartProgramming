package ide.mainpanel;

import language.Piece;

/**
 * The Class InterpreterTask.
 */
class InterpreterTask implements Runnable {

	private static final int FRAMES_PER_SECOND = 60;
	private static final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;

	private boolean shouldStop = false;

	private final MainPanel mainPanel;

	/**
	 * @param mainPanel
	 */
	InterpreterTask(final MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		long nextTime = System.currentTimeMillis();
		long sleepTime = 0;

		while (!shouldStop) {
			synchronized (mainPanel.getPieces()) {
				for (final Piece p : mainPanel.getPieces()) {
					if (p.shouldUpdateNextTick() || p.shouldUpdateEveryTick()) {
						p.update(mainPanel.context);
					}
				}
			}

			nextTime += SKIP_TICKS;
			sleepTime = nextTime - System.currentTimeMillis();

			mainPanel.repaint(sleepTime);

			if (sleepTime >= 0) {
				try {
					Thread.sleep(sleepTime);
				} catch (final InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				// we are running behind!
			}
		}
	}

	public void stop() {
		shouldStop = true;
	}

}