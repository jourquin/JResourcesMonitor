package org.uclouvain.gtm.util.gui;

/**
 * This interface allows the RepaintThread to notify an object that the
 * current CPU state has changed.
 */
public interface CpuChangeListener {
    /**
     * Indicates that the CPU state has changed.
     * @param percent Percentage of CPU usage
  
     */
    void cpuStateChanged(double percent);
}