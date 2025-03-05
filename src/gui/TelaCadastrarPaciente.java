package gui;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Paciente;
import service.PacienteService;

public class TelaCadastrarPaciente extends JDialog{
	private static final long serialVersionUID = 1L;
	
	private PacienteService pacService;
	private TelaPrincipal main;
	private JPanel painelForm;
	private JPanel painelBotoes;
	private JButton btnSalvar;
	private JButton btnLimpar;
	private JButton btnSair;
	private JLabel lblNome;
	private JLabel lblCpf;
	private JTextField txfNome;
	private JTextField txfCpf;
	
	
	
	public TelaCadastrarPaciente(PacienteService pacService, TelaPrincipal main) {
		this.pacService = pacService;
		this.main = main;
		setSize(360,200);
		setResizable(false);
		setTitle("Tela de Cadastro de Paciente");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		painelForm = new JPanel();
		lblNome = new JLabel("Nome: ");
		lblCpf = new JLabel("CPF: ");
		txfNome = new JTextField(24);
		txfCpf = new JTextField(24);
		painelForm.add(lblNome);
		painelForm.add(txfNome);
		painelForm.add(lblCpf);
		painelForm.add(txfCpf);
		add(painelForm, BorderLayout.CENTER);
		painelBotoes = new JPanel();
		btnSair = new JButton("Sair");
		btnSair.addActionListener(e -> fecharTela());
		btnLimpar = new JButton("Limpar");
		btnSalvar = new JButton("Salvar");
		btnSalvar.addActionListener(e -> addPaciente());
		painelBotoes.add(btnSalvar);
		painelBotoes.add(btnLimpar);
		painelBotoes.add(btnSair);
		add(painelBotoes, BorderLayout.SOUTH);
		setModal(true);
		setVisible(true);
	}
	
	private void fecharTela() {
		this.hide();
	}
	
	private void limparCampos() {
        txfNome.setText("");
        txfCpf.setText("");
    }
	
	private void addPaciente() {
        String cpf = txfCpf.getText().trim();
        String nome = txfNome.getText().trim();

        if (cpf.isEmpty() || nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos.", 
                "Campos vazios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Paciente p = new Paciente(0L, cpf, nome);
            pacService.adicionarPaciente(p);
            JOptionPane.showMessageDialog(this, "Paciente cadastrado com sucesso!");
            limparCampos();
            if (main != null) {
                main.loadTablePaciente();
            }
            fecharTela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao cadastrar paciente: Falha no banco de dados - " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao cadastrar paciente: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro inesperado ao cadastrar paciente: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
	
}
