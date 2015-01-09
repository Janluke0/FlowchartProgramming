package ide;

import ide.graphics.GraphicsConstants;
import ide.graphics.GraphicsUtils;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The Class ToolbarPanel.
 */
@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {

	public ToolbarPanel() {
		super();

		setLayout(new FlowLayout(FlowLayout.LEFT, GraphicsConstants.TOOLBAR_PADDING, GraphicsConstants.TOOLBAR_PADDING));
		addButton(GraphicsConstants.FILE_ICON, (e) -> {
			System.out.println("File icon clicked");
		});
		setBackground(GraphicsConstants.MENU_BACKROUND_COLOR);
	}

	private JButton addButton(final ImageIcon icon, final ActionListener al) {
		final JButton button = new JButton(fitToToolbar(icon));
		button.addActionListener(al);
		button.setBorder(null);

		add(button);

		return button;
	}

	private static ImageIcon fitToToolbar(final ImageIcon icon) {
		final int newHeight = GraphicsConstants.TOOLBAR_HEIGHT - GraphicsConstants.TOOLBAR_PADDING * 2;

		final int newWidth = (int) ((double) icon.getIconWidth() / icon.getIconHeight() * newHeight);
		return GraphicsUtils.resize(icon, newWidth, newHeight);
	}
}
