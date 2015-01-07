package ide;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

@SuppressWarnings("serial")
public class PieceList extends JTree {
	public PieceList(final MainPanel panel, final DefaultMutableTreeNode root) {
		super(root);
		setRootVisible(false);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		addTreeSelectionListener(new ListInputHandler(this, panel));
	}
}
