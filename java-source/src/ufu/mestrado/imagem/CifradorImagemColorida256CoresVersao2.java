package ufu.mestrado.imagem;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import ufu.mestrado.AutomatoCelular;
import ufu.mestrado.AutomatoCelularHandler;
import ufu.mestrado.Regra;
import ufu.mestrado.Reticulado;

public class CifradorImagemColorida256CoresVersao2 implements AutomatoCelularHandler {
	private static final boolean DEBUG = false;

	/** Quantidade de bits contidos em um pixel. */
	private static final int BITS_PIXEL = 8;
	
	/** Imagem fornecida. */
	public BufferedImage buffer;
	
	private int pixelsImagem[][];
	
	/** Reticulado inicial gerado a partir da imagem. */
	private Reticulado reticuladoInicial[];
	
	/** Regra utilizada na cifragem. */
	private Regra regra;
	
	
	/** Histograma da imagem carregada/criada. */
	public double[] histograma;
	
	/**
	 * Cria uma nova instância de um cifrador de imagens em preto em branco.
	 * @param caminhoImage
	 * @param nucleoRegra
	 */
	public CifradorImagemColorida256CoresVersao2(String caminhoImage, String nucleoRegra, int direcao) {
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
	public CifradorImagemColorida256CoresVersao2(String caminhoImage, Regra regra, int direcao) {
		try {
			this.reticuladoInicial = criarReticuladoInicial(caminhoImage);
			this.regra = regra;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void cifrar(int pi, boolean rotSensitividade) throws Exception {
		for (int i = 0; i < BITS_PIXEL; i++) {
			System.out.println("Entropia regra: " + regra.entropia());
			
			Regra novaRegra = regra; /* Regra.criarAPatirNucleo(Integer.toString(valor, 2).substring(1), 0);
			System.out.println(Integer.toString(valor, 2).substring(1));*/
			
			AutomatoCelular ac = new AutomatoCelular(reticuladoInicial[i], novaRegra);
			ac.rotacionarReticulado = rotSensitividade;
			ac.setRegra(novaRegra);
			
			//regra.direcaoCalculo = i % 4;
			//ac.regraPrincipal.direcaoCalculo = i% 2;;
			//ac.regraContorno.direcaoCalculo = i% 2;;
			
			ac.calcularPreImage(pi);
			//ac.evoluir(pi);
			reticuladoInicial[i] = ac.getReticulado();
		}		
	}
	
	public void decifrar(int pi, boolean rotSensitividade) throws Exception  {
		for (int i = 0; i < BITS_PIXEL; i++) {
			
			AutomatoCelular ac = new AutomatoCelular(reticuladoInicial[i], regra);
			ac.rotacionarReticulado = rotSensitividade;
			ac.setRegra(regra);
			ac.evoluir(pi);
			reticuladoInicial[i] = ac.getReticulado();
		}
	}
	
	/**
	 * Cria um reticulado inicial a partir da imagem fornecida.
	 * @param caminhoImagem Caminho da imagem a convertida em reticulado.
	 * @return
	 * @throws Exception
	 */
	private Reticulado[] criarReticuladoInicial(String caminhoImagem) throws Exception {
		buffer =  ImageIO.read(new File(caminhoImagem));
		
		reticuladoInicial = new Reticulado[BITS_PIXEL]; //new Reticulado(buffer.getHeight(), buffer.getWidth());
		
		pixelsImagem = new int[buffer.getHeight()][buffer.getWidth()];
		
		// Os pixels da imagem
		carregarPixels(false);
		
		for (int i = 0; i < BITS_PIXEL; i++) {
			carregarReticulado(true, i);
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
		for (int i = 0; i < BITS_PIXEL; i++) {
			carregarReticulado(false, i);
		}
		
		carregarPixels(true);
		
		String tipo = "BMP";
		int i = caminho.lastIndexOf('.');
		if (i != -1 && (i + 1) != caminho.length()) {
			tipo = caminho.substring(i + 1);
		}
		
		ImageIO.write(buffer, tipo.toUpperCase(), new File(caminho));
	}

	private void carregarPixels(boolean set) {
		int pixel[] = new int[1];
		
		for (int i = 0; i < buffer.getHeight(); i++) {
			
			for (int j = 0; j < buffer.getWidth(); j++) {
				
				buffer.getRaster().getPixel(j, i, pixel);
				
				int rgb = set ? 0 : pixel[0];
				
				if (set) {
					pixel[0] = pixelsImagem[i][j];
					buffer.getRaster().setPixel(j, i, pixel);
				} else {
					pixelsImagem[i][j] = rgb;
				}
			}
		}
	}
	
	private void carregarReticulado(boolean set, int posicao) {
		Reticulado r = reticuladoInicial[posicao];
		
		if (r == null) {
			r = new Reticulado(pixelsImagem.length, pixelsImagem[0].length);
			reticuladoInicial[posicao] = r;
		}
		
		for (int i = 0; i < pixelsImagem.length; i++) {
			
			for (int j = 0; j < pixelsImagem[0].length; j++) {
				if (set) {
					r.set(i, j, (pixelsImagem[i][j] & (1 << posicao)) != 0);
				} else {
					pixelsImagem[i][j] = pixelsImagem[i][j] | ((r.get(i, j) ? 1 : 0) << posicao);
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
		return null;
	}

	@Override
	public void setAutomatoCelular(AutomatoCelular ac) {
		//automatoCelular = ac;
	}

	
	public static void main(String[] args) throws Exception {
		CifradorImagemColorida256CoresVersao2 v2 = new CifradorImagemColorida256CoresVersao2(
				"C:\\Temp\\teste_v2\\128x128_001.png", "0110100101001010", 0);
		
		v2.cifrar(10, false);
		
		v2.criarImagem("C:\\Temp\\teste_v2\\cifragem.bmp");
		
		//v2.decifrar(100, false);
		
		//v2.criarImagem("C:\\Temp\\teste_v2\\decifragem.bmp");
		System.out.println("FIM");
	}
}
