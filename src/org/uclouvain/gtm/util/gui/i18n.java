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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Simple wrapper for the i18n mechanism.
 *
 * @author Bart Jourquin
 */
public class i18n {

  static ResourceBundle res;

  public i18n() {
    res = ResourceBundle.getBundle("i18n", Locale.getDefault());
  }

  /**
   * Get a localized string from key.
   *
   * @param key key in the properties
   * @return Localized string
   */
  public static String get(String key) {
    return (String) res.getObject(key);
  }
}
