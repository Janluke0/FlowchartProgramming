package language.pieces.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BooleanConstantTest {
	@Test
	public void testSetValue() {
		final BooleanConstant piece = new BooleanConstant(0, 0);
		piece.setValue(true);
		assertEquals(true, piece.getValue());
		piece.setValue(false);
		assertEquals(false, piece.getValue());
	}

}
