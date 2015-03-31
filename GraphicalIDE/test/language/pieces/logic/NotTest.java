package language.pieces.logic;

import language.Piece;
import language.pieces.TestHelper;
import language.pieces.logic.operators.Not;
import language.value.ProgramValueBoolean;

import org.junit.Test;

public class NotTest {

	@Test
	public void testUpdatePiece() {
		final Piece p = new Not(0, 0);
		final ProgramValueBoolean[][] inputs = { { ProgramValueBoolean.TRUE },//
				{ ProgramValueBoolean.FALSE },//
		};
		final ProgramValueBoolean[][] outputs = {
				{ ProgramValueBoolean.FALSE },//
				{ ProgramValueBoolean.TRUE },//
		};//
		TestHelper.truthTable(inputs, outputs, p);
	}
}
