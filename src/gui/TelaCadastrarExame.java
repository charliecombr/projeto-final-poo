package gui;

import java.awt.BorderLayout;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
    private JLabel lblPaciente;
    private JTextField txfDescricao;
    private JComboBox<PacienteItem> cmbPacientes;

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
        txfDescricao = new JTextField(17);
        lblPaciente = new JLabel("Paciente:");
        cmbPacientes = new JComboBox<>();
        
        carregarPacientes();
        
        painelForm.add(lblDescricao);
        painelForm.add(txfDescricao);
        painelForm.add(lblPaciente);
        painelForm.add(cmbPacientes);
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
    
    private void fecharTela() {
        this.dispose();
    }
    
    private void limparCampos() {
        txfDescricao.setText("");
        if (cmbPacientes.getItemCount() > 0) {
            cmbPacientes.setSelectedIndex(0);
        }
    }

    private void addExame() {
        String descricao = txfDescricao.getText().trim();
        PacienteItem pacienteItem = (PacienteItem) cmbPacientes.getSelectedItem();

        if (descricao.isEmpty() || pacienteItem == null) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos.", 
                "Campos vazios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Paciente paciente = pacienteItem.getPaciente();

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataAtual = sdf.format(new Date());

            if (exameServ.verificarExameExistente(paciente.getId(), descricao, new Date())) {
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
    
    // Classe auxiliar para representar pacientes no ComboBox
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
            return paciente.getNome() + " (CPF: " + paciente.getCpf() + ")";
        }
    }
}