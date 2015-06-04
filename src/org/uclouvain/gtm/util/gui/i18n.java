package org.uclouvain.gtm.util.gui;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Simple wrapper for the i18n mechanism
 * @author jourquin
 *
 */
public class i18n {

	static ResourceBundle res;

	public i18n() {
		res = ResourceBundle.getBundle("i18n", Locale.getDefault());
	}

	/**
	 * Get a localized string from key
	 * 
	 * @param key
	 *            key in the properties
	 * @return Localized string
	 */
	public static String get(String key) {
		return (String) res.getObject(key);
	}

}
