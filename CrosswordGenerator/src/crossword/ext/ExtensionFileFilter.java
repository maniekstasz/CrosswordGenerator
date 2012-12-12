package crossword.ext;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileFilter;

public class ExtensionFileFilter extends FileFilter implements FilenameFilter {
	private String description;

	private String extensions[];

	public ExtensionFileFilter(String description, String... extensions) {
		if (description == null) {
			this.description = extensions[0];
		} else {
			this.description = description;
		}
		this.extensions = (String[]) extensions.clone();
		toLower(this.extensions);
	}

	private void toLower(String array[]) {
		for (int i = 0, n = array.length; i < n; i++) {
			array[i] = array[i].toLowerCase();
		}
	}

	public String getDescription() {
		return description;
	}

	@Override
	public boolean accept(File file) {
		if (file.isDirectory()) {
			return true;
		} else {
			String path = file.getAbsolutePath().toLowerCase();
			for (int i = 0, n = extensions.length; i < n; i++) {
				String extension = extensions[i];
				if ((path.endsWith(extension) && (path.charAt(path.length()
						- extension.length() - 1)) == '.')) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean accept(File dir, String name) {
		String newName = name.toLowerCase();
		for (int i = 0, n = extensions.length; i < n; i++) {
			String extension = extensions[i];
			if ((newName.endsWith(extension) && (newName.charAt(newName.length()
					- extension.length() - 1)) == '.')) {
				return true;
			}
		}
		return false;
	}
}
