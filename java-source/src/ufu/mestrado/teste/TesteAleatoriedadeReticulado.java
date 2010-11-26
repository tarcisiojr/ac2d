package ufu.mestrado.teste;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

import ufu.mestrado.Reticulado;
import ufu.mestrado.Util;

public class TesteAleatoriedadeReticulado {
	
	private static final NumberFormat  nf = DecimalFormat.getInstance(new Locale("pt", "BR"));
	
	static {
		nf.setMaximumFractionDigits(9);
		nf.setMinimumFractionDigits(9);
	}

	private static final Random rand = new Random(0);
	
	
	private static final void testar(int qtdBits, int qtdAmostras, int linhaJanela, int colunaJanela) {
		
		double amostra[] = new double[qtdAmostras];
		double entropias[] = new double[qtdAmostras];
		
		double total = 0;
		
		double totalEntropia = 0;
		
		for (int i = 0; i < qtdAmostras; i++) {
			
			int qtdZeros = 0;
			
			boolean reticulado[][] = new boolean[qtdBits][qtdBits];
			
			for (int b = 0; b < qtdBits; b++) {
				for (int j = 0; j < qtdBits; j++) {
					if (!rand.nextBoolean()) {
						qtdZeros++;
					} else {
						reticulado[b][j] = true;
					}
				}
			}
			
			//System.out.println(new Reticulado(reticulado));
			
			
			entropias[i] = new Reticulado(reticulado).entropia(linhaJanela, colunaJanela);
			
			//System.out.println(entropias[i]);
			
			totalEntropia += entropias[i];
				
			//System.out.println(qtdZeros);
			
			amostra[i] = ((double) qtdZeros / (double)(qtdBits * qtdBits)) * 100.0;
			
			total += amostra[i];
		}
		
		double media = total / qtdAmostras;
		
		double mediaEntropia = totalEntropia / qtdAmostras;
		
		System.out.println("Percentual\t" + qtdBits + "\t" + nf.format(media) + "\t" + nf.format(Util.desvioPadrao(amostra, media)));
		System.out.println("Entropia\t" + qtdBits + "\t" + nf.format(mediaEntropia) + "\t" + nf.format(Util.desvioPadrao(entropias, mediaEntropia)));
	}

	public static void main(String[] args) {
		System.out.println("Teste\tQtd_bits\tMedia\tDesvio_Padrao");
		
		int amostra = 10000;
		
		testar(512, amostra, 3, 6);
		testar(256, amostra, 4, 4);
		testar(128, amostra, 2, 7);
		testar(64, amostra, 3, 4);
		testar(32, amostra, 2, 5);
		testar(16, amostra, 2, 4);
	}
}
