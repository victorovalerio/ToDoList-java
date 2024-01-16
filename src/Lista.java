import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Lista extends JFrame implements ActionListener{
	static List<Item> lista = new ArrayList<>();
	static String nome = "";
	static String descricao = "";
	JButton adicionar = new JButton("Adicionar");
	JButton marcar = new JButton("Marcar");
	JButton excluir = new JButton("Excluir");
	JButton atualizar = new JButton("Atualizar");
	JButton mudaDescricao = new JButton("Mudar Descrição");
	JPanel botoes = new JPanel();
	JLabel label = new JLabel();
	static Connection conexao = FabricaConexao.getConnection();
	static Scanner entrada = new Scanner(System.in);
	static boolean loop = true;
	static String listaString = "";
	
	public Lista(){
		try {
			importaItems();
		}catch(SQLException e) {
			JOptionPane.showMessageDialog(null, "Não foi possível importar a lista", "ERRO DE ACESSO AO BANCO",JOptionPane.INFORMATION_MESSAGE);
		}
		label.setText(listaString);
		label.setFont(new Font(Arial,18));
		setSize(620, 480);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(ne w BorderLayout());
		add(botoes,BorderLayout.SOUTH);
		add(label,BorderLayout.NORTH);
		botoes.add(adicionar);
		botoes.add(atualizar);
		botoes.add(marcar);
		botoes.add(excluir);
		adicionar.addActionListener(this);
		atualizar.addActionListener(this);
		marcar.addActionListener(this);
		excluir.addActionListener(this);
		atualizaString();
	}
	
	
	private void adicionarItem(){
		String comandoInsert = "INSERT INTO todolist (nome, descricao, feito) VALUES (?, ?, ?)";
		nome = JOptionPane.showInputDialog(null, "Digite o nome da tarefa", "ADICIONAR ITEM", JOptionPane.PLAIN_MESSAGE);
		boolean feito = false;
		descricao = JOptionPane.showInputDialog(null,"Descricao", "ADICIONAR DESCRICAO", JOptionPane.PLAIN_MESSAGE);
		
		try {
			PreparedStatement stmt = conexao.prepareStatement(comandoInsert);
			stmt.setString(1, nome);
			stmt.setString(2, descricao);
			stmt.setBoolean(3, false);
			int rowsAffected = stmt.executeUpdate();
			if(rowsAffected > 0)	JOptionPane.showMessageDialog(null,"Item adicionado a lista com sucesso","CONCLUIDO", JOptionPane.INFORMATION_MESSAGE);
			else 	JOptionPane.showMessageDialog(null,"Erro ao adicionar item.","CONCLUIDO", JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,"Erro ao adicionar item: "+ e.getMessage(),"ERRO", JOptionPane.ERROR_MESSAGE);
		}
		Item item = new Item(nome, descricao,feito);
		lista.add(item);
		atualizaString();
	}
	
	private void marcarItem() {
		String comandoSQL = "UPDATE todolist "
				+ "set feito = 1 "
				+ "where nome = ?";
		
		String nomeDoItem = JOptionPane.showInputDialog(null,"Digite o nome do item para eu marcar", "MARCAR", JOptionPane.PLAIN_MESSAGE);
	
		try {
			PreparedStatement stmt = conexao.prepareStatement(comandoSQL);
			stmt.setString(1, nomeDoItem);
			stmt.execute();
			JOptionPane.showMessageDialog(null, "Item adicionado com sucesso","FEITO", JOptionPane.INFORMATION_MESSAGE);
			Thread.sleep(1000);
			atualizaString();			
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível marcar o item. Erro: " + e.getMessage(),
					"ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
	}
	private void mudarDescricao() {
		String comandoSQL = "UPDATE todolist "
				+ "set descricao = ?"
				+ "where nome = ?";
		
		String nomeDoItem = JOptionPane.showInputDialog(null,"Digite o nome do item para trocar a descricao", "MUDAR DESCRIÇÃO", JOptionPane.PLAIN_MESSAGE);
		String novaDescricao = JOptionPane.showInputDialog(null,"Digite a nova descricao", "MUDAR DESCRIÇÃO", JOptionPane.PLAIN_MESSAGE);
	
		try {
			PreparedStatement stmt = conexao.prepareStatement(comandoSQL);
			stmt.setString(1,novaDescricao);
			stmt.setString(2, nomeDoItem);
			stmt.execute();
			JOptionPane.showMessageDialog(null, "Item adicionado com sucesso","FEITO", JOptionPane.INFORMATION_MESSAGE);
			Thread.sleep(1000);
			atualizaString();			
		} catch (SQLException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Não foi possível marcar o item. Erro: " + e.getMessage(),
					"ERRO",
					JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private void deletarItem() {
		System.out.println("CLICOU EM DELETAR");
		atualizaString();
	}

	private void atualizaString(){
		try {
			importaItems();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listaString = "";
		listaString += "<html>";
		for(Item i : lista) {
    		listaString += "<li>" + "<b>Nome<b/>: " + i.nome + ". Descrição: " + i.descricao + ". Feito? " + i.feito + ".\n" ;
		}
		listaString += "<html/>";
		label.setText(listaString);
	}

	private void importaItems() throws SQLException {
		lista.clear();
	    String comandoSelect = "SELECT * FROM todolist";
	    try {
	    	PreparedStatement stmt = conexao.prepareStatement(comandoSelect);
	    	ResultSet resultado = stmt.executeQuery();
	    	while(resultado.next()) {
	    		String nome = resultado.getString("nome");
	    		String descricao = resultado.getString("descricao");
	    		boolean feito = resultado.getBoolean("feito");
	    		Item item = new Item(nome, descricao, feito);
	    		lista.add(item);
	    	}
	    } catch(SQLException e) {
	    	e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Não foi possível importar a lista:" + e.getMessage(), "ERRO DE ACESSO AO BANCO",JOptionPane.INFORMATION_MESSAGE);
	    }
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton botao = (JButton) e.getSource();
			
			switch(botao.getText()) {
				case "Adicionar" :	adicionarItem();
								  	break;
				case "Excluir" : 	deletarItem();
									break;
				case "Marcar"	:	marcarItem();
									break;	
				case "Mudar Descrição":	mudarDescricao();
									break;	
			}
		}
		entrada.close();
		}


	
}