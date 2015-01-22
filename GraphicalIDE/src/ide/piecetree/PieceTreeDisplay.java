package ide.piecetree;

import ide.graphics.GraphicsConstants;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

@SuppressWarnings("serial")
public class PieceTreeDisplay extends DefaultTreeCellRenderer implements
		TreeCellRenderer {
	public PieceTreeDisplay() {
		super();

		setTextNonSelectionColor(GraphicsConstants.PIECE_TREE_TEXT_COLOR);
		setFont(GraphicsConstants.APP_FONT);
		setLeafIcon(GraphicsConstants.PIECE_TREE_LEAF_ICON);
		setOpenIcon(GraphicsConstants.PIECE_TREE_OPEN_FOLDER_ICON);
		setClosedIcon(GraphicsConstants.PIECE_TREE_CLOSED_FOLDER_ICON);
	}
}
