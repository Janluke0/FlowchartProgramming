package ide;

import ide.graphics.GraphicsConstants;
import ide.mainpanel.MainPanel;
import ide.piecetree.PieceTree;
import ide.tabs.TabPanel;
import ide.toolbar.ToolbarPanel;

import java.awt.CardLayout;
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

	/** The piece picker and main panel seperator. */
	private JSplitPane piecePickerAndMainSeperator;

	/** The main and toolbar seperator. */
	public JSplitPane mainAndToolbarSeperator;

	private JSplitPane toolbarAndTabSeperator;

	/** The panel that holds the tabs */
	private TabPanel tabPanel;

	/** The main panel. */
	private MainPanel mainPanel;

	/** The piece list. */
	private PieceTree pieceList;

	/** The piece picker panel. */
	private JPanel piecePickerPanel;

	/** The toolbar panel. */
	private JPanel toolbarPanel;

	public int currentMainPanel;
	public JPanel mainPanelHolder;

	/**
	 * Creates new form GUIFrame.
	 */
	public WindowFrame() {
		super();
		setUIFont(new FontUIResource(GraphicsConstants.APP_FONT));
		initComponents();
	}

	private final void initComponents() {

		mainPanelHolder = new JPanel();
		mainPanelHolder.setBorder(null);
		mainPanelHolder.setLayout(new CardLayout());

		setMainPanel(new MainPanel().start());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		piecePickerAndMainSeperator = new JSplitPane();
		piecePickerPanel = new JPanel();
		jScrollPane1 = new JScrollPane();
		mainAndToolbarSeperator = new JSplitPane();
		toolbarPanel = new ToolbarPanel(this);
		pieceList = new PieceTree(this);
		toolbarAndTabSeperator = new JSplitPane();

		setTabPanel(new TabPanel(this));
		getTabPanel().addTab("Untitled", getMainPanel());

		toolbarAndTabSeperator.setDividerSize(0);
		toolbarAndTabSeperator.setRightComponent(getTabPanel());
		toolbarAndTabSeperator.setLeftComponent(toolbarPanel);

		piecePickerAndMainSeperator.setDividerSize(0);

		jScrollPane1.setViewportView(pieceList);

		final GroupLayout piecePickerPanelLayout = new GroupLayout(piecePickerPanel);
		piecePickerPanel.setLayout(piecePickerPanelLayout);

		final int maxPixelWidthOfPieceNames = (int) (getMainPanel().getFontMetrics(GraphicsConstants.APP_FONT).stringWidth(Piece.LONGEST_PIECE_NAME) * 1.5);

		piecePickerPanelLayout.setHorizontalGroup(piecePickerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, maxPixelWidthOfPieceNames, Short.MAX_VALUE));
		piecePickerPanelLayout.setVerticalGroup(piecePickerPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE));

		piecePickerAndMainSeperator.setLeftComponent(piecePickerPanel);

		mainAndToolbarSeperator.setDividerLocation(GraphicsConstants.TOOLBAR_HEIGHT);
		mainAndToolbarSeperator.setDividerSize(0);
		mainAndToolbarSeperator.setOrientation(JSplitPane.VERTICAL_SPLIT);

		mainAndToolbarSeperator.setTopComponent(toolbarAndTabSeperator);

		final GroupLayout mainPanelLayout = new GroupLayout(getMainPanel());
		getMainPanel().setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 454, Short.MAX_VALUE));
		mainPanelLayout.setVerticalGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 472, Short.MAX_VALUE));

		mainAndToolbarSeperator.setRightComponent(mainPanelHolder);

		piecePickerAndMainSeperator.setRightComponent(mainAndToolbarSeperator);

		final GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(piecePickerAndMainSeperator));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(piecePickerAndMainSeperator));

		pack();
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
				final Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
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
			for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (final ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(WindowFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (final InstantiationException ex) {
			java.util.logging.Logger.getLogger(WindowFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (final IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(WindowFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (final UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(WindowFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

	public TabPanel getTabPanel() {
		return tabPanel;
	}

	public void setTabPanel(final TabPanel tabPanel) {
		this.tabPanel = tabPanel;
	}

}
