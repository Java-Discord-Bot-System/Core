package com.almightyalpaca.adbs4j.plugins;

public class PluginInfo {

	public static class Version {

		private final String number;

		public Version(final String number) {
			this.number = number;
		}

		/**
		 * Compares two version strings. Use this instead of String.compareTo() for a non-lexicographical comparison that works for version strings. e.g. "1.10".compareTo("1.6"). <b> It does not work
		 * if "1.10" is supposed to be equal to "1.10.0". </b>
		 * 
		 * @param v1
		 *            first version to compare.
		 * @param v2
		 *            second version to compare.
		 * @return The result is a negative integer if v1 is _numerically_ less than v2. The result is a positive integer if v1 is _numerically_ greater than v2. The result is zero if the strings are
		 *         _numerically_ equal.
		 * @author Alex Gitelman (stackoverflow.com)
		 */
		public static int compare(final Version v1, final Version v2) {
			final String[] vals1 = v1.number.split("\\.");
			final String[] vals2 = v2.number.split("\\.");
			int i = 0;
			// set index to first non-equal ordinal or length of shortest
			// version string
			while (i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
				i++;
			}
			// compare first non-equal ordinal number
			if (i < vals1.length && i < vals2.length) {
				final int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
				return Integer.signum(diff);
			}
			// the strings are equal or one string is a substring of the other
			// e.g. "1.2.3" = "1.2.3" or "1.2.3" < "1.2.3.4"
			else {
				return Integer.signum(vals1.length - vals2.length);
			}
		}

		public int compare(final Version v) {
			return Version.compare(this, v);
		}

		public final String getNumber() {
			return this.number;
		}
	}

	private final String				author;
	private final String				name;
	private final String				description;
	private final String				id;

	private final PluginInfo.Version	version;

	/**
	 * @param author
	 *            the author's name
	 * @param name
	 *            the plugin's name
	 * @param description
	 *            the plugin's description
	 */
	public PluginInfo(final String id, final PluginInfo.Version version, final String author, final String name, final String description) {
		this.id = id;
		this.version = version;
		this.author = author;
		this.name = name;
		this.description = description;
	}

	public PluginInfo(final String id, final String version, final String author, final String name, final String description) {
		this(id, new Version(version), author, name, description);
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj != null && obj instanceof PluginInfo) {
			final PluginInfo info = (PluginInfo) obj;
			return this.equals(info);
		} else {
			return false;
		}
	}

	public boolean equals(final PluginInfo info) {
		return info.id.equalsIgnoreCase(this.id);
	}

	public final String getAuthor() {
		return this.author;
	}

	public final String getDescription() {
		return this.description;
	}

	public String getId() {
		return this.id;
	}

	public final String getName() {
		return this.name;
	}

	public PluginInfo.Version getVersion() {
		return this.version;
	}

}
