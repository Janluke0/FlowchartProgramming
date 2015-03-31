package language.pieces.logic;

import language.Piece;
import language.pieces.TestHelper;
import language.pieces.logic.operators.And;
import language.value.ProgramValueBoolean;

import org.junit.Test;

public class AndTest {

	@Test
	public void testUpdatePiece() {
		final Piece p = new And(0, 0);
		final ProgramValueBoolean[][] inputs = {
				{ ProgramValueBoolean.TRUE, ProgramValueBoolean.TRUE },
				{ ProgramValueBoolean.FALSE, ProgramValueBoolean.FALSE },
				{ ProgramValueBoolean.TRUE, ProgramValueBoolean.FALSE },
				{ ProgramValueBoolean.FALSE, ProgramValueBoolean.TRUE } };
		final ProgramValueBoolean[][] outputs = { { ProgramValueBoolean.TRUE },//
				{ ProgramValueBoolean.FALSE },//
				{ ProgramValueBoolean.FALSE },//
				{ ProgramValueBoolean.FALSE } };//
		TestHelper.truthTable(inputs, outputs, p);
	}
}
