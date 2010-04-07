package ufu.mestrado.imagem;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ufu.mestrado.AutomatoCelularHandler;
import ufu.mestrado.DirecaoCalculo;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;
import ufu.mestrado.util.BitsUtil;

public class CifradorImagemRotacaoBits implements AutomatoCelularHandler {
	/** Valor RGB da cor preta */
	final int PRETO = Color.BLACK.getRGB();
	
	/** Valor RGB da cor branca */
	final int BRANCO = Color.WHITE.getRGB();
	
	public static final int RGB_MEIO = 0x7F7F7F;
	
	/** Imagem fornecida. */
	private BufferedImage buffer;
	
	/** Reticulado inicial gerado a partir da imagem. */
	private Reticulado reticuladoInicial;
	
	/** Regra utilizada na cifragem. */
	private Regra regra;

	public int contador = 0;
	
	/**
	 * Cria uma nova instância de um cifrador de imagens em preto em branco.
	 * @param caminhoImage
	 * @param nucleoRegra
	 */
	public CifradorImagemRotacaoBits(String caminhoImage, String nucleoRegra) {
		try {
			reticuladoInicial = criarReticuladoInicial(caminhoImage);
			regra = Regra.criarAPatirNucleo(nucleoRegra, DirecaoCalculo.NORTE);
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

		for (int i = 0; i < buffer.getHeight(); i++) {
			
			for (int j = 0; j < buffer.getWidth(); j++) {
				
				int rgb = buffer.getRGB(j, i);
				reticuladoInicial.set(i, j, (rgb & 1) == 1);
			}
		}
		
		
		return reticuladoInicial;
	}
	
	/**
	 * Cria uma imagem a partir do reticulado fornecido. 
	 * @param reticulado Reticulado o qual será criado a imagem.
	 * @param caminho Caminho onde será criada a imagem.
	 * @throws Exception
	 */
	public void criarImagem(String caminho) throws Exception {
		String tipo = "BMP";
		int i = caminho.lastIndexOf('.');
		if (i != -1 && (i + 1) == caminho.length()) {
			tipo = caminho.substring(i + 1);
		}
		
		ImageIO.write(buffer, tipo.toUpperCase(), new File(caminho));
	}
	
	
	@Override
	public void antesCalcularPreImagem() {
	}

	@Override
	public void antesCriarReticuladoPreImagem(Reticulado preImagem) {
	}

	@Override
	public void aposBuscaTransicoes(int[] indices) {
	}

	@Override
	public void aposCalcularPreImagem(Reticulado preImagem) {
	}

	@Override
	public void aposCriarReticuladoPreImagem(Reticulado preImagem) {
	}

	@Override
	public void aposGetBitReticulado(Reticulado reticulado, int linha, int coluna) {
	}

	@Override
	public void aposSetBitReticulado(Reticulado reticulado, int linha, int coluna, boolean preImagem) {
		//boolean valor = reticulado.get(linha, coluna);
		
		linha = reticulado.getIndiceLinha(linha);
		coluna = reticulado.getIndiceColuna(coluna); 
		
		int rgb = buffer.getRGB(coluna, linha);
		
		//int op = preImagem ? 1 : -1;
		
		if (preImagem) {
			contador++;
			
			int rgbOriginal = rgb;
			rgb = rgb >> 1; 
			
			rgb = BitsUtil.rotacionar(rgb, 23, 1, BitsUtil.DIREITA);
			
			rgb = (rgb << 1) | (rgbOriginal & 1);
		} else {
			contador--;

			int rgbOriginal = rgb;
			rgb = rgb >> 1; 
			rgb = BitsUtil.rotacionar(rgb, 23, 1, BitsUtil.ESQUERDA);
			
			rgb = (rgb << 1) | (rgbOriginal & 1);
		}
		
		buffer.setRGB(
				coluna,
				linha,
				rgb);
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

	public static void main(String[] args) {
		int valor = RGB_MEIO;
		
		System.out.println(valor);
		
		for (int i = 0; i < 10; i++) {
			valor = BitsUtil.rotacionar(valor, 24, 5, BitsUtil.ESQUERDA);
			//valor = valor | 1;
		}
		System.out.println(valor);
		
		for (int i = 0; i < 10; i++) {
			//valor = valor | 1;
			valor = BitsUtil.rotacionar(valor, 24, 5, BitsUtil.DIREITA);
		}
		System.out.println(valor);
	}
}
