package org.uclouvain.gtm.util.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A panel composed of a Usage instance and a History instance for memory and CPU usage.
 */
public class GraphsPanel extends JPanel {
	
	private static final long serialVersionUID = -8002911015500069974L;
	
	/**
	 * The repaint thread.
	 */
	protected RepaintThread repaintThread;

	/**
	 * Creates a new resource monitor panel, composed of a Usage instance and
	 * a History instance. The time between two repaints is 1s.
	 */
	public GraphsPanel() {
		this(1000);
	}

	/**
	 * Creates a new resource monitor panel, composed of a Usage instance and
	 * a History instance for memory and CPU usage. 
	 * 
	 * @param time
	 *            The time between two repaints in ms.
	 */
	public GraphsPanel(long time) {
		super(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(5, 5, 5, 5);

		List<JComponent> l = new ArrayList<JComponent>();
		
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEtchedBorder());		
		JComponent c = new MemoryUsage();
		p.add(c);
		constraints.weightx = 0.3;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(p, constraints);
		l.add(c);
		

		p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEtchedBorder());
		c = new MemoryHistory();
		p.add(c);
		constraints.weightx = 0.7;
		constraints.gridx = 1;
		constraints.gridy = 0;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(p, constraints);
		l.add(c);

		
		p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEtchedBorder());		
		c = new CpuUsage();
		p.add(c);
		constraints.weightx = 0.3;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(p, constraints);
		l.add(c);
		
		
		p = new JPanel(new BorderLayout());
		p.setBorder(BorderFactory.createEtchedBorder());
		c = new CpuHistory();
		p.add(c);
		constraints.weightx = 0.7;
		constraints.gridx = 1;
		constraints.gridy = 1;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		add(p, constraints);
		l.add(c);
		
		repaintThread = new RepaintThread(time, l);
	}

	/**
	 * Returns the repaint thread.
	 */
	public RepaintThread getRepaintThread() {
		return repaintThread;
	}
}