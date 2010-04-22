package ufu.mestrado;

import ufu.mestrado.exception.AutomatoCelularException;

public class AutomatoCelular implements AutomatoCelularHandler {
	
	private Reticulado reticuladoInicial;
	private Reticulado reticulado;
	private Regra regraPrincipal;
	private Regra regraContorno;
	//private AutomatoCelularHandler handler;
	
	private boolean debug = false;
	
	/**
	 * Construtor.
	 * @param reticuladoInicial Reticulado inicial do automato.
	 * @param regra Regra para cálculo da pré-image e evolução.
	 * @throws AutomatoCelularException
	 */
	public AutomatoCelular(Reticulado reticuladoInicial, Regra regra) throws AutomatoCelularException {
		this.reticuladoInicial = reticuladoInicial;
		this.reticulado = reticuladoInicial;
		this.regraPrincipal = regra;
		this.regraContorno = Regra.criarRegraContorno(regra);
//		this.handler = this;
	}
	
	/**
	 * Constrói uma nova instância do automato a partir do Handler fornecido.
	 * @param handler Handler de automato celular.
	 */
	public AutomatoCelular(AutomatoCelularHandler handler) {
//		this.handler = handler;
		// Configurando o AC no handler
		handler.setAutomatoCelular(this);
		
		this.reticulado = handler.getReticuladoInicial();
		this.regraPrincipal = handler.getRegraPrincipal();
		this.regraContorno = Regra.criarRegraContorno(this.regraPrincipal);
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
	
	/** Linha que será iniciado o cálculo da pré-imagem. */
	private int linhaInicial;
	
	/** Coluna incial que será iniciado o cálculo da pré-imagem. */
	private int colunaInicial;
	
	/** Linha atual do calculo da pré-imagem. */
	private int linha;
	
	/** Coluna atual do calculo da pré-imagem. */
	private int coluna;
	
	/**
	 * Verifica se o cálculo da pré-imagem foi finalizado.
	 * @param regraPrincipal Regra principal utilizada no cálculo da pré-imagem.
	 * @param preImagem Reticulado da pré-imagem (Borda já calculada).
	 * @return True se calculo da pré-image ainda não foi finalizado, do contrário retorna-se false.
	 */
	private boolean continuarCalculoPI(Reticulado preImagem, Regra regraPrincipal)  {
		switch (regraPrincipal.getDirecaoCalculo()) {
		case DirecaoCalculo.NORTE:
			return linha >= regraPrincipal.getRaio();
			
		case DirecaoCalculo.SUL:
			return linha < (preImagem.getLinhas() - regraPrincipal.getRaio());
			
		case DirecaoCalculo.ESQUERDA:
			return coluna >= regraPrincipal.getRaio();
			
		case DirecaoCalculo.DIREITA:
			return coluna < (preImagem.getColunas() - regraPrincipal.getRaio());
		}
		
		throw new RuntimeException("O tipo de direção da regra fornecida não é suportado!");
	}
	
	/**
	 * Itera o cálculo da pré-imagem, ou seja, atualiza os ponteiros para
	 * a próxima celular a ser calculada.
	 * @param preImagem Reticulado da pré-imagem (Borda já calculada).
	 * @param regraPrincipal Regra principal utilizada no cálculo da pré-imagem.
	 */
	private void iterarCalculoPI(Reticulado preImagem, Regra regraPrincipal) {
		int raio = regraPrincipal.getRaio();
		
		switch (regraPrincipal.getDirecaoCalculo()) {
		case DirecaoCalculo.NORTE:
			coluna--;
			// direita para esquerda, de baixo para cimaa
			if (coluna < raio) {
				linha--;
				coluna = colunaInicial;
			}
			break;
			
		case DirecaoCalculo.SUL:
			// direita para esquerda, de cima para baixo
			coluna--;
			
			if (coluna < raio) {
				linha++;
				coluna = colunaInicial;
			}
			break;
			
		case DirecaoCalculo.ESQUERDA:
			// de cima para baixo, direita para esquerda
			linha++;
			
			if (linha >= preImagem.getLinhas() - raio) {
				coluna--;
				linha = linhaInicial;
			}
			break;
			
		case DirecaoCalculo.DIREITA:
			// de cima para baixo, esquerda para direita
			linha++;
			
			if (linha >= preImagem.getLinhas() - raio) {
				coluna++;
				linha = linhaInicial;
			}
			break;
		}
	}
	
	/**
	 * Inicia os ponteiros para o cálculo da pré-imagem.
	 * @param preImagem Reticulado da pré-imagem (Borda já calculada).
	 * @param regraPrincipal Regra principal utilizada no cálculo da pré-imagem.
	 */
	private void iniciarCalculoPI(Reticulado preImagem, Regra regraPrincipal) {
		int raio = regraPrincipal.getRaio();
		
		switch (regraPrincipal.getDirecaoCalculo()) {
		case DirecaoCalculo.NORTE:
			linhaInicial = preImagem.getLinhas() - raio - 1;
			colunaInicial = preImagem.getColunas() - raio - 1;
			break;
			
		case DirecaoCalculo.SUL:
			linhaInicial = raio;
			colunaInicial = preImagem.getColunas() - raio - 1;
			break;
			
		case DirecaoCalculo.ESQUERDA:
			linhaInicial = raio;
			colunaInicial = preImagem.getColunas() - raio - 1;
			break;
			
		case DirecaoCalculo.DIREITA:
			linhaInicial = raio;
			colunaInicial = raio;
			break;
		}
		
		linha = linhaInicial;
		coluna = colunaInicial;
	}
	
	/**
	 * Retorna o valor da célula central do reticulado a partir
	 * das coordenadas do calculo da pré-imagem.
	 * 
	 * @return Valor da celula centra do reticulado.
	 */
	private boolean getValorReticulado() {
		switch (regraPrincipal.getDirecaoCalculo()) {
		case DirecaoCalculo.NORTE:
			return reticulado.get(linha + regraPrincipal.getRaio(), coluna);
			
		case DirecaoCalculo.SUL:
			return reticulado.get(linha - regraPrincipal.getRaio(), coluna);
			
		case DirecaoCalculo.ESQUERDA:
			return reticulado.get(linha, coluna + regraPrincipal.getRaio());
			
		case DirecaoCalculo.DIREITA:
			return reticulado.get(linha, coluna - regraPrincipal.getRaio());
		}
		
		throw new RuntimeException("Direção inválida!");
	}
	
	/**
	 * Realiza o cálculo da pré-imagem.
	 * @return O reticulado da pré-imagem calculada.
	 */
	public Reticulado calcularPreImagem() {
//		handler.antesCalcularPreImagem();
		
		// Cria o reticulado da préimagem, e já calcula os bits da borda.
		Reticulado preImagem = criarReticuladoPreImagem(reticulado);
		
//		handler.aposCriarReticuladoPreImagem(preImagem);
		
		int raio = regraPrincipal.getRaio();
		 
		// Calculando os bits do interior do reticulado
		// Calculo = 0
		iniciarCalculoPI(preImagem, regraPrincipal);
		
		while (continuarCalculoPI(preImagem, regraPrincipal)) {
			//int indices[] = Transicao.getIndices(preImagem, linha, coluna, raio, regraPrincipal.getDirecaoCalculo());
			int indice =  Transicao.getIndice0(preImagem, linha, coluna, raio, regraPrincipal.getDirecaoCalculo());
			
//			handler.aposBuscaTransicoes(indices);
			
//			int indice = 0;
			
//			Transicao transicao = regraPrincipal.getTransicao(indices[indice]);
			Transicao transicao = regraPrincipal.getTransicao(indice);
			
			boolean bit = transicao.getPrimeiroBit(raio);
			
			if (transicao.getValor() != getValorReticulado()) {
				bit ^= true;
//				indice = 1;
			}
			
//			handler.aposTransicaoEscolhida(indices[indice]);
//			handler.aposGetBitReticulado(reticulado, linhaReticulado, coluna);
			
			preImagem.set(linha, coluna, bit);
			
//			handler.aposSetBitReticulado(preImagem, linha, coluna, true);
			
			//calculo++
			iterarCalculoPI(preImagem, regraPrincipal);
		}
		
		// Remove o deslocamento da pré-imagem.
		preImagem.removerDeslocamento();
		
//		handler.aposCalcularPreImagem(preImagem);
		
		reticulado = preImagem;
		
		return preImagem;
	}
	
	/**
	 * Realiza o cálculo da pré-imagem.
	 * @return O reticulado da pré-imagem calculada.
	public Reticulado calcularPreImagem2() {
		handler.antesCalcularPreImagem();
		
		// Cria o reticulado da préimagem, e já calcula os bits da borda.
		Reticulado preImagem = criarReticuladoPreImagem(reticulado);
		
		handler.aposCriarReticuladoPreImagem(preImagem);
		
		int raio = regraPrincipal.getRaio();
		
		// Calculando os bits do interior do reticulado
		for (int linha = preImagem.getLinhas() - raio - 1; linha >= raio; linha--) {
			for (int coluna = preImagem.getColunas() - raio - 1; coluna >= raio; coluna--) {
				int indices[] = Transicao.getIndices(preImagem, linha, coluna, raio, regraPrincipal.getDirecaoCalculo());

				handler.aposBuscaTransicoes(indices);
				
				int indice = 0;
				
				Transicao transicao = regraPrincipal.getTransicao(indices[indice]);
				
				boolean bit = transicao.getPrimeiroBit(raio);
				
				int linhaReticulado = linha + raio;
				
				if (transicao.getValor() != reticulado.get(linhaReticulado, coluna)) {
					bit = !bit;
					indice = 1;
				}
				
				handler.aposTransicaoEscolhida(indices[indice]);
				handler.aposGetBitReticulado(reticulado, linhaReticulado, coluna);
				
				preImagem.set(linha, coluna, bit);
				
				handler.aposSetBitReticulado(preImagem, linha, coluna, true);
				//handler.aposSetBitPreImagem(preImagem, linha, coluna);
			}
		}
		
		// Remove o deslocamento da pré-imagem.
		preImagem.removerDeslocamento();
		
		handler.aposCalcularPreImagem(preImagem);
		
		reticulado = preImagem;
		
		return preImagem;
	}
	 */
	
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
		
		final int raio = regraPrincipal.getRaio();
		final int linhas = reticulado.getLinhas();
		final int colunas = reticulado.getColunas();
		final int direcao = regraPrincipal.getDirecaoCalculo();
		
		for (int coluna = 0; coluna < reticulado.getColunas(); coluna++) {
			for (int linha = 0; linha < reticulado.getLinhas(); linha++) {
				
				Regra regra = this.regraPrincipal;
				
				if ((coluna < raio) || (coluna >= (colunas - raio)) || 
					(linha  < raio) || (linha  >= (linhas  - raio))) {
					regra = this.regraContorno;
				}
				
				int indice = Transicao.getIndice(preImagem, linha, coluna, regra.getRaio(), regra.getDirecaoCalculo());
				
				Transicao transicao = regra.getTransicao(indice);
				
				switch (direcao) {
				case DirecaoCalculo.NORTE:
					reticulado.set(linha + raio, coluna, transicao.getValor());
					break;

				case DirecaoCalculo.SUL:
					reticulado.set(linha - raio, coluna, transicao.getValor());
					break;
					
				case DirecaoCalculo.ESQUERDA:
					reticulado.set(linha, coluna + raio, transicao.getValor());
					break;
					
				case DirecaoCalculo.DIREITA:
					reticulado.set(linha, coluna - raio, transicao.getValor());
					break;

				}
//				handler.aposSetBitReticulado(reticulado, linha + raio, coluna, false);
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
		final int raio = regraPrincipal.getRaio();
		final int linhas = reticulado.getLinhas();
		final int colunas = reticulado.getColunas();
		
		// Criando o reticulado da pré-imagem.
		Reticulado preImagem = aplicarDeslocamento(new Reticulado(linhas, colunas)); 
			//new Reticulado(new boolean[linhas][colunas]);
		
//		handler.antesCriarReticuladoPreImagem(preImagem);
		
		final int deslocamento = raio * 4; 
		
		// Tamanho da borda calculada atraves da regra de contorno
		final int borda = raio * 2;

		linha = linhas - raio;
		
		// Bordas superior e inferior da pre-imagem
		for (int i = borda; i > 0; i--, linha++) {
			for (coluna = 0; coluna < colunas; coluna++) {
				boolean bit = getValorReticulado();
				
				int indice = Util.toInt(bit) << deslocamento;
				
				Transicao transicao = regraContorno.getTransicao(indice);
				
				preImagem.set(linha, coluna, transicao.getValor());
			}
		}
		
		linha = raio;
		
		for (int i = linhas - borda; i >= 0; i--, linha++) {
			coluna = colunas - raio;
			
			for (int j = borda; j > 0; j--, coluna++) {
				boolean bit = getValorReticulado();
				
				int indice = Util.toInt(bit) << deslocamento;
				
				Transicao transicao = regraContorno.getTransicao(indice);
				
				preImagem.set(linha, coluna, transicao.getValor());
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
	
	public AutomatoCelularHandler getHandler() {
//		return handler;
		return null;
	}

	public void setHandler(AutomatoCelularHandler listener) {
//		this.handler = listener;
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
	public void aposSetBitReticulado(Reticulado reticulado, int linha, int coluna, boolean preImagem) {
	}

	/*@Override
	public void aposSetBitPreImagem(Reticulado preImagem, int linha, int coluna) {
	}*/

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
		
		Regra regra = Regra.criar("01101000111101001001011100001011", DirecaoCalculo.NORTE);
		
		AutomatoCelular ac = new AutomatoCelular(reticulado, regra);
		
		Reticulado pi = ac.calcularPreImagem();
		pi = ac.calcularPreImagem();
		pi = ac.calcularPreImagem();
		
		System.out.println(pi);
		
		ac.evolouir();
		ac.evolouir();
		System.out.println(ac.evolouir());
	}

	@Override
	public Regra getRegraPrincipal() {
		return regraPrincipal;
	}

	@Override
	public Reticulado getReticuladoInicial() {
		return reticuladoInicial;
	}

	@Override
	public void setAutomatoCelular(AutomatoCelular ac) {
	}
}
