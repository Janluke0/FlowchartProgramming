package ide.toolbar;

import ide.WindowFrame;
import ide.graphics.GraphicsConstants;
import ide.mainpanel.MainPanel;
import ide.tabs.TabPanel;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Optional;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

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
			newClicked(frame.getTabPanel());
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
			final JTextArea console = GraphicsConstants.createConsole();
			frame.getTabPanel().addTab(file.get().getName(), new MainPanel(file.get(), console), console);
		}
	}

	/**
	 * Creates a new tab called "Untitled"
	 */
	public void newClicked(final TabPanel panel) {
		final JTextArea console = GraphicsConstants.createConsole();
		final MainPanel mainPanel = new MainPanel(console);
		addKeybindings(mainPanel);
		panel.addTab("Untitled", mainPanel.start(), console);
	}

	private void addKeybindings(final MainPanel mainPanel) {
		final String SAVE = "save";
		mainPanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), SAVE);
		final AbstractAction saveAction = new AbstractAction() {

			@Override
			public void actionPerformed(final ActionEvent e) {
				final Optional<File> file = frame.getMainPanel().getOrAskForFilename();
				if (file.isPresent()) {
					// Save the file
					frame.getTabPanel().setFilename(file.get().getName());
					frame.getMainPanel().save(file.get());
				}
			}
		};
		mainPanel.getActionMap().put(SAVE, saveAction);
	}
}
