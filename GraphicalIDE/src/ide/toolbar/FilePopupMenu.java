package ide.toolbar;

import ide.mainpanel.MainPanel;

import java.awt.HeadlessException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class FilePopupMenu extends JPopupMenu {

	private final MainPanel panel;

	public FilePopupMenu(final MainPanel panel) throws HeadlessException {
		init();
		this.panel = panel;
	}

	private void init() {
		// Create a pop-up menu components
		final JMenuItem newItem = new JMenuItem("New");
		newItem.addActionListener((e) -> {
			newClicked();
		});

		final JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener((e) -> {
			openClicked();
		});
		final JMenuItem saveItem = new JMenuItem("Save");
		saveItem.addActionListener((e) -> {
			saveClicked();
		});
		final JMenuItem saveAsItem = new JMenuItem("Save as");
		saveAsItem.addActionListener((e) -> {
			saveAsClicked();
		});

		final JMenuItem closeItem = new JMenuItem("Close");
		closeItem.addActionListener((e) -> {
			closeClicked();
		});

		// Add components to pop-up menu
		add(newItem);
		add(openItem);
		addSeparator();
		add(saveItem);
		add(saveAsItem);
		addSeparator();
		add(closeItem);

	}

	private void closeClicked() {
		// TODO Auto-generated method stub

	}

	private void saveAsClicked() {
		// TODO Auto-generated method stub

	}

	private void saveClicked() {
		// TODO Auto-generated method stub

	}

	private void openClicked() {
		// TODO Auto-generated method stub

	}

	private void newClicked() {
		// TODO Auto-generated method stub

	}
}
