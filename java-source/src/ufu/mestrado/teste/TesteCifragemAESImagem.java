package ufu.mestrado.teste;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import ufu.mestrado.Cronometro;

public class TesteCifragemAESImagem {
	/** Valor RGB da cor preta */
	public static final int PRETO = Color.BLACK.getRGB();
	
	/** Valor RGB da cor branca */
	public static final int BRANCO = Color.WHITE.getRGB();
	
	/**
	 * Cifra a imagem fornecida e gera uma imagem cifrada de saída.
	 * @param imagem Imagem a ser cifrada.
	 * @param operacao Operação de cifragem.
	 * @param pastaSaida Pasta onde será salvo o arquivo cifrado.
	 */
	private static void cifrar(int altura, int largura, byte imagem[], String operacao, String pastaSaida, String nomeArquivo) throws Exception {
		System.gc();
		
		Cronometro.iniciar();
		
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);

		// Generate the secret key specs.
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance(operacao);
		
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

		byte[] encrypted = cipher.doFinal(imagem);
		
		Cronometro.parar("TAMANHO: " + imagem.length +" Cifragem '" + nomeArquivo + "' " + operacao + " Tempo gasto: ");
		
		gerarImagem(largura, altura, encrypted, pastaSaida + operacao.replaceAll("/", "_") + "_" + nomeArquivo);
		
	}

	/**
	 * Gera a imagem (BMP) fornecida em um arquivo físico.
	 * @param imagem Imagem a ser gerada.
	 * @param nomeArquivoSaida Nome do arquivo de saída.
	 * @throws Exception
	 */
	private static void gerarImagem(int largura, int altura, byte imagem[], String nomeArquivoSaida) throws Exception {
		BufferedImage buffer = new BufferedImage(
				largura, 
				altura, 
				BufferedImage.TYPE_INT_RGB);
		
		ByteArrayInputStream bais = new ByteArrayInputStream(imagem);
		
		for (int i = 0; i < altura; i++) {
			for (int j = 0; j < largura; j++) {
				buffer.setRGB(j, i, bais.read());
			}
		}
		
		ImageIO.write(buffer, "BMP", new File(nomeArquivoSaida));
	}


	public static void main(String[] args) throws Exception {
		final String pastaSaida = "C:/Temp/aes/";
		
		final String imagens[] = {"512x512_0113.png"/*, "tux.bmp", "quadrado.bmp", "circulo.bmp", "triangulo.bmp"*/};
		
		
		for (String arquivoImagem : imagens) {
			
			BufferedImage buffer =  ImageIO.read(new File(pastaSaida + arquivoImagem));
			
			//ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			final int height = buffer.getHeight();
			final int width = buffer.getWidth();
			/*
			for (int i = 0; i < buffer.getHeight(); i++) {
				for (int j = 0; j < buffer.getWidth(); j++) {
					baos.write(buffer.getRGB(j, i) == PRETO ? 1 : 0);
				}
			}*/
			
			final byte[] imagem = new byte[height * width / 8];//baos.toByteArray();
		
			//Default
			//cifrar(height, width, imagem, "AES", pastaSaida, arquivoImagem);
			
			//ECB
			//cifrar(height, width, imagem, "AES/ECB/NoPadding", pastaSaida, arquivoImagem);
			//cifrar(height, width, imagem, "AES/ECB/PKCS5Padding", pastaSaida, arquivoImagem);
		
			//CBC
			//cifrar(height, width, imagem, "AES/CBC/NoPadding", pastaSaida, arquivoImagem);
			//cifrar(height, width, imagem, "AES/CBC/PKCS5Padding", pastaSaida, arquivoImagem);
			
			//CFB
			//cifrar(height, width, imagem, "AES/CFB/NoPadding", pastaSaida, arquivoImagem);
			//cifrar(height, width, imagem, "AES/CFB/PKCS5Padding", pastaSaida, arquivoImagem);
		
			//OFB
			//cifrar(height, width, imagem, "AES/OFB/NoPadding", pastaSaida, arquivoImagem);
			//cifrar(height, width, imagem, "AES/OFB/PKCS5Padding", pastaSaida, arquivoImagem);
			
			//PCBC
			//cifrar(height, width, imagem, "AES/PCBC/NoPadding", pastaSaida, arquivoImagem);
			//cifrar(height, width, imagem, "AES/PCBC/PKCS5Padding", pastaSaida, arquivoImagem);
			
			cifrar(height, width, imagem, "AES/CFB/NoPadding", pastaSaida, arquivoImagem);
			
			System.out.println("FIM");
			
			/*for (int i = 0; i < 50; i++) {
				cifrar(height, width, imagem, "AES/CFB/NoPadding", pastaSaida, arquivoImagem);
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					
				}
			}*/
		}
	}
}
