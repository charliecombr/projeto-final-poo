package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
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

public class TelaAtualizarExame extends JDialog {
    
    private static final long serialVersionUID = 1L;

    private ExameService exaService;
    private PacienteService pacService;
    private TelaPrincipal main;
    
    private JPanel painelPesquisa;
    private JPanel painelForm;
    private JPanel painelBotoes;
    
    private JButton btnBuscar;
    private JButton btnAtualizar;
    private JButton btnSair;
    
    private JTextField txfId;
    private JTextField txfDesc;
    private JTextField txfCpf;
    
    private JLabel lblDescricaoAtual;
    private JLabel lblDataAtual;
    private JLabel lblPacienteAtual;
    
    public TelaAtualizarExame(ExameService exaService, PacienteService pacService, TelaPrincipal main) {
        this.exaService = exaService;
        this.pacService = pacService;
        this.main = main;
        
        setSize(400, 350);
        setResizable(false);
        setTitle("Atualizar Exame");    
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        painelPesquisa = new JPanel();
        painelPesquisa.setLayout(new BoxLayout(painelPesquisa, BoxLayout.X_AXIS));
        painelPesquisa.add(new JLabel("ID do Exame: "));
        painelPesquisa.add(Box.createRigidArea(new Dimension(5, 0)));
        
        txfId = new JTextField(10);
        txfId.setMaximumSize(new Dimension(100, 25));
        painelPesquisa.add(txfId);
        painelPesquisa.add(Box.createRigidArea(new Dimension(5, 0)));
        
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarExame(txfId, txfDesc, txfCpf));
        painelPesquisa.add(btnBuscar);
        painelPesquisa.add(Box.createHorizontalGlue());
        add(painelPesquisa, BorderLayout.NORTH);
        
        painelForm = new JPanel();
        painelForm.setLayout(new BoxLayout(painelForm, BoxLayout.Y_AXIS));
        
        JPanel panelDescAtual = new JPanel();
        panelDescAtual.setLayout(new BoxLayout(panelDescAtual, BoxLayout.X_AXIS));
        panelDescAtual.add(new JLabel("Descrição Atual: "));
        panelDescAtual.add(Box.createRigidArea(new Dimension(10, 0)));
        lblDescricaoAtual = new JLabel("");
        panelDescAtual.add(lblDescricaoAtual);
        panelDescAtual.add(Box.createHorizontalGlue());
        painelForm.add(panelDescAtual);
        painelForm.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel panelDataAtual = new JPanel();
        panelDataAtual.setLayout(new BoxLayout(panelDataAtual, BoxLayout.X_AXIS));
        panelDataAtual.add(new JLabel("Data Atual: "));
        panelDataAtual.add(Box.createRigidArea(new Dimension(10, 0)));
        lblDataAtual = new JLabel("");
        panelDataAtual.add(lblDataAtual);
        panelDataAtual.add(Box.createHorizontalGlue());
        painelForm.add(panelDataAtual);
        painelForm.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel panelPacienteAtual = new JPanel();
        panelPacienteAtual.setLayout(new BoxLayout(panelPacienteAtual, BoxLayout.X_AXIS));
        panelPacienteAtual.add(new JLabel("Paciente Atual: "));
        panelPacienteAtual.add(Box.createRigidArea(new Dimension(10, 0)));
        lblPacienteAtual = new JLabel("");
        panelPacienteAtual.add(lblPacienteAtual);
        panelPacienteAtual.add(Box.createHorizontalGlue());
        painelForm.add(panelPacienteAtual);
        painelForm.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JPanel panelDesc = new JPanel();
        panelDesc.setLayout(new BoxLayout(panelDesc, BoxLayout.X_AXIS));
        panelDesc.add(new JLabel("Nova Descrição: "));
        panelDesc.add(Box.createRigidArea(new Dimension(10, 0)));
        txfDesc = new JTextField(20);
        txfDesc.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        txfDesc.setEnabled(false);
        panelDesc.add(txfDesc);
        painelForm.add(panelDesc);
        painelForm.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel panelCPF = new JPanel();
        panelCPF.setLayout(new BoxLayout(panelCPF, BoxLayout.X_AXIS));
        panelCPF.add(new JLabel("CPF do Paciente: "));
        panelCPF.add(Box.createRigidArea(new Dimension(10, 0)));
        txfCpf = new JTextField(20);
        txfCpf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        txfCpf.setEnabled(false);
        panelCPF.add(txfCpf);
        painelForm.add(panelCPF);
        
