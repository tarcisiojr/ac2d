package ufu.mestrado;

public class Transicao {
	public int indice;
	public int raio;
	public boolean valor;
	public int direcao;
	public int deslocamento;
	
	public Transicao(int indice, int raio, int direcao, boolean valor) {
		this.indice = indice;
		this.valor = valor;
		this.raio = raio;
		this.direcao = direcao;
		this.deslocamento = 4 * raio;
	}

	/*
	public boolean getValor() {
		return valor;
	}

	public void setValor(boolean valor) {
		this.valor = valor;
	}
	
	public int getIndice() {
		return indice;
	}
	
	public int getRaio() {
		return raio;
	}

	public void setRaio(int raio) {
		this.raio = raio;
	}

	public int getDirecao() {
		return direcao;
	}

	public void setDirecao(int direcao) {
		this.direcao = direcao;
	}
	*/

	/**
	 * Retorna o primeiro bit do indice da transi��o.
	 * @param raio Raio da regra.
	 * @return Valor 1 ou 0.
	 */
	public boolean getPrimeiroBit(int raio) {
		return (indice & (1 << deslocamento)) != 0;
	}
	
	/**
	 * Retorna os dois poss�veis �ndices para a posi��o fornecida no reticulado.
	 * @param reticulado Reticulado.
	 * @param linha Linha
	 * @param coluna Coluna
	 * @param raio Raio
	 * @param direcaoCalculo Dire��o do c�lculo.
	 * @return Um array com os 2 poss�veis �ndices.
	 */
	public static int[] getIndices(Reticulado reticulado, int linha, int coluna, int raio, int direcaoCalculo) {
		
		int indice = getIndice(reticulado, linha, coluna, raio, direcaoCalculo, -1, -1);
		
		int deslocamento = (raio * 4);
		
		indice = indice | (1 << deslocamento);
		
		return new int[] {indice - (1 << deslocamento), indice};
	}
	
	public static final int getIndice0(final Reticulado reticulado, final int linha, final int coluna, final int raio, final int direcaoCalculo) {
		return getIndice0(reticulado, linha, coluna, raio, direcaoCalculo, -1, -1);
	}
	
	public static final int getIndice0(final Reticulado reticulado, final int linha, final int coluna, final int raio, final int direcaoCalculo, int cacheInterno, int cacheExterno) {
		
		/*final int deslocamento = (raio * 4);
		
		int indice = getIndice(reticulado, linha, coluna, raio, direcaoCalculo);
		
		indice = indice | (1 << deslocamento);
		
		return indice - (1 << deslocamento);*/
		
		final int mascara = (1 << (raio * 4)) -1;
		
		int indice = getIndice(reticulado, linha, coluna, raio, direcaoCalculo, cacheInterno, cacheExterno);
		
		return indice & mascara;
		
	}
	
	/**
	 * Obt�m o �ndice parcial a partir do reticulado. A linha e coluna fornecidos 
	 * � referente ao bit que est� sendo calculado. Sem cache na linha vertical.
	 * @param reticulado Reticulado.
	 * @param linha Linha
	 * @param coluna Coluna
	 * @param raio Raio
	 * @param direcaoCalculo Dire��o do c�lculo
	 */
	public static int getIndice(final Reticulado reticulado, final int linha, final int coluna, final int raio, final int direcaoCalculo) {
		return getIndice(reticulado, linha, coluna, raio, direcaoCalculo, -1, -1);
	}
	
	/**
	 * Obt�m o �ndice parcial a partir do reticulado. A linha e coluna fornecidos 
	 * � referente ao bit que est� sendo calculado.
	 * @param reticulado Reticulado.
	 * @param linha Linha
	 * @param coluna Coluna
	 * @param raio Raio
	 * @param direcaoCalculo Dire��o do c�lculo
	 * @param cacheInterno Cache dos bits interno.
	 */
	public static int getIndice(final Reticulado reticulado, final int linha, final int coluna, final int raio, final int direcaoCalculo, final int cacheInterno, int cacheExterno) {
		switch (direcaoCalculo) {
		case DirecaoCalculo.NORTE:
			return getIndiceNorte(reticulado, linha, coluna, raio, cacheInterno, cacheExterno);
			
		case DirecaoCalculo.SUL:
			return getIndiceSul(reticulado, linha, coluna, raio);
			
		case DirecaoCalculo.ESQUERDA:
			return getIndiceEsquerda(reticulado, linha, coluna, raio);
			
		case DirecaoCalculo.DIREITA:
			return getIndiceDireita(reticulado, linha, coluna, raio);
		}
		
		throw new RuntimeException("Dire��o n�o suportada!");
	}
	
