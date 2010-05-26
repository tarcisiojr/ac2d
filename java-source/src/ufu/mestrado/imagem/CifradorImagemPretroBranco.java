package ufu.mestrado.imagem;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.AutomatoCelularHandler;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;

public class CifradorImagemPretroBranco implements AutomatoCelularHandler {
	private static final boolean DEBUG = false;
	
	/** Valor RGB da cor preta */
	final int PRETO = Color.BLACK.getRGB();
	
	/** Valor RGB da cor branca */
	final int BRANCO = Color.WHITE.getRGB();
	
	/** Imagem fornecida. */
	private BufferedImage buffer;
	
	/** Reticulado inicial gerado a partir da imagem. */
	private Reticulado reticuladoInicial;
	
	/** Regra utilizada na cifragem. */
	private Regra regra;
	
	/**
	 * AC a qual o cifrador pertence.
	 */
	private AutomatoCelular automatoCelular;
	
	/**
	 * Cria uma nova instância de um cifrador de imagens em preto em branco.
	 * @param caminhoImage
	 * @param nucleoRegra
	 */
	public CifradorImagemPretroBranco(String caminhoImage, String nucleoRegra, int direcao) {
		try {
			reticuladoInicial = criarReticuladoInicial(caminhoImage);
			regra = Regra.criarAPatirNucleo(nucleoRegra, direcao);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Cria uma nova instância de um cifrador de imagens em preto em branco.
	 * @param caminhoImage
	 * @param nucleoRegra
	 */
	public CifradorImagemPretroBranco(String caminhoImage, Regra regra, int direcao) {
		try {
			this.reticuladoInicial = criarReticuladoInicial(caminhoImage);
			this.regra = regra;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Cria um reticulado inicial a partir da imagem fornecida.
	 * @param caminhoImagem Caminho da imagem a convertida em reticulado.
	 * @return
	 * @throws Exception
	 */
	private Reticulado criarReticuladoInicial(String caminhoImagem) throws Exception {
		buffer =  ImageIO.read(new File(caminhoImagem));
		reticuladoInicial = new Reticulado(buffer.getHeight(), buffer.getWidth());
		
		carregarImagem(false, reticuladoInicial);
		
		return reticuladoInicial;
	}
	
	/**
	 * Cria uma imagem a partir do reticulado fornecido. 
	 * @param reticulado Reticulado o qual será criado a imagem.
	 * @param caminho Caminho onde será criada a imagem.
	 * @throws Exception
	 */
	public void criarImagem(String caminho) throws Exception {
		carregarImagem(true, automatoCelular.getReticulado());
		
		String tipo = "BMP";
		int i = caminho.lastIndexOf('.');
		if (i != -1 && (i + 1) == caminho.length()) {
			tipo = caminho.substring(i + 1);
		}
		
		ImageIO.write(buffer, tipo.toUpperCase(), new File(caminho));
	}
	
	/**
	 * Carrega o reticulado na imagem.
	 */
	private void carregarImagem(boolean set, Reticulado reticulado) {
		
		final int PRETO = Color.BLACK.getRGB();

		for (int i = 0; i < buffer.getHeight(); i++) {
			
			for (int j = 0; j < buffer.getWidth(); j++) {
				
				if (set) {
					buffer.setRGB(j, i, reticulado.get(i, j) ? PRETO : BRANCO);
				} else {
					int rgb = buffer.getRGB(j, i);
					reticulado.set(i, j, rgb == PRETO);
				}
			}
		}
	}
	
	
	@Override
	public void antesCalcularPreImagem() {
	}

	@Override
	public void antesCriarReticuladoPreImagem(Reticulado preImagem) {
	}

	@Override
	public void aposBuscaTransicoes(int[] indices) {
		if (DEBUG) System.out.print("Indices apos busca de transicoes: " + Arrays.toString(indices));
	}

	@Override
	public void aposCalcularPreImagem(Reticulado preImagem) {
	}

	@Override
	public void aposCriarReticuladoPreImagem(Reticulado preImagem) {
		if (DEBUG) System.out.println("\n" + preImagem + "\n");
	}

	@Override
	public void aposGetBitReticulado(Reticulado reticulado, int linha, int coluna) {
	}

	@Override
	public void aposSetBitReticulado(Reticulado reticulado, int linha, int coluna, boolean preImagem) {
		/*if (preImagem) if (DEBUG) System.out.println(" [linha, coluna]=[" + linha + ", " + coluna + "]");
		
		boolean valor = reticulado.get(linha, coluna);
		
		buffer.setRGB(
				reticulado.getIndiceColuna(coluna),
				reticulado.getIndiceLinha(linha),
				valor ? PRETO : BRANCO);*/
		
	}

	@Override
	public void aposTransicaoEscolhida(int indice) {
		
	}

	@Override
	public Regra getRegraPrincipal() {
		return regra;
	}

	@Override
	public Reticulado getReticuladoInicial() {
		return reticuladoInicial;
	}

	@Override
	public void setAutomatoCelular(AutomatoCelular ac) {
		automatoCelular = ac;
	}

	
	public static void main(String[] args) {
	}
}
