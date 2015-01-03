package ide;

import java.awt.Graphics;

import javax.swing.JPanel;

public class ToolbarPanel extends JPanel {
	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.setColor(GraphicsConstants.MENU_BACKROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
