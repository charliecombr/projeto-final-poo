package gui;

import exception.ValidarCpfException;
import java.awt.BorderLayout;
import java.sql.SQLException;
import java.util.InputMismatchException;
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
		txfNome = new JTextField(28);
		txfCpf = new JTextField(28);
		painelForm.add(lblNome);
		painelForm.add(txfNome);
		painelForm.add(lblCpf);
		painelForm.add(txfCpf);
		add(painelForm, BorderLayout.CENTER);
		painelBotoes = new JPanel();
		btnSair = new JButton("Sair");
		btnSair.addActionListener(e -> fecharTela());
		btnLimpar = new JButton("Limpar");
		btnLimpar.addActionListener(e -> limparCampos());
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
		this.dispose();
	}
	
    private void limparCampos() {
        txfNome.setText("");
        txfCpf.setText("");
    }
    
    private boolean validarCPF(String cpf) throws ValidarCpfException, SQLException {
		if (cpf.length() != 11) {
			throw new ValidarCpfException("CPF deve ter 11 dígitos");
		}
		
		try {
			Paciente pacienteExistente = pacService.localizarPacientePorCpf(cpf);
			if (pacienteExistente != null) {
				throw new ValidarCpfException("CPF já cadastrado para outro paciente");
			}
		} catch (exception.PacienteNaoEncontradoException e) {
			return true;
		}
	
		return true;
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
        String cpfSemFormatacao = cpf.replaceAll("[.-]", "");

        for (int i = 0; i < cpfSemFormatacao.length(); i++) {
            if (!Character.isDigit(cpfSemFormatacao.charAt(i))) {
                throw new InputMismatchException("CPF deve conter apenas números");
            }
        }
        
        // Validar o CPF (usando a exceção ValidarCpfException)
        if (!validarCPF(cpfSemFormatacao)) {
            throw new ValidarCpfException("CPF inválido");
        }
        
        // Verificar se o CPF já existe no banco
        try {
            Paciente pacienteExistente = pacService.localizarPacientePorCpf(cpfSemFormatacao);
            if (pacienteExistente != null) {
                JOptionPane.showMessageDialog(this, 
                    "Já existe um paciente cadastrado com este CPF.", 
                    "CPF Duplicado", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (exception.PacienteNaoEncontradoException e) {
            // CPF não encontrado, podemos continuar com o cadastro
        }

        // Criar e adicionar o novo paciente
        Paciente p = new Paciente(0L, cpfSemFormatacao, nome);
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
    } catch (ValidarCpfException e) {
        JOptionPane.showMessageDialog(this, 
            "Erro de validação de CPF: " + e.getMessage(), 
            "Erro", 
            JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, 
            "Erro ao cadastrar paciente: " + e.getMessage(), 
            "Erro", 
            JOptionPane.ERROR_MESSAGE);
    } catch (InputMismatchException e) {
        JOptionPane.showMessageDialog(this,
            "Erro, CPF não pode conter letras: " + e.getMessage(),
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
