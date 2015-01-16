package ide.graphics;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;

import javax.swing.ImageIcon;

/**
 * The Class GraphicsConstants. Holds the IDE default colors and fonts.
 */
public final class GraphicsConstants {
	// Constants class, can't be instantiated
	private GraphicsConstants() {
	}

	public static final int TOOLBAR_HEIGHT = 40;
	public static final int TOOLBAR_PADDING = 5;

	/** The Default APP_FONT. */
	public static final Font APP_FONT = new Font("Arial", Font.PLAIN, 12);

	public static final Color PIECE_TREE_BACKGROUND_COLOR = new Color(55, 85,
			80);

	public static final Color PIECE_TREE_TEXT_COLOR = Color.WHITE;

	/** The Default MAIN_BACKROUND_COLOR. */
	public static final Color MAIN_BACKROUND_COLOR = new Color(100, 125, 120);

	/** The Default MENU_BACKROUND_COLOR. */
	public static final Color MENU_BACKROUND_COLOR = new Color(140, 165, 160);

	/** The Default PIECE_BACKGROUND. */
	public static final Color PIECE_BACKGROUND = new Color(190, 200, 200);

	/** The Default PIECE_TEXT. */
	public static final Color PIECE_TEXT = new Color(0, 10, 10);

	/** The Default MAIN_GRID_COLOR. */
	public static final Color MAIN_GRID_COLOR = new Color(180, 220, 220);

	/** The Default MAIN_GRID_ORIGIN_COLOR. */
	public static final Color MAIN_GRID_ORIGIN_COLOR = new Color(150, 74, 89);

	/** The Default LINE_DRAG_COLOR. */
	public static final Color LINE_DRAG_COLOR = Color.GREEN;

	public static final Color SELECTION_COLOR = new Color(255, 200, 0, 100);
	public static final int SELECTION_WIDTH = 3;

	/** The Default PORT_COLOR. */
	public static final Color PORT_COLOR = Color.RED;
	/**
	 * The Default File Icon.
	 */
	public static final ImageIcon FILE_ICON = loadImageIcon("/icons/toolbar/file.png");

	public static final int TRASH_WIDTH = 50;
	public static final int TRASH_BORDER_SIZE = 15;
	public static final ImageIcon TRASH_ICON = resizeImage(
			loadImageIcon("/icons/trashcan.png"), TRASH_WIDTH);
	private static final int LEAF_WIDTH = 15;
	public static final ImageIcon PIECE_TREE_LEAF_ICON = resizeImage(
			loadImageIcon("/icons/piecetree/circleicon.png"), LEAF_WIDTH);
	private static final int OPEN_FOLDER_WIDTH = 15;
	public static final ImageIcon PIECE_TREE_OPEN_FOLDER_ICON = resizeImage(
			loadImageIcon("/icons/piecetree/circleicon.png"), OPEN_FOLDER_WIDTH);
	private static final int CLOSED_FOLDER_WIDTH = 15;
	public static final ImageIcon PIECE_TREE_CLOSED_FOLDER_ICON = resizeImage(
			loadImageIcon("/icons/piecetree/closedcircleicon.png"),
			CLOSED_FOLDER_WIDTH);

	private static ImageIcon loadImageIcon(final String filename) {
		ImageIcon icon;
		try {
			icon = new ImageIcon(GraphicsUtils.loadImage(filename));
		} catch (final IOException e) {
			icon = new ImageIcon("Could not find image");
			e.printStackTrace();
		}
		return icon;
	}

	private static ImageIcon resizeImage(final ImageIcon original,
			final int width) {
		return new ImageIcon(GraphicsUtils.resize(original.getImage(), width,
				(int) (original.getIconHeight() * ((double) width / original
						.getIconWidth()))));
	}
}
