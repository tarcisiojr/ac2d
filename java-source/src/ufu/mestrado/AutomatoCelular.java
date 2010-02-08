package ufu.mestrado;

import ufu.mestrado.exception.AutomatoCelularException;

public class AutomatoCelular implements AutomatoCelularListener {

	private Reticulado reticulado;
	private Regra regraPrincipal;
	private Regra regraContorno;
	private AutomatoCelularListener listener;
	
	private boolean debug = false;
	
	/**
	 * Construtor.
	 * @param reticuladoInicial Reticulado inicial do automato.
	 * @param regra Regra para cálculo da pré-image e evolução.
	 * @throws AutomatoCelularException
	 */
	public AutomatoCelular(Reticulado reticuladoInicial, Regra regra) throws AutomatoCelularException {
		this.reticulado = reticuladoInicial;
		this.regraPrincipal = regra;
		this.regraContorno = Regra.criarRegraContorno(regra);
		this.listener = this;
	}
	
	/**
	 * Realiza o cálcula da pre-imagem de acordo com o número solicitado.
	 * @param numeroCalculos Números de cálculos de pré-imagem.
	 * @return O Reticulado atual do automato.
	 */
	public Reticulado calcularPreImage(int numeroCalculos) {
		Reticulado preImagem = this.reticulado;
		
		for (int i = 0; i < numeroCalculos; i++) {
			preImagem = calcularPreImagem(); 
		}
		
		return preImagem;
	}
	
	/**
	 * Realiza o cálculo da pré-imagem.
	 * @return O reticulado da pré-imagem calculada.
	 */
	public Reticulado calcularPreImagem() {
		listener.antesCalcularPreImagem();
		
		// Cria o reticulado da préimagem, e já calcula os bits da borda.
		Reticulado preImagem = criarReticuladoPreImagem(reticulado);
		
		listener.aposCriarReticuladoPreImagem(preImagem);
		
		int raio = regraPrincipal.getRaio();
		
		// Calculando os bits do interior do reticulado
		for (int linha = preImagem.getLinhas() - raio - 1; linha >= raio; linha--) {
			for (int coluna = preImagem.getColunas() - raio - 1; coluna >= raio; coluna--) {
				int indices[] = Transicao.getIndices(preImagem, linha, coluna, raio, regraPrincipal.getDirecaoCalculo());

				listener.aposBuscaTransicoes(indices);
				
				int indice = 0;
				
				Transicao transicao = regraPrincipal.getTransicao(indices[indice]);
				
				boolean bit = transicao.getPrimeiroBit(raio);
				
				int linhaReticulado = linha + raio;
				
				if (transicao.getValor() != reticulado.get(linhaReticulado, coluna)) {
					bit = !bit;
					indice = 1;
				}
				
				listener.aposTransicaoEscolhida(indices[indice]);
				listener.aposGetBitReticulado(reticulado, linhaReticulado, coluna);
				
				preImagem.set(linha, coluna, bit);
				
				listener.aposSetBitPreImagem(preImagem, linha, coluna);
			}
		}
		
		// Remove o deslocamento da pré-imagem.
		preImagem.removerDeslocamento();
		
		listener.aposCalcularPreImagem(preImagem);
		
		reticulado = preImagem;
		
		return preImagem;
	}
	
	public Reticulado evoluir(int numeroEvolucoes) {
		Reticulado ret = reticulado;
		
		for (int i = 0; i < numeroEvolucoes; i++) {
			ret = evolouir();
		}
		
		return ret;
	}
	
	public Reticulado evolouir() {
		Reticulado preImagem = aplicarDeslocamento(reticulado); 
		reticulado = criarReticulado(preImagem);
		
		int raio = regraPrincipal.getRaio();
		int linhas = reticulado.getLinhas();
		int colunas = reticulado.getColunas();
		
		for (int coluna = 0; coluna < reticulado.getColunas(); coluna++) {
			for (int linha = 0; linha < reticulado.getLinhas(); linha++) {
				
				Regra regra = this.regraPrincipal;
				
				if ((coluna < raio) || (coluna >= (colunas - raio)) || 
					(linha  < raio) || (linha  >= (linhas  - raio))) {
					regra = this.regraContorno;
				}
				
				int indice = Transicao.getIndice(preImagem, linha, coluna, regra.getRaio(), regra.getDirecaoCalculo());
				
				Transicao transicao = regra.getTransicao(indice);
				
				reticulado.set(linha + raio, coluna, transicao.getValor());
			}
		}
		
		reticulado.removerDeslocamento();
		
		return reticulado;
	}
	
