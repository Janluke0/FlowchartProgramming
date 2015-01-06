package ide;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import language.Piece;

public class ListInputHandler implements ListSelectionListener {

	private final JList<String> list;
	private final MainPanel panel;

	public ListInputHandler(final JList<String> list, final MainPanel panel) {
		this.list = list;
		this.panel = panel;
	}

	@Override
	public void valueChanged(final ListSelectionEvent e) {
		// Only add once for every selection, and make sure that at least one is
		// selected
		if (!e.getValueIsAdjusting() && list.getSelectedIndex() != -1) {
			final Class<? extends Piece> c = Piece.getPieces().get(
					list.getSelectedIndex());
			list.clearSelection();
			Constructor<? extends Piece> ctor = null;
			try {
				// type is because its int, not Integer
				ctor = c.getConstructor(Integer.TYPE, Integer.TYPE);
			} catch (NoSuchMethodException | SecurityException e1) {
				e1.printStackTrace();
				return;
			}
			Object createdPiece = null;
			try {
				// create piece in center of screen
				createdPiece = ctor.newInstance(new Object[] {
						panel.getSpaceX() + panel.getWidth() / 2,
						panel.getSpaceY() + panel.getHeight() / 2 });
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e1) {
				e1.printStackTrace();
				return;
			}
			panel.createPiece((Piece) createdPiece);
		}
	}
}
