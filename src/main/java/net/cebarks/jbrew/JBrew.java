package net.cebarks.jbrew;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * A GUI front end to the great Homebrew package manager for OS X.
 * 
 * @see http://www.eclipse.org/windowbuilder/ - Used to layout/build the GUI
 * @author Anten Skrabec <cebarks@gmail.com>
 */
@SuppressWarnings("rawtypes")
public class JBrew {

	private JFrame jBrewFrame;
	private JList installedFormulaeList;
	private JList availableFormulaeList;

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JBrew jBrew = new JBrew();
		jBrew.jBrewFrame.setVisible(true);
	}

	/**
	 * Create the application.
	 * 
	 * @wbp.parser.entryPoint
	 */
	public JBrew() {
		if (!JBrewUtil.isHomebrewInstalled()) {
			int result;
			int uc = JOptionPane.showConfirmDialog(null, "Homebrew was not found installed on this system- would you like to install it?", "Homebrew not found - Install it?", JOptionPane.YES_NO_OPTION);
			if (uc == 0) {
				result = JBrewUtil.installHomebrew();
				if (result == 0) {
					JOptionPane.showMessageDialog(null, "Homebrew has been successfully installed!", "Homebrew installed", JOptionPane.OK_OPTION);
				} else {
					JOptionPane.showMessageDialog(null, "An error occured while Homebrew was being installed.", "Error during installation", JOptionPane.OK_OPTION);
					System.exit(1);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Homebrew is required for the use of this program.", "Homebrew is required", JOptionPane.OK_OPTION);
			}
		}
		initialize();
	}

	private void initialize() {
		jBrewFrame = new JFrame();
		jBrewFrame.setResizable(false);
		jBrewFrame.setTitle("JBrew");
		jBrewFrame.setSize(800, 425);
		jBrewFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jBrewFrame.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 800, 400);
		jBrewFrame.getContentPane().add(tabbedPane);

		JPanel managePanel = new JPanel();
		tabbedPane.addTab("Manage", null, managePanel, null);

		managePanel.setLayout(null);

		JScrollPane installedScrollPane = new JScrollPane();
		installedScrollPane.setBounds(10, 34, 182, 314);
		managePanel.add(installedScrollPane);

		installedFormulaeList = new JList();
		setInstalledFormulaeListModel(JBrewUtil.getInstalledFormulaeInListModel());
		installedScrollPane.setViewportView(installedFormulaeList);
		installedFormulaeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane availableScrollPane = new JScrollPane();
		availableScrollPane.setBounds(404, 34, 182, 314);
		managePanel.add(availableScrollPane);

		availableFormulaeList = new JList();
		setAvailableFormulaeListModel(JBrewUtil.getAvailableFormulaeInListModel());
		availableScrollPane.setViewportView(availableFormulaeList);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.DARK_GRAY);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(382, 6, 10, 342);
		managePanel.add(separator);

		JButton btnInstalledUninstall = new JButton("Uninstall");
		btnInstalledUninstall.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JBrewUtil.uninstallFormula((String) installedFormulaeList.getSelectedValue());
				reloadInstalledList();
			}
		});
		btnInstalledUninstall.setBounds(204, 135, 166, 40);
		managePanel.add(btnInstalledUninstall);

		JButton btnInstalledReinstall = new JButton("Reinstall");
		btnInstalledReinstall.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JBrewUtil.reinstallFormula((String) installedFormulaeList.getSelectedValue());
				reloadInstalledList();
			}
		});
		btnInstalledReinstall.setBounds(204, 187, 166, 40);
		managePanel.add(btnInstalledReinstall);

		JButton btnInstalledUpgrade = new JButton("Upgrade");
		btnInstalledUpgrade.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JBrewUtil.upgradeFormula((String) installedFormulaeList.getSelectedValue());
			}
		});
		btnInstalledUpgrade.setBounds(204, 239, 166, 40);
		managePanel.add(btnInstalledUpgrade);

		JLabel lblInstalledFormulae = new JLabel("Installed Formulae");
		lblInstalledFormulae.setHorizontalAlignment(SwingConstants.CENTER);
		lblInstalledFormulae.setBounds(10, 6, 182, 16);
		managePanel.add(lblInstalledFormulae);

		JButton btnInstalledReload = new JButton("Reload");
		btnInstalledReload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				reloadInstalledList();
			}
		});
		btnInstalledReload.setBounds(204, 83, 166, 40);
		managePanel.add(btnInstalledReload);

		JButton btnReload = new JButton("Reload");
		btnReload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				reloadAvaibleList();
			}
		});
		btnReload.setBounds(598, 83, 166, 40);
		managePanel.add(btnReload);

		JButton btnInstall = new JButton("Install");
		btnInstall.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JBrewUtil.installForumula((String) availableFormulaeList.getSelectedValue());
				reloadInstalledList();
			}
		});
		btnInstall.setBounds(598, 135, 166, 40);
		managePanel.add(btnInstall);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JBrewUtil.updateHomebrew();
				reloadAvaibleList();
			}
		});
		btnUpdate.setBounds(598, 187, 166, 40);
		managePanel.add(btnUpdate);

		JLabel lblAvailableFormulae = new JLabel("Available Formulae");
		lblAvailableFormulae.setHorizontalAlignment(SwingConstants.CENTER);
		lblAvailableFormulae.setBounds(404, 6, 182, 16);
		managePanel.add(lblAvailableFormulae);
		
		JOptionPane.showMessageDialog(jBrewFrame, "Some of these buttons may freeze the program! Do NOT worry, this is normal.");
	}

	public ListModel getInstalledFormulaeListModel() {
		return installedFormulaeList.getModel();
	}

	@SuppressWarnings("unchecked")
	public void setInstalledFormulaeListModel(ListModel model) {
		installedFormulaeList.setModel(model);
	}

	public ListModel getAvailableFormulaeListModel() {
		return availableFormulaeList.getModel();
	}

	@SuppressWarnings("unchecked")
	public void setAvailableFormulaeListModel(ListModel model_1) {
		availableFormulaeList.setModel(model_1);
	}
	
	public void reloadInstalledList() {
		JBrewUtil.parseInstalledFormulae();
		setInstalledFormulaeListModel(JBrewUtil.getInstalledFormulaeInListModel());
	}
	
	public void reloadAvaibleList() {
		JBrewUtil.parseAvailableFormulae();
		setAvailableFormulaeListModel(JBrewUtil.getAvailableFormulaeInListModel());
	}
}
