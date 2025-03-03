package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import model.Paciente;
import service.PacienteService;

public class TelaCadastrarPaciente extends JDialog{

	/**
	 * 
	 */
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
	
	private void addPaciente() {
		String cpf = txfCpf.getText().trim();

		Paciente pacienteExistente = pacService.localizarPacientePorCpf(cpf);
		if (pacienteExistente != null) {
			JOptionPane.showMessageDialog(this, 
				"Já existe um paciente cadastrado com o CPF: " + cpf, 
				"CPF duplicado", 
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		Paciente p = new Paciente(0L, txfCpf.getText(), txfNome.getText());
		pacService.adicionarPaciente(p);
		JOptionPane.showMessageDialog(null, "Paciente cadastrado com sucesso");
		txfCpf.setText("");
		txfNome.setText("");
		main.loadTablePaciente();
	}
	
}
