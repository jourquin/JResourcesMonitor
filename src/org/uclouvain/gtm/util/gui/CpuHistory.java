package org.uclouvain.gtm.util.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;

/**
 * Displays the CPU usage history in a chart.
 */
public class CpuHistory extends JPanel implements CpuChangeListener {
	
	private static final long serialVersionUID = 5579965579824639053L;

	/**
	 * The preferred width.
	 */
	public static final int PREFERRED_WIDTH = 200;

	/**
	 * The preferred height.
	 */
	public static final int PREFERRED_HEIGHT = 100;

	/**
	 * The grid lines stroke.
	 */
	protected static final Stroke GRID_LINES_STROKE = new BasicStroke(1);

	/**
	 * The curve stroke.
	 */
	protected static final Stroke CURVE_STROKE = new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

	/**
	 * The grid lines color.
	 */
	protected Color gridLinesColor = Color.lightGray; 

	/**
	 * The curve color.
	 */
	protected Color curveColor = Color.red;

	/**
	 * The border color.
	 */
	protected Color borderColor = Color.black;

	/**
	 * The background color
	 */
	protected Color backgroundColor = new Color(238,238,238);
	
	
	/**
	 * The data.
	 */
	protected List<Long> data = new LinkedList<Long>();

	/**
	 * The vertical lines shift.
	 */
	protected int xShift = 0;

	/**
	 * The curve representation.
	 */
	protected GeneralPath path = new GeneralPath();

	/**
	 * Creates a new History object.
	 */
	public CpuHistory() {
		this.setBackground(backgroundColor);
		setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
	}

	/**
	 * Indicates that the cpu state has changed.
	 * 
	 * @param percent
	 *            The percentage of CPU used by this JVM
	 */
	public void cpuStateChanged(double percent) {

		// Add a new point to the data
		data.add(new Long((long) (percent * 100)));
		if (data.size() > 190) {
			data.remove(0);
			xShift = (xShift + 1) % 10;
		}

		// Create the path that match the data
		Iterator<Long> it = data.iterator();
		GeneralPath p = new GeneralPath();
		long l = ((Long) it.next()).longValue();
		p.moveTo(5, ((float) (100 - l) / 100) * 80 + 10);
		int i = 6;
		while (it.hasNext()) {
			l = ((Long) it.next()).longValue();
			p.lineTo(i, ((float) (100 - l) / 100) * 80 + 10);
			i++;
		}
		path = p;
	}

	/**
	 * To paint the component.
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		// Turns the antialiasing on
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Sets the transform
		Dimension dim = getSize();
		double sx = ((double) dim.width) / PREFERRED_WIDTH;
		double sy = ((double) dim.height) / PREFERRED_HEIGHT;
		g2d.transform(AffineTransform.getScaleInstance(sx, sy));

		// The vertical lines
		g2d.setPaint(gridLinesColor);
		g2d.setStroke(GRID_LINES_STROKE);
		for (int i = 1; i < 19; i++) {
			int lx = i * 10 + 5 - xShift;
			g2d.draw(new Line2D.Double(lx, 5, lx, PREFERRED_HEIGHT - 5));
		}

		// The horizontal lines
		for (int i = 1; i < 9; i++) {
			int ly = i * 10 + 5;
			g2d.draw(new Line2D.Double(5, ly, PREFERRED_WIDTH - 5, ly));
		}

		// The curve.
		g2d.setPaint(curveColor);
		g2d.setStroke(CURVE_STROKE);

		g2d.draw(path);

	}
}