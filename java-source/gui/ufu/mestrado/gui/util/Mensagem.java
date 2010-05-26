package ufu.mestrado.gui.util;

import javax.swing.JOptionPane;

public abstract class Mensagem {
	public static void info(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, 
				"Informa��o", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void warn(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, 
				"Advert�ncia", JOptionPane.WARNING_MESSAGE);
	}
	
	public static void error(String mensagem) {
		JOptionPane.showMessageDialog(null, mensagem, 
				"ERRO", JOptionPane.WARNING_MESSAGE);
	}
}
