package gui;

import exception.ValidarCpfException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnSair;
    private JLabel lblPaciente;
    private JLabel lblCpfAtual;
    private JLabel lblNomeAtual;
    private JLabel lblNovoCpf;
    private JLabel lblNovoNome;
    private JComboBox<PacienteItem> cmbPacientes;
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
        lblPaciente = new JLabel("Selecione o Paciente:");
        cmbPacientes = new JComboBox<>();
        carregarPacientes();
        
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
        
        painelForm.add(lblPaciente);
        painelForm.add(cmbPacientes);
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
        
        // Add action listener to populate fields when a patient is selected
        cmbPacientes.addActionListener(e -> exibirDadosPaciente());
        
        painelBotoes = new JPanel();
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> atualizarPaciente());
        btnSalvar.setEnabled(false);
        btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> fecharTela());
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnSair);
        add(painelBotoes, BorderLayout.SOUTH);
        
        setModal(true);
        setVisible(true);
    }
    
    private void carregarPacientes() {
        cmbPacientes.removeAllItems();
        try {
            List<Paciente> pacientes = pacService.getPacientes();
            if (pacientes.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Não há pacientes cadastrados no sistema.", 
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
            } else {
                for (Paciente paciente : pacientes) {
                    cmbPacientes.addItem(new PacienteItem(paciente));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar lista de pacientes: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exibirDadosPaciente() {
        PacienteItem pacienteItem = (PacienteItem) cmbPacientes.getSelectedItem();
        if (pacienteItem != null) {
            paciente = pacienteItem.getPaciente();
            txfCpfAtual.setText(paciente.getCpf());
            txfNomeAtual.setText(paciente.getNome());
            txfNovoCpf.setText(paciente.getCpf());
            txfNovoNome.setText(paciente.getNome());
            btnSalvar.setEnabled(true);
        }
    }
    
    private void fecharTela() {
        this.dispose();
    }
    
    private void limparCampos() {
        if (cmbPacientes.getItemCount() > 0) {
            cmbPacientes.setSelectedIndex(0);
        }
        txfCpfAtual.setText("");
        txfNomeAtual.setText("");
        txfNovoCpf.setText("");
        txfNovoNome.setText("");
        paciente = null;
        btnSalvar.setEnabled(false);
    }
    
    private void atualizarPaciente() {
    if (paciente == null) {
        JOptionPane.showMessageDialog(this, 
            "Nenhum paciente selecionado para atualização.", 
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
        // Remover formatação do CPF e verificar se contém apenas dígitos
        String cpfSemFormatacao = novoCpf.replaceAll("[.-]", "");

        for (int i = 0; i < cpfSemFormatacao.length(); i++) {
            if (!Character.isDigit(cpfSemFormatacao.charAt(i))) {
                throw new InputMismatchException("CPF deve conter apenas números");
            }
        }
        
        // Verificar se o CPF mudou e se ele já existe para outro paciente
        if (!cpfSemFormatacao.equals(paciente.getCpf())) {
            try {
                Paciente pacienteExistente = pacService.localizarPacientePorCpf(cpfSemFormatacao);
                // Se encontrou um paciente com esse CPF e não é o paciente atual, lança exceção
                if (pacienteExistente != null && !pacienteExistente.getId().equals(paciente.getId())) {
                    throw new ValidarCpfException("CPF já cadastrado para outro paciente");
                }
            } catch (exception.PacienteNaoEncontradoException e) {
                // CPF não encontrado, o que significa que podemos usá-lo
                // Podemos continuar com a atualização
            }
        }
        
        // Atualizar os dados do paciente
        paciente.setCpf(cpfSemFormatacao);
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
    } catch (ValidarCpfException e) {
        JOptionPane.showMessageDialog(this, 
            "Erro de validação de CPF: " + e.getMessage(), 
            "Erro", 
            JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, 
            "Erro ao atualizar paciente: " + e.getMessage(), 
            "Erro", 
            JOptionPane.ERROR_MESSAGE);
    } catch (InputMismatchException e) {
        JOptionPane.showMessageDialog(this,
            "Erro, CPF não pode conter letras: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, 
            "Erro inesperado ao atualizar paciente: " + e.getMessage(), 
            "Erro", 
            JOptionPane.ERROR_MESSAGE);
    }
}
    
    private class PacienteItem {
        private Paciente paciente;
        
        public PacienteItem(Paciente paciente) {
            this.paciente = paciente;
        }
        
        public Paciente getPaciente() {
            return paciente;
        }
        
        @Override
        public String toString() {
            return paciente.getNome() + " (ID: " + paciente.getId() + ", CPF: " + paciente.getCpf() + ")";
        }
    }
}