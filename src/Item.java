import java.io.Serializable;

public class Item implements Serializable {
	public String nome;
	public String descricao;
	public boolean feito;

	public Item(String nome, String descricao, boolean feito) {
		this.nome = nome;
		this.descricao = descricao;
		this.feito = feito;
	}
	
	public void marcar() {
		feito = !feito;
	}

}
