package language;

import ide.graphics.PieceRenderer;
import ide.piecetree.PieceTree;
import ide.piecetree.PieceTreeRepresentation;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.util.Arrays;
import java.util.Optional;

import language.type.Type;
import language.value.ProgramValue;
import language.value.ProgramValueNothing;

/**
 * Abstract class. To implement this class, you must add a method with the signature<br>
 * <code>public static String name()</code> <br>
 * and a default constructor with two int parameters (position)<br>
 * <code>public PieceXXX(int x, int y)</code>
 */
public abstract class Piece {

	/** Holds whether this piece should update next tick or not. */
	private boolean shouldUpdateNextTick = false;

	/** The inputs values */
	private final ProgramValue<?>[] inputs;

	/** The outputs connections. */
	private final Connection[] outputs;

	/** The x position of the upper left corner of this piece. */
	private int x;

	/** The y position of the upper right corner of this piece. */
	private int y;

	private String name;

	private final PieceRenderer renderer;

	/**
	 * Instantiates a new piece.
	 *
	 * @param inputs
	 *            the inputs
	 * @param outputs
	 *            the outputs
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	protected Piece(final int inputs, final int outputs, final int x, final int y) {
		this.inputs = new ProgramValue[inputs];
		this.outputs = new Connection[outputs];

		for (int i = 0; i < outputs; i++) {
			setOutput(i, new Connection(null, -1));
		}
		for (int i = 0; i < inputs; i++) {
			setInput(i, ProgramValueNothing.NOTHING);
		}

		// defaults to class name
		name = getClass().getSimpleName();
		for (final PieceTreeRepresentation p : PieceTree.getPieces()) {
			if (p.clazz.equals(getClass())) {
				name = p.name;
			}
		}

		setX(x);
		setY(y);

		renderer = new PieceRenderer(this);
	}

	/**
	 * Piece should take inputs and figure out its output
	 *
	 * @param programContext
	 *            the ProgramContext for this tick
	 */
	protected abstract void updatePiece(ProgramContext context);

	/**
	 * Pieces like time that take no inputs but decide outputs should update every tick. Otherwise, the pieces should
	 * update only when they receive input.
	 *
	 * @return whether this piece should update every tick
	 */
	public abstract boolean shouldUpdateEveryTick();

	public void update(final ProgramContext context) {
		try {
			updatePiece(context);
		} catch (final Exception e) {
			// don't let any exception thrown by pieces crash the application
			e.printStackTrace();
		}
		shouldUpdateNextTick = false;
	}

	/**
	 * Double click event.
	 *
	 * @param clickPoint
	 *            the point in world coordinates
	 */
	public abstract void doubleClicked(Point clickPoint);

	/**
	 * Assumes that this should draw at (0,0).
	 *
	 * @param g
	 *            the graphics object
	 */
	public void draw(final Graphics2D g) {
		renderer.draw(g);
	}

	public void drawConnections(final Graphics2D g) {
		renderer.drawConnections(g);
	}

	public abstract Type getOutputType();

	protected void setInputText(final int port, final String text) {
		renderer.setInputDisplay(port, text);
	}

	protected void setOutputText(final int port, final String text) {
		renderer.setOutputDisplay(port, text);
		shouldUpdateNextTick = true;
	}

	/**
	 * Output port containing point.
	 *
	 * @param worldCoord
	 *            the world coordinate
	 * @return the index of the connection that was clicked on
	 */
	public Optional<Integer> outputPortContainingPoint(final Point worldCoord) {
		return renderer.outputPortContainingPoint(worldCoord);
	}

	/**
	 * Contains point.
	 *
	 * @param worldCoord
	 *            the world coord
	 * @return true, if successful
	 */
	public boolean containsPoint(final Point worldCoord) {
		return renderer.containsPoint(worldCoord);
	}

	/**
	 * Change input.
	 *
	 * @param inputPort
	 *            the input port
	 * @param value
	 *            the value
	 */
	public void setInput(final int inputPort, final ProgramValue<?> value) {
		assert value != null;
		inputs[inputPort] = value;
		shouldUpdateNextTick = true;
	}

	/**
	 * Gets the outputs.
	 *
	 * @return the outputs
	 */
	public Connection[] getOutputs() {
		return Arrays.copyOf(outputs, outputs.length);
	}

	/**
	 * Sets the position.
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public void setPosition(final int x, final int y) {
		setX(x);
		setY(y);
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Point getPosition() {
		return new Point(getX(), getY());
	}

	/**
	 * Gets the inputs.
	 *
	 * @return the inputs
	 */
	public ProgramValue<?>[] getInputs() {
		return Arrays.copyOf(inputs, inputs.length);
	}

	/**
	 * p is translated so that the origin is (0,0) and the top left corner of this piece.
	 *
	 * @param i
	 *            the i
	 * @param p
	 *            the p
	 * @return true, if successful
	 */
	public boolean inputContainsPoint(final int i, final Point p) {
		return renderer.inputContainsPoint(i, p);
	}

	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @param x
	 *            the new x
	 */
	public void setX(final int x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @param y
	 *            the new y
	 */
	public void setY(final int y) {
		this.y = y;
	}

	/**
	 * Sets the output.
	 *
	 * @param index
	 *            the index
	 * @param connection
	 *            the connection
	 */
	public void setOutput(final int index, final Connection connection) {
		outputs[index] = connection;
		shouldUpdateNextTick = true;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public Connection getOutput(final int index) {
		return outputs[index];
	}

	public boolean shouldUpdateNextTick() {
		return shouldUpdateNextTick;
	}

	public Shape getBodyShape() {
		return renderer.getBodyShape();
	}

}
