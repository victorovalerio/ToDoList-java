import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Lista {
	static Scanner entrada = new Scanner(System.in);
	static List<Item> lista = new ArrayList<>();
	public static void main(String[] args) {
		int escolha = 0;
		int loop = 1;
		System.out.println("Bem vindo ao to-do app do beerman");
		System.out.println("INICIALIZANDO");
		        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("lista.dat"))) {
		            List<Item> readObject = (List<Item>) ois.readObject();
					lista = readObject;
		            System.out.println("Lista.dat carregado");
		        } catch (IOException | ClassNotFoundException e) {
		            System.out.println("ERRO AO LER ARQUIVO");
		       	}
		while(loop > 0) {
			System.out.println("Digite o que deseja fazer agora:\n"
					+ "1) Adicionar um item\n"
					+ "2) Ler a descrição de um item\n"
					+ "3) Deletar um item\n"
					+ "4) Marcar como feito\n"
					+ "5) Listar itens\n"
					+ "6) Salvar alterações\n");
			escolha = entrada.nextInt();
			
			switch(escolha) {
				case 1 : adicionarItem();
					break;
				case 2 : lerDescricao();
					break;
				case 3 : deletarItem();
					break;
				case 4 : marcarItem();
					break;
				case 5 : listarItens();
					break;
				case 6 : salvar();
					break;
				default	: System.out.println("aff");
			}
			System.out.println("Quer continuar? 1/0\n");
			
			loop = entrada.nextInt();
		}
		entrada.close();
	}


	private static void adicionarItem() {
		System.out.println("adicionar item()\n");
		String nome;
		String descricao;
		boolean feito = false;
		System.out.println("Digite o nome do item");
		entrada.nextLine();
		nome = entrada.nextLine();
		System.out.println("Digite a descrição do item");
		descricao = entrada.nextLine();
		Item item = new Item(nome, descricao,feito);
		lista.add(item);
		
		//verifica se foi adicionado
		if(lista.stream().anyMatch(i -> i.nome.equals(nome))) System.out.println("ITEM ADICIONADO COM SUCESSO");
	}

	private static void lerDescricao() {
		System.out.println("Digite o nome de um item para ver a descrição");
		entrada.nextLine();
		String Pesquisa = entrada.nextLine();
		
		Optional<Item> itemEncontrado = 
		lista.stream().filter(i -> i.nome.equals(Pesquisa)).findFirst();
		
		Item item = itemEncontrado.orElse(null);
		if(item!= null) {
			System.out.println("ITEM \"" + item.nome + "\" TEM A DESCRIÇÃO: \"" + item.descricao + "\"");
		} else {
			System.out.println("ITEM NÃO ENCONTRADO");
		}
	}

	private static void marcarItem() {
		System.out.println("Digite o nome de um item para marcar como feito ou nao");
		entrada.nextLine();
		String Pesquisa = entrada.nextLine();
		
		Optional<Item> itemEncontrado = 
		lista.stream().filter(i -> i.nome.equals(Pesquisa)).findFirst();
		
		Item item = itemEncontrado.orElse(null);
		if(item!= null) {
			System.out.println("ITEM  \"" + item.nome + "\" ALTERADO");
			item.marcar();
		} else {
			System.out.println("ITEM NÃO ENCONTRADO");
		}
	}
	private static void deletarItem()  {
		System.out.println("Digite o nome de um item pra eu deletar");
		entrada.nextLine();
		String Pesquisa = entrada.nextLine();
		
		Optional<Item> itemEncontrado = 
		lista.stream().filter(i -> i.nome.equals(Pesquisa)).findFirst();
		
		Item item = itemEncontrado.orElse(null);
		if(item!= null) {
			System.out.println("ITEM  \"" + item.nome + "\" ENCONTRADO.\n"
					+ "Tem certeza que quer deletar "+ item.nome + "? -> 1/0\n");
			int excluir = entrada.nextInt();
			
			if (excluir <= 0) {
				System.out.println("ok, deixa pra la"); 
			}	else {
				System.out.println("MORRA, " + item.nome + "!!!!!!");
				lista.remove(item);
			}
		} else {
			System.out.println("ITEM NÃO ENCONTRADO");
		}
	}

	private static void listarItens() {
		lista.stream().forEach(i -> System.out.println(lista.indexOf(i) + ") " + i.nome + "\nDescrição: \"" +
	i.descricao +
	"\"\nfeito? " + i.feito + "\n"));	
	}

	private static void salvar() {
		   try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("lista.dat"))) {
	            oos.writeObject(lista);
	            System.out.println("Procura o \"lista.dat\"");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

}
