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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

/** Displays the current CPU usage. */
public class CpuUsage extends JPanel implements CpuChangeListener {

  private static final long serialVersionUID = 6942503726834045933L;

  /** The preferred width. */
  public static final int PREFERRED_WIDTH = 120;

  /** The preferred height. */
  public static final int PREFERRED_HEIGHT = 100;

  /** The font size. */
  protected static final int FONT_SIZE = 9;

  /** The blocks margin. */
  protected static final int BLOCK_MARGIN = 10;

  /** The number of blocks. */
  protected static final int BLOCKS = 15;

  /** The blocks width. */
  protected static final double BLOCK_WIDTH = PREFERRED_WIDTH - BLOCK_MARGIN * 2;

  /** The blocks height. */
  protected static final double BLOCK_HEIGHT =
      ((double) PREFERRED_HEIGHT - (3 * FONT_SIZE) - BLOCKS) / BLOCKS;

  /** The font used to draw the strings. */
  protected Font font = new Font("SansSerif", Font.BOLD, FONT_SIZE);

  /** The text color. */
  protected Color textColor = Color.black;

  /** The background color. */
  protected Color backgroundColor = new Color(238, 238, 238);

  protected double usedCpu;

  /** Creates a new Usage object. */
  public CpuUsage() {
    this.setBackground(backgroundColor);
    setPreferredSize(new Dimension(PREFERRED_WIDTH, PREFERRED_HEIGHT));
  }

  /**
   * Indicates that the cpu state has changed.
   *
   * @param percent The percentage of CPU used by this JVM
   */
  public void cpuStateChanged(double percent) {
    usedCpu = percent;
  }

  /** To paint the component. */
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

    // Draw the cpu blocks
    int nfree = (int) Math.round(((double) BLOCKS) * (1.0 - usedCpu));
    for (int i = 0; i < nfree; i++) {
      Rectangle2D rect =
          new Rectangle2D.Double(
              10, i * BLOCK_HEIGHT + i + FONT_SIZE + 5, BLOCK_WIDTH, BLOCK_HEIGHT);
      g2d.setPaint(Color.LIGHT_GRAY);
      g2d.fill(rect);
    }

    for (int i = nfree; i < 15; i++) {
      Rectangle2D rect =
          new Rectangle2D.Double(
              10, i * BLOCK_HEIGHT + i + FONT_SIZE + 5, BLOCK_WIDTH, BLOCK_HEIGHT);
      g2d.setPaint(Color.red);
      g2d.fill(rect);
    }

    // Draw the cpu usage text
    g2d.setPaint(textColor);
    g2d.setFont(font);

    g2d.drawString(i18n.get("used_cpu"), 10, 10);

    String legend;
    legend = (int) (100 * usedCpu) + "%";
    g2d.drawString(legend, 10, PREFERRED_HEIGHT - 3);
  }
}
