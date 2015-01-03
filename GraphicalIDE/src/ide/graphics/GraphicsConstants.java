package ide.graphics;

import java.awt.Color;
import java.awt.Font;

public final class GraphicsConstants {
	// Constants class, can't be instantiated
	private GraphicsConstants() {
	}

	public static final Font APP_FONT = new Font("Arial", Font.PLAIN, 12);
	public static final Color MAIN_BACKROUND_COLOR = new Color(100, 125, 120);
	public static final Color MENU_BACKROUND_COLOR = new Color(140, 165, 160);
	public static final Color PIECE_BACKGROUND = new Color(190, 200, 200);
	public static final Color PIECE_TEXT = new Color(0, 10, 10);
	public static final Color MAIN_GRID_COLOR = new Color(180, 220, 220);
	public static final Color MAIN_GRID_ORIGIN_COLOR = new Color(150, 74, 89);
}
