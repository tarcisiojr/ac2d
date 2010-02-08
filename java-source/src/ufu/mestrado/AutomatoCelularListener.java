package ufu.mestrado;

public interface AutomatoCelularListener {

	public void antesCalcularPreImagem();
	
	public void aposCalcularPreImagem(Reticulado preImagem);
	
	public void aposCriarReticuladoPreImagem(Reticulado preImagem);
	
	public void aposBuscaTransicoes(int indices[]);
	
	public void aposTransicaoEscolhida(int indice);
	
	public void antesCriarReticuladoPreImagem(Reticulado preImagem);
	
	public void aposSetBitReticulado(int linha, int coluna, boolean valor);
	
	public void aposGetBitReticulado(Reticulado reticulado, int linha, int coluna);
	
	public void aposSetBitPreImagem(Reticulado preImagem, int linha, int coluna);
}
