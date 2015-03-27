package ide.toolbar;

import ide.WindowFrame;
import ide.graphics.GraphicsConstants;
import ide.graphics.GraphicsUtils;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * The Class ToolbarPanel.
 */
@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {

	public ToolbarPanel(final WindowFrame frame) {
		super();

		setLayout(new FlowLayout(FlowLayout.LEFT, GraphicsConstants.TOOLBAR_PADDING, GraphicsConstants.TOOLBAR_PADDING));

		addButton(GraphicsConstants.FILE_ICON, new FilePopupMenu(frame));
		addButton(GraphicsConstants.RUN_ICON, new RunPopupMenu(frame));

		setBackground(GraphicsConstants.MENU_BACKROUND_COLOR);
	}

	private JButton addButton(final ImageIcon icon, final JPopupMenu popup) {
		final JButton button = new JButton(fitToToolbar(icon));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		button.setBorder(null);
		button.setBorderPainted(false);
		button.setOpaque(false);
		button.setContentAreaFilled(false);

		add(button);

		return button;
	}

	private static ImageIcon fitToToolbar(final ImageIcon icon) {
		final int newHeight = GraphicsConstants.TOOLBAR_HEIGHT - GraphicsConstants.TOOLBAR_PADDING * 2;

		final int newWidth = (int) ((double) icon.getIconWidth() / icon.getIconHeight() * newHeight);
		return GraphicsUtils.resize(icon, newWidth, newHeight);
	}
}