	/**
	 * Cria um reticulado da pré image a partir do reticulado fornecido.
	 * @param reticulado
	 * @return Reticulado da pré-imagem.
	 */
	private Reticulado criarReticuladoPreImagem(Reticulado reticulado) {
		int raio = regraPrincipal.getRaio();
		
		int linhas = reticulado.getLinhas();
		int colunas = reticulado.getColunas();
		
		// Criando o reticulado da pré-imagem.
		Reticulado preImagem = aplicarDeslocamento(new Reticulado(linhas, colunas)); 
			//new Reticulado(new boolean[linhas][colunas]);
		
		listener.antesCriarReticuladoPreImagem(preImagem);
		
		int deslocamento = raio * 4; 
		
		for (int coluna = 0; coluna < colunas; coluna++) {
			for (int linha = 0; linha < linhas; linha++) {
				// Calcula os bits de contorno da primeira e da ultima coluna
				// e da primeira e ultima linha
				if ((coluna < raio) || (coluna >= (colunas - raio)) || 
					(linha  <= raio)) {
					
					boolean bit = reticulado.get(linha, coluna);
					
					listener.aposGetBitReticulado(reticulado, linha, coluna);
					
					int indice = Util.toInt(bit) << deslocamento;
					
					Transicao transicao = regraContorno.getTransicao(indice);
					
					preImagem.set(linha - raio, coluna, transicao.getValor());
					
					listener.aposSetBitPreImagem(preImagem, linha, coluna);
				}
			}
		}
		
		return preImagem;
	}
	
	
	/**
	 * Cria um reticulado para evolução.
	 * @param preImage Pré imagem o qual será criado o reticulado.
	 * @return
	 */
	private Reticulado criarReticulado(Reticulado preImage) {
		int linhas = preImage.getLinhas();
		int colunas = preImage.getColunas();
		
		return new Reticulado(linhas, colunas);
	}

	/**
	 * Aplica um deslocamento no reticulado fornecido.
	 * @param reticulado
	 * @return
	 */
	private Reticulado aplicarDeslocamento(Reticulado reticulado) {
		int deslocamento = getDeslocamento();
		
		//reticulado.setDeslocamentoColuna(deslocamento);
		reticulado.setDeslocamentoLinha(deslocamento);
		
		return reticulado;
	}
	
	private int getDeslocamento() {
		return 0;
		//return 1;
	}
	
	public AutomatoCelularListener getListener() {
		return listener;
	}

	public void setListener(AutomatoCelularListener listener) {
		this.listener = listener;
	}
	
	public Reticulado getReticulado() {
		return reticulado;
	}
	
	public void setReticulado(Reticulado reticulado) {
		this.reticulado = reticulado;
	}
	
	//=============== LISTENERS ==============
	@Override
	public void antesCalcularPreImagem() {
	}

	@Override
	public void aposBuscaTransicoes(int indices[]) {
	}

	@Override
	public void aposCalcularPreImagem(Reticulado preImagem) {
		if (debug) {
			System.out.println("AutomatoCelular.aposCalcularPreImagem()");
			System.out.println("Reticulado");
			System.out.println(reticulado);
			System.out.println("Pre-imagem");
			System.out.println(preImagem);
			System.out.println("XOR pre-image com o reticulado");
			System.out.println(reticulado.xor(preImagem));
		}
	}

	@Override
	public void aposCriarReticuladoPreImagem(Reticulado preImagem) {
		if (debug/* || true*/) {
			System.out.println("-> aposCriarReticuladoPreImagem ");
			System.out.println("XOR reticulado com a pré-imagem");
			System.out.println(reticulado.xor(preImagem));
			System.out.println("Reticulado");
			System.out.println(reticulado);
			System.out.println("Pré-imagem");
			System.out.println(preImagem);
		}
	}

	@Override
	public void aposSetBitReticulado(int linha, int coluna, boolean valor) {
	}

	@Override
	public void aposSetBitPreImagem(Reticulado preImagem, int linha, int coluna) {
	}

	@Override
	public void antesCriarReticuladoPreImagem(Reticulado preImagem) {
	}

	@Override
	public void aposGetBitReticulado(Reticulado reticulado, int linha, int coluna) {
	}

	@Override
	public void aposTransicaoEscolhida(int indice) {
		if (debug) {
			System.out.println("Transicao escolhida: " + indice);
		} 
	} 
	
	public static void main(String[] args) throws Exception {
		String ret[] = {"0101",
				        "0001",
				        "0110",
				        "1001",
				        "1100"};
		
		Reticulado reticulado = new Reticulado(ret);
		
		Regra regra = Regra.criar("01101000111101001001011100001011", DirecaoCalculo.CIMA);
		
		AutomatoCelular ac = new AutomatoCelular(reticulado, regra);
		
		Reticulado pi = ac.calcularPreImagem();
		pi = ac.calcularPreImagem();
		pi = ac.calcularPreImagem();
		
		System.out.println(pi);
		
		ac.evolouir();
		ac.evolouir();
		System.out.println(ac.evolouir());
	}
}
