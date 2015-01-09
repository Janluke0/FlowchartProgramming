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

	/** The Default PORT_COLOR. */
	public static final Color PORT_COLOR = Color.RED;

	// All icons are not resized, they are the default size.
	/**
	 * The Default File Icon.
	 */
	public static final ImageIcon FILE_ICON = loadImageIcon("/icons/toolbar/file.png");
	public static final ImageIcon TRASH_ICON = loadImageIcon("/icons/trashcan.png");
	public static final int TRASH_BORDER_SIZE = 25;

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
}
