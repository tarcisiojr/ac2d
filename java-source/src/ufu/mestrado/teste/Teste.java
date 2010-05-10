package ufu.mestrado.teste;

public class Teste {

	public static void main(String[] args) {
		(new Thread() {
			@Override
			public void run() {
				while (true) {
					System.out.println(System.nanoTime());
				}
			}
		}).start();
	}
}
