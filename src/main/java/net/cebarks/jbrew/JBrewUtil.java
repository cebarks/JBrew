package net.cebarks.jbrew;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class JBrewUtil {

	private static final String HOMEBREW_LOCATION = "/usr/local/bin/brew";
	// private static final String
	private static ArrayList<String> installedFormulae;
	private static ArrayList<String> availableFormulae;

	/**
	 * 
	 * WARNING: IS BLOCKING
	 * 
	 * @param name
	 *            - name of the formula to be installed
	 * @return wether the 'brew' process exitted normally
	 */
	public static boolean installForumula(String name) {
		boolean success = false;
		try {
			Process p = runCommand(HOMEBREW_LOCATION, "install", name);
			success = (p.exitValue() == 0) ? true : false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (success) {
			JOptionPane.showMessageDialog(null, "Successfully installed '" + name + "'");
		}
		
		return success;
	}

	public static boolean upgradeFormula(String name) {
		boolean success = false;
		try {
			Process p = runCommand(HOMEBREW_LOCATION, "upgrade", name);
			success = (p.exitValue() == 0) ? true : false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (success) {
			JOptionPane.showMessageDialog(null, "Successfully upgraded '" + name + "'");
		}
		
		return success;
	}

	public static boolean reinstallFormula(String name) {
		boolean success = false;
		try {
			Process p = runCommand(HOMEBREW_LOCATION, "reinstall", name);
			success = (p.exitValue() == 0) ? true : false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (success) {
			JOptionPane.showMessageDialog(null, "Successfully reinstalled '" + name + "'");
		}
		
		return success;
	}

	public static boolean uninstallFormula(String name) {
		boolean success = false;
		try {
			Process p = runCommand(HOMEBREW_LOCATION, "uninstall", name);
			success = (p.exitValue() == 0) ? true : false;
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (success) {
			JOptionPane.showMessageDialog(null, "Successfully uninstalled '" + name + "'");
		}

		return success;
	}

	public static int installHomebrew() {
		int result = 1;
		try {
			Process p = runCommand("ruby", "-e \"$(curl -fsSL https://raw.github.com/Homebrew/homebrew/go/install)\"");
			for (;;)
				if (!p.isAlive())
					break;

			result = p.exitValue();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<String> parseAvailableFormulae() {
		availableFormulae = new ArrayList<String>();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(runCommand(HOMEBREW_LOCATION, "search").getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				availableFormulae.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return availableFormulae;
	}

	public static ArrayList<String> parseInstalledFormulae() {
		installedFormulae = new ArrayList<String>();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(runCommand(HOMEBREW_LOCATION, "list").getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				installedFormulae.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return installedFormulae;
	}

	private static Process runCommand(String... command) throws IOException {
		ProcessBuilder pb = new ProcessBuilder(command);
		Process p = pb.start();
		for (;;)
			if (!p.isAlive())
				break;
		return p;
	}

	public static boolean isHomebrewInstalled() {
		return new File(HOMEBREW_LOCATION).exists();
	}

	public static ArrayList<String> getInstalledFormulae() {
		return (installedFormulae == null) ? parseInstalledFormulae() : installedFormulae;
	}

	public static ArrayList<String> getAvailableFormulae() {
		return (availableFormulae == null) ? parseAvailableFormulae() : availableFormulae;
	}

	public static DefaultListModel<String> getInstalledFormulaeInListModel() {
		DefaultListModel<String> model = new DefaultListModel<String>();

		for (String s : getInstalledFormulae()) {
			model.addElement(s);
		}

		return model;
	}

	public static DefaultListModel<String> getAvailableFormulaeInListModel() {
		DefaultListModel<String> model = new DefaultListModel<String>();

		for (String s : getAvailableFormulae()) {
			model.addElement(s);
		}

		return model;
	}

	public static void updateHomebrew() {
		try {
			runCommand(HOMEBREW_LOCATION, "update");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
