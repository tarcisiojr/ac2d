package ufu.mestrado.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import ufu.mestrado.Reticulado;
import ufu.mestrado.gui.ui.LblBitUI;
import ufu.mestrado.gui.ui.PnlReticuladoUI;

/**
 * Panel que representa uma reticulado.
 * 
 * @author Tarcísio Abadio de Magalhães Júnior
 *
 */
public class PnlReticulado extends PnlReticuladoUI {
	private static final long serialVersionUID = 1L;
	private Reticulado reticulado;  //  @jve:decl-index=0:
	private LblBitUI lblReticulado[][];
	
	/**
	 * Construtor padrão.
	 */
	public PnlReticulado() {
		super();
	}
	
	/**
	 * Configura um novo reticulado no painel.
	 * @param reticulado Novo reticulado.
	 */
	public void setReticulado(Reticulado reticulado) {
		this.reticulado = reticulado;
		
		// Cria a parte gráfica do panel.
		criarReticuladoUI();
	}

	/**
	 * Cria a parte gráfica do reticulado.
	 */
	private void criarReticuladoUI() {
		removeAll();
		setLayout(new GridBagLayout());
		
		// Reticulado da parte gráfica.
		lblReticulado = new LblBitUI[reticulado.getLinhas()][reticulado.getColunas()];
		
		for (int i = 0; i < reticulado.getLinhas(); i++) {
			for (int j = 0; j < reticulado.getColunas(); j++) {
				
				adicionarBit(i, j, reticulado.get(i, j));
			}
		}
		
		updateUI();
	}
	
	/**
	 * Adiciona um novo bit na reticulado gráfico.
	 * @param linha Linha do novo bit.
	 * @param coluna Coluna do novo bit.
	 * @param bit Novo bit.
	 */
	public void adicionarBit(int linha, int coluna, boolean bit) {
		GridBagConstraints constraints = new GridBagConstraints();
		
		constraints.gridx = coluna;
		constraints.gridy = linha;
		
		LblBitUI lblBit = new LblBitUI(bit);
		
		if (lblReticulado != null)
			lblReticulado[linha][coluna] = lblBit;
		
		add(lblBit, constraints);
	}
	
	/**
	 * Retorna o label para a linha e coluna fornecidos.
	 * @param linha Linha.
	 * @param coluna Coluna.
	 * @return O label que está posicionado na linha e coluna fornecidos.
	 */
	public LblBitUI getLblBit(int linha, int coluna) {
		return lblReticulado[linha][coluna];
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
