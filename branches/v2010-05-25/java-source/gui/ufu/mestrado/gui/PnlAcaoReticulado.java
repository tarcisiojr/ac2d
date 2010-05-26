package ufu.mestrado.gui;

import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.AutomatoCelularHandler;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;
import ufu.mestrado.Transicao;
import ufu.mestrado.exception.AutomatoCelularException;
import ufu.mestrado.gui.ui.LblBitUI;
import ufu.mestrado.gui.ui.PnlAcaoReticuladoUI;
import ufu.mestrado.gui.ui.PnlTransicaoUI;
import ufu.mestrado.gui.util.Configuracoes;
import ufu.mestrado.gui.util.Logger;
import ufu.mestrado.gui.util.Mensagem;

public class PnlAcaoReticulado extends PnlAcaoReticuladoUI implements AutomatoCelularHandler {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getClass(PnlAcaoReticulado.class);
	
	private static final DirecaoCalculo DIRECAO_CALCULO = DirecaoCalculo.NORTE;

	private Configuracoes configuracoes;
	
	private Regra regra; 
	private Regra regraContorno;
	private Reticulado reticulado;
	private AutomatoCelular automatoCelular;
	private boolean passoApasso = true;
	private boolean calculoFinalizado = true;
	
	private Color corAtual;
	
	/**
	 * This method initializes 
	 */
	public PnlAcaoReticulado() {
		super();
		
		configuracoes = new Configuracoes();
	}

	@Override
	protected void criarReticulado() {
		Integer linhas = 4;
		Integer colunas = 4;
		
		try {
			linhas = Integer.parseInt(getFldLinhas().getText().trim());
		} catch (NumberFormatException e) {
			Mensagem.warn("O valor fornecido na linhas é inválido!");
			return;
		}
		
		try {
			colunas = Integer.parseInt(getFldColunas().getText().trim());
		} catch (NumberFormatException e) {
			Mensagem.warn("O valor fornecido na colunas é inválido!");
			return;
		}
		
		if (linhas < 4 || colunas < 4) {
			Mensagem.warn("O valor da linhas/colunas deve ser superior à 4!");
			return;
		}
		
		setReticulado(new Reticulado(new boolean[linhas][colunas]));
	}
	
	/**
	 * Carrega o reticulado.
	 * @param reticulado Reticulado a ser carregado.
	 */
	private void setReticulado(Reticulado reticulado) {
		this.reticulado = reticulado;
		getPnlReticulado().setReticulado(this.reticulado);
		
		getFldColunas().setText(String.valueOf(reticulado.getColunas()));
		getFldLinhas().setText(String.valueOf(reticulado.getLinhas()));
	}
	
	/**
	 * Carrega a regra fornecida na tela.
	 * @param regra Regra a ser carregada.
	 */
	private void setRegra(Regra regra) {
		this.regra = regra;
		this.regraContorno = Regra.criarRegraContorno(this.regra);
		
		int total = regra.getTotalTransicoes();
		
		getPnlTransicoes().removeAll();
		getPnlTransicoes().setLayout(new GridLayout(1, 2 * total - 1));
		
		for (int i = 0; i < total; i++) {
			if (i > 0)
				getPnlTransicoes().add(new JPanel());
			
			Transicao transicao = regra.getTransicao(i);
			Transicao transicaoContorno = regraContorno.getTransicao(i);
			
			getPnlTransicoes().add(new PnlTransicaoUI(transicao, transicaoContorno));
		}
		
		updateUI();
	}

	@Override
	protected void carregarRegra() {
		int raio = 1;
		
		try {
			raio = Integer.parseInt(getFldRaio().getText().trim());
		} catch (NumberFormatException e) {
			Mensagem.warn("Informe um raio válido!");
			return;
		}
		
		String strRegra = getFldRegra().getText().trim();
		
		int tamMaxRegra = Regra.getTamanhoMaximoRegra(raio);
		
		if (strRegra.length() != tamMaxRegra) {
			Mensagem.warn("O tamanho da regra fornecida não condiz com o valor esperado! Valor esperado " + 
					tamMaxRegra);
			return;
		}
		
		// Carrega a regra na tela.
		setRegra(Regra.criar(strRegra, DIRECAO_CALCULO));
	}

