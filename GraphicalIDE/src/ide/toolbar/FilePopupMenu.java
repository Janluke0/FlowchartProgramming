package ide.toolbar;

import ide.WindowFrame;
import ide.mainpanel.MainPanel;

import java.awt.HeadlessException;
import java.io.File;
import java.util.Optional;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class FilePopupMenu extends JPopupMenu {

	private final WindowFrame frame;

	public FilePopupMenu(final WindowFrame frame) throws HeadlessException {
		init();
		this.frame = frame;
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
		frame.getTabPanel().closeTab();
	}

	private void saveAsClicked() {
		final Optional<File> file = frame.getMainPanel().askForAndGetFilename();
		if (file.isPresent()) {
			frame.getTabPanel().setFilename(file.get().getName());
			saveClicked();
		}
	}

	private void saveClicked() {
		final Optional<File> file = frame.getMainPanel().getOrAskForFilename();
		if (file.isPresent()) {
			// Save the file
			frame.getMainPanel().save(file.get());
		}
	}

	private void openClicked() {
		// TODO Auto-generated method stub

	}

	/**
	 * Creates a new tab called "Untitled"
	 */
	private void newClicked() {
		frame.getTabPanel().addTab("Untitled", new MainPanel().start());
	}
}
