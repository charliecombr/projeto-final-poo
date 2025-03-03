package model;

public class Impressora <T> {

	private T valor;
	
	public Impressora(T valorNovo) {
		this.valor = valorNovo;
	}
	
	public void imprimirString() {
		System.out.println(valor);
	}
}
