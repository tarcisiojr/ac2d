package ufu.mestrado;

public abstract class DirecaoCalculo {
	public static final int NORTE = 0;
	public static final int ESQUERDA = 1;
	public static final int SUL = 2;
	public static final int DIREITA = 3;
	
	
	public static String toString(int direcao) {
		switch (direcao) {
		case NORTE:
			return "NORTE";
			
		case SUL:
			return "SUL";
			
		case ESQUERDA:
			return "ESQUERDA";
			
		case DIREITA:
			return "DIREITA";
		}
		
		return null;
	}
}
