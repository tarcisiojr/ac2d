package ufu.mestrado.gui.ui;

import java.awt.GridBagLayout;
import javax.swing.JPanel;

import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Transicao;
import ufu.mestrado.Util;
import ufu.mestrado.gui.PnlReticulado;

import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.ImageIcon;

public class PnlTransicaoUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private JLabel lblIndice = null;
	private PnlReticulado pnlTransicao = null;
	private JLabel lblSeta = null;
	private JLabel lblValor = null;
	private JLabel lblContorno = null;
	
	private Transicao transicao;
	private Transicao transicaoContorno;
	
	/**
	 * This is the default constructor
	 */
	public PnlTransicaoUI(Transicao transicao, Transicao transicaoContorno) {
		super();
		
		this.transicaoContorno = transicaoContorno;
		
		if (transicao == null)
			transicao = new Transicao(1, 2, DirecaoCalculo.NORTE, false);
		
		this.transicao = transicao;
		
		initialize();
		criarTransicaoUI();
	}
	
	public PnlTransicaoUI() {
		this(null, null);
	}

	private void criarTransicaoUI() {
		lblIndice.setText(String.valueOf(transicao.getIndice()));
		lblValor.setText(String.valueOf(Util.toInt(transicao.getValor())));
		
		if (transicaoContorno != null)
			lblContorno.setText(String.valueOf(Util.toInt(transicaoContorno.getValor())));
		
		pnlTransicao.setLayout(new GridBagLayout());
		
		int bits = transicao.getBitsCima();
		int colunaCentral = transicao.getRaio();
		for (int i = transicao.getRaio() -1, linha = 0; i >= 0; i--, linha++) {
			pnlTransicao.adicionarBit(linha, colunaCentral, (bits & (1 << i)) != 0);
		}

		bits = transicao.getBitsLinhaCentral();
		int linha = transicao.getRaio();
		for (int i = transicao.getRaio() * 2, coluna = 0; i >= 0; i--, coluna++) {
			pnlTransicao.adicionarBit(linha, coluna, (bits & (1 << i)) != 0);
		}
		
		linha = transicao.getRaio() + 1;
		bits = transicao.getBitsBaixo();
		for (int i = transicao.getRaio() -1; i >= 0; i--, linha++) {
			pnlTransicao.adicionarBit(linha, colunaCentral, (bits & (1 << i)) != 0);
		}
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
		gridBagConstraints12.gridx = 0;
		gridBagConstraints12.gridy = 4;
		lblContorno = new JLabel();
		lblContorno.setText("");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 2;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 3;
		lblValor = new JLabel();
		lblValor.setText("Valor");
		lblSeta = new JLabel();
		lblSeta.setText("");
		lblSeta.setIcon(new ImageIcon(getClass().getResource("/ufu/mestrado/imagens/seta_24x24.png")));
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 0;
		gridBagConstraints1.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		lblIndice = new JLabel();
		lblIndice.setText("Índice");
		this.setSize(300, 200);
		this.setLayout(new GridBagLayout());
		this.add(lblIndice, gridBagConstraints);
		this.add(getPnlTransicao(), gridBagConstraints1);
		this.add(lblSeta, gridBagConstraints2);
		this.add(lblValor, gridBagConstraints11);
		this.add(lblContorno, gridBagConstraints12);
	}

	/**
	 * This method initializes pnlTransicao	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private PnlReticulado getPnlTransicao() {
		if (pnlTransicao == null) {
			pnlTransicao = new PnlReticulado();
		}
		return pnlTransicao;
	}

}
