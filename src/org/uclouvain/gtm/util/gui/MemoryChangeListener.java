package org.uclouvain.gtm.util.gui;

/**
 * This interface allows the RepaintThread to notify an object that the
 * current memory state has changed.
 */
public interface MemoryChangeListener {
    /**
     * Indicates that the memory state has changed.
     * @param total The total amount of memory.
     * @param free  The free memory.
     */
    void memoryStateChanged(long total, long free);
}