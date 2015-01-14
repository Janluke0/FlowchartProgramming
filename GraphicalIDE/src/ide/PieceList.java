package ide;

import ide.graphics.GraphicsConstants;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import language.Piece;

@SuppressWarnings("serial")
public class PieceList extends JTree {
	private static DefaultMutableTreeNode root;
	static {
		// Initialize the root node and all the children node from
		// Piece.getPieceNames()
		root = new DefaultMutableTreeNode("Root");
		for (final PieceTreeRepresentation cl : Piece.getPieceNames()) {
			DefaultMutableTreeNode folderParent = root;
			final String[] folder = cl.packageString;
			// Don't do the last one
			for (int i = 0; i < folder.length; i++) {
				final String s = folder[i];
				@SuppressWarnings("unchecked")
				final Enumeration<DefaultMutableTreeNode> e = folderParent.children();
				DefaultMutableTreeNode sChild = null;
				boolean hasSChild = false;
				while (e.hasMoreElements()) {
					final DefaultMutableTreeNode element = e.nextElement();
					if (element.getUserObject().equals(s)) {

						// folderParent already contains a child named s
						hasSChild = true;
						sChild = element;
						break;
					}
				}
				if (hasSChild) {
					// We don't need to create it
					folderParent = sChild;

				} else {
					// adds a folder node, stored as a string
					final DefaultMutableTreeNode newParent = new DefaultMutableTreeNode(s);
					folderParent.add(newParent);
					// recurse for next string with this as the parent
					folderParent = newParent;
				}
			}
			// Adds a leaf node
			folderParent.add(new DefaultMutableTreeNode(cl));
		}

	}

	public PieceList(final MainPanel panel) {
		super(new AlphabeticalTreeModel(root));

		setRootVisible(false);
		setBackground(GraphicsConstants.LIST_BACKGROUND_COLOR);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		addTreeSelectionListener(new ListInputHandler(this, panel));
	}

	private static class AlphabeticalTreeModel extends DefaultTreeModel {

		public AlphabeticalTreeModel(final TreeNode root) {
			super(sort(root));
		}

		private static DefaultMutableTreeNode sort(final TreeNode a) {
			final DefaultMutableTreeNode root = (DefaultMutableTreeNode) a;

			final int cc = root.getChildCount();
			for (int i = 0; i < cc - 1; i++) {
				for (int j = i + 1; j <= cc - 1; j++) {
					final DefaultMutableTreeNode here = sort(root.getChildAt(i));
					final DefaultMutableTreeNode there = sort(root.getChildAt(j));

					// If its a leaf, it is a PieceTreeRepresentation, else it
					// is a string
					final Object hereObject = here.getUserObject();
					final Object thereObject = there.getUserObject();
					String s1 = null;
					String s2 = null;

					if (hereObject instanceof String) {
						s1 = (String) hereObject;
					}
					if (thereObject instanceof String) {
						s2 = (String) thereObject;
					}
					if (hereObject instanceof PieceTreeRepresentation) {
						s1 = ((PieceTreeRepresentation) hereObject).name;
					}
					if (thereObject instanceof PieceTreeRepresentation) {
						s2 = ((PieceTreeRepresentation) thereObject).name;
					}
					assert s1 != null && s2 != null;

					if (s1.compareTo(s2) > 0) {
						root.remove(here);
						root.remove(there);
						root.insert(there, i);
						root.insert(here, j);
					}
				}
			}

			return root;
		}

	}
}
