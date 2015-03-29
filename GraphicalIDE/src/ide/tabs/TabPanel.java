package ide.tabs;

import ide.WindowFrame;
import ide.graphics.GraphicsConstants;
import ide.mainpanel.MainPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class TabPanel extends JPanel {

	private final JPanel panel = new JPanel();

	private final List<MainPanelAndButton> components = new ArrayList<>();

	private final WindowFrame frame;

	public TabPanel(final WindowFrame frame) {
		super();
		this.frame = frame;

		setBackground(GraphicsConstants.MENU_BACKROUND_COLOR);

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		final JScrollPane scrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	public void addTab(final String s, final MainPanel panel) {

		final JButton button = new JButton(s);
		this.panel.add(button);

		components.add(new MainPanelAndButton(button, panel));
		frame.mainPanelHolder.add(panel, "" + (components.size() - 1));

		// if this is the only tab, select it
		if (components.size() == 1) {
			button.setBackground(GraphicsConstants.SELECTED_TAB_BACKGROUND);
			button.setForeground(GraphicsConstants.SELECTED_TAB_FOREGROUND);
		} else {
			button.setBackground(GraphicsConstants.DESELECTED_TAB_BACKGROUND);
			button.setForeground(GraphicsConstants.DESELECTED_TAB_FOREGROUND);
		}

		button.addActionListener((e) -> {
			for (int i = 0; i < components.size(); i++) {
				if (components.get(i).button.equals(button)) {

					components.get(i).button.setBackground(GraphicsConstants.SELECTED_TAB_BACKGROUND);
					components.get(i).button.setForeground(GraphicsConstants.SELECTED_TAB_FOREGROUND);

					frame.setMainPanel(components.get(i).panel);
					final CardLayout cl = (CardLayout) frame.mainPanelHolder.getLayout();
					cl.show(frame.mainPanelHolder, "" + i);
					frame.currentMainPanel = i;
				} else {
					components.get(i).button.setBackground(GraphicsConstants.DESELECTED_TAB_BACKGROUND);
					components.get(i).button.setForeground(GraphicsConstants.DESELECTED_TAB_FOREGROUND);
				}
			}
		});
		revalidate();
		repaint();
	}

	/**
	 * Sets the current MainPanel's filename in the tabs.
	 *
	 * @param s
	 */
	public void setFilename(final String s) {
		for (final Component c : panel.getComponents()) {
			if (c.equals(components.get(frame.currentMainPanel).button)) {
				((JButton) c).setText(s);
				break;
			}
		}
		revalidate();
		repaint();
	}

	private static class MainPanelAndButton {
		public JComponent button;
		public MainPanel panel;

		public MainPanelAndButton(final JComponent button, final MainPanel panel) {
			this.button = button;
			this.panel = panel;
		}

	}
}
