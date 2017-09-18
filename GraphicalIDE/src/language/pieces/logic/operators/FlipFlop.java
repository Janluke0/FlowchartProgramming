package language.pieces.logic.operators;

import java.awt.Point;

import javax.swing.JOptionPane;

import language.Connection;
import language.Piece;
import language.ProgramContext;
import language.type.Type;
import language.value.ProgramValue;
import language.value.ProgramValueBoolean;

/**
 * The Class Add.
 */
public class FlipFlop extends Piece {

	ProgramValueBoolean currentValue;

	/**
	 * Instantiates a new flip flop piece
	 *
	 * @param x
	 *            the x
	 * @param y
	 *            the y
	 */
	public FlipFlop(final int x, final int y) {
		super(1, 1, x, y);
		setCurrentValue(ProgramValueBoolean.FALSE);
	}

	/**
	 * Name.
	 *
	 * @return the string
	 */
	public static String name() {
		return "Logic.Operators.FlipFlop";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#update(language.ProgramContext)
	 */
	@Override
	public void updatePiece(final ProgramContext context) {
		final ProgramValue<?> v1 = getInputs()[0];
		if (v1 instanceof ProgramValueBoolean) {
			if (((ProgramValueBoolean) v1).getValue() == true) {
				setCurrentValue(new ProgramValueBoolean(!currentValue.getValue()));
			}

		}
		for (final Connection c : getOutputs()) {
			c.changeInput(currentValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see language.Piece#doubleClicked(java.awt.Point)
	 */
	@Override
	public void doubleClicked(final Point p) {
		final String input = JOptionPane.showInputDialog("Set Value: ", String.valueOf(currentValue.getValue()));
		if (input != null) {
			setCurrentValue(new ProgramValueBoolean(Boolean.valueOf(input)));
		}
	}

	public ProgramValueBoolean getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(final ProgramValueBoolean currentValue) {
		this.currentValue = currentValue;
		setInputText(0, String.valueOf(currentValue.getValue()));
		this.updateNextTick();
	}

	@Override
	public boolean shouldUpdateEveryTick() {
		return false;
	}

	@Override
	public Type getOutputType() {
		return Type.BOOLEAN;
	}

}
