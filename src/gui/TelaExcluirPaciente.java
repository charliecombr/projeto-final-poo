package gui;

import exception.PacienteNaoEncontradoException;
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

public class TelaExcluirPaciente extends JDialog {

    private static final long serialVersionUID = 1L;
    
    private PacienteService pacService;
    private TelaPrincipal main;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JButton btnBuscar;
    private JButton btnExcluir;
    private JButton btnSair;
    private JLabel lblIdPaciente;
    private JLabel lblCpf;
    private JLabel lblNome;
    private JTextField txfIdPaciente;
    private JTextField txfCpf;
    private JTextField txfNome;
    private Paciente paciente;

    public TelaExcluirPaciente(PacienteService pacService, TelaPrincipal main) {
        this.pacService = pacService;
        this.main = main;
        setSize(400, 200);
        setResizable(false);
        setTitle("Tela de Exclusão de Paciente");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        painelForm = new JPanel();
        lblIdPaciente = new JLabel("ID do Paciente:");
        txfIdPaciente = new JTextField(26);
        lblCpf = new JLabel("CPF:");
        txfCpf = new JTextField(31);
        txfCpf.setEditable(false); // Campo apenas para visualização
        lblNome = new JLabel("Nome:");
        txfNome = new JTextField(30);
        txfNome.setEditable(false); // Campo apenas para visualização
        
        painelForm.add(lblIdPaciente);
        painelForm.add(txfIdPaciente);
        painelForm.add(lblCpf);
        painelForm.add(txfCpf);
        painelForm.add(lblNome);
        painelForm.add(txfNome);
        add(painelForm, BorderLayout.CENTER);
        
        painelBotoes = new JPanel();
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPaciente());
        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirPaciente());
        btnExcluir.setEnabled(false); // Desativado até buscar um paciente
        btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> fecharTela());
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnSair);
        add(painelBotoes, BorderLayout.SOUTH);
        
        setModal(true);
        setVisible(true);
    }
    
    private void fecharTela() {
        this.dispose();
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
            txfCpf.setText(paciente.getCpf());
            txfNome.setText(paciente.getNome());
            btnExcluir.setEnabled(true);
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
        } catch (PacienteNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            limparCampos();
        }
    }
    
    private void excluirPaciente() {
        if (paciente == null) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum paciente selecionado para exclusão. Busque um paciente primeiro.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int escolha = JOptionPane.showConfirmDialog(this, 
            "Deseja excluir o paciente " + paciente.getNome() + "?", 
            "Confirmação", 
            JOptionPane.YES_NO_OPTION);
        if (escolha == JOptionPane.YES_OPTION) {
            try {
                pacService.deletarPaciente(paciente);
                JOptionPane.showMessageDialog(this, "Paciente excluído com sucesso!");
                if (main != null) {
                    main.loadTablePaciente();
                }
                fecharTela();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir paciente: Falha no banco de dados - " + e.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao excluir paciente: " + e.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Erro inesperado ao excluir paciente: " + e.getMessage(), 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void limparCampos() {
        txfIdPaciente.setText("");
        txfCpf.setText("");
        txfNome.setText("");
        paciente = null;
        btnExcluir.setEnabled(false);
    }
}