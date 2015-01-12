package ide;

import ide.graphics.GraphicsConstants;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.FontUIResource;

import language.Piece;

/**
 * The Class WindowFrame.
 *
 * @author Kyran Adams
 */
@SuppressWarnings("serial")
public class WindowFrame extends JFrame {

	/** The j scroll pane1. */
	private JScrollPane jScrollPane1;

	/** The j split pane1. */
	private JSplitPane jSplitPane1;

	/** The main and toolbar seperator. */
	private JSplitPane mainAndToolbarSeperator;

	/** The main panel. */
	private MainPanel mainPanel;

	/** The piece list. */
	private PieceList pieceList;

	/** The piece picker panel. */
	private JPanel piecePickerPanel;

	/** The toolbar panel. */
	private JPanel toolbarPanel;

	/**
	 * Creates new form GUIFrame.
	 */
	public WindowFrame() {
		super();
		setUIFont(new FontUIResource(GraphicsConstants.APP_FONT));
		initComponents();
	}

	private final void initComponents() {

		jSplitPane1 = new JSplitPane();
		piecePickerPanel = new JPanel();
		jScrollPane1 = new JScrollPane();
		mainAndToolbarSeperator = new JSplitPane();
		toolbarPanel = new ToolbarPanel();
		setMainPanel(new MainPanel().start());

		pieceList = new PieceList(getMainPanel());

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jSplitPane1.setDividerSize(0);

		jScrollPane1.setViewportView(pieceList);

		final GroupLayout piecePickerPanelLayout = new GroupLayout(
				piecePickerPanel);
		piecePickerPanel.setLayout(piecePickerPanelLayout);

		final int maxPixelWidthOfPieceNames = (int) (getMainPanel()
				.getFontMetrics(GraphicsConstants.APP_FONT).stringWidth(
						Piece.LONGEST_PIECE_NAME) * 1.5);

		piecePickerPanelLayout.setHorizontalGroup(piecePickerPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE,
						maxPixelWidthOfPieceNames, Short.MAX_VALUE));
		piecePickerPanelLayout.setVerticalGroup(piecePickerPanelLayout
				.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 513,
						Short.MAX_VALUE));

		jSplitPane1.setLeftComponent(piecePickerPanel);

		mainAndToolbarSeperator
				.setDividerLocation(GraphicsConstants.TOOLBAR_HEIGHT);
		mainAndToolbarSeperator.setDividerSize(0);
		mainAndToolbarSeperator.setOrientation(JSplitPane.VERTICAL_SPLIT);

		mainAndToolbarSeperator.setTopComponent(toolbarPanel);

		final GroupLayout mainPanelLayout = new GroupLayout(getMainPanel());
		getMainPanel().setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 454, Short.MAX_VALUE));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGap(0, 472, Short.MAX_VALUE));

		mainAndToolbarSeperator.setRightComponent(getMainPanel());

		jSplitPane1.setRightComponent(mainAndToolbarSeperator);

		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jSplitPane1));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jSplitPane1));

		pack();

		getMainPanel().centerOnOrigin();
	}

	/**
	 * Sets the UI font.
	 *
	 * @param f
	 *            the new UI font
	 */
	public static void setUIFont(final FontUIResource f) {
		final Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			final Object key = keys.nextElement();
			final Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				final FontUIResource orig = (FontUIResource) value;
				final Font font = new Font(f.getFontName(), orig.getStyle(),
						f.getSize());
				UIManager.put(key, new FontUIResource(font));
			}
		}
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String... args) {

		try {
			for (final UIManager.LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (final ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(WindowFrame.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (final InstantiationException ex) {
			java.util.logging.Logger.getLogger(WindowFrame.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (final IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(WindowFrame.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (final UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(WindowFrame.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}

		EventQueue.invokeLater(() -> {
			final WindowFrame frame = new WindowFrame();
			frame.setVisible(true);

		});
	}

	/**
	 * Gets the main panel.
	 *
	 * @return the main panel
	 */
	public final MainPanel getMainPanel() {
		return mainPanel;
	}

	/**
	 * Sets the main panel.
	 *
	 * @param mainPanel
	 *            the new main panel
	 */
	public final void setMainPanel(final MainPanel mainPanel) {
		this.mainPanel = mainPanel;
	}

}
