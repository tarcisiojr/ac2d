package ufu.mestrado;

public class Transicao {
	private int indice;
	private int raio;
	private boolean valor;
	private DirecaoCalculo direcao;
	
	public Transicao(int indice, int raio, DirecaoCalculo direcao, boolean valor) {
		this.indice = indice;
		this.valor = valor;
		this.raio = raio;
		this.direcao = direcao;
	}

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

	public DirecaoCalculo getDirecao() {
		return direcao;
	}

	public void setDirecao(DirecaoCalculo direcao) {
		this.direcao = direcao;
	}

	/**
	 * Retorna o primeiro bit do indice da transição.
	 * @param raio Raio da regra.
	 * @return Valor 1 ou 0.
	 */
	public boolean getPrimeiroBit(int raio) {
		int deslocamento = 4 * raio;
		
		return (indice & (1 << deslocamento)) != 0;
	}
	
	/**
	 * Retorna os dois possíveis índices para a posição fornecida no reticulado.
	 * @param reticulado Reticulado.
	 * @param linha Linha
	 * @param coluna Coluna
	 * @param raio Raio
	 * @param direcaoCalculo Direção do cálculo.
	 * @return Um array com os 2 possíveis índices.
	 */
	public static int[] getIndices(Reticulado reticulado, int linha, int coluna, int raio, DirecaoCalculo direcaoCalculo) {
		
		int indice = getIndice(reticulado, linha, coluna, raio, direcaoCalculo);
		
		int deslocamento = (raio * 4);
		
		indice = indice | (1 << deslocamento);
		
		return new int[] {indice - (1 << deslocamento), indice};
	}
	
	/**
	 * Obtém o índice parcial a partir do reticulado. A linha e coluna fornecidos 
	 * é referente ao bit que está sendo calculado.
	 * @param reticulado Reticulado.
	 * @param linha Linha
	 * @param coluna Coluna
	 * @param raio Raio
	 * @param direcaoCalculo Direção do cálculo
	 */
	public static int getIndice(Reticulado reticulado, int linha, int coluna, int raio, DirecaoCalculo direcaoCalculo) {
		if (direcaoCalculo == DirecaoCalculo.NORTE) {
			return getIndiceNorte(reticulado, linha, coluna, raio);
			
		} else if (direcaoCalculo == DirecaoCalculo.SUL) {
			return getIndiceSul(reticulado, linha, coluna, raio);
			
		} else if (direcaoCalculo == DirecaoCalculo.ESQUERDA) {
			return getIndiceEsquerda(reticulado, linha, coluna, raio);
			
		} else if (direcaoCalculo == DirecaoCalculo.DIREITA) {
			return getIndiceDireita(reticulado, linha, coluna, raio);
		}
		
		return -1;
	}
	
	/**
	 * Obtém o índice do reticulado a partir do bit mais acima.
	 * @param reticulado Reticulado a ser obtido o bit mais acima.
	 * @param linha Linha inicial.
	 * @param coluna Coluna inicial.
	 * @param raio Raio da regra.
	 * @return o índica a partir do reticulado fornecido.
	 */
	public static int getIndiceNorte(Reticulado reticulado, int linha, int coluna, int raio) {
		int indice = 0;
		
		// Indice é montado a partir do bit mais acima da esquerda para direita
		
		// Cima
		for (int i = 0; i < raio; i++, linha++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}

		// Linha central
		coluna = coluna - raio;
		for (int i = 0; i < raio * 2 + 1; i++, coluna++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}
		linha++;
		coluna = coluna - raio - 1;
		// Baixo
		for (int i = 0; i < raio; i++, linha++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}
		
		return indice;
	}
	
	/**
	 * Obtém o índice do reticulado a partir do bit mais baixo.
	 * @param reticulado Reticulado a ser obtido o bit mais abaixo.
	 * @param linha Linha inicial.
	 * @param coluna Coluna inicial.
	 * @param raio Raio da regra.
	 * @return o índica a partir do reticulado fornecido.
	 */
	public static int getIndiceSul(Reticulado reticulado, int linha, int coluna, int raio) {
		int indice = 0;
		
		//Ordem de montagem 1 até 5
		// X 5 X
		// 4 3 2
		// X 1 X
		
		// Indice é montado a partir do bit mais abaixo da direita para esquerda
		
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
	 * Obtém o índice do reticulado a partir do bit da esquerda.
	 * @param reticulado Reticulado a ser obtido o bit da esquerda.
	 * @param linha Linha inicial.
	 * @param coluna Coluna inicial.
	 * @param raio Raio da regra.
	 * @return o índica a partir do reticulado fornecido.
	 */
	public static int getIndiceEsquerda(Reticulado reticulado, int linha, int coluna, int raio) {
		int indice = 0;
		
		//Ordem de montagem 1 até 5
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
	 * Obtém o índice do reticulado a partir do bit da direita.
	 * @param reticulado Reticulado a ser obtido o bit da direita.
	 * @param linha Linha inicial.
	 * @param coluna Coluna inicial.
	 * @param raio Raio da regra.
	 * @return o índica a partir do reticulado fornecido.
	 */
	public static int getIndiceDireita(Reticulado reticulado, int linha, int coluna, int raio) {
		int indice = 0;
		
		//Ordem de montagem 1 até 5
		// X 2 X
		// 5 3 1
		// X 4 X
		
		// Direita
		for (int i = 0; i < raio; i++, coluna--) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}

		// coluna central
		linha = linha - raio;
		for (int i = 0; i < raio * 2 + 1; i++, linha++) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}
		
		coluna--; // linha da direita
		linha = linha - raio - 1;
		// cima
		for (int i = 0; i < raio; i++, coluna--) {
			boolean bit = reticulado.get(linha, coluna);
			
			indice = indice << 1;
			indice |= Util.toInt(bit);
		}
		
		return indice;
	}
	
	/**
	 * Obtém a sequencia dos bits que estão acima da linha central.
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