        add(painelForm, BorderLayout.CENTER);
        
        painelBotoes = new JPanel();
        painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.X_AXIS));
        painelBotoes.add(Box.createHorizontalGlue());
        
        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizarExame());
        btnAtualizar.setEnabled(false);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(Box.createRigidArea(new Dimension(10, 0)));
        
        btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> dispose());
        painelBotoes.add(btnSair);
        painelBotoes.add(Box.createHorizontalGlue());
        
        add(painelBotoes, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
        setVisible(true);
    }


    private void buscarExame(JTextField txfId, JTextField txfDesc, JTextField txfCpf) {
    try {
        if (txfId.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, informe o ID do exame.",
                "Campo vazio", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        long id = Long.parseLong(txfId.getText().trim());
        Exame exame = exaService.localizarExamePorId(id);
            
        if (exame != null) {
            lblDescricaoAtual.setText(exame.getDescricao());
            lblDataAtual.setText(exame.getDataExame());
            
            Paciente paciente = pacService.localizarPacientePorId(exame.getPacienteId());
            if (paciente != null) {
                lblPacienteAtual.setText(paciente.getNome() + " (CPF: " + paciente.getCpf() + ")");
            } else {
                lblPacienteAtual.setText("Paciente ID: " + exame.getPacienteId() + " (não encontrado)");
            }
            
            txfDesc.setText(exame.getDescricao());
            
            if (paciente != null) {
                txfCpf.setText(paciente.getCpf());
            }
            
            txfDesc.setEnabled(true);
            txfCpf.setEnabled(true);
            btnAtualizar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Exame não encontrado com o ID: " + id,
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            
            limparCampos();
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, 
            "ID inválido. Por favor, insira um número válido.",
            "Erro", 
            JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Erro ao buscar exame: " + e.getMessage(),
            "Erro", 
            JOptionPane.ERROR_MESSAGE);
    }
}

private void limparCampos() {
    lblDescricaoAtual.setText("");
    lblDataAtual.setText("");
    lblPacienteAtual.setText("");
    
    txfDesc.setText("");
    txfCpf.setText("");
    
    txfDesc.setEnabled(false);
    txfCpf.setEnabled(false);
    btnAtualizar.setEnabled(false);
}

private void atualizarExame() {
    try {
        long id = Long.parseLong(txfId.getText().trim());
        
        Exame exame = exaService.localizarExamePorId(id);
        if (exame == null) {
            JOptionPane.showMessageDialog(this,
                "Exame não encontrado. Tente buscar novamente.",
                "Erro",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String descricao = txfDesc.getText().trim();
        String cpf = txfCpf.getText().trim();
        
        if (descricao.isEmpty() || cpf.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor, preencha todos os campos.",
                "Campos vazios",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Paciente paciente = pacService.localizarPacientePorCpf(cpf);
        if (paciente == null) {
            JOptionPane.showMessageDialog(this,
                "Paciente com CPF " + cpf + " não encontrado.",
                "Paciente inválido",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        exame.setDescricao(descricao);
        exame.setPacienteId(paciente.getId());
        
        exaService.atualizarExame(exame);
        
        JOptionPane.showMessageDialog(this, 
            "Exame atualizado com sucesso!",
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE);
        
        dispose();
        if (main != null) {
            main.loadTableExame();
        }
        
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
            "ID inválido. Por favor, insira um número válido.",
            "Erro",
            JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Erro ao atualizar exame: " + e.getMessage(),
            "Erro",
            JOptionPane.ERROR_MESSAGE);
    }
}

}
