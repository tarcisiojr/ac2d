package ufu.mestrado;

import ufu.mestrado.exception.AutomatoCelularException;

public class AutomatoCelular implements AutomatoCelularHandler {
	private boolean debug = false;
	
	/** Reticulado incial. */
	private Reticulado reticuladoInicial;
	
	/** Reticulado atual do automato ceular. */
	private Reticulado reticulado;
	
	/** Regra utilizada para cifrar os bist internos. */
	private Regra regraPrincipal;
	
	/** Regra utilizada na borda do reticulado. */
	private Regra regraContorno;
	
	/** Indica se o reticulado deve ser rotacionando a cada passo de pr�-imagem. */
	public boolean rotacionarReticulado = false;
	
	/** Indica se o reticulado deve ser deslocado a cada passo de pr�-imagem. */
	public boolean deslocarReticulado = true;
	
	/** Ordem que o reticulado deve ser rotacionado. */
	private int ordemRotacaoReticulado[] = {
			DirecaoCalculo.NORTE, 
			DirecaoCalculo.ESQUERDA,
			DirecaoCalculo.SUL,
			DirecaoCalculo.DIREITA
	}; 
	
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
	 * Configura a regra que ser� utilizada na evolu��o e no calculo da pr�-imagem.
	 * @param regra Nova regra.
	 */
	public void setRegra(Regra regra) {
		this.regraPrincipal = regra;
		this.regraContorno = Regra.getRegraContorno(regra);
		
		this.regraPrincipal.rotacaoNucleo = 0;
		this.regraContorno.rotacaoNucleo = 0;
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
		
		// Bits internos. 000..YYY...000
		mascaraInterna = ((1 << (raio * 2)) -1) << (raio + 1);
		
		// Bis externos. YYY....00....YYY
		mascaraExterna = ((1 << (raio * 4 + 1)) -1) - (((1 << (raio * 2 + 1)) -1) << raio) - (1 << (raio -1));
		
		//System.out.println(Integer.toString(mascaraBitCentral, 2));
		//System.out.println(Integer.toString(mascaraExterna, 2));
		//System.out.println(Integer.toString(mascaraInterna, 2));
		
		// Criamos o cache com o maximo entre as linhas e colunas, pois devido ao reticulaodo poder 
		// ser rotacionado, n�o sabemos qual ser� o tamanho do cache, e para melhoria de performance
		// vamos iniciar o cache uma �nica vez
		cacheIndices = new int[Math.max(this.reticulado.getColunas(), this.reticulado.getLinhas())];
		
		for (int i = 0; i < numeroCalculos; i++) {
			// � para alterar a direcao de calculo a cada pr�-imagem?
			if (rotacionarReticulado) {
				// Sim, rotacionando a direcao de calculo, assim ao calcular a pr�-imagem o reticulado ser� rotacionado
				regraPrincipal.direcaoCalculo = ordemRotacaoReticulado[i % ordemRotacaoReticulado.length];
			}
			
			preImagem = calcularPreImagem();
			
			// Rotacionando o n�cleo a regra para direita.
			regraPrincipal.rotacaoNucleo++;
			regraContorno.rotacaoNucleo = regraPrincipal.rotacaoNucleo;
		}
		
		return preImagem;
	}
	
	/**
	 * Verifica se o c�lculo da pr�-imagem foi finalizado.
	 * @param regraPrincipal Regra principal utilizada no c�lculo da pr�-imagem.
	 * @param preImagem Reticulado da pr�-imagem (Borda j� calculada).
	 * @return True se calculo da pr�-image ainda n�o foi finalizado, do contr�rio retorna-se false.
	 */
	private boolean continuarCalculoPI(Reticulado preImagem, Regra regraPrincipal)  {
		posicaoCache = coluna;
		valorReticulado = reticulado.get(linha + regraPrincipal.raio, coluna); 
		return linha >= regraPrincipal.raio;
	}
	
	/**
	 * Itera o c�lculo da pr�-imagem, ou seja, atualiza os ponteiros para
	 * a pr�xima celular a ser calculada.
	 * @param preImagem Reticulado da pr�-imagem (Borda j� calculada).
	 * @param regraPrincipal Regra principal utilizada no c�lculo da pr�-imagem.
	 */
	private void iterarCalculoPI(Reticulado preImagem, Regra regraPrincipal) {
		int raio = regraPrincipal.raio;
		
		coluna--;
		
		// direita para esquerda, de baixo para cimaa
		if (coluna < raio) {
			linha--;
			coluna = colunaInicial;
			cacheIndice = -1;
			usarCache = true;
		}
		
	}
	
	/**
	 * Inicia os ponteiros para o c�lculo da pr�-imagem.
	 * @param preImagem Reticulado da pr�-imagem (Borda j� calculada).
	 * @param regraPrincipal Regra principal utilizada no c�lculo da pr�-imagem.
	 */
	private void iniciarCalculoPI(Reticulado preImagem, Regra regraPrincipal) {
		int raio = regraPrincipal.raio;
		
		cacheIndice = -1;
		usarCache = false;
		
		linhaInicial = preImagem.getLinhas() - raio - 1;
		colunaInicial = preImagem.getColunas() - raio - 1;
		
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
		return reticulado.get(linha + regraPrincipal.raio, coluna);
	}
	
