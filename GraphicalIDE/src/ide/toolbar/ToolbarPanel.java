package ide.toolbar;

import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.stream.IntStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import ide.WindowFrame;
import ide.graphics.GraphicsConstants;
import ide.graphics.GraphicsUtils;
import ide.mainpanel.FunctionData;
import ide.mainpanel.MainPanel;

/**
 * The Class ToolbarPanel.
 */
@SuppressWarnings("serial")
public class ToolbarPanel extends JPanel {

	public ToolbarPanel(final WindowFrame frame) {
		super();

		setLayout(
				new FlowLayout(FlowLayout.LEFT, GraphicsConstants.TOOLBAR_PADDING, GraphicsConstants.TOOLBAR_PADDING));

		addButton(GraphicsConstants.FILE_ICON, new FilePopupMenu(frame));
		addButton(GraphicsConstants.RUN_ICON, new RunPopupMenu(frame));
		addFunctionGUI(frame);

		setBackground(GraphicsConstants.MENU_BACKROUND_COLOR);
	}

	private void addFunctionGUI(WindowFrame frame) {
		JCheckBox isFunction = new JCheckBox("Is a function: ");
		JComboBox<Integer> inputsPicker = new JComboBox<Integer>(
				IntStream.range(0, 10).boxed().toArray(Integer[]::new));
		JComboBox<Integer> outputsPicker = new JComboBox<Integer>(
				IntStream.range(0, 10).boxed().toArray(Integer[]::new));

		int[] inputsRef = new int[1];
		int[] outputsRef = new int[1];

		inputsPicker.addItemListener((e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				inputsRef[0] = (Integer) e.getItem();
				MainPanel mainPanel = frame.getMainPanel();
				if (mainPanel.isFunction()) {
					mainPanel.getFunctionData().setInputNumber(inputsRef[0]);
				}
			}
		});
		outputsPicker.addItemListener((e) -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				outputsRef[0] = (Integer) e.getItem();
				MainPanel mainPanel = frame.getMainPanel();
				if (mainPanel.isFunction()) {
					mainPanel.getFunctionData().setOutputNumber(outputsRef[0]);
				}
			}
		});

		isFunction.addItemListener((e) -> {
			MainPanel mainPanel = frame.getMainPanel();
			if (e.getStateChange() == ItemEvent.SELECTED) {
				mainPanel.setFunction(Optional.of(new FunctionData(inputsRef[0], outputsRef[0])));
			} else {
				mainPanel.setFunction(Optional.empty());
			}
		});
		add(isFunction);
		add(new JLabel("Number of inputs:"));
		add(inputsPicker);
		add(new JLabel("Number of outputs:"));
		add(outputsPicker);
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
