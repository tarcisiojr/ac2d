package ufu.mestrado.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Aplicacao {

	private JFrame jFrame = null;  //  @jve:decl-index=0:visual-constraint="10,10"
	private JPanel jContentPane = null;
	private JPanel pnlAcaoReticulado = null;
	
	public Aplicacao() {
		initLookAndFeel();
	}
	
	private void initLookAndFeel() {
		try {
			UIManager.put("OptionPane.yesButtonText", "Sim");
			UIManager.put("OptionPane.cancelButtonText", "Cancelar");
			UIManager.put("OptionPane.noButtonText", "Não");
			UIManager.put("OptionPane.okButtonText", "OK");
			javax.swing.UIManager.setLookAndFeel("easytech.plaf.xplookandfeel.XPLookAndFeel");
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, "Inicialzação de padrão de interface falhou.\n"
					+ "Solicite ao suporte instalação de pacote de interface.", "AVISO", javax.swing.JOptionPane.WARNING_MESSAGE);
		}
	}
	
	/**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame();
			jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jFrame.setSize(741, 569);
			jFrame.setContentPane(getJContentPane());
			jFrame.setTitle("AC2D");
		}
		return jFrame;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.insets = new Insets(0, 0, 0, 0);
			gridBagConstraints.gridy = 0;
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.weightx = 1.0;
			gridBagConstraints.weighty = 1.0;
			gridBagConstraints.gridx = 0;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.add(getPnlAcaoReticulado(), gridBagConstraints);
			
		}
		return jContentPane;
	}

	/**
	 * This method initializes pnlReticulado	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlAcaoReticulado() {
		if (pnlAcaoReticulado == null) {
			pnlAcaoReticulado = new PnlAcaoReticulado();
		}
		return pnlAcaoReticulado;
	}

	/**
	 * Launches this application
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Aplicacao application = new Aplicacao();
				application.getJFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
				application.getJFrame().setVisible(true);
			}
		});
	}

}
