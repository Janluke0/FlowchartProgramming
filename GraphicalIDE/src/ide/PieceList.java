package ide;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

@SuppressWarnings("serial")
public class PieceList extends JTree {
	public PieceList(final MainPanel panel, final DefaultMutableTreeNode root) {
		super(new AlphabeticalTreeModel(root));

		setRootVisible(false);
		getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		addTreeSelectionListener(new ListInputHandler(this, panel));
		System.out.println(getModel().getRoot());
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
					final DefaultMutableTreeNode there = sort(root
							.getChildAt(j));
					final Comparable hereObject = (Comparable) here
							.getUserObject();
					final Comparable thereObject = (Comparable) there
							.getUserObject();
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