	/**
	 * Obt�m o �ndice do reticulado a partir do bit mais acima.
	 * @param reticulado Reticulado a ser obtido o bit mais acima.
	 * @param linha Linha inicial.
	 * @param coluna Coluna inicial.
	 * @param raio Raio da regra.
	 * @return o �ndica a partir do reticulado fornecido.
	 */
	public static int getIndiceNorte(final Reticulado reticulado, int linha, int coluna, final int raio, final int cacheInterno, final int cacheExterno) {
		int indice = 0;
		int deslocamento = raio * 4 + 1;
		
		// Indice � montado a partir do bit mais acima da esquerda para direita
		
		if (cacheExterno < 0) {
			// Cima
			for (int i = raio; i > 0; i--, linha++) {
				final boolean bit = reticulado.get(linha, coluna);
				
				indice = indice | ((bit ? 1 : 0) << --deslocamento);
			}
		} else {
			final boolean bit = reticulado.get(linha, coluna);
			
			indice = ((bit ? 1 : 0) << --deslocamento) | cacheExterno;
			
			deslocamento = deslocamento - (raio - 1);
			
			linha = linha + raio;
		}

		if (cacheInterno < 0) {
			// Linha central
			coluna = coluna - raio;
			
			for (int i = raio * 2 + 1; i > 0 ; i--, coluna++) {
				final boolean bit = reticulado.get(linha, coluna);
				
				indice = indice | ((bit ? 1 : 0) << --deslocamento);
			}
			
			coluna = coluna - raio - 1;
		} else {
			final boolean bit = reticulado.get(linha, coluna - raio);
			
			indice = indice | ((bit ? 1 : 0) << --deslocamento) | cacheInterno;
			
			deslocamento = deslocamento - raio * 2;
		}
		
		if (cacheExterno < 0) {
			linha++;
			
			// Baixo
			for (int i = raio; i > 0; i--, linha++) {
				final boolean bit = reticulado.get(linha, coluna);
				
				indice = indice | ((bit ? 1 : 0) << --deslocamento);
			}
		} 
		
		return indice;
	}
	
	/**
	 * Obt�m o �ndice do reticulado a partir do bit mais baixo.
	 * @param reticulado Reticulado a ser obtido o bit mais abaixo.
	 * @param linha Linha inicial.
	 * @param coluna Coluna inicial.
	 * @param raio Raio da regra.
	 * @return o �ndica a partir do reticulado fornecido.
	 */
	public static int getIndiceSul(Reticulado reticulado, int linha, int coluna, int raio) {
		int indice = 0;
		
		//Ordem de montagem 1 at� 5
		// X 5 X
		// 4 3 2
		// X 1 X
		
		// Indice � montado a partir do bit mais abaixo da direita para esquerda
		
		// Baixo
		for (int i = 0; i < raio; i++, linha--) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}

		// Linha central
		coluna = coluna + raio;
		for (int i = 0; i < raio * 2 + 1; i++, coluna--) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}
		
		linha--; // linha central
		coluna = coluna + raio + 1;
		// cima
		for (int i = 0; i < raio; i++, linha++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}
		
		return indice;
	}
	
	/**
	 * Obt�m o �ndice do reticulado a partir do bit da esquerda.
	 * @param reticulado Reticulado a ser obtido o bit da esquerda.
	 * @param linha Linha inicial.
	 * @param coluna Coluna inicial.
	 * @param raio Raio da regra.
	 * @return o �ndica a partir do reticulado fornecido.
	 */
	public static int getIndiceEsquerda(Reticulado reticulado, int linha, int coluna, int raio) {
		int indice = 0;
		
		//Ordem de montagem 1 at� 5
		// X 4 X
		// 1 3 5
		// X 2 X
		
		// Esquerda
		for (int i = 0; i < raio; i++, coluna++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}

		// coluna central
		linha = linha + raio;
		for (int i = 0; i < raio * 2 + 1; i++, linha--) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}
		
		coluna++; // linha da direita
		linha = linha + raio + 1;
		// cima
		for (int i = 0; i < raio; i++, coluna++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}
		
		return indice;
	}
	
	/**
	 * Obt�m o �ndice do reticulado a partir do bit da direita.
	 * @param reticulado Reticulado a ser obtido o bit da direita.
	 * @param linha Linha inicial.
	 * @param coluna Coluna inicial.
	 * @param raio Raio da regra.
	 * @return o �ndica a partir do reticulado fornecido.
	 */
	public static int getIndiceDireita(Reticulado reticulado, int linha, int coluna, int raio) {
		int indice = 0;
		
		//Ordem de montagem 1 at� 5
		// X 2 X
		// 5 3 1
		// X 4 X
		
		// Direita
		for (int i = 0; i < raio; i++, coluna--) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= (bit ? 1 : 0);
		}

		// coluna central
		linha = linha - raio;
		for (int i = 0; i < raio * 2 + 1; i++, linha++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= (bit ? 1 : 0);
		}
		
		coluna--; // linha da direita
		linha = linha - raio - 1;
		// cima
		for (int i = 0; i < raio; i++, coluna--) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= (bit ? 1 : 0);
		}
		
		return indice;
	}
	
	/**
	 * Obt�m a sequencia dos bits que est�o acima da linha central.
	 * @return
	 */
	public int getBitsCima() {
		int bits = 0;
		int deslocamento = raio + raio + (raio * 2 + 1) -1;
		for (int i = 0; i < raio; i++) {
			bits = bits << 1;
			bits |= (indice & (1 << (deslocamento - i))) != 0 ? 1 : 0;
		}
		
		System.out.println(bits);
		return bits;
	}
	
	public int getBitsLinhaCentral() {
		int bits = 0;
		int deslocamento = raio + (raio * 2 + 1) - 1;
		
		for (int i = 0; i < raio * 2 + 1; i++) {
			System.out.println(deslocamento - i);
			System.out.println();
			
			bits = bits << 1;
			bits |= (indice & (1 << (deslocamento - i))) != 0 ? 1 : 0;
		}
		
		return bits;
	}
	
	public int getBitsBaixo() {
		int bits = 0;
		int deslocamento = raio -1;
		
		for (int i = 0; i < raio; i++) {
			bits = bits << 1;
			bits |= (indice & (1 << (deslocamento - i))) != 0 ? 1 : 0;
		}
		System.out.println(bits);
		return bits;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + indice;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transicao other = (Transicao) obj;
		if (indice != other.indice)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return indice + " -> " + valor;
	}
}
