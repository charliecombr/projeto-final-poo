package gui;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Exame;
import model.Paciente;
import service.ExameService;
import service.PacienteService;

public class TelaCadastrarExame extends JDialog {

    private static final long serialVersionUID = 1L;
    
    private ExameService exameServ;
    private PacienteService pacService;
    private TelaPrincipal main;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnSair;
    private JLabel lblDescricao;
    private JLabel lblCpfPaciente;
    private JTextField txfDescricao;
    private JTextField txfCpfPaciente;

    public TelaCadastrarExame(ExameService exameServ, PacienteService pacService, TelaPrincipal main) {
        this.exameServ = exameServ;
        this.pacService = pacService;
        this.main = main;
        setSize(360, 150);
        setResizable(false);
        setTitle("Tela de Cadastro de Exame");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        painelForm = new JPanel();
        lblDescricao = new JLabel("Descrição do Exame:");
        txfDescricao = new JTextField(15);
        lblCpfPaciente = new JLabel("CPF do Paciente:");
        txfCpfPaciente = new JTextField(15);
        painelForm.add(lblDescricao);
        painelForm.add(txfDescricao);
        painelForm.add(lblCpfPaciente);
        painelForm.add(txfCpfPaciente);
        add(painelForm, BorderLayout.CENTER);
        
        painelBotoes = new JPanel();
        btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> fecharTela());
        btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> addExame());
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
        txfDescricao.setText("");
        txfCpfPaciente.setText("");
    }

    private void addExame() {
        String descricao = txfDescricao.getText().trim();
        String cpf = txfCpfPaciente.getText().trim();

        if (descricao.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos.", 
                "Campos vazios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Paciente paciente = pacService.localizarPacientePorCpf(cpf);
            if (paciente == null) {
                throw new exception.PacienteNaoEncontradoException("Paciente com CPF " + cpf + " não encontrado");
            }

            // Define a data atual automaticamente
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataAtual = sdf.format(new Date());

            if (exameServ.verificarExameExistente(paciente.getId(), descricao, new Date())) { // Usa Date para verificar
                JOptionPane.showMessageDialog(this, 
                    "Já existe um exame com essa descrição para o paciente na data atual.", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Exame exame = new Exame(null, descricao, dataAtual, paciente.getId());
            exameServ.adicionarExame(exame);
            JOptionPane.showMessageDialog(this, "Exame cadastrado com sucesso!");
            limparCampos();
            if (main != null) {
                main.loadTableExame();
            }
            fecharTela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao cadastrar exame: Falha no banco de dados - " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (exception.PacienteNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, 
                "Paciente não encontrado com o CPF fornecido: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao cadastrar exame: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro inesperado ao cadastrar exame: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}