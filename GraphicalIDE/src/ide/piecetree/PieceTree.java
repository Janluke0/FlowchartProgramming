package ide.piecetree;

import ide.WindowFrame;
import ide.graphics.GraphicsConstants;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;

import language.Piece;
import language.pieces.logic.BooleanConstant;
import language.pieces.logic.operators.And;
import language.pieces.logic.operators.FlipFlop;
import language.pieces.logic.operators.Not;
import language.pieces.logic.operators.Or;
import language.pieces.numbers.NumberConstant;
import language.pieces.numbers.operators.Addition;
import language.pieces.numbers.operators.Division;
import language.pieces.numbers.operators.Modulus;
import language.pieces.numbers.operators.Multiplication;
import language.pieces.utils.Display;
import language.pieces.utils.Print;
import language.pieces.utils.time.Time;
import language.pieces.utils.time.Timer;

@SuppressWarnings("serial")
public class PieceTree extends JTree {
	private static DefaultMutableTreeNode root;

	/** A map of added piece classes to their names */
	private static List<PieceTreeRepresentation> pieces = new ArrayList<>();

	static {
		initializePieceTree();
	}

	private static void initializePieceTree() {
		// Initialize the root node and all the children node from
		// Piece.getPieceNames()
		addPieces();

		root = new DefaultMutableTreeNode("Root");
		for (final PieceTreeRepresentation cl : getPieces()) {
			DefaultMutableTreeNode folderParent = root;
			final String[] folder = cl.packageString;
			// Don't do the last one
			for (int i = 0; i < folder.length; i++) {
				final String s = folder[i];
				@SuppressWarnings("unchecked")
				final Enumeration<DefaultMutableTreeNode> e = folderParent.children();
				DefaultMutableTreeNode sChild = null;
				boolean hasSChild = false;
				while (e.hasMoreElements()) {
					final DefaultMutableTreeNode element = e.nextElement();
					if (element.getUserObject().equals(s)) {

						// folderParent already contains a child named s
						hasSChild = true;
						sChild = element;
						break;
					}
				}
				if (hasSChild) {
					// We don't need to create it
					folderParent = sChild;

				} else {
					// adds a folder node, stored as a string
					final DefaultMutableTreeNode newParent = new DefaultMutableTreeNode(s);
					folderParent.add(newParent);
					// recurse for next string with this as the parent
					folderParent = newParent;
				}
			}
			// Adds a leaf node
			folderParent.add(new DefaultMutableTreeNode(cl));
		}
	}

	private static void addPieces() {
		final Object[][] pieces = { //
				{ And.class, And.name() }, //
				{ FlipFlop.class, FlipFlop.name() },//
				{ Not.class, Not.name() },//
				{ Or.class, Or.name() },//
				{ BooleanConstant.class, BooleanConstant.name() },//
				{ Addition.class, Addition.name() },//
				{ Division.class, Division.name() },//
				{ Modulus.class, Modulus.name() },//
				{ Multiplication.class, Multiplication.name() },//
				{ NumberConstant.class, NumberConstant.name() },//
				{ Time.class, Time.name() },//
				{ Timer.class, Timer.name() },//
				{ Display.class, Display.name() },//
				{ Print.class, Print.name() } };//

		for (final Object[] o : pieces) {
			for (int i = 0; i < o.length; i += 2) {
				addPiece((Class<? extends Piece>) o[i], (String) o[i + 1]);
			}
		}
	}

	/**
	 * Adds the piece.
	 *
	 *
	 * @param p
	 *            the p
	 */
	private static void addPiece(final Class<? extends Piece> p, final String name) {
		final String[] parts = name.split("\\.");
		final String[] packageString = new String[parts.length - 1];
		// don't get the name, just the folders
		for (int i = 0; i < packageString.length; i++) {
			packageString[i] = parts[i];
		}
		getPieces().add(new PieceTreeRepresentation(p, packageString, parts[parts.length - 1]));
	}

	public PieceTree(final WindowFrame frame) {
		super(new AlphabeticalTreeModel(root));

		setRootVisible(false);
		setBackground(GraphicsConstants.PIECE_TREE_BACKGROUND_COLOR);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		addTreeSelectionListener(new PieceTreeInputHandler(this, frame));
		setCellRenderer(new PieceTreeDisplay());
		setToggleClickCount(1);
	}

	public static List<PieceTreeRepresentation> getPieces() {
		return pieces;
	}

	public static void setPieces(final List<PieceTreeRepresentation> pieces) {
		PieceTree.pieces = pieces;
	}

	private static class AlphabeticalTreeModel extends DefaultTreeModel {

		public AlphabeticalTreeModel(final TreeNode root) {
			super(sort(root));
		}

		private static DefaultMutableTreeNode sort(final TreeNode a) {
			final DefaultMutableTreeNode root = (DefaultMutableTreeNode) a;

			final int cc = root.getChildCount();
			for (int i = 0; i < cc - 1; i++) {
				for (int j = i + 1; j <= cc - 1; j++) {
					final DefaultMutableTreeNode here = sort(root.getChildAt(i));
					final DefaultMutableTreeNode there = sort(root.getChildAt(j));

					// If its a leaf, it is a PieceTreeRepresentation, else it
					// is a string
					final Object hereObject = here.getUserObject();
					final Object thereObject = there.getUserObject();
					String s1 = null;
					String s2 = null;

					if (hereObject instanceof String) {
						s1 = (String) hereObject;
					}
					if (thereObject instanceof String) {
						s2 = (String) thereObject;
					}
					if (hereObject instanceof PieceTreeRepresentation) {
						s1 = ((PieceTreeRepresentation) hereObject).name;
					}
					if (thereObject instanceof PieceTreeRepresentation) {
						s2 = ((PieceTreeRepresentation) thereObject).name;
					}
					// Make sure it's a string or a piecetreerepresentation
					assert s1 != null && s2 != null;

					if (s1.compareTo(s2) > 0) {
						root.remove(here);
						root.remove(there);
						root.insert(there, i);
						root.insert(here, j);
					}
				}
			}

			return root;
		}

	}
}
