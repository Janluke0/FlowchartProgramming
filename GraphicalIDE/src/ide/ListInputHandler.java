package ide;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import language.Piece;

// TODO: Auto-generated Javadoc
/**
 * The Class ListInputHandler.
 */
public class ListInputHandler implements TreeSelectionListener {

	/** The jlist. */
	private final JTree tree;

	/** The main panel. */
	private final MainPanel panel;

	/**
	 * Instantiates a new list input handler.
	 *
	 * @param list
	 *            the list
	 * @param panel
	 *            the panel
	 */
	public ListInputHandler(final JTree list, final MainPanel panel) {
		tree = list;
		this.panel = panel;
	}

	@Override
	public void valueChanged(final TreeSelectionEvent e) {
		if (tree.getSelectionCount() >= 1) {
			final DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

			if (node == null) {
				// Nothing is selected.
				return;
			}

			final Object nodeInfo = node.getUserObject();

			if (node.isLeaf()) {
				assert nodeInfo instanceof ClassDisplayer;
			} else {
				return;
			}

			// Get the class of the piece that was selected
			final Class<? extends Piece> c = ((ClassDisplayer) nodeInfo).c;
			// Deselect so it acts like a button
			tree.clearSelection();
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
				final int pieceX = panel.getViewX() + panel.getWidth() / 2;
				final int pieceY = panel.getViewY() + panel.getHeight() / 2;
				createdPiece = ctor.newInstance(new Object[] { pieceX, pieceY });
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				e1.printStackTrace();
				return;
			}
			panel.createPiece((Piece) createdPiece);
		}
	}
}
