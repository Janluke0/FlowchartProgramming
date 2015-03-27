package ide.toolbar;

import ide.WindowFrame;

import java.awt.HeadlessException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class RunPopupMenu extends JPopupMenu {

	private final WindowFrame frame;

	public RunPopupMenu(final WindowFrame frame) throws HeadlessException {
		init();
		this.frame = frame;
	}

	private void init() {
		// Create a pop-up menu components
		final JMenuItem runItem = new JMenuItem("Reset");
		runItem.addActionListener((e) -> {
			runClicked();
		});

		final JMenuItem compileItem = new JMenuItem("Compile");
		compileItem.addActionListener((e) -> {
			compileClicked();
		});

		// Add components to pop-up menu
		add(runItem);
		addSeparator();
		add(compileItem);

	}

	private void compileClicked() {
		// TODO Auto-generated method stub

	}

	private void runClicked() {
		// TODO Auto-generated method stub

	}
}
