package ufu.mestrado;

import ufu.mestrado.exception.AutomatoCelularException;

public class AutomatoCelular implements AutomatoCelularHandler {
	
	private Reticulado reticuladoInicial;
	private Reticulado reticulado;
	private Regra regraPrincipal;
	private Regra regraContorno;
	
	private boolean debug = false;
	
	/**
	 * Construtor.
	 * @param reticuladoInicial Reticulado inicial do automato.
	 * @param regra Regra para c�lculo da pr�-image e evolu��o.
	 * @throws AutomatoCelularException
	 */
	public AutomatoCelular(Reticulado reticuladoInicial, Regra regra) throws AutomatoCelularException {
		this.reticuladoInicial = reticuladoInicial;
		this.reticulado = reticuladoInicial;
		this.regraPrincipal = regra;
		this.regraContorno = Regra.criarRegraContorno(regra);
	}
	
	/**
	 * Constr�i uma nova inst�ncia do automato a partir do Handler fornecido.
	 * @param handler Handler de automato celular.
	 */
	public AutomatoCelular(AutomatoCelularHandler handler) {
		// Configurando o AC no handler
		handler.setAutomatoCelular(this);
		
		this.reticulado = handler.getReticuladoInicial();
		this.regraPrincipal = handler.getRegraPrincipal();
		this.regraContorno = Regra.criarRegraContorno(this.regraPrincipal);
	}
	
	/**
	 * Realiza o c�lcula da pre-imagem de acordo com o n�mero solicitado.
	 * @param numeroCalculos N�meros de c�lculos de pr�-imagem.
	 * @return O Reticulado atual do automato.
	 */
	public Reticulado calcularPreImage(int numeroCalculos) {
		Reticulado preImagem = this.reticulado;

		final int raio = regraPrincipal.raio;
		
		mascaraBitCentral = 1 << (raio * 2); // 2 x raio
		mascaraInterna = ((1 << (raio * 2)) -1) << (raio + 1);
		mascaraExterna = ((1 << (raio * 4 + 1)) -1) - (((1 << (raio * 2 + 1)) -1) << raio) - (1 << (raio -1));
		
		//System.out.println(Integer.toString(mascaraBitCentral, 2));
		//System.out.println(Integer.toString(mascaraExterna, 2));
		//System.out.println(Integer.toString(mascaraInterna, 2));
		
		switch (regraPrincipal.direcaoCalculo) {
		case DirecaoCalculo.NORTE:
			cacheIndices = new int[this.reticulado.colunas];
			break;
			
		case DirecaoCalculo.SUL:
			cacheIndices = new int[this.reticulado.colunas];
			break;
			
		case DirecaoCalculo.ESQUERDA:
			cacheIndices = new int[this.reticulado.linhas];
			break;
			
		case DirecaoCalculo.DIREITA:
			cacheIndices = new int[this.reticulado.linhas];
			break;
			
		}
		
		for (int i = 0; i < numeroCalculos; i++) {
			preImagem = calcularPreImagem(); 
		}
		
		return preImagem;
	}
	
	/** Linha que ser� iniciado o c�lculo da pr�-imagem. */
	private int linhaInicial;
	
	/** Coluna incial que ser� iniciado o c�lculo da pr�-imagem. */
	private int colunaInicial;
	
	/** Linha atual do calculo da pr�-imagem. */
	private int linha;
	
	/** Coluna atual do calculo da pr�-imagem. */
	private int coluna;

	/** Utilizado para realizar cache da parte interna do indice (onde pode ser paralelizado). */
	private int cacheIndice;
	
	/** Utilizado apra realizar o cache de indices de uma linha para outra quando direcao = NORTE por exemplo. */
	private int cacheIndices[];
	
	/** Indica quando devemos utilizaro o array de cache. */
	private boolean usarCache = false;
	
	/** Mascara utilizada na parte interna do indice. */
	private int mascaraInterna;
	
	/** Mascara utilizada na parte externa do indice. */
	private int mascaraExterna;
	
	/** Marcara utilizada para obter o valor do bit central. */
	private int mascaraBitCentral;
	
	/** Indica se o cache sera feito na linha ou na coluna. */
	private int posicaoCache = -1;
	
	/** Valor do bit central do reticulado no momento do calculo de um bit da pre-imagem. */
	private boolean valorReticulado;
	
	
	/**
	 * Verifica se o c�lculo da pr�-imagem foi finalizado.
	 * @param regraPrincipal Regra principal utilizada no c�lculo da pr�-imagem.
	 * @param preImagem Reticulado da pr�-imagem (Borda j� calculada).
	 * @return True se calculo da pr�-image ainda n�o foi finalizado, do contr�rio retorna-se false.
	 */
	private boolean continuarCalculoPI(Reticulado preImagem, Regra regraPrincipal)  {
		switch (regraPrincipal.direcaoCalculo) {
		case DirecaoCalculo.NORTE:
			posicaoCache = coluna;
			valorReticulado = reticulado.get(linha + regraPrincipal.raio, coluna); 
			return linha >= regraPrincipal.raio;
			
		case DirecaoCalculo.SUL:
			posicaoCache = coluna;
			return linha < (preImagem.linhas - regraPrincipal.raio);
			
		case DirecaoCalculo.ESQUERDA:
			posicaoCache = linha;
			return coluna >= regraPrincipal.raio;
			
		case DirecaoCalculo.DIREITA:
			posicaoCache = linha;
			return coluna < (preImagem.colunas - regraPrincipal.raio);
		}
		
		throw new RuntimeException("O tipo de dire��o da regra fornecida n�o � suportado!");
	}
	
