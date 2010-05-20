package ufu.mestrado.teste;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;

import ufu.mestrado.Cronometro;

public class TesteDecifragemRuidoImagemAES {
	private static final IvParameterSpec iv = new IvParameterSpec(new byte[16]);
	
	/**
	 * Cifra a imagem fornecida e gera uma imagem cifrada de saída.
	 * @param imagem Imagem a ser cifrada.
	 * @param operacao Operação de cifragem.
	 * @param pastaSaida Pasta onde será salvo o arquivo cifrado.
	 */
	private static byte[] cifrar(int altura, int largura, byte imagem[], String operacao, String pastaSaida, String nomeArquivo, boolean decifrar) throws Exception {
		Cronometro.iniciar();
		
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);

		// Generate the secret key specs.
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();

		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance(operacao);
		
		if (!operacao.contains("ECB")) {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		} else {
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		}
		
		

		byte[] encrypted = cipher.doFinal(imagem);
		//byte[] encrypted = imagem;//cipher.doFinal(imagem);
		
		Cronometro.parar("Cifragem '" + nomeArquivo + "' " + operacao + " ");
		
		String novaImg = pastaSaida + operacao.replaceAll("/", "_") + "_" + nomeArquivo;
		
		gerarImagem(largura, altura, encrypted, novaImg);
		
		
		// Aplicando ruido na mensagem cifrada.
		for (int i = 0; i < 100; i++) {
			for (int j = 0; j < 100; j++) {
				encrypted[(i * 3 * largura) + j] = 0;
			}
		}
		
		gerarImagem(largura, altura, encrypted, pastaSaida + "cifrada_ruido_" + operacao.replaceAll("/", "_") + "_" + nomeArquivo);
		
		if (decifrar) {
			if (!operacao.contains("ECB")) {
				cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			} else {
				cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			}
			
			byte[] decrypted = cipher.doFinal(encrypted);
			
			gerarImagem(largura, altura, decrypted, pastaSaida + "decifrada_" +operacao.replaceAll("/", "_") + "_" + nomeArquivo);
		}
		
		return raw;
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
				//00000000  00000000  00000000
				int rgb = (bais.read() & 0xFF) << 16;
				rgb |= (bais.read() & 0xFF) << 8;
				rgb |= (bais.read() & 0xFF);
				
				buffer.setRGB(j, i, rgb);
			}
		}
		
		ImageIO.write(buffer, "BMP", new File(nomeArquivoSaida));
	}


	public static void main(String[] args) throws Exception {
		final String pastaSaida = "E:/junior/Desktop/mestrado/testes_aes/teste_decifragem_ruido/";
		
		final String imagens[] = {"ferrari.bmp", "mulher.bmp", "tux.bmp", "quadrado.bmp", "circulo.bmp", "triangulo.bmp"};
		
		
		for (String arquivoImagem : imagens) {
			
			BufferedImage buffer =  ImageIO.read(new File(pastaSaida + arquivoImagem));
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			final int height = buffer.getHeight();
			final int width = buffer.getWidth();
			
			for (int i = 0; i < buffer.getHeight(); i++) {
				for (int j = 0; j < buffer.getWidth(); j++) {
					int rgb = buffer.getRGB(j, i);
					
					baos.write((rgb & 0xFF0000) >> 16);
					baos.write((rgb & 0xFF00) >> 8);
					baos.write(rgb & 0xFF);
				}
			}
			
			final byte[] imagem = baos.toByteArray();
		
			//Default
			//cifrar(height, width, imagem, "AES", pastaSaida, arquivoImagem);
			
			//ECB
			cifrar(height, width, imagem, "AES/ECB/NoPadding", pastaSaida, arquivoImagem, true);
			cifrar(height, width, imagem, "AES/ECB/PKCS5Padding", pastaSaida, arquivoImagem, true);
		
			//CBC
			cifrar(height, width, imagem, "AES/CBC/NoPadding", pastaSaida, arquivoImagem, true);
			cifrar(height, width, imagem, "AES/CBC/PKCS5Padding", pastaSaida, arquivoImagem, true);
			
			//CFB
			cifrar(height, width, imagem, "AES/CFB/NoPadding", pastaSaida, arquivoImagem, true);
			cifrar(height, width, imagem, "AES/CFB/PKCS5Padding", pastaSaida, arquivoImagem, true);
		
			//OFB
			cifrar(height, width, imagem, "AES/OFB/NoPadding", pastaSaida, arquivoImagem, true);
			cifrar(height, width, imagem, "AES/OFB/PKCS5Padding", pastaSaida, arquivoImagem, true);
			
			//PCBC
			cifrar(height, width, imagem, "AES/PCBC/NoPadding", pastaSaida, arquivoImagem, true);
			//cifrar(height, width, imagem, "AES/PCBC/PKCS5Padding", pastaSaida, arquivoImagem, true);
		}
	}
}
