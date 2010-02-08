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
	 * Retorna o primeiro bit do indice da transi��o.
	 * @param raio Raio da regra.
	 * @return Valor 1 ou 0.
	 */
	public boolean getPrimeiroBit(int raio) {
		int deslocamento = 4 * raio;
		
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
	public static int[] getIndices(Reticulado reticulado, int linha, int coluna, int raio, DirecaoCalculo direcaoCalculo) {
		
		int indice = getIndice(reticulado, linha, coluna, raio, direcaoCalculo);
		
		int deslocamento = (raio * 4);
		
		indice = indice | (1 << deslocamento);
		
		return new int[] {indice - (1 << deslocamento), indice};
	}
	
	/**
	 * Obt�m o �ndice parcial a partir do reticulado. A linha e coluna fornecidos 
	 * � referente ao bit que est� sendo calculado.
	 * @param reticulado Reticulado.
	 * @param linha Linha
	 * @param coluna Coluna
	 * @param raio Raio
	 * @param direcaoCalculo Dire��o do c�lculo
	 */
	public static int getIndice(Reticulado reticulado, int linha, int coluna, int raio, DirecaoCalculo direcaoCalculo) {
		int indice = 0;
		
		if (direcaoCalculo == DirecaoCalculo.CIMA) {
			// Indice � montado a partir do bit mais acima da esquerda para direita
			
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
