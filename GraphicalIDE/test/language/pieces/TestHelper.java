package language.pieces;

import language.Connection;
import language.Piece;
import language.value.ProgramValue;

import org.junit.Assert;

public class TestHelper {

	public static void truthTable(final ProgramValue[][] inputs,
			final ProgramValue[][] outputs, final Piece piece) {

		final Piece inputSink = new InputSinkPiece();
		piece.setOutput(0, new Connection(inputSink, 0));

		assert inputs.length == outputs.length;

		for (int i = 0; i < inputs.length; i++) {
			for (int j = 0; j < inputs[i].length; j++) {
				piece.setInput(j, inputs[i][j]);
			}
			piece.update();
			for (int j = 0; j < outputs[i].length; j++) {
				Assert.assertEquals(outputs[i][j], inputSink.getInputs()[j]);
			}
		}

	}
}
