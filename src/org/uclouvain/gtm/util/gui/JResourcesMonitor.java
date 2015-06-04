package org.uclouvain.gtm.util.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.management.ManagementFactory;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.management.OperatingSystemMXBean;

/**
 * A simple GUI that tracks and display memory and CPU usage of the current running JVM. Can be simply embedded in a java
 * application.
 * 
 * The code is inspired from Apache Batik's "MemoryMonitor", but is rewritten in order not to rely any external library.
 * 
 * Note : It uses OperatingSystemMXBean. Being a "MX" JDK API element, it could be removed by Oracle in later versions of the JDK.
 * 
 * Java 7 or later is needed
 *
 * @author bart.jourquin@uclouvain.be
 * @version 1.0, June 2015
 */

public class JResourcesMonitor extends JFrame {

	private static final long serialVersionUID = -6145945442924301622L;

	/**
	 * The Panel instance.
	 */
	protected GraphsPanel panel;

	/**
	 * The operating system bean, used to collect CPU usage
	 */
	static OperatingSystemMXBean osBean;

	/**
	 * Creates a new resource monitor frame. The time between two repaints is 1s.
	 */
	public JResourcesMonitor() {
		this(1000, false);
	}

	/**
	 * Creates a new resource monitor frame. The time between two repaints is 1s.
	 * 
	 * @param exitOnClose
	 *            Tell the GUI to exit on close (or not)
	 */
	public JResourcesMonitor(boolean exitOnClose) {
		this(1000, exitOnClose);
	}

	/**
	 * 
	 * @param time
	 *            The time between two repaints in ms.
	 * @param exitOnClose
	 *            Tell the GUI to exit on close (or not)
	 */
	public JResourcesMonitor(long time, final boolean exitOnClose) {
		super();

		// Initialize the i18n mechanism
		new i18n();

		setTitle(i18n.get("frame_title"));

		osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

		panel = new GraphsPanel(time);

		getContentPane().add(panel);
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder()));

		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		JButton collectButton = new JButton(i18n.get("collect"));
		collectButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.gc();
			}
		});

		JButton closeButton = new JButton(i18n.get("close"));
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.getRepaintThread().safeSuspend();
				dispose();
				if (exitOnClose) {
					System.exit(0);
				}
			}
		});

		p.add(collectButton);
		p.add(closeButton);
		getContentPane().add(p, BorderLayout.SOUTH);

		pack();

		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				RepaintThread t = panel.getRepaintThread();
				if (!t.isAlive()) {
					t.start();
				} else {
					t.safeResume();
				}
			}

			public void windowClosing(WindowEvent ev) {
				panel.getRepaintThread().safeSuspend();
				dispose();
				if (exitOnClose) {
					System.exit(0);
				}
			}

			public void windowDeiconified(WindowEvent e) {
				panel.getRepaintThread().safeResume();
			}

			public void windowIconified(WindowEvent e) {
				panel.getRepaintThread().safeSuspend();
			}
		});

		setVisible(true);
	}

	/**
	 * Main entry point
	 */
	public static void main(String[] args) {

		// Launch a new Resource monitor and tell it to exit at closing time
		new JResourcesMonitor(true);

		/**
		 * A time & memory consuming task can be launched here in order to visualize Memory and CPU usage in the monitor.
		 */

	}
}
