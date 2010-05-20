package ufu.mestrado;

public class Cronometro {
	public static long tempo;
	
	public static void iniciar() {
		iniciar(null);
	}
	
	public static void iniciar(String mensagem) {
		if (mensagem != null) {
			System.out.print(mensagem);
		}
		tempo = System.currentTimeMillis();
	}
	
	public static void parar() {
		parar(null);
	}
	
	public static void parar(String mensagem) {
		tempo = System.currentTimeMillis() - tempo;
		if (mensagem != null) {
			System.out.print(mensagem);
		}
		System.out.println(tempo);
	}
}
