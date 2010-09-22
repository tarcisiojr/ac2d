package ufu.mestrado.imagem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.AutomatoCelularHandler;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;

public class CifradorImagemColorida implements AutomatoCelularHandler {
	private static final boolean DEBUG = false;

	/** Quantidade de bits contidos em um pixel. */
	private static final int BITS_PIXEL = 24; // 3 bytes
	
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
	public CifradorImagemColorida(String caminhoImage, String nucleoRegra, int direcao) {
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
	public CifradorImagemColorida(String caminhoImage, Regra regra, int direcao) {
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
		reticuladoInicial = new Reticulado(buffer.getHeight(), buffer.getWidth() * BITS_PIXEL);
		
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
		if (i != -1 && (i + 1) != caminho.length()) {
			tipo = caminho.substring(i + 1);
		}
		
		ImageIO.write(buffer, tipo.toUpperCase(), new File(caminho));
	}
	
	/**
	 * Carrega o reticulado na imagem.
	 */
	private void carregarImagem(boolean set, Reticulado reticulado) {
		final int laguraMaxina = buffer.getWidth();

		for (int i = 0; i < buffer.getHeight(); i++) {
			
			for (int j = 0; j < laguraMaxina; j++) {
				
				int rgb = set ? 0 : buffer.getRGB(j, i);
				
				for (int b = BITS_PIXEL -1, k = 0; b >= 0; b--, k++) { // 0000 0000
					if (set) {
						rgb |= reticulado.get(i, (j * BITS_PIXEL) + k) ? 1 << b: 0;
						
					} else {
						reticulado.set(i, (j * BITS_PIXEL) + k, (rgb & (1 << b)) != 0);
					}
				}
				
				if (set) {
					buffer.setRGB(j, i, rgb);
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

}
