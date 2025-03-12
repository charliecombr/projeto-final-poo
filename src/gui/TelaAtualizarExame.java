package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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

import exception.ExameNaoEncontradoException;
import exception.PacienteNaoEncontradoException;
import model.Exame;
import model.Paciente;
import service.ExameService;
import service.PacienteService;

public class TelaAtualizarExame extends JDialog {

    private static final long serialVersionUID = 1L;
    
    private ExameService exameServ;
    private PacienteService pacService;
    private TelaPrincipal main;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnSair;
    private JLabel lblExame;
    private JLabel lblDescricaoAtual;
    private JLabel lblPacienteAtual;
    private JLabel lblNovoPaciente;
    private JLabel lblNovaDescricao;
    private JComboBox<ExameItem> cmbExames;
    private JTextField txfDescricaoAtual;
    private JTextField txfPacienteAtual;
    private JComboBox<PacienteItem> cmbPacientes;
    private JTextField txfNovaDescricao;
    private Exame exame;

    public TelaAtualizarExame(ExameService exameServ, PacienteService pacService, TelaPrincipal main) {
        this.exameServ = exameServ;
        this.pacService = pacService;
        this.main = main;
        setSize(400, 300);
        setResizable(false);
        setTitle("Tela de Atualização de Exame");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        painelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        lblExame = new JLabel("Selecione o Exame:");
        cmbExames = new JComboBox<>();
        carregarExames();
        
        lblDescricaoAtual = new JLabel("Descrição Atual:");
        txfDescricaoAtual = new JTextField(15);
        txfDescricaoAtual.setEditable(false); // Apenas visualização
        
        lblPacienteAtual = new JLabel("Paciente Atual:");
        txfPacienteAtual = new JTextField(15);
        txfPacienteAtual.setEditable(false); // Apenas visualização
        
        lblNovoPaciente = new JLabel("Novo Paciente:");
        cmbPacientes = new JComboBox<>();
        carregarPacientes();
        
        lblNovaDescricao = new JLabel("Nova Descrição:");
        txfNovaDescricao = new JTextField(15);
        
        painelForm.add(lblExame);
        painelForm.add(cmbExames);
        painelForm.add(lblDescricaoAtual);
        painelForm.add(txfDescricaoAtual);
        painelForm.add(lblPacienteAtual);
        painelForm.add(txfPacienteAtual);
        painelForm.add(lblNovoPaciente);
        painelForm.add(cmbPacientes);
        painelForm.add(lblNovaDescricao);
        painelForm.add(txfNovaDescricao);
        add(painelForm, BorderLayout.CENTER);
        
        // Add action listener to populate fields when an exam is selected
        cmbExames.addActionListener(e -> exibirDadosExame());
        
        painelBotoes = new JPanel();
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> atualizarExame());
        btnSalvar.setEnabled(false); // Desativado até selecionar um exame
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
    
    private void carregarExames() {
        cmbExames.removeAllItems();
        try {
            List<Exame> exames = exameServ.getExames();
            if (exames.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Não há exames cadastrados no sistema.", 
                    "Aviso", 
                    JOptionPane.WARNING_MESSAGE);
            } else {
                for (Exame exame : exames) {
                    cmbExames.addItem(new ExameItem(exame));
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao carregar lista de exames: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
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
    
    private void exibirDadosExame() {
        ExameItem exameItem = (ExameItem) cmbExames.getSelectedItem();
        if (exameItem != null) {
            exame = exameItem.getExame();
            txfDescricaoAtual.setText(exame.getDescricao());
            txfNovaDescricao.setText(exame.getDescricao());
            
            try {
                Paciente pacienteAtual = pacService.localizarPacientePorId(exame.getPacienteId());
                txfPacienteAtual.setText(pacienteAtual.getNome() + " (CPF: " + pacienteAtual.getCpf() + ")");
                
                // Selecionar o paciente atual no combo de novos pacientes
                for (int i = 0; i < cmbPacientes.getItemCount(); i++) {
                    PacienteItem item = cmbPacientes.getItemAt(i);
                    if (item.getPaciente().getId().equals(pacienteAtual.getId())) {
                        cmbPacientes.setSelectedIndex(i);
                        break;
                    }
                }
                
                btnSalvar.setEnabled(true);
            } catch (SQLException | PacienteNaoEncontradoException e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao carregar dados do paciente do exame: " + e.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void fecharTela() {
        this.dispose();
    }
    
    private void limparCampos() {
        if (cmbExames.getItemCount() > 0) {
            cmbExames.setSelectedIndex(0);
        }
        txfDescricaoAtual.setText("");
        txfPacienteAtual.setText("");
        txfNovaDescricao.setText("");
        if (cmbPacientes.getItemCount() > 0) {
            cmbPacientes.setSelectedIndex(0);
        }
        exame = null;
        btnSalvar.setEnabled(false);
    }
    
    private void atualizarExame() {
        if (exame == null) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum exame selecionado para atualização.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        PacienteItem pacienteItem = (PacienteItem) cmbPacientes.getSelectedItem();
        String novaDescricao = txfNovaDescricao.getText().trim();

        if (pacienteItem == null || novaDescricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos necessários.", 
                "Campos vazios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Paciente novoPaciente = pacienteItem.getPaciente();

            // Define a data atual automaticamente
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataAtual = sdf.format(new Date());

            exame.setDescricao(novaDescricao);
            exame.setPacienteId(novoPaciente.getId());
            exame.setDataExame(dataAtual); // Data atualizada automaticamente
            exameServ.atualizarExame(exame);
            JOptionPane.showMessageDialog(this, "Exame atualizado com sucesso!");
            
            // Recarregue os combos após a atualização
            carregarExames();
            limparCampos();
            
            if (main != null) {
                main.loadTableExame();
            }
            fecharTela();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar exame: Falha no banco de dados - " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar exame: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro inesperado ao atualizar exame: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Classe auxiliar para representar exames no ComboBox
    private class ExameItem {
        private Exame exame;
        
        public ExameItem(Exame exame) {
            this.exame = exame;
        }
        
        public Exame getExame() {
            return exame;
        }
        
        @Override
        public String toString() {
            try {
                Paciente pac = pacService.localizarPacientePorId(exame.getPacienteId());
                return "ID: " + exame.getId() + " - " + exame.getDescricao() + 
                       " (" + exame.getDataExame() + ") - Paciente: " + pac.getNome();
            } catch (Exception e) {
                return "ID: " + exame.getId() + " - " + exame.getDescricao() + 
                       " (" + exame.getDataExame() + ")";
            }
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