package ide.tabs;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;

import ide.WindowFrame;
import ide.graphics.GraphicsConstants;
import ide.mainpanel.MainPanel;
import ide.toolbar.FilePopupMenu;

@SuppressWarnings("serial")
public class TabPanel extends JPanel {

	private final JPanel panel = new JPanel();

	private final List<MainPanelAndButton> components = new ArrayList<>();

	private final WindowFrame frame;

	public TabPanel(final WindowFrame frame) {
		super();
		this.frame = frame;

		panel.setBackground(GraphicsConstants.MENU_BACKROUND_COLOR);
		setBackground(GraphicsConstants.MENU_BACKROUND_COLOR);

		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		final JScrollPane scrollPane = new JScrollPane(panel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getHorizontalScrollBar().setUI(new TabPanelScrollBarUI());

		setLayout(new BorderLayout());
		scrollPane.setBorder(null);
		add(scrollPane, BorderLayout.CENTER);
	}

	public void addTab(final String s, final MainPanel panel, JTextArea console) {

		final JButton button = new JButton(s);
		button.addMouseListener(new MouseAdapter() {
			FilePopupMenu filePopupMenu = new FilePopupMenu(frame);

			@Override
			public void mousePressed(final MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					button.getModel().setArmed(true);
					button.getModel().setPressed(true);
					filePopupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
				switchToPanel(button);
			}

			@Override
			public void mouseReleased(final MouseEvent e) {
				if (button.getModel().isPressed()) {
					button.getModel().setArmed(false);
					button.getModel().setPressed(false);
				}
			}
		});

		this.panel.add(button);

		components.add(new MainPanelAndButton(button, panel, console));
		frame.mainPanelHolder.add(panel, String.valueOf(components.size() - 1));
		frame.consoleHolder.add(new JScrollPane(console,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),  String.valueOf(components.size() - 1));

		highlightSelected();

		// Select it if we're creating the only tab available
		if (components.size() == 1) {
			switchToPanel(0);
		}

		revalidate();
		repaint();
	}

	private void switchToPanel(final JButton button) {
		for (int i = 0; i < components.size(); i++) {
			if (components.get(i).button.equals(button)) {
				switchToPanel(i);
			}
		}
	}

	private void switchToPanel(int i) {
		if(components.size() <= i){
			return;
		}
		frame.setMainPanel(components.get(i).panel);
		final CardLayout cl = (CardLayout) frame.consoleHolder.getLayout();
		cl.show(frame.consoleHolder, String.valueOf(i));
		final CardLayout cl2 = (CardLayout) frame.mainPanelHolder.getLayout();
		cl2.show(frame.mainPanelHolder, String.valueOf(i));
		frame.currentMainPanel = i;

		frame.revalidate();
		frame.repaint();

		highlightSelected();
	}

	public void closeTab() {

		final int current = frame.currentMainPanel;
		components.get(current).panel.stop();
		components.remove(current);
		panel.remove(current);
		frame.mainPanelHolder.remove(current);
		frame.consoleHolder.remove(current);
		
		//re-add components with correct indices
		for(int i = 0; i < components.size(); i++){
			MainPanelAndButton comp = components.get(i);
			
			frame.mainPanelHolder.remove(comp.panel);
			frame.mainPanelHolder.add(comp.panel, String.valueOf(i));
			frame.consoleHolder.remove(comp.console);
			frame.consoleHolder.add(comp.console, String.valueOf(i));
		}
		
		switchToPanel(Math.max(0, frame.currentMainPanel - 1));

		revalidate();
		repaint();
	}

	public void closeAllTab() {
		while(components.size() > 0){
			closeTab();
		}
	}

	/**
	 * Highlights the selected tab and un-hightlights the rest.
	 */
	private void highlightSelected() {
		for (int i = 0; i < components.size(); i++) {
			JComponent button = components.get(i).button;
			if (i == frame.currentMainPanel) {
				button.setBackground(GraphicsConstants.SELECTED_TAB_BACKGROUND);
				button.setForeground(GraphicsConstants.SELECTED_TAB_FOREGROUND);
			} else {
				button.setBackground(GraphicsConstants.DESELECTED_TAB_BACKGROUND);
				button.setForeground(GraphicsConstants.DESELECTED_TAB_FOREGROUND);
			}
		}
	}

	/**
	 * Sets the current MainPanel's filename in the tabs.
	 *
	 * @param filename
	 */
	public void setFilename(final String filename) {
		for (final Component c : panel.getComponents()) {
			if (c.equals(components.get(frame.currentMainPanel).button)) {
				((JButton) c).setText(filename);
				break;
			}
		}
		revalidate();
		repaint();
	}

	private static final class TabPanelScrollBarUI extends BasicScrollBarUI {
		@Override
		protected void configureScrollBarColors() {
			thumbColor = GraphicsConstants.TAB_PANEL_SCROLL_BAR_COLOR;
			thumbHighlightColor = GraphicsConstants.TAB_PANEL_SCROLL_BAR_HIGHLIGHT_COLOR;
			trackColor = GraphicsConstants.TAB_PANEL_SCROLL_BAR_TRACK_COLOR;
			thumbLightShadowColor = GraphicsConstants.TAB_PANEL_SCROLL_BAR_LIGHT_SHADOW;
			thumbDarkShadowColor = GraphicsConstants.TAB_PANEL_SCROLL_BAR_DARK_SHADOW;
		}

		@Override
		protected JButton createDecreaseButton(final int orientation) {
			return createZeroButton();
		}

		@Override
		protected JButton createIncreaseButton(final int orientation) {
			return createZeroButton();
		}

		private JButton createZeroButton() {
			final JButton jbutton = new JButton();
			jbutton.setPreferredSize(new Dimension(0, 0));
			jbutton.setMinimumSize(new Dimension(0, 0));
			jbutton.setMaximumSize(new Dimension(0, 0));
			return jbutton;
		}
	}

	private static class MainPanelAndButton {
		public JComponent button;
		public MainPanel panel;
		public JTextArea console;

		public MainPanelAndButton(final JComponent button, final MainPanel panel, JTextArea console) {
			this.button = button;
			this.panel = panel;
			this.console = console;
		}

	}
}
