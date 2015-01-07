package ide;

import language.Piece;

public class PieceTreeRepresentation {
	public final Class<? extends Piece> clazz;
	public final String[] packageString;

	public PieceTreeRepresentation(final Class<? extends Piece> clazz,
			final String[] packageString) {
		this.clazz = clazz;
		this.packageString = packageString;
	}

	@Override
	public String toString() {
		return clazz.getSimpleName();
	}
}
