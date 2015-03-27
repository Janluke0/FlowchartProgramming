package ide.piecetree;

import ide.WindowFrame;

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
public class PieceTreeInputHandler implements TreeSelectionListener {

	/** The jlist. */
	private final JTree tree;

	/** The main frame to get the current panel from. */
	private final WindowFrame frame;

	/**
	 * Instantiates a new list input handler.
	 *
	 * @param list
	 *            the list
	 * @param panel
	 *            the panel
	 */
	public PieceTreeInputHandler(final JTree list, final WindowFrame frame) {
		tree = list;
		this.frame = frame;
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
				assert nodeInfo instanceof PieceTreeRepresentation;
			} else {
				return;
			}

			// Get the class of the piece that was selected
			final Class<? extends Piece> c = ((PieceTreeRepresentation) nodeInfo).clazz;
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
				final int pieceX = frame.getMainPanel().getViewX() + frame.getMainPanel().getWidth() / 2;
				final int pieceY = frame.getMainPanel().getViewY() + frame.getMainPanel().getHeight() / 2;
				createdPiece = ctor.newInstance(new Object[] { pieceX, pieceY });
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				e1.printStackTrace();
				return;
			}
			frame.getMainPanel().createPiece((Piece) createdPiece);
		}
	}
}
