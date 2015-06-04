package org.uclouvain.gtm.util.gui;

import java.awt.EventQueue;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

/**
 * This thread repaints a list of components.
 */
public class RepaintThread extends Thread {
	
	/**
	 * The repaint timeout
	 */
	protected long timeout;

	/**
	 * The components to repaint.
	 */
	protected List<JComponent> components;

	/**
	 * The runtime.
	 */
	protected Runtime runtime = Runtime.getRuntime();

	/**
	 * Whether or not the thread was suspended.
	 */
	protected boolean suspended;

	/**
	 * Runnable for updating components.
	 */
	protected UpdateRunnable updateRunnable;

	/**
	 * Creates a new Thread.
	 * 
	 * @param timeout
	 *            The time between two repaint in ms.
	 * @param components
	 *            The components to repaint.
	 */
	public RepaintThread(long timeout, List<JComponent> components) {
		this.timeout = timeout;
		this.components = components;
		this.updateRunnable = createUpdateRunnable();
		setPriority(Thread.MIN_PRIORITY);
	}

	/**
	 * The thread main method.
	 */
	public void run() {
		for (;;) {
			try {
				synchronized (updateRunnable) {
					if (!updateRunnable.inEventQueue)
						EventQueue.invokeLater(updateRunnable);
					updateRunnable.inEventQueue = true;
				}
				sleep(timeout);
				synchronized (this) {
					while (suspended) {
						wait();
					}
				}
			} catch (InterruptedException e) {
			}
		}
	}

	protected UpdateRunnable createUpdateRunnable() {
		return new UpdateRunnable();
	}

	protected class UpdateRunnable implements Runnable {
		public boolean inEventQueue = false;

		public void run() {
			// Get free and used memory
			long free = runtime.freeMemory();
			long total = runtime.totalMemory();			

			// Get CPU usage
			double cpuUsage = JResourcesMonitor.osBean.getProcessCpuLoad();

			Iterator<JComponent> it = components.iterator();
			while (it.hasNext()) {
				JComponent c = it.next();
				if (c instanceof MemoryUsage || c instanceof MemoryHistory) {
					((MemoryChangeListener) c).memoryStateChanged(total, free);
				} else {
					((CpuChangeListener) c).cpuStateChanged(cpuUsage);
				}
				c.repaint();
			}
			synchronized (this) {
				inEventQueue = false;
			}
		}
	}

	/**
	 * Suspends the thread.
	 */
	public synchronized void safeSuspend() {
		if (!suspended) {
			suspended = true;
		}
	}

	/**
	 * Resumes the thread.
	 */
	public synchronized void safeResume() {
		if (suspended) {
			suspended = false;
			notify();
		}
	}
}