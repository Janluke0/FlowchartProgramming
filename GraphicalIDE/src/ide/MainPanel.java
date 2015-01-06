package ide;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import language.Piece;
import language.ProgramContext;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private final List<Piece> pieces = new ArrayList<>();
	private int x;
	private int y;

	private final Thread interpreterThread;

	private final MainPanelGraphicsHandler graphicsHandler;

	public MainPanel() {
		super();
		x = y = 0;
		final MainInputHandler input = new MainInputHandler(this);

		addMouseListener(input);
		addMouseMotionListener(input);

		graphicsHandler = new MainPanelGraphicsHandler(this);

		interpreterThread = new Thread(new InterpreterTask(this));
		interpreterThread.start();
	}

	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		getGraphicsHandler().draw((Graphics2D) g);
	}

	/**
	 * Centers this panel's position on (0,0)
	 * */
	public void centerOnOrigin() {
		x = -getWidth() / 2;
		y = -getHeight() / 2;
	}

	public void setSpacePosition(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	public int getSpaceX() {
		return x;
	}

	public int getSpaceY() {
		return y;
	}

	public Point getSpacePosition() {
		return new Point(x, y);
	}

	public Point getWorldCoordFromMouse(final Point p) {
		return new Point(x + p.x, y + p.y);
	}

	public synchronized List<Piece> getPieces() {
		return pieces;
	}

	private static class InterpreterTask implements Runnable {
		private final List<Piece> pieces;
		private final MainPanel mainPanel;

		public InterpreterTask(final MainPanel panel) {
			pieces = panel.getPieces();
			mainPanel = panel;
		}

		@Override
		public void run() {
			while (true) {
				synchronized (pieces) {
					final ProgramContext pc = new ProgramContext();
					for (final Piece p : pieces) {
						p.update(pc);
					}
				}
				synchronized (mainPanel) {
					mainPanel.repaint();
				}
				try {
					Thread.sleep(100);
				} catch (final InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void createPiece(final Piece piece) {
		synchronized (pieces) {
			pieces.add(piece);
		}
	}

	public MainPanelGraphicsHandler getGraphicsHandler() {
		return graphicsHandler;
	}
}
