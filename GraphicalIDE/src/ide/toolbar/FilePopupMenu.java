package ide.toolbar;

import java.awt.HeadlessException;
import java.io.File;
import java.util.Optional;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

import ide.WindowFrame;
import ide.graphics.GraphicsConstants;
import ide.mainpanel.MainPanel;

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
		final JMenuItem closeAllItem = new JMenuItem("Close all");
		closeAllItem.addActionListener((e) -> {
			closeAllClicked();
		});

		// Add components to pop-up menu
		add(newItem);
		add(openItem);
		addSeparator();
		add(saveItem);
		add(saveAsItem);
		addSeparator();
		add(closeItem);
		add(closeAllItem);

	}

	private void closeAllClicked() {
		frame.getTabPanel().closeAllTab();
	}

	private void closeClicked() {
		frame.getTabPanel().closeTab();
	}

	private void saveAsClicked() {
		final Optional<File> file = frame.getMainPanel().askForAndGetFilename();
		if (file.isPresent()) {
			saveClicked();
		}
	}

	private void saveClicked() {
		final Optional<File> file = frame.getMainPanel().getOrAskForFilename();
		if (file.isPresent()) {
			// Save the file
			frame.getTabPanel().setFilename(file.get().getName());
			frame.getMainPanel().save(file.get());
		}
	}

	private void openClicked() {
		final Optional<File> file = frame.getMainPanel().askForAndGetFilename();
		if (file.isPresent()) {
			JTextArea console = GraphicsConstants.createConsole();
			frame.getTabPanel().addTab(file.get().getName(), new MainPanel(file.get(), console), console);
		}
	}

	/**
	 * Creates a new tab called "Untitled"
	 */
	private void newClicked() {
		JTextArea console = GraphicsConstants.createConsole();
		frame.getTabPanel().addTab("Untitled", new MainPanel(console).start(),console);
	}
}
