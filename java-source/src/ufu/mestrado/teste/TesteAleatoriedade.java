package ufu.mestrado.teste;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

import ufu.mestrado.Util;

public class TesteAleatoriedade {
	
	private static final NumberFormat  nf = DecimalFormat.getInstance(new Locale("pt", "BR"));
	
	static {
		nf.setMaximumFractionDigits(9);
		nf.setMinimumFractionDigits(9);
	}

	private static final Random rand = new Random(0);
	
	
	private static final void testar(int qtdBits, int qtdAmostras) {
		
		double amostra[] = new double[qtdAmostras];
		
		double total = 0;
		
		for (int i = 0; i < qtdAmostras; i++) {
			
			int qtdZeros = 0;
			
			for (int b = 0; b < qtdBits; b++) {
				if (!rand.nextBoolean()) {
					qtdZeros++;
				}
			}
			
			//System.out.println(qtdZeros);
			
			amostra[i] = (double) qtdZeros / (double)qtdBits * 100;
			
			total += amostra[i];
		}
		
		double media = total / qtdAmostras;
		
		
		double desvioPadrao = Util.desvioPadrao(amostra, media);
		
		System.out.println(qtdBits + "\t" + nf.format(media) + "\t" + nf.format(desvioPadrao));
	}

	public static void main(String[] args) {
		System.out.println("Qtd_bits\tMedia\tDesvio_Padrao");
		
		int amostra = 10000;
		
		//int []qtdBits = new int[] {128, 512, 1024, 4096, 16384, 65536, 262144};
		//int []qtdBits = new int[] {32 * 32 * 8, 64 * 64 * 8, 128 * 128 * 8};
		int []qtdBits = new int[] {2048*1024};
		
		for (int i = 0; i < qtdBits.length; i++) {
			testar(qtdBits[i], amostra);
		}
	}
}
