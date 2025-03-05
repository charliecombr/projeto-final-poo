package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Paciente;
import service.PacienteService;

public class TelaAtualizarPaciente extends JDialog {

    private static final long serialVersionUID = 1L;
    
    private PacienteService pacService;
    private TelaPrincipal main;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JButton btnBuscar;
    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnSair;
    private JLabel lblIdPaciente;
    private JLabel lblCpfAtual;
    private JLabel lblNomeAtual;
    private JLabel lblNovoCpf;
    private JLabel lblNovoNome;
    private JTextField txfIdPaciente;
    private JTextField txfCpfAtual;
    private JTextField txfNomeAtual;
    private JTextField txfNovoCpf;
    private JTextField txfNovoNome;
    private Paciente paciente;

    public TelaAtualizarPaciente(PacienteService pacService, TelaPrincipal main) {
        this.pacService = pacService;
        this.main = main;
        setSize(300, 200);
        setResizable(false);
        setTitle("Tela de Atualização de Paciente");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        painelForm = new JPanel(new GridLayout(6, 2, 5, 5));
        lblIdPaciente = new JLabel("ID do Paciente:");
        txfIdPaciente = new JTextField(24);
        lblCpfAtual = new JLabel("CPF Atual:");
        txfCpfAtual = new JTextField(24);
        txfCpfAtual.setEditable(false);
        lblNomeAtual = new JLabel("Nome Atual:");
        txfNomeAtual = new JTextField(24);
        txfNomeAtual.setEditable(false);
        lblNovoCpf = new JLabel("Novo CPF:");
        txfNovoCpf = new JTextField(24);
        lblNovoNome = new JLabel("Novo Nome:");
        txfNovoNome = new JTextField(24);
        
        painelForm.add(lblIdPaciente);
        painelForm.add(txfIdPaciente);
        painelForm.add(new JLabel(""));
        painelForm.add(new JLabel(""));
        painelForm.add(lblCpfAtual);
        painelForm.add(txfCpfAtual);
        painelForm.add(lblNomeAtual);
        painelForm.add(txfNomeAtual);
        painelForm.add(lblNovoCpf);
        painelForm.add(txfNovoCpf);
        painelForm.add(lblNovoNome);
        painelForm.add(txfNovoNome);
        add(painelForm, BorderLayout.CENTER);
        
        painelBotoes = new JPanel();
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPaciente());
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> atualizarPaciente());
        btnSalvar.setEnabled(false);
        btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> fecharTela());
        painelBotoes.add(btnBuscar);
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
        txfIdPaciente.setText("");
        txfCpfAtual.setText("");
        txfNomeAtual.setText("");
        txfNovoCpf.setText("");
        txfNovoNome.setText("");
        paciente = null;
        btnSalvar.setEnabled(false);
    }
    
    private void buscarPaciente() {
        String idStr = txfIdPaciente.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, insira o ID do paciente.", 
                "Campos vazios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            paciente = pacService.localizarPacientePorId(id);
            txfCpfAtual.setText(paciente.getCpf());
            txfNomeAtual.setText(paciente.getNome());
            txfNovoCpf.setText(paciente.getCpf());
            txfNovoNome.setText(paciente.getNome());
            btnSalvar.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Paciente encontrado com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "ID inválido. Por favor, insira um número válido.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar paciente: Falha no banco de dados - " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (exception.PacienteNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            limparCampos();
        }
    }
    
    private void atualizarPaciente() {
        if (paciente == null) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum paciente selecionado para atualização. Busque um paciente primeiro.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String novoCpf = txfNovoCpf.getText().trim();
        String novoNome = txfNovoNome.getText().trim();

        if (novoCpf.isEmpty() || novoNome.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos de novo CPF e novo nome.", 
                "Campos vazios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            paciente.setCpf(novoCpf);
            paciente.setNome(novoNome);
            pacService.atualizarPaciente(paciente);
            JOptionPane.showMessageDialog(this, "Paciente atualizado com sucesso!");
            limparCampos();
            if (main != null) {
                main.loadTablePaciente();
            }
            fecharTela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar paciente: Falha no banco de dados - " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar paciente: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro inesperado ao atualizar paciente: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}