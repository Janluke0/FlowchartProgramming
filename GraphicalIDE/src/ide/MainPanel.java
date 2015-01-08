package ide;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import language.Piece;
import language.ProgramContext;

// TODO: Auto-generated Javadoc
/**
 * The Class MainPanel.
 */
@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	/** The pieces. */
	private final List<Piece> pieces;

	/** The x coordinate of the view frame. */
	private int x;

	/** The y coordinate of the view frame. */
	private int y;

	/** The interpreter thread. This thread constantly updates every piece. */
	private final transient Thread interpreterThread;

	/** The graphics handler. */
	private final transient MainPanelGraphicsHandler graphicsHandler;

	/**
	 * Instantiates a new main panel.
	 */
	public MainPanel() {
		super();
		x = y = 0;
		pieces = new ArrayList<>();
		final MainInputHandler input = new MainInputHandler(this);

		addMouseListener(input);
		addMouseMotionListener(input);

		graphicsHandler = new MainPanelGraphicsHandler(this);

		interpreterThread = new Thread(new InterpreterTask());
	}

	public MainPanel start() {
		interpreterThread.start();
		return this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		getGraphicsHandler().draw((Graphics2D) g);
	}

	/**
	 * Centers this panel's position on (0,0).
	 */
	public void centerOnOrigin() {
		x = -getWidth() / 2;
		y = -getHeight() / 2;
	}

	/**
	 * Sets the position of the view.
	 *
	 * @param x
	 *            the x coordinate
	 * @param y
	 *            the y
	 */
	public void setViewPosition(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the space x.
	 *
	 * @return the space x
	 */
	public int getViewX() {
		return x;
	}

	/**
	 * Gets the space y.
	 *
	 * @return the space y
	 */
	public int getViewY() {
		return y;
	}

	/**
	 * Gets the space position.
	 *
	 * @return the space position
	 */
	public Point getViewPosition() {
		return new Point(x, y);
	}

	/**
	 * Gets the coordinate in the world from a mouse coordinate.
	 *
	 * @param p
	 *            the p
	 * @return the world coordinate from mouse
	 */
	public Point getWorldCoordFromMouse(final Point p) {
		return new Point(x + p.x, y + p.y);
	}

	/**
	 * Gets the pieces.
	 *
	 * @return the pieces
	 */
	public List<Piece> getPieces() {
		return pieces;
	}

	/**
	 * The Class InterpreterTask.
	 */
	private class InterpreterTask implements Runnable {
		private static final int FRAMES_PER_SECOND = 60;
		private static final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;

		/*
		 * (non-Javadoc)
		 *
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {
			long nextTime = System.currentTimeMillis();
			long sleepTime = 0;

			while (true) {
				synchronized (getPieces()) {
					final ProgramContext pc = new ProgramContext();
					for (final Piece p : getPieces()) {
						p.update(pc);
					}
				}

				nextTime += SKIP_TICKS;
				sleepTime = nextTime - System.currentTimeMillis();

				repaint(sleepTime);

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
	}

	/**
	 * Creates the piece.
	 *
	 * @param piece
	 *            the piece
	 */
	public void createPiece(final Piece piece) {
		synchronized (getPieces()) {
			getPieces().add(piece);
		}
	}

	/**
	 * Gets the graphics handler.
	 *
	 * @return the graphics handler
	 */
	public MainPanelGraphicsHandler getGraphicsHandler() {
		return graphicsHandler;
	}
}