	/**
	 * Realiza o c�lculo da pr�-imagem.
	 * @return O reticulado da pr�-imagem calculada.
	 */
	public Reticulado calcularPreImagem() {
		final int raio = regraPrincipal.raio;
		final int direcao = regraPrincipal.direcaoCalculo;
		
		// Ajustando a rotacao do reticulado de acordo com a direcao de calculo da regra
		reticulado.direcao = regraPrincipal.direcaoCalculo;
		
		if (deslocarReticulado) {
			// Aplicando um deslocamento no reticulado.
			reticulado.deslocamentoLinha = 2 * regraPrincipal.raio;
			reticulado.deslocamentoColuna = 2 * regraPrincipal.raio;
		}

		// Cria o reticulado da pr�imagem, e j� calcula os bits da borda.
		Reticulado preImagem = criarReticuladoPreImagem(reticulado);
		
		// Calculando os bits do interior do reticulado
		// calculo = 0
		iniciarCalculoPI(preImagem, regraPrincipal);
		
		//int mascaraVertical = Integer.valueOf("01100", 2);
		//int mascarax = Integer.valueOf("00100", 2);
		
		final int RAIO_MAIS_UM = raio + 1;
		
		// calculo < maximo
		while (continuarCalculoPI(preImagem, regraPrincipal)) {
			int indice =  Transicao.getIndice0(preImagem, linha, coluna, raio, direcao,
					cacheIndice, usarCache ? cacheIndices[posicaoCache] : -1);

			cacheIndice = (indice & mascaraInterna) >> 1;
		
			cacheIndices[posicaoCache] = ((indice >> 1) & mascaraExterna) | ((indice & mascaraBitCentral) >> RAIO_MAIS_UM);
				
			Transicao transicao = regraPrincipal.getTransicao(indice);
			
			boolean bit = transicao.getPrimeiroBit(raio);
			
			if (transicao.valor != valorReticulado/*getValorReticulado()*/) {
				bit ^= true; // bit = !bit (melhoria na performance)
			}
			
			preImagem.set(linha, coluna, bit);
			
			//calculo++
			iterarCalculoPI(preImagem, regraPrincipal);
		}
		
		reticulado = preImagem;
		
		reticulado.direcao = 0;
		
		return preImagem;
	}
	
	public Reticulado evoluir(int numeroEvolucoes) {
		Reticulado ret = reticulado;
		
		int rotacaoReticulado = (numeroEvolucoes % ordemRotacaoReticulado.length) - 1;
		
		// � para rotacionar o reticulado?
		if (rotacionarReticulado) {
			regraPrincipal.rotacaoNucleo = (numeroEvolucoes % regraPrincipal.tamanhoNucleo);
		}
		
		for (int i = 0; i < numeroEvolucoes; i++, rotacaoReticulado--) {
			// � para rotacionar o reticulado?
			if (rotacionarReticulado) {
				// Obt�m a dire��o correta, inverso do que ocorre no c�lculo da pr�-imagem
				regraPrincipal.direcaoCalculo = ordemRotacaoReticulado[Util.getIndice(ordemRotacaoReticulado.length, rotacaoReticulado)];
			}
			
			regraPrincipal.rotacaoNucleo--;
			regraContorno.rotacaoNucleo = regraPrincipal.rotacaoNucleo;
			
			ret = evolouir();
		}
		
		return ret;
	}
	
	public Reticulado evolouir() {
		
		reticulado.direcao = regraPrincipal.direcaoCalculo;
		
		Reticulado preImagem = reticulado;//aplicarDeslocamento(reticulado); 
		reticulado = criarReticulado(preImagem);
		
		if (deslocarReticulado) {
			reticulado.deslocamentoLinha = 2 * regraPrincipal.raio;
			reticulado.deslocamentoColuna = 2 * regraPrincipal.raio;
		}
		
		final int raio = regraPrincipal.raio;
		final int linhas = reticulado.getLinhas();
		final int colunas = reticulado.getColunas();
		
		for (int coluna = 0; coluna < colunas; coluna++) {
			for (int linha = 0; linha < linhas; linha++) {
				
				Regra regra = this.regraPrincipal;
				
				if ((coluna < raio) || (coluna >= (colunas - raio)) || 
					(linha  < raio) || (linha  >= (linhas  - raio))) {
					regra = this.regraContorno;
				}
				
				int indice = Transicao.getIndice(preImagem, linha, coluna, regra.raio, regra.direcaoCalculo);
				
				Transicao transicao = regra.getTransicao(indice);
				
				reticulado.set(linha + raio, coluna, transicao.valor);
			}
		}
		
		reticulado.deslocamentoLinha = 0;
		reticulado.deslocamentoColuna = 0;
		
		reticulado.direcao = 0;
		
		return reticulado;
	}
	
	/**
	 * Cria um reticulado da pr� image a partir do reticulado fornecido.
	 * @param reticulado
	 * @return Reticulado da pr�-imagem.
	 */
	private Reticulado criarReticuladoPreImagem(Reticulado reticulado) {
		final int raio = regraPrincipal.raio;
		final int linhas = reticulado.getLinhas();
		final int colunas = reticulado.getColunas();
		
		// Criando o reticulado da pr�-imagem.
		Reticulado preImagem = new Reticulado(reticulado); 
			//new Reticulado(linhas, colunas, reticulado.direcao);
			//aplicarDeslocamento(new Reticulado(linhas, colunas)); 
			//new Reticulado(new boolean[linhas][colunas]);
		
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
				
				preImagem.set(linha, coluna, transicao.valor);
			}
		}
		
		linha = raio;
		
		for (int i = linhas - borda; i >= 0; i--, linha++) {
			coluna = colunas - raio;
			
			for (int j = borda; j > 0; j--, coluna++) {
				boolean bit = getValorReticulado();
				
				int indice = Util.toInt(bit) << deslocamento;
				
				Transicao transicao = regraContorno.getTransicao(indice);
				
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
		return new Reticulado(preImage);
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
