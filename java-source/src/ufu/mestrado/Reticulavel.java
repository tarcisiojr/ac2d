package ufu.mestrado;

public interface Reticulavel {

	/**
	 * Configura o valor da c�lular especificada.
	 * @param linha Linha.
	 * @param coluna Coluna.
	 * @param valor Novo valor da c�lula.
	 */
	public void set(int linha, int coluna, boolean valor);
	
	/**
	 * Obt�m o valor de uma c�lula do reticulado.
	 * @param linha Linha.
	 * @param coluna Coluna.
	 * @return Valor da c�lula.
	 */
	public boolean get(int linha, int coluna); 
	
	/**
	 * Retorna o total de linhas do reticulado.
	 * @return O total de linhas.
	 */
	public int getLinhas();
	
	/**
	 * Retorna o total de colunas.
	 * @return Total de colunas.
	 */
	public int getColunas();
}