	@Override
	protected void carregarArquivo() {
		logger.debug("Open file chooser->" + configuracoes.getCaminhoPadrao());
		
		JFileChooser fileChooser = new JFileChooser(configuracoes.getCaminhoPadrao());
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "*.ac - Automato celular";
			}
			
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(".ac");
			}
		});
		
		fileChooser.showOpenDialog(this);
		
		File file = fileChooser.getSelectedFile();
		
		logger.debug("Arquivo selecionado: " + file);
		
		// Foi selecionado um arquivo
		if (file != null) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				
				String strRegra = reader.readLine();
				
				if (strRegra == null)
					Mensagem.error("O arquivo fornecido não possui regra!");
				
				Regra regra = Regra.criar(strRegra, DIRECAO_CALCULO);
				
				getFldRegra().setText(strRegra);
				getFldRaio().setText(String.valueOf(regra.getRaio()));
				
				setRegra(regra);
				
				List<String> lista = new ArrayList<String>();
				String linha = null;
				
				// Lendo as linhas do reticulado.
				while ((linha = reader.readLine()) != null) {
					lista.add(linha);
				}
				
				logger.debug("Lista=>" + lista);
				Reticulado reticulado = new Reticulado(lista.toArray(new String[lista.size()]));
				setReticulado(reticulado);
			} catch (Exception e) {
				Mensagem.error("Erro ao carregar o arquivo selecionado!");
				logger.error("Erro ao ler o arquivo!", e);
			}
		}
	}

	@Override
	protected void executarAteFim() {
		passoApasso = false;
		passoAPasso();
	}

	@Override
	protected void iniciar() {
		try {
			if (reticulado == null) {
				Mensagem.warn("Informe um reticulado!");
				return;
			}
			
			if (regra == null) {
				Mensagem.warn("Informe uma regra!");
				return;
			}
			
			automatoCelular = new AutomatoCelular(reticulado, regra);
			automatoCelular.setHandler(this);
		} catch (AutomatoCelularException e) {
			Mensagem.error("Erro ao criar o automato celular: " + e.getMessage());
			logger.error("Erro ao criar o automato celular!", e);
		}
	}

	@Override
	protected void passoAPasso() {
		if (calculoFinalizado) {
			calculoFinalizado = false;
			
			(new Thread() {
				public void run() {
					parar();
					
					// Inicia o calculo da pre-imagem.
					automatoCelular.calcularPreImagem();
				};
			}).start();
		} else {
			continuar();	
		}
	}

	@Override
	public void antesCalcularPreImagem() {
		setReticulado(automatoCelular.getReticulado());
	}

	private void continuar() {
		synchronized (automatoCelular) {
			try {
				automatoCelular.notify();
			} catch (Exception e) {}	
		}
	}
	
	private void parar() {
		if (passoApasso) {
			synchronized (automatoCelular) {
				try {
					automatoCelular.wait();
				} catch (InterruptedException e) {
					Mensagem.error(e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void aposBuscaTransicoes(int indices[]) {
		
	}

	@Override
	public void aposCalcularPreImagem(Reticulado preImagem) {
		passoApasso = true;
		calculoFinalizado = true;
	}

	@Override
	public void aposCriarReticuladoPreImagem(Reticulado preImagem) {
		corAtual = Color.LIGHT_GRAY;
	}

/*	@Override
	public void aposSetBitReticulado(Reticulado reticulado, int linha, int coluna) {
	}*/

	@Override
	public void aposSetBitReticulado(Reticulado preImagem, int linha, int coluna, boolean isPreImagem) {
		boolean valor = preImagem.get(linha, coluna);
		
		LblBitUI lblBit = getPnlPreImagem().getLblBit(preImagem.getIndiceLinha(linha), preImagem.getIndiceColuna(coluna));
		
		lblBit.setBackground(corAtual);
		lblBit.setBit(valor);
		
		parar();
	}
	
	@Override
	public void aposGetBitReticulado(Reticulado reticulado, int linha, int coluna) {
		//System.out.println("PnlAcaoReticulado.aposGetBitReticulado()");
		
		LblBitUI lblBit = getPnlReticulado().getLblBit(reticulado.getIndiceLinha(linha), reticulado.getIndiceColuna(coluna));
		
		lblBit.setBackground(corAtual);
		
		//parar();
	}

	@Override
	public void antesCriarReticuladoPreImagem(Reticulado preImagem) {
		getPnlPreImagem().setReticulado(preImagem);
		
		corAtual = Color.YELLOW;
	}

	@Override
	public void aposTransicaoEscolhida(int indice) {
		JPanel panel  = getPnlTransicao();
		panel.removeAll();
		panel.setLayout(new GridLayout(1, 1));
		
		Transicao transicao = regra.getTransicao(indice);
		
		panel.add(new PnlTransicaoUI(transicao, regraContorno.getTransicao(indice)));
		panel.updateUI();
	}

	@Override
	public Regra getRegraPrincipal() {
		return null;
	}

	@Override
	public Reticulado getReticuladoInicial() {
		return null;
	} 
}
