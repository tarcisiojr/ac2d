package ufu.mestrado.gui.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import ufu.mestrado.gui.PnlReticulado;

public abstract class PnlAcaoReticuladoUI extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel pnlConfiguracoes = null;
	private JLabel lblLinhas = null;
	private JLabel lblColunas = null;
	private JTextField fldLinhas = null;
	private JTextField fldColunas = null;
	private JButton btnCriarReticulado = null;
	private PnlReticulado pnlReticulado = null;
	private JScrollPane scrollPane = null;
	private JScrollPane scrTransicoes = null;
	private JPanel pnlTransicoes = null;
	private JPanel pnlRegra = null;
	private JLabel lblRegra = null;
	private JTextField fldRegra = null;
	private JButton btnCarregarRegra = null;
	private JButton btnIniciar = null;
	private JButton btnPassoAPasso = null;
	private JButton btnIrAteFim = null;
	private JLabel lblRaio = null;
	private JTextField fldRaio = null;
	private JPanel pnlRegraConf = null;
	private JPanel pnlTransicao = null;
	private JButton btnCarregarReticulado = null;
	private JScrollPane scrPreImagem = null;
	private PnlReticulado pnlPreImagem = null;
	private JScrollPane scrTransicao = null;
	private JPanel pnlStatus = null;
	private JLabel lblStatus = null;
	/**
	 * This is the default constructor
	 */
	public PnlAcaoReticuladoUI() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints17 = new GridBagConstraints();
		gridBagConstraints17.gridx = 0;
		gridBagConstraints17.weightx = 1.0;
		gridBagConstraints17.anchor = GridBagConstraints.WEST;
		gridBagConstraints17.insets = new Insets(2, 2, 2, 2);
		gridBagConstraints17.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints17.gridwidth = 3;
		gridBagConstraints17.gridy = 3;
		GridBagConstraints gridBagConstraints19 = new GridBagConstraints();
		gridBagConstraints19.fill = GridBagConstraints.BOTH;
		gridBagConstraints19.weighty = 1.0;
		gridBagConstraints19.gridx = 0;
		gridBagConstraints19.gridy = 1;
		gridBagConstraints19.weightx = 0.0;
		GridBagConstraints gridBagConstraints22 = new GridBagConstraints();
		gridBagConstraints22.fill = GridBagConstraints.BOTH;
		gridBagConstraints22.gridy = 0;
		gridBagConstraints22.weightx = 2.0;
		gridBagConstraints22.weighty = 2.0;
		gridBagConstraints22.insets = new Insets(4, 4, 4, 4);
		gridBagConstraints22.gridheight = 2;
		gridBagConstraints22.gridx = 2;
		GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
		gridBagConstraints41.gridx = 0;
		gridBagConstraints41.gridwidth = 3;
		gridBagConstraints41.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints41.insets = new Insets(0, 4, 4, 4);
		gridBagConstraints41.gridy = 2;
		GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
		gridBagConstraints21.gridx = 1;
		gridBagConstraints21.fill = GridBagConstraints.BOTH;
		gridBagConstraints21.weightx = 2.0;
		gridBagConstraints21.weighty = 2.0;
		gridBagConstraints21.insets = new Insets(4, 4, 4, 4);
		gridBagConstraints21.gridwidth = 1;
		gridBagConstraints21.gridheight = 2;
		gridBagConstraints21.gridy = 0;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.gridy = 1;
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.fill = GridBagConstraints.NONE;
		gridBagConstraints.insets = new Insets(4, 4, 0, 0);
		gridBagConstraints.gridy = 0;
		this.setSize(707, 672);
		this.setLayout(new GridBagLayout());
		this.add(getPnlConfiguracoes(), gridBagConstraints);
		this.add(getScrollPane(), gridBagConstraints21);
		this.add(getPnlRegra(), gridBagConstraints41);
		this.add(getScrTransicao(), gridBagConstraints19);
		this.add(getScrPreImagem(), gridBagConstraints22);
		this.add(getPnlStatus(), gridBagConstraints17);
	}

	/**
	 * This method initializes pnlConfiguracoes	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlConfiguracoes() {
		if (pnlConfiguracoes == null) {
			GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
			gridBagConstraints18.gridx = 0;
			gridBagConstraints18.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints18.gridwidth = 2;
			gridBagConstraints18.gridy = 3;
			GridBagConstraints gridBagConstraints13 = new GridBagConstraints();
			gridBagConstraints13.gridx = 0;
			gridBagConstraints13.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints13.gridwidth = 2;
			gridBagConstraints13.gridy = 6;
			GridBagConstraints gridBagConstraints12 = new GridBagConstraints();
			gridBagConstraints12.gridx = 0;
			gridBagConstraints12.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints12.gridwidth = 2;
			gridBagConstraints12.gridy = 5;
			GridBagConstraints gridBagConstraints10 = new GridBagConstraints();
			gridBagConstraints10.gridx = 0;
			gridBagConstraints10.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints10.gridwidth = 2;
			gridBagConstraints10.gridy = 4;
			GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
			gridBagConstraints5.gridx = 0;
			gridBagConstraints5.gridheight = 1;
			gridBagConstraints5.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints5.gridwidth = 2;
			gridBagConstraints5.gridy = 2;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints4.anchor = GridBagConstraints.EAST;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.NONE;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints3.anchor = GridBagConstraints.NORTH;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints1.anchor = GridBagConstraints.EAST;
			gridBagConstraints1.gridy = 1;
			lblColunas = new JLabel();
			lblColunas.setText("Colunas:");
			lblLinhas = new JLabel();
			lblLinhas.setText("Linhas:");
			pnlConfiguracoes = new JPanel();
			pnlConfiguracoes.setLayout(new GridBagLayout());
			pnlConfiguracoes.setBorder(BorderFactory.createTitledBorder(null, "Configurações", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), Color.black));
			pnlConfiguracoes.add(lblLinhas, gridBagConstraints4);
			pnlConfiguracoes.add(lblColunas, gridBagConstraints1);
			pnlConfiguracoes.add(getFldLinhas(), gridBagConstraints2);
			pnlConfiguracoes.add(getFldColunas(), gridBagConstraints3);
			pnlConfiguracoes.add(getBtnCriarReticulado(), gridBagConstraints5);
			pnlConfiguracoes.add(getBtnIniciar(), gridBagConstraints10);
			pnlConfiguracoes.add(getBtnPassoAPasso(), gridBagConstraints12);
			pnlConfiguracoes.add(getBtnIrAteFim(), gridBagConstraints13);
			pnlConfiguracoes.add(getBtnCarregarReticulado(), gridBagConstraints18);
		}
		return pnlConfiguracoes;
	}

	/**
	 * This method initializes fldLinhas	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	protected JTextField getFldLinhas() {
		if (fldLinhas == null) {
			fldLinhas = new JTextField();
			fldLinhas.setPreferredSize(new Dimension(70, 20));
			fldLinhas.setMaximumSize(new Dimension(70, 20));
			fldLinhas.setText("5");
			fldLinhas.setMinimumSize(new Dimension(70, 20));
		}
		return fldLinhas;
	}

	/**
	 * This method initializes fldColunas	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	protected JTextField getFldColunas() {
		if (fldColunas == null) {
			fldColunas = new JTextField();
			fldColunas.setPreferredSize(new Dimension(70, 20));
			fldColunas.setMaximumSize(new Dimension(70, 20));
			fldColunas.setText("5");
			fldColunas.setMinimumSize(new Dimension(70, 20));
		}
		return fldColunas;
	}

	/**
	 * This method initializes btnCriarReticulado	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCriarReticulado() {
		if (btnCriarReticulado == null) {
			btnCriarReticulado = new JButton();
			
			btnCriarReticulado.setPreferredSize(new Dimension(120, 40));
			btnCriarReticulado.setMaximumSize(new Dimension(120, 40));
			btnCriarReticulado.setToolTipText("Criar Reticulado");
			btnCriarReticulado.setText("<html><center>Criar Reticulado</center></html>");
			btnCriarReticulado.setFont(new Font("Dialog", Font.PLAIN, 11));
			btnCriarReticulado.setHorizontalTextPosition(SwingConstants.RIGHT);
			btnCriarReticulado.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnCriarReticulado.setVerticalAlignment(SwingConstants.BOTTOM);
			btnCriarReticulado.setFocusPainted(false);
			btnCriarReticulado.setMinimumSize(new Dimension(120, 40));
			btnCriarReticulado.setHorizontalAlignment(SwingConstants.RIGHT);
			btnCriarReticulado.setIcon(new ImageIcon(getClass().getResource("/ufu/mestrado/imagens/grid_32x32.png")));
			btnCriarReticulado.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					criarReticulado();
				}
			});
		}
		return btnCriarReticulado;
	}
	
	/**
	 * This method initializes pnlReticulado	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	protected PnlReticulado getPnlReticulado() {
		if (pnlReticulado == null) {
			pnlReticulado = new PnlReticulado();
		}
		return pnlReticulado;
	}

	/**
	 * This method initializes scrollPane	
	 * 	
	 * @return java.awt.ScrollPane	
	 */
	private JScrollPane getScrollPane() {
		if (scrollPane == null) {
			scrollPane = new JScrollPane();
			scrollPane.setBorder(BorderFactory.createTitledBorder(null, "Reticulado", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), Color.black));
			scrollPane.setViewportView(getPnlReticulado());
		}
		return scrollPane;
	}

	/**
	 * This method initializes scrTransicoes	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrTransicoes() {
		if (scrTransicoes == null) {
			scrTransicoes = new JScrollPane();
			scrTransicoes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			scrTransicoes.setBorder(BorderFactory.createTitledBorder(null, "Transições", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), Color.black));
			scrTransicoes.setViewportView(getPnlTransicoes());
		}
		return scrTransicoes;
	}

	/**
	 * This method initializes pnlTransicoes	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	protected JPanel getPnlTransicoes() {
		if (pnlTransicoes == null) {
			pnlTransicoes = new JPanel();
			GridLayout layout = new GridLayout();
			layout.setVgap(5);
			pnlTransicoes.setLayout(layout);
		}
		return pnlTransicoes;
	}

	/**
	 * This method initializes pnlRegra	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlRegra() {
		if (pnlRegra == null) {
			GridBagConstraints gridBagConstraints16 = new GridBagConstraints();
			gridBagConstraints16.gridx = 0;
			gridBagConstraints16.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints16.weightx = 1.0;
			gridBagConstraints16.gridy = 0;
			lblRaio = new JLabel();
			lblRaio.setText("Raio:");
			GridBagConstraints gridBagConstraints9 = new GridBagConstraints();
			gridBagConstraints9.gridx = 1;
			gridBagConstraints9.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints9.gridy = 0;
			lblRegra = new JLabel();
			lblRegra.setText("Regra:");
			GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
			gridBagConstraints6.fill = GridBagConstraints.BOTH;
			gridBagConstraints6.gridwidth = 2;
			gridBagConstraints6.gridx = 0;
			gridBagConstraints6.gridy = 1;
			gridBagConstraints6.ipadx = 2;
			gridBagConstraints6.ipady = 120;
			gridBagConstraints6.weightx = 1.0;
			gridBagConstraints6.weighty = 1.0;
			gridBagConstraints6.insets = new Insets(2, 2, 2, 2);
			pnlRegra = new JPanel();
			pnlRegra.setLayout(new GridBagLayout());
			pnlRegra.setBorder(BorderFactory.createTitledBorder(null, "Regra", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), Color.black));
			pnlRegra.add(getScrTransicoes(), gridBagConstraints6);
			pnlRegra.add(getBtnCarregarRegra(), gridBagConstraints9);
			pnlRegra.add(getPnlRegraConf(), gridBagConstraints16);
		}
		return pnlRegra;
	}

	/**
	 * This method initializes fldRegra	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	protected JTextField getFldRegra() {
		if (fldRegra == null) {
			fldRegra = new JTextField();
			fldRegra.setText("01101000111101001001011100001011");
		}
		return fldRegra;
	}

	/**
	 * This method initializes btnCarregarRegra	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCarregarRegra() {
		if (btnCarregarRegra == null) {
			btnCarregarRegra = new JButton();
			btnCarregarRegra.setText("Carregar Regra");
			btnCarregarRegra.setIcon(new ImageIcon(getClass().getResource("/ufu/mestrado/imagens/reload_16x16.png")));
			btnCarregarRegra.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					carregarRegra();
				}
			});
		}
		return btnCarregarRegra;
	}

	protected abstract void carregarRegra();

	/**
	 * This method initializes btnIniciar	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnIniciar() {
		if (btnIniciar == null) {
			btnIniciar = new JButton();
			btnIniciar.setPreferredSize(new Dimension(120, 40));
			btnIniciar.setMaximumSize(new Dimension(120, 40));
			btnIniciar.setToolTipText("Iniciar");
			btnIniciar.setText("<html><center>Iniciar</center></html>");
			btnIniciar.setFont(new Font("Dialog", Font.PLAIN, 11));
			btnIniciar.setHorizontalTextPosition(SwingConstants.RIGHT);
			btnIniciar.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnIniciar.setVerticalAlignment(SwingConstants.BOTTOM);
			btnIniciar.setFocusPainted(false);
			btnIniciar.setMinimumSize(new Dimension(120, 40));
			btnIniciar.setHorizontalAlignment(SwingConstants.CENTER);
			btnIniciar.setIcon(new ImageIcon(getClass().getResource("/ufu/mestrado/imagens/player_play_32x32.png")));
			btnIniciar.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					iniciar();
				}
			});
		}
		return btnIniciar;
	}

	/**
	 * This method initializes btnPassoAPasso	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnPassoAPasso() {
		if (btnPassoAPasso == null) {
			btnPassoAPasso = new JButton();
			btnPassoAPasso.setPreferredSize(new Dimension(120, 40));
			btnPassoAPasso.setMaximumSize(new Dimension(120, 40));
			btnPassoAPasso.setToolTipText("Iniciar");
			btnPassoAPasso.setText("<html><center>Passo a Passo</center></html>");
			btnPassoAPasso.setFont(new Font("Dialog", Font.PLAIN, 11));
			btnPassoAPasso.setHorizontalTextPosition(SwingConstants.RIGHT);
			btnPassoAPasso.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnPassoAPasso.setVerticalAlignment(SwingConstants.BOTTOM);
			btnPassoAPasso.setFocusPainted(false);
			btnPassoAPasso.setMinimumSize(new Dimension(120, 40));
			btnPassoAPasso.setHorizontalAlignment(SwingConstants.CENTER);
			btnPassoAPasso.setIcon(new ImageIcon(getClass().getResource("/ufu/mestrado/imagens/passo_a_passo_32x32.png")));
			btnPassoAPasso.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					passoAPasso();
				}
			});
		}
		return btnPassoAPasso;
	}

	/**
	 * This method initializes btnIrAteFim	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnIrAteFim() {
		if (btnIrAteFim == null) {
			btnIrAteFim = new JButton();
			btnIrAteFim.setPreferredSize(new Dimension(120, 40));
			btnIrAteFim.setMaximumSize(new Dimension(120, 40));
			btnIrAteFim.setToolTipText("Iniciar");
			btnIrAteFim.setText("<html><center>Executar até o fim</center></html>");
			btnIrAteFim.setFont(new Font("Dialog", Font.PLAIN, 11));
			btnIrAteFim.setHorizontalTextPosition(SwingConstants.RIGHT);
			btnIrAteFim.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnIrAteFim.setVerticalAlignment(SwingConstants.BOTTOM);
			btnIrAteFim.setFocusPainted(false);
			btnIrAteFim.setMinimumSize(new Dimension(120, 40));
			btnIrAteFim.setIcon(new ImageIcon(getClass().getResource("/ufu/mestrado/imagens/player_end_32x32.png")));
			btnIrAteFim.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					executarAteFim();
				}
			});
		}
		return btnIrAteFim;
	}

	/**
	 * This method initializes fldRaio	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	protected JTextField getFldRaio() {
		if (fldRaio == null) {
			fldRaio = new JTextField();
			fldRaio.setPreferredSize(new Dimension(50, 20));
			fldRaio.setMinimumSize(new Dimension(50, 20));
			fldRaio.setText("1");
			fldRaio.setMaximumSize(new Dimension(50, 20));
		}
		return fldRaio;
	}

	/**
	 * This method initializes pnlRegraConf	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlRegraConf() {
		if (pnlRegraConf == null) {
			GridBagConstraints gridBagConstraints15 = new GridBagConstraints();
			gridBagConstraints15.anchor = GridBagConstraints.WEST;
			gridBagConstraints15.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints15.gridx = -1;
			gridBagConstraints15.gridy = -1;
			gridBagConstraints15.weightx = 0.0;
			gridBagConstraints15.fill = GridBagConstraints.NONE;
			GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
			gridBagConstraints14.anchor = GridBagConstraints.WEST;
			gridBagConstraints14.gridx = -1;
			gridBagConstraints14.gridy = -1;
			gridBagConstraints14.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
			gridBagConstraints7.fill = GridBagConstraints.HORIZONTAL;
			gridBagConstraints7.gridx = -1;
			gridBagConstraints7.gridy = -1;
			gridBagConstraints7.weightx = 1.0;
			gridBagConstraints7.insets = new Insets(2, 2, 2, 2);
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.insets = new Insets(2, 2, 2, 2);
			gridBagConstraints8.gridy = -1;
			gridBagConstraints8.gridx = -1;
			pnlRegraConf = new JPanel();
			pnlRegraConf.setLayout(new GridBagLayout());
			pnlRegraConf.add(lblRaio, gridBagConstraints14);
			pnlRegraConf.add(getFldRaio(), gridBagConstraints15);
			pnlRegraConf.add(lblRegra, gridBagConstraints8);
			pnlRegraConf.add(getFldRegra(), gridBagConstraints7);
		}
		return pnlRegraConf;
	}

	/**
	 * This method initializes pnlTransicao	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	protected JPanel getPnlTransicao() {
		if (pnlTransicao == null) {
			pnlTransicao = new JPanel();
			pnlTransicao.setLayout(new GridBagLayout());
			pnlTransicao.setBorder(BorderFactory.createTitledBorder(null, "Transição", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), Color.black));
		}
		return pnlTransicao;
	}

	/**
	 * This method initializes btnCarregarReticulado	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getBtnCarregarReticulado() {
		if (btnCarregarReticulado == null) {
			btnCarregarReticulado = new JButton();
			btnCarregarReticulado.setPreferredSize(new Dimension(120, 40));
			btnCarregarReticulado.setMaximumSize(new Dimension(120, 40));
			btnCarregarReticulado.setToolTipText("Carregar configurações do arquivo");
			btnCarregarReticulado.setText("<html><center>Carregar Arquivo</center></html>");
			btnCarregarReticulado.setFont(new Font("Dialog", Font.PLAIN, 11));
			btnCarregarReticulado.setHorizontalTextPosition(SwingConstants.RIGHT);
			btnCarregarReticulado.setVerticalTextPosition(SwingConstants.BOTTOM);
			btnCarregarReticulado.setVerticalAlignment(SwingConstants.BOTTOM);
			btnCarregarReticulado.setFocusPainted(false);
			btnCarregarReticulado.setMinimumSize(new Dimension(120, 40));
			btnCarregarReticulado.setHorizontalAlignment(SwingConstants.RIGHT);
			btnCarregarReticulado.setIcon(new ImageIcon(getClass().getResource("/ufu/mestrado/imagens/fileopen_32x32.png")));
			btnCarregarReticulado.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					carregarArquivo();
				}
			});
		}
		return btnCarregarReticulado;
	}

	protected abstract void criarReticulado();
	
	protected abstract void executarAteFim();
	
	protected abstract void passoAPasso();
	
	protected abstract void iniciar();
	
	protected abstract void carregarArquivo();

	/**
	 * This method initializes scrPreImagem	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrPreImagem() {
		if (scrPreImagem == null) {
			scrPreImagem = new JScrollPane();
			scrPreImagem.setBorder(BorderFactory.createTitledBorder(null, "Pré-imagem", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Tahoma", Font.PLAIN, 11), Color.black));
			scrPreImagem.setViewportView(getPnlPreImagem());
		}
		return scrPreImagem;
	}

	/**
	 * This method initializes pnlPreImagem	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	protected PnlReticulado getPnlPreImagem() {
		if (pnlPreImagem == null) {
			pnlPreImagem = new PnlReticulado();
		}
		return pnlPreImagem;
	}

	/**
	 * This method initializes scrTransicao	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
	private JScrollPane getScrTransicao() {
		if (scrTransicao == null) {
			scrTransicao = new JScrollPane();
			scrTransicao.setViewportView(getPnlTransicao());
		}
		return scrTransicao;
	}

	/**
	 * This method initializes pnlStatus	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getPnlStatus() {
		if (pnlStatus == null) {
			GridBagConstraints gridBagConstraints20 = new GridBagConstraints();
			gridBagConstraints20.anchor = GridBagConstraints.WEST;
			gridBagConstraints20.fill = GridBagConstraints.NONE;
			lblStatus = getLblStatus();
			lblStatus.setText("AC2D");
			lblStatus.setHorizontalTextPosition(SwingConstants.LEFT);
			lblStatus.setHorizontalAlignment(SwingConstants.LEFT);
			pnlStatus = new JPanel();
			pnlStatus.setLayout(new GridBagLayout());
			pnlStatus.add(lblStatus, gridBagConstraints20);
		}
		return pnlStatus;
	}
	
	protected JLabel getLblStatus() {
		if (lblStatus == null) {
			lblStatus = new JLabel();
		}
		
		return lblStatus;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10"
