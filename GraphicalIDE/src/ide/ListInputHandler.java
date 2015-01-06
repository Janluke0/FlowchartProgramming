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
		// Only add once for every selection
		if (!e.getValueIsAdjusting()) {
			final Class<? extends Piece> c = Piece.getPieces().get(list.getSelectedIndex());
			Constructor<? extends Piece> ctor = null;
			try {
				// type is because its int, not Integer
				ctor = c.getConstructor(Integer.TYPE, Integer.TYPE);
			} catch (NoSuchMethodException | SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			Object object = null;
			try {
				object = ctor.newInstance(new Object[] { 0, 0 });
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			panel.createPiece((Piece) object);
		}
	}

}