	/**
	 * Itera o c�lculo da pr�-imagem, ou seja, atualiza os ponteiros para
	 * a pr�xima celular a ser calculada.
	 * @param preImagem Reticulado da pr�-imagem (Borda j� calculada).
	 * @param regraPrincipal Regra principal utilizada no c�lculo da pr�-imagem.
	 */
	private void iterarCalculoPI(Reticulado preImagem, Regra regraPrincipal) {
		int raio = regraPrincipal.raio;
		
		switch (regraPrincipal.direcaoCalculo) {
		case DirecaoCalculo.NORTE:
			coluna--;
			// direita para esquerda, de baixo para cimaa
			if (coluna < raio) {
				linha--;
				coluna = colunaInicial;
				cacheIndice = -1;
				usarCache = true;
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
			
			if (linha >= preImagem.linhas - raio) {
				coluna--;
				linha = linhaInicial;
			}
			break;
			
		case DirecaoCalculo.DIREITA:
			// de cima para baixo, esquerda para direita
			linha++;
			
			if (linha >= preImagem.linhas - raio) {
				coluna++;
				linha = linhaInicial;
			}
			break;
		}
	}
	
	/**
	 * Inicia os ponteiros para o c�lculo da pr�-imagem.
	 * @param preImagem Reticulado da pr�-imagem (Borda j� calculada).
	 * @param regraPrincipal Regra principal utilizada no c�lculo da pr�-imagem.
	 */
	private void iniciarCalculoPI(Reticulado preImagem, Regra regraPrincipal) {
		int raio = regraPrincipal.raio;
		
		switch (regraPrincipal.direcaoCalculo) {
		case DirecaoCalculo.NORTE:
			linhaInicial = preImagem.linhas - raio - 1;
			colunaInicial = preImagem.colunas - raio - 1;
			cacheIndice = -1;
			usarCache = false;
			break;
			
		case DirecaoCalculo.SUL:
			linhaInicial = raio;
			colunaInicial = preImagem.colunas - raio - 1;
			break;
			
		case DirecaoCalculo.ESQUERDA:
			linhaInicial = raio;
			colunaInicial = preImagem.colunas - raio - 1;
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
	 * Retorna o valor da c�lula central do reticulado a partir
	 * das coordenadas do calculo da pr�-imagem.
	 * 
	 * @return Valor da celula centra do reticulado.
	 */
	private boolean getValorReticulado() {
		switch (regraPrincipal.direcaoCalculo) {
		case DirecaoCalculo.NORTE:
			return reticulado.get(linha + regraPrincipal.raio, coluna);
			
		case DirecaoCalculo.SUL:
			return reticulado.get(linha - regraPrincipal.raio, coluna);
			
		case DirecaoCalculo.ESQUERDA:
			return reticulado.get(linha, coluna + regraPrincipal.raio);
			
		case DirecaoCalculo.DIREITA:
			return reticulado.get(linha, coluna - regraPrincipal.raio);
		}
		
		throw new RuntimeException("Dire��o inv�lida!");
	}
	
	/**
	 * Realiza o c�lculo da pr�-imagem.
	 * @return O reticulado da pr�-imagem calculada.
	 */
	public Reticulado calcularPreImagem() {
		// Cria o reticulado da pr�imagem, e j� calcula os bits da borda.
		Reticulado preImagem = criarReticuladoPreImagem(reticulado);
		
		final int raio = regraPrincipal.raio;
		final int direcao = regraPrincipal.direcaoCalculo;
		 
		// Calculando os bits do interior do reticulado
		// calculo = 0
		iniciarCalculoPI(preImagem, regraPrincipal);
		
		//int mascaraVertical = Integer.valueOf("01100", 2);
		//int mascarax = Integer.valueOf("00100", 2);
		
		//cacheLinha = new int [reticulado.colunas];
		
		final int raioMaisUm = raio + 1;
		
		// calculo < maximo
		while (continuarCalculoPI(preImagem, regraPrincipal)) {
			int indice =  Transicao.getIndice0(preImagem, linha, coluna, raio, direcao,
					cacheIndice, usarCache ? cacheIndices[posicaoCache] : -1);
			
			//switch (direcao) {
			//case DirecaoCalculo.NORTE:
				cacheIndice = (indice & mascaraInterna) >> 1;
				cacheIndices[posicaoCache] = ((indice >> 1) & mascaraExterna) | ((indice & mascaraBitCentral) >> raioMaisUm);
				//break;
				/*
			case DirecaoCalculo.SUL:
				break;
			
			case DirecaoCalculo.ESQUERDA:
				break;

			case DirecaoCalculo.DIREITA:
				break;
			}
			*/
				
			Transicao transicao = regraPrincipal.transicoes[indice];
			
			boolean bit = transicao.getPrimeiroBit(raio);
			
			if (transicao.valor != valorReticulado/*getValorReticulado()*/) {
				bit ^= true; // bit = !bit (melhoria na performance)
			}
			
			preImagem.set(linha, coluna, bit);
			
			//calculo++
			iterarCalculoPI(preImagem, regraPrincipal);
		}
		
		reticulado = preImagem;
		
		return preImagem;
	}
	
	/**
	 * Realiza o c�lculo da pr�-imagem.
	 * @return O reticulado da pr�-imagem calculada.
	public Reticulado calcularPreImagem2() {
		handler.antesCalcularPreImagem();
		
		// Cria o reticulado da pr�imagem, e j� calcula os bits da borda.
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
		
		// Remove o deslocamento da pr�-imagem.
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
		Reticulado preImagem = reticulado;//aplicarDeslocamento(reticulado); 
		reticulado = criarReticulado(preImagem);
		
		final int raio = regraPrincipal.raio;
		final int linhas = reticulado.linhas;
		final int colunas = reticulado.colunas;
		final int direcao = regraPrincipal.direcaoCalculo;
		
		for (int coluna = 0; coluna < reticulado.colunas; coluna++) {
			for (int linha = 0; linha < reticulado.linhas; linha++) {
				
				Regra regra = this.regraPrincipal;
				
				if ((coluna < raio) || (coluna >= (colunas - raio)) || 
					(linha  < raio) || (linha  >= (linhas  - raio))) {
					regra = this.regraContorno;
				}
				
				int indice = Transicao.getIndice(preImagem, linha, coluna, regra.raio, regra.direcaoCalculo);
				
				Transicao transicao = regra.transicoes[indice];
				
				switch (direcao) {
				case DirecaoCalculo.NORTE:
					reticulado.set(linha + raio, coluna, transicao.valor);
					break;

				case DirecaoCalculo.SUL:
					reticulado.set(linha - raio, coluna, transicao.valor);
					break;
					
				case DirecaoCalculo.ESQUERDA:
					reticulado.set(linha, coluna + raio, transicao.valor);
					break;
					
				case DirecaoCalculo.DIREITA:
					reticulado.set(linha, coluna - raio, transicao.valor);
					break;

				}
//				handler.aposSetBitReticulado(reticulado, linha + raio, coluna, false);
			}
		}
		
		reticulado.removerDeslocamento();
		
		return reticulado;
	}
	
	/**
	 * Cria um reticulado da pr� image a partir do reticulado fornecido.
	 * @param reticulado
	 * @return Reticulado da pr�-imagem.
	 */
	private Reticulado criarReticuladoPreImagem(Reticulado reticulado) {
		final int raio = regraPrincipal.raio;
		final int linhas = reticulado.linhas;
		final int colunas = reticulado.colunas;
		
		// Criando o reticulado da pr�-imagem.
		Reticulado preImagem = new Reticulado(linhas, colunas);
			//aplicarDeslocamento(new Reticulado(linhas, colunas)); 
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
				
				Transicao transicao = regraContorno.transicoes[indice];
				
				preImagem.set(linha, coluna, transicao.valor);
			}
		}
		
		linha = raio;
		
		for (int i = linhas - borda; i >= 0; i--, linha++) {
			coluna = colunas - raio;
			
			for (int j = borda; j > 0; j--, coluna++) {
				boolean bit = getValorReticulado();
				
				int indice = Util.toInt(bit) << deslocamento;
				
				Transicao transicao = regraContorno.transicoes[indice];
				
				preImagem.set(linha, coluna, transicao.valor);
			}
		}
		
		return preImagem;
	}
	
	
	/**
	 * Cria um reticulado para evolu��o.
	 * @param preImage Pr� imagem o qual ser� criado o reticulado.
	 * @return
	 */
	private Reticulado criarReticulado(Reticulado preImage) {
		int linhas = preImage.linhas;
		int colunas = preImage.colunas;
		
		return new Reticulado(linhas, colunas);
	}

	/**
	 * Aplica um deslocamento no reticulado fornecido.
	 * @param reticulado
	 * @return
	 */
	/*private Reticulado aplicarDeslocamento(Reticulado reticulado) {
		int deslocamento = getDeslocamento();
		
		//reticulado.setDeslocamentoColuna(deslocamento);
		//reticulado.setDeslocamentoLinha(deslocamento);
		
		return reticulado;
	}*/
	
	/*private int getDeslocamento() {
		return 0;
		//return 1;
	}*/
	
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
			System.out.println("XOR reticulado com a pr�-imagem");
			System.out.println(reticulado.xor(preImagem));
			System.out.println("Reticulado");
			System.out.println(reticulado);
			System.out.println("Pr�-imagem");
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
		//System.out.println(Integer.toString(Integer.valueOf("01100", 2) >> 1, 2));
		/*
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
		System.out.println(ac.evolouir());*/
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
