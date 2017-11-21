/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.uclouvain.gtm.util.gui;

import java.awt.EventQueue;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

/** This thread repaints a list of components. */
public class RepaintThread extends Thread {

  /** The repaint timeout. */
  protected long timeout;

  /** The components to repaint. */
  protected List<JComponent> components;

  /** The runtime. */
  protected Runtime runtime = Runtime.getRuntime();

  /** Whether or not the thread was suspended. */
  protected boolean suspended;

  /** Runnable for updating components. */
  protected UpdateRunnable updateRunnable;

  /**
   * Creates a new Thread.
   *
   * @param timeout The time between two repaint in ms.
   * @param components The components to repaint.
   */
  public RepaintThread(long timeout, List<JComponent> components) {
    this.timeout = timeout;
    this.components = components;
    this.updateRunnable = createUpdateRunnable();
    setPriority(Thread.MIN_PRIORITY);
  }

  /** The thread main method. */
  public void run() {
    for (; ; ) {
      try {
        synchronized (updateRunnable) {
          if (!updateRunnable.inEventQueue) {
            EventQueue.invokeLater(updateRunnable);
          }
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

  /** Suspends the thread. */
  public synchronized void safeSuspend() {
    if (!suspended) {
      suspended = true;
    }
  }

  /** Resumes the thread. */
  public synchronized void safeResume() {
    if (suspended) {
      suspended = false;
      notify();
    }
  }
}
