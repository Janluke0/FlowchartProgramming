package ide.tabs;

import ide.WindowFrame;
import ide.graphics.GraphicsConstants;
import ide.mainpanel.MainPanel;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
		final JButton component = new JButton(s);
		this.panel.add(component);

		components.add(new MainPanelAndButton(component, panel));
		frame.mainPanelHolder.add(panel, "" + (components.size() - 1));

		component.addActionListener((e) -> {
			for (int i = 0; i < components.size(); i++) {
				if (components.get(i).button.equals(component)) {
					frame.setMainPanel(components.get(i).panel);
					final CardLayout cl = (CardLayout) frame.mainPanelHolder.getLayout();
					cl.show(frame.mainPanelHolder, "" + i);
				}
			}
		});
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
