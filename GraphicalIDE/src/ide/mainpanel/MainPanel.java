package ide.mainpanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import ide.graphics.GraphicsConstants;
import language.Piece;
import language.ProgramContext;

/**
 * The Class MainPanel.
 */
@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private Optional<File> filename = Optional.empty();

	/** The pieces. */
	private final List<Piece> pieces;

	private final List<Piece> selectedPieces = new ArrayList<>();

	/** The x coordinate of the view frame. */
	private int x;

	/** The y coordinate of the view frame. */
	private int y;

	/** The interpreter thread. This thread constantly updates every piece. */
	private final transient Thread interpreterThread;
	private final InterpreterTask interpreterTask;

	/** The graphics handler. */
	private final transient MainPanelGraphicsHandler graphicsHandler;

	final ProgramContext context;

	/**
	 * Instantiates a new main panel.
	 */
	public MainPanel(JTextArea console) {
		super();
		x = y = 0;
		pieces = new ArrayList<>();
		final MainInputHandler input = new MainInputHandler(this);

		addMouseListener(input);
		addMouseMotionListener(input);
		addKeyListener(input);

		graphicsHandler = new MainPanelGraphicsHandler(this);

		context = new ProgramContext(new TextAreaOutputStream(console));

		interpreterThread = new Thread(interpreterTask = new InterpreterTask(this));
		setFocusable(true);
		requestFocusInWindow();
	}

	public MainPanel(final File file, JTextArea console) {
		this(console);
		load(file);
	}

	/**
	 * Starts the interpreter thread.
	 *
	 */
	public MainPanel start() {
		interpreterThread.start();
		return this;
	}

	public void stop() {
		interpreterTask.stop();
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

	public boolean pointIsInTrash(final Point worldCoord) {
		final Point screenCoord = new Point(worldCoord);
		screenCoord.translate(-x, -y);
		final int imageWidth = GraphicsConstants.TRASH_ICON.getImage().getWidth(null);
		final int imageHeight = GraphicsConstants.TRASH_ICON.getImage().getHeight(null);

		if (new Rectangle(getWidth() - imageWidth - GraphicsConstants.TRASH_BORDER_SIZE,
				getHeight() - imageHeight - GraphicsConstants.TRASH_BORDER_SIZE, imageWidth, imageHeight)
						.contains(screenCoord)) {
			return true;
		}
		return false;
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

	public void reset() {
		context.reset();
	}

	/**
	 * Creates the piece and selects it.
	 *
	 * @param piece
	 *            the piece
	 */
	public void createPiece(final Piece piece) {
		synchronized (getPieces()) {
			getPieces().add(piece);
			getSelectedPieces().clear();
			getSelectedPieces().add(piece);
		}
	}

	/**
	 * Returns the graphics handler.
	 *
	 * @return the graphics handler
	 */
	public MainPanelGraphicsHandler getGraphicsHandler() {
		return graphicsHandler;
	}

	/**
	 * Returns a list of the currently selected pieces
	 *
	 * @return
	 */
	public List<Piece> getSelectedPieces() {
		return selectedPieces;
	}

	/**
	 * If we have a filename, returns that. If not, we ask the user for one with
	 * a JFileChooser dialog.
	 *
	 * @return the current filename or {@link MainPanel#askForAndGetFilename()}
	 */
	public Optional<File> getOrAskForFilename() {
		if (filename.isPresent()) {
			return filename;
		}
		return askForAndGetFilename();
	}

	/**
	 * Asks the user for a filename with a JFileChooser dialog.
	 *
	 * @return the file or empty if the user clicked cancel
	 */
	public Optional<File> askForAndGetFilename() {
		final JFileChooser chooser = new JFileChooser();
		final int returnVal = chooser.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			filename = Optional.of(chooser.getSelectedFile());
			return Optional.of(chooser.getSelectedFile());
		}
		return Optional.empty();
	}

	/**
	 * Saves the currently open file into the passed in file
	 *
	 * @param file
	 *            the file to save to
	 */
	public void save(final File file) {
		// TODO Auto-generated method stub

	}

	/**
	 * Loads the passed in file into this main panel
	 *
	 * @param file
	 *            the file to load from
	 */
	private void load(final File file) {
		// TODO Auto-generated method stub

	}

}
