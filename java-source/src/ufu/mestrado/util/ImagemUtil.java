package ufu.mestrado.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.FilenameFilter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImagemUtil {
	public static void main(String[] args) throws Exception {
		/*
		ImagemUtil.redimensionarImgsDiretorio("E:/junior/Desktop/mestrado/base_dados_imagens/mpeg7shapeB/original", 
				"gif", 512, 512, "E:/junior/Desktop/mestrado/base_dados_imagens/512x512");
		*/
		
/*		ImagemUtil.redimensionarImgsDiretorio("E:/junior/Desktop/mestrado/base_dados_imagens/512x512", 
				"png", 1024, 1024, "E:/junior/Desktop/mestrado/base_dados_imagens/1024x1024");
 */		
		/*
		
		ImagemUtil.converterEm256Cores("D:/desktop/mestrado/testes_ac2d/teste_histograma_colorida/imagens", 
				"jpg", "png", "D:/desktop/mestrado/testes_ac2d/teste_histograma_colorida/imagens_cifradas");
		
		ImagemUtil.redimensionarImgsDiretorio("D:/desktop/mestrado/testes_ac2d/teste_histograma_colorida/imagens", 
				"jpg", 128, 128, "D:/desktop/mestrado/testes_ac2d/teste_histograma_colorida/imagens_cifradas");
		*/
		/*ImagemUtil.renomearArquivos("D:/desktop/mestrado/testes_ac2d/teste_histograma_colorida/imagens_cifradas", 
				"D:/desktop/mestrado/testes_ac2d/teste_histograma_colorida/imagens_cifradas", "jpg", "png");
				*/
		
		ImagemUtil.converterEmEscalaCinza("D:/desktop/mestrado/testes_ac2d/teste_histograma_colorida/imagens_cifradas", 
				"png", "D:/desktop/mestrado/testes_ac2d/teste_histograma_colorida/imagens_decifradas");
		//ImagemUtil.apenasPretoBranco("E:/junior/Desktop/mestrado/base_dados_imagens/512x512", "gif", "png");
	}
	
	/**
	 * Converte a imagem para utilizar apenas a cor preta e branco.
	 * @throws Exception
	 */
	public static void renomearArquivos(String dirStr, String dirSaida, String substr, String replacement) throws Exception {
		File dir = new File(dirStr);
		
		File arquivos[] = dir.listFiles();
		
		for (File arq : arquivos) {
			arq.renameTo(new File(arq.getAbsolutePath().substring(0, arq.getAbsolutePath().lastIndexOf('\\')) + "/" + arq.getName().replaceAll(substr, replacement)));
		}
	}
		
	
	/**
	 * Converte a imagem para utilizar apenas a cor preta e branco.
	 * @throws Exception
	 */
	public static void apenasPretoBranco(String dirImagens, String extensao, String extensaoSaida) throws Exception {
		
		final String novaExtensao = extensao.toLowerCase();
		
		File dir = new File(dirImagens);
		
		File arquivos[] = dir.listFiles(
				new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(novaExtensao);
					}
				});
		
		final int PRETO = Color.BLACK.getRGB();
		final int BRANCO = Color.WHITE.getRGB();
		
		System.out.println("Total de arquivos: " + arquivos.length);
		
		int k = 1;
		for (File arquivo : arquivos) {
			if (arquivo.isFile()) {
				BufferedImage buffer = ImageIO.read(arquivo);
				
				for (int i = 0; i < buffer.getWidth(); i++) {
					for (int j = 0; j < buffer.getHeight(); j++) {
						buffer.setRGB(i, j, buffer.getRGB(i, j) == PRETO ? PRETO : BRANCO);
					}
				}
				
				String nomeArquivoSaida = arquivo.getAbsolutePath();
				
				if (!extensao.equals(extensaoSaida)) {
					String novoNome = arquivo.getAbsolutePath();
					novoNome = novoNome.substring(0, novoNome.lastIndexOf('.') + 1);
					novoNome += extensaoSaida;
					nomeArquivoSaida = novoNome;
					
					arquivo.delete();
				} 
				
				ImageIO.write(buffer, extensaoSaida, new File(nomeArquivoSaida));
				
				System.out.println("Imagem Preto e Branco: " + k++);
			}
		}
	}
	
	/**
	 * Converte a imagem para utilizar apenas a cor preta e branco.
	 * @throws Exception
	 */
	public static void converterEm256Cores(String dirImagens, final String extensaoFiltro, String extensao, String dirSaida) throws Exception {
		
		final String novaExtensao = extensao.toLowerCase();
		
		File dir = new File(dirImagens);
		
		File arquivos[] = dir.listFiles(
				new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(extensaoFiltro);
					}
				});
		
		System.out.println("Total de arquivos: " + arquivos.length);
		
		int k = 1;
		for (File arquivo : arquivos) {
			if (arquivo.isFile()) {
				BufferedImage buffer = ImageIO.read(arquivo);
				
				BufferedImage image = new BufferedImage(buffer.getWidth(), buffer.getHeight(),  BufferedImage.TYPE_BYTE_INDEXED);
				
				for (int i = 0; i < buffer.getWidth(); i++) {
					for (int j = 0; j < buffer.getHeight(); j++) {
						image.setRGB(i, j, buffer.getRGB(i, j));
					}
				}
				
				ImageIO.write(image, novaExtensao, new File(dirSaida + "/" + arquivo.getName()));
				
				System.out.println("Imagem Colorida (256 cores): " + k++);
			}
		}
	}
	
	/**
	 * Converte a imagem para utilizar apenas em escala de cinza
	 * @throws Exception
	 */
	public static void converterEmEscalaCinza(String dirImagens, String extensao, String dirSaida) throws Exception {
		
		final String novaExtensao = extensao.toLowerCase();
		
		File dir = new File(dirImagens);
		
		File arquivos[] = dir.listFiles(
				new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(novaExtensao);
					}
				});
		
		System.out.println("Total de arquivos: " + arquivos.length);
		
		int k = 1;
		for (File arquivo : arquivos) {
			if (arquivo.isFile()) {
				BufferedImage buffer = ImageIO.read(arquivo);
				
				BufferedImage image = new BufferedImage(buffer.getWidth(), buffer.getHeight(),  BufferedImage.TYPE_BYTE_GRAY);
				
				for (int i = 0; i < buffer.getWidth(); i++) {
					for (int j = 0; j < buffer.getHeight(); j++) {
						image.setRGB(i, j, buffer.getRGB(i, j));
					}
				}
				
				ImageIO.write(image, novaExtensao, new File(dirSaida + "/" + arquivo.getName()));
				
				System.out.println("Imagem Cinza: " + k++);
			}
		}
	}
	
	
	/**
	 * Redimensiona as imagens do diret�rio fornecido.
	 * @param dirArquivos Diret�rio onde ser�o procuradas as imagens.
	 * @param extensao Extensao das imagens.
	 * @param largura Largura.
	 * @param altura Altura.
	 * @param dirSaida Diretorio de saida das imagens redimensionadas.
	 * @throws Exception
	 */
	public static void redimensionarImgsDiretorio(String dirArquivos, String extensao, int largura, int altura, String dirSaida) throws Exception {
		File diretorio = new File(dirSaida);
		
		if (!diretorio.exists()) {
			diretorio.mkdirs();
		}
		
		final String extensaoLower = extensao.toLowerCase();
		
		File arquivos[] = new File(dirArquivos).listFiles(
				new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.toLowerCase().endsWith(extensaoLower);
					}
				});
		
		System.out.println("Total de imagens que serao redimensionadas: " + arquivos.length);
		
		int k = 1;
		for (File arquivo : arquivos) {
			if (arquivo.isFile()) {
				System.out.print(k++ + " - ");
				redimensionar(arquivo.getAbsolutePath(), largura, altura, dirSaida);
			}
		}
	}
	
	/**
	 * Redimensiona a imagem fornecida.
	 * @param nomeArquivoImg Nome do arquivo da Imagem.
	 * @param largura Nova largura.
	 * @param altura Nova altura.
	 * @param dirSaida Diret�rio de saida das imagens.
	 * @throws Exception
	 */
	public static void redimensionar(String nomeArquivoImg, int largura, int altura, String dirSaida) throws Exception {

		String extensaoImg = nomeArquivoImg.substring(nomeArquivoImg.lastIndexOf('.') + 1);
		
		int indiceBarra = nomeArquivoImg.lastIndexOf(File.separatorChar);
		
		String nomeArquivo = indiceBarra < 0 ? nomeArquivoImg : nomeArquivoImg.substring(indiceBarra + 1);
		
		BufferedImage buffer = ImageIO.read(new File(nomeArquivoImg));

		//Image image = buffer.getScaledInstance(largura, altura, Image.SCALE_REPLICATE);
		
		buffer = scaleImage(buffer, largura, altura); //toBufferedImage(image);
		
		String diretorio = new File(dirSaida).getAbsolutePath();
		
		File arquivoNovaImg = new File(diretorio + File.separatorChar + nomeArquivo);
		
		ImageIO.write(buffer, extensaoImg.toUpperCase(), arquivoNovaImg);
		
		System.out.println("Imagem redimensionada (" + largura + "x" + altura 
				+ "): " + arquivoNovaImg.getAbsolutePath());
		
	}

	// This method returns a buffered image with the contents of an image public static
	public static BufferedImage toBufferedImage(Image image) {
		if (image instanceof BufferedImage) {
			return (BufferedImage) image;
		}
		
		// This code ensures that all the pixels in the image are loaded
		image = new ImageIcon(image).getImage();
		
		// Determine if the image has transparent pixels; for this method's
		// implementation, see Determining If an Image Has Transparent Pixels
		boolean hasAlpha = hasAlpha(image);
		
		// Create a buffered image with a format that's compatible with the screen
		BufferedImage bimage = null;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		try {
			// Determine the type of transparency of the new buffered image
			int transparency = Transparency.OPAQUE;
			
			if (hasAlpha) {
				transparency = Transparency.BITMASK;
			}
			
			// Create the buffered image
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
			
		} catch (HeadlessException e) {
			// The system does not have a screen } if (bimage == null) {
			// Create a buffered image using the default color model
			int type = BufferedImage.TYPE_INT_RGB;
			if (hasAlpha) {
				type = BufferedImage.TYPE_INT_ARGB;
			}
			bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		}
		// Copy image to buffered image
		Graphics g = bimage.createGraphics();
		
		// Paint the image onto the buffered image
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return bimage;
	}

	// This method returns true if the specified image has transparent pixels
	public static boolean hasAlpha(Image image) {
		// If buffered image, the color model is readily available
		if (image instanceof BufferedImage) {
			BufferedImage bimage = (BufferedImage) image;
			return bimage.getColorModel().hasAlpha();
		}
		
		// Use a pixel grabber to retrieve the image's color model;
		// grabbing a single pixel is usually sufficient
		PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
		
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
		}
		
		// Get the image's color model
		ColorModel cm = pg.getColorModel();
		return cm.hasAlpha();
	}
	
	public static BufferedImage scaleImage(BufferedImage buffer, int largura, int altura) throws Exception {

		/*BufferedImage resizedImage = new BufferedImage(largura, altura,
				BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = resizedImage.createGraphics();
				g.drawImage(buffer, 0, 0, largura, altura, null);
				g.dispose();*/
		
		int type = buffer.getType() == 0? BufferedImage.TYPE_INT_ARGB : buffer.getType();
		BufferedImage resizedImage = new BufferedImage(largura, altura, type);
		Graphics2D g = resizedImage.createGraphics();
		g.setComposite(AlphaComposite.Src);

		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);

		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

		g.drawImage(buffer, 0, 0, largura, altura, null);
		g.dispose();
		return resizedImage;
	}
}
