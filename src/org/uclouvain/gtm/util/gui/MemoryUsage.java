package org.uclouvain.gtm.util.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/**
 * Displays the current memory usage.
 */
public class MemoryUsage extends JPanel implements MemoryChangeListener {

	private static final long serialVersionUID = -6017510475867006209L;

	/**
	 * The preferred width.
	 */
	public static final int PREFERRED_WIDTH = 120;

	/**
	 * The preferred height.
	 */
	public static final int PREFERRED_HEIGHT = 100;

	/**
	 * The font size.
	 */
	protected static final int FONT_SIZE = 9;

	/**
	 * The blocks margin.
	 */
	protected static final int BLOCK_MARGIN = 10;

	/**
	 * The number of blocks.
	 */
	protected static final int BLOCKS = 15;

	/**
	 * The blocks width.
	 */
	protected static final double BLOCK_WIDTH = PREFERRED_WIDTH - BLOCK_MARGIN * 2;

	/**
	 * The blocks height.
	 */
	protected static final double BLOCK_HEIGHT = ((double) PREFERRED_HEIGHT - (3 * FONT_SIZE) - BLOCKS) / BLOCKS;

	/**
	 * The font used to draw the strings.
	 */
	protected Font font = new Font("SansSerif", Font.BOLD, FONT_SIZE);

	/**
	 * The text color.
	 */
	protected Color textColor = Color.black;

	/**
	 * The background color
	 */
	protected Color backgroundColor = new Color(238, 238, 238);

	/**
	 * The total memory.
	 */
	protected long totalMemory;

	/**
	 * The free memory.
	 */
	protected long freeMemory;

	/**
	 * Creates a new Usage object.
	 */
	public MemoryUsage() {
		this.setBackground(backgroundColor);
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
	}

	/**
	 * Indicates that the memory state has changed.
	 * 
	 * @param total
	 *            The total amount of memory.
	 * @param free
	 *            The free memory.
	 */
	public void memoryStateChanged(long total, long free) {
		totalMemory = total;
		freeMemory = free;
	}

	/**
	 * To paint the component.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// Sets the transform
		Dimension dim = getSize();
		double sx = ((double) dim.width) / PREFERRED_WIDTH;
		double sy = ((double) dim.height) / PREFERRED_HEIGHT;
		g2d.transform(AffineTransform.getScaleInstance(sx, sy));

		// Turns the antialiasing on
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw the memory blocks
		int nfree = (int) Math.round(((double) BLOCKS) * freeMemory / totalMemory);

		for (int i = 0; i < nfree; i++) {
			Rectangle2D rect = new Rectangle2D.Double(10, i * BLOCK_HEIGHT + i + FONT_SIZE + 5, BLOCK_WIDTH, BLOCK_HEIGHT);
			g2d.setPaint(Color.lightGray);
			g2d.fill(rect);
		}

		for (int i = nfree; i < 15; i++) {
			Rectangle2D rect = new Rectangle2D.Double(10, i * BLOCK_HEIGHT + i + FONT_SIZE + 5, BLOCK_WIDTH, BLOCK_HEIGHT);
			g2d.setPaint(Color.red);
			g2d.fill(rect);
		}

		// Draw the memory usage text
		g2d.setPaint(textColor);
		g2d.setFont(font);

		// Convert into Mo's
		long total = totalMemory / (1024*1024);
		long used = (totalMemory - freeMemory) / (1024*1024);
		String totalText;
		String usedText;

		totalText = total + i18n.get("mem_total");
		usedText = used + i18n.get("mem_used");

		g2d.drawString(totalText, 10, 10);
		g2d.drawString(usedText, 10, PREFERRED_HEIGHT - 3);
	}
}