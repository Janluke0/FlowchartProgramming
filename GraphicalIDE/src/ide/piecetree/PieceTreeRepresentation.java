package ide.piecetree;

import java.util.Arrays;

import language.Piece;

public class PieceTreeRepresentation {
	public final Class<? extends Piece> clazz;
	public final String[] packageString;
	public final String name;

	public PieceTreeRepresentation(final Class<? extends Piece> clazz, final String[] packageString, final String name) {
		this.clazz = clazz;
		this.packageString = Arrays.copyOf(packageString, packageString.length);
		this.name = name;
	}

	@Override
	public String toString() {
		return clazz.getSimpleName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (clazz == null ? 0 : clazz.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + Arrays.hashCode(packageString);
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PieceTreeRepresentation other = (PieceTreeRepresentation) obj;
		if (clazz == null) {
			if (other.clazz != null) {
				return false;
			}
		} else if (!clazz.equals(other.clazz)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (!Arrays.equals(packageString, other.packageString)) {
			return false;
		}
		return true;
	}

}
