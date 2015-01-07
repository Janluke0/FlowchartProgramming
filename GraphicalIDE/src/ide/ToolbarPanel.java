package ide;

import ide.graphics.GraphicsConstants;

import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The Class ToolbarPanel.
 */
@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {

	public ToolbarPanel() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.setColor(GraphicsConstants.MENU_BACKROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}
