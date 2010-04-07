package ufu.mestrado.util;

public abstract class BitsUtil {

	public static int DIREITA = 1;
	public static int ESQUERDA = -1;
	
	/**
	 * Rotaciona os bits do valor fornecido de acordo com o tamanho da máscara fornecida.
	 * @param valor Valor a ser rotacionado.
	 * @param tamMascara Quantidade bits da máscara utilizada na rotação.
	 * @param deslocamento Quantidade de casas que os bits serão rotacionados.
	 * @param direcao Direção da rotação.,
	 * @return Valor rotacionado de acordo com os valores forencidos.
	 */
	public static int rotacionar(int valor, int tamMascara, int deslocamento, int direcao) {
		deslocamento = (direcao == DIREITA) ? deslocamento : tamMascara - deslocamento;
		
		//System.out.println("deslocamento.: " + (direcao == DIREITA ? "DIREITA" : "ESQUERDA"));
		
		//System.out.println("valor........: "  + Integer.toHexString(valor));
		
		int mascara = (1 << tamMascara) - 1;
		
		// Assegurando que o valor não é maior que a mascara
		valor = valor & mascara;
		
		//System.out.println("Mascara......: "  + Integer.toHexString(mascara));
		
		// Criando as mascaras para aplicar no valor
		int mascaraBaixa = (1 << deslocamento) - 1;
		int mascaraAlta = mascara - mascaraBaixa;
		
		//System.out.println("Mascara-alta.: "  + Integer.toHexString(mascaraAlta));
		//System.out.println("Mascara-baixa: "  + Integer.toHexString(mascaraBaixa));
		
		// Separando o valor em parte alta e baixa
		int parteAlta = valor & mascaraAlta;
		int parteBaixa = valor & mascaraBaixa;
		
		//System.out.println("parte-alta...: "  + Integer.toHexString(parteAlta));
		//System.out.println("parte-baixa..: "  + Integer.toHexString(parteBaixa));
		
		// Rotacionando o bits da parte alta e baixa
		parteBaixa = (parteBaixa << tamMascara - deslocamento) & mascara;
		parteAlta = (parteAlta >> deslocamento) & mascara;
		
		// Juntando as partes rotacionadas
		valor = parteAlta | parteBaixa;
		
		//System.out.println("valor........: "  + Integer.toHexString(valor));
		
		return valor;
	}
	
	public static void main(String[] args) {
		rotacionar(0x123456, 24, 4, DIREITA);
	}
}
