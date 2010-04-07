package ufu.mestrado;

import ufu.mestrado.exception.AutomatoCelularException;

public class AutomatoCelular implements AutomatoCelularHandler {
	
	private Reticulado reticuladoInicial;
	private Reticulado reticulado;
	private Regra regraPrincipal;
	private Regra regraContorno;
	private AutomatoCelularHandler handler;
	
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
		this.handler = this;
	}
	
	/**
	 * Constr�i uma nova inst�ncia do automato a partir do Handler fornecido.
	 * @param handler Handler de automato celular.
	 */
	public AutomatoCelular(AutomatoCelularHandler handler) {
		this.handler = handler;
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
	
	/**
	 * Verifica se o c�lculo da pr�-imagem foi finalizado.
	 * @param regraPrincipal Regra principal utilizada no c�lculo da pr�-imagem.
	 * @param preImagem Reticulado da pr�-imagem (Borda j� calculada).
	 * @return True se calculo da pr�-image ainda n�o foi finalizado, do contr�rio retorna-se false.
	 */
	private boolean continuarCalculoPI(Reticulado preImagem, Regra regraPrincipal)  {
		if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.NORTE) {
			return linha >= regraPrincipal.getRaio();
			
		} else if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.SUL) {
			return linha < (preImagem.getLinhas() - regraPrincipal.getRaio());
			
		} else if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.ESQUERDA) {
			return coluna >= regraPrincipal.getRaio();
			
		} else if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.DIREITA) {
			return coluna < (preImagem.getColunas() - regraPrincipal.getRaio());
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
		int raio = regraPrincipal.getRaio();
		
		if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.NORTE) {
			coluna--;
			// direita para esquerda, de baixo para cimaa
			if (coluna < raio) {
				linha--;
				coluna = colunaInicial;
			}
		} else if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.SUL) {
			// direita para esquerda, de cima para baixo
			coluna--;
			
			if (coluna < raio) {
				linha++;
				coluna = colunaInicial;
			}
		} else if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.ESQUERDA) {
			// de cima para baixo, direita para esquerda
			linha++;
			
			if (linha >= preImagem.getLinhas() - raio) {
				coluna--;
				linha = linhaInicial;
			}
		} else if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.DIREITA) {
			// de cima para baixo, esquerda para direita
			linha++;
			
			if (linha >= preImagem.getLinhas() - raio) {
				coluna++;
				linha = linhaInicial;
			}
		}
	}
	
	/**
	 * Inicia os ponteiros para o c�lculo da pr�-imagem.
	 * @param preImagem Reticulado da pr�-imagem (Borda j� calculada).
	 * @param regraPrincipal Regra principal utilizada no c�lculo da pr�-imagem.
	 */
	private void iniciarCalculoPI(Reticulado preImagem, Regra regraPrincipal) {
		int raio = regraPrincipal.getRaio();
		
		if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.NORTE) {
			linhaInicial = preImagem.getLinhas() - raio - 1;
			colunaInicial = preImagem.getColunas() - raio - 1;
			
		} else if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.SUL) {
			linhaInicial = raio;
			colunaInicial = preImagem.getColunas() - raio - 1;
			
		} else if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.ESQUERDA) {
			linhaInicial = raio;
			colunaInicial = preImagem.getColunas() - raio - 1;
			
		} else if (regraPrincipal.getDirecaoCalculo() == DirecaoCalculo.DIREITA) {
			linhaInicial = raio;
			colunaInicial = raio;
		}
		
		linha = linhaInicial;
		coluna = colunaInicial;
	}
	
	/**
	 * Realiza o c�lculo da pr�-imagem.
	 * @return O reticulado da pr�-imagem calculada.
	 */
	public Reticulado calcularPreImagem() {
		handler.antesCalcularPreImagem();
		
		// Cria o reticulado da pr�imagem, e j� calcula os bits da borda.
		Reticulado preImagem = criarReticuladoPreImagem(reticulado);
		
		handler.aposCriarReticuladoPreImagem(preImagem);
		
		int raio = regraPrincipal.getRaio();
		 
		// Calculando os bits do interior do reticulado
		// Calculo = 0
		iniciarCalculoPI(preImagem, regraPrincipal);
		
		while (continuarCalculoPI(preImagem, regraPrincipal)) {
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
			
			//calculo++
			iterarCalculoPI(preImagem, regraPrincipal);
		}
		
		// Remove o deslocamento da pr�-imagem.
		preImagem.removerDeslocamento();
		
		handler.aposCalcularPreImagem(preImagem);
		
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
				handler.aposSetBitReticulado(reticulado, linha + raio, coluna, false);
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
		int raio = regraPrincipal.getRaio();
		
		int linhas = reticulado.getLinhas();
		int colunas = reticulado.getColunas();
		
		// Criando o reticulado da pr�-imagem.
		Reticulado preImagem = aplicarDeslocamento(new Reticulado(linhas, colunas)); 
			//new Reticulado(new boolean[linhas][colunas]);
		
		handler.antesCriarReticuladoPreImagem(preImagem);
		
		int deslocamento = raio * 4; 
		
		for (int coluna = 0; coluna < colunas; coluna++) {
			for (int linha = 0; linha < linhas; linha++) {
				// Calcula os bits de contorno da primeira e da ultima coluna
				// e da primeira e ultima linha
				if ((coluna < raio) || (coluna >= (colunas - raio)) || 
					(linha  <= raio)) {
					
					boolean bit = reticulado.get(linha, coluna);
					
					handler.aposGetBitReticulado(reticulado, linha, coluna);
					
					int indice = Util.toInt(bit) << deslocamento;
					
					Transicao transicao = regraContorno.getTransicao(indice);
					
					preImagem.set(linha - raio, coluna, transicao.getValor());
					
					handler.aposSetBitReticulado(preImagem, linha, coluna, true);
					//handler.aposSetBitPreImagem(preImagem, linha, coluna);
				}
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
		return handler;
	}

	public void setHandler(AutomatoCelularHandler listener) {
		this.handler = listener;
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
}
