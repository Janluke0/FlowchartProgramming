package language.pieces.logic;

import language.Piece;
import language.pieces.TestHelper;
import language.pieces.logic.operators.Or;
import language.value.ProgramValueBoolean;

import org.junit.Test;

public class OrTest {

	@Test
	public void testUpdatePiece() {
		final Piece p = new Or(0, 0);
		final ProgramValueBoolean[][] inputs = {
				{ ProgramValueBoolean.TRUE, ProgramValueBoolean.TRUE },
				{ ProgramValueBoolean.FALSE, ProgramValueBoolean.FALSE },
				{ ProgramValueBoolean.TRUE, ProgramValueBoolean.FALSE },
				{ ProgramValueBoolean.FALSE, ProgramValueBoolean.TRUE } };
		final ProgramValueBoolean[][] outputs = { { ProgramValueBoolean.TRUE },//
				{ ProgramValueBoolean.FALSE },//
				{ ProgramValueBoolean.TRUE },//
				{ ProgramValueBoolean.TRUE } };//
		TestHelper.truthTable(inputs, outputs, p);
	}
}
