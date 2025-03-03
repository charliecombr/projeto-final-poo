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

import model.Paciente;
import service.PacienteService;

public class TelaAtualizarPaciente extends JDialog {
    
    private static final long serialVersionUID = 1L;

    private PacienteService pacService;
    private TelaPrincipal main;
    private JPanel painelPesquisa;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JButton btnBuscar;
    private JButton btnAtualizar;
    private JButton btnSair;
    private JTextField txfid;
    private JTextField txfcpf;
    private JTextField txfnome;
    private JLabel lblCpfAtual;
    private JLabel lblNomeAtual;
    
    public TelaAtualizarPaciente(PacienteService pacService, TelaPrincipal main) {
        this.pacService = pacService;
        this.main = main;
        setSize(400, 300);
        setResizable(false);
        setTitle("Tela de Atualizar Paciente");    
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        painelPesquisa = new JPanel();
        painelPesquisa.setLayout(new BoxLayout(painelPesquisa, BoxLayout.X_AXIS));
        painelPesquisa.add(new JLabel("ID do Paciente: "));
        painelPesquisa.add(Box.createRigidArea(new Dimension(5, 0)));
        txfid = new JTextField(10);
        txfid.setMaximumSize(new Dimension(100, 25));
        painelPesquisa.add(txfid);
        painelPesquisa.add(Box.createRigidArea(new Dimension(5, 0)));
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarPaciente());
        painelPesquisa.add(btnBuscar);
        painelPesquisa.add(Box.createHorizontalGlue());
        add(painelPesquisa, BorderLayout.NORTH);
        
        painelForm = new JPanel();
        painelForm.setLayout(new BoxLayout(painelForm, BoxLayout.Y_AXIS));
        
        JPanel panelCpfAtual = new JPanel();
        panelCpfAtual.setLayout(new BoxLayout(panelCpfAtual, BoxLayout.X_AXIS));
        panelCpfAtual.add(new JLabel("CPF Atual: "));
        panelCpfAtual.add(Box.createRigidArea(new Dimension(10, 0)));
        lblCpfAtual = new JLabel("");
        panelCpfAtual.add(lblCpfAtual);
        panelCpfAtual.add(Box.createHorizontalGlue());
        painelForm.add(panelCpfAtual);
        painelForm.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel panelNomeAtual = new JPanel();
        panelNomeAtual.setLayout(new BoxLayout(panelNomeAtual, BoxLayout.X_AXIS));
        panelNomeAtual.add(new JLabel("Nome Atual: "));
        panelNomeAtual.add(Box.createRigidArea(new Dimension(10, 0)));
        lblNomeAtual = new JLabel("");
        panelNomeAtual.add(lblNomeAtual);
        panelNomeAtual.add(Box.createHorizontalGlue());
        painelForm.add(panelNomeAtual);
        painelForm.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel panelNovoCpf = new JPanel();
        panelNovoCpf.setLayout(new BoxLayout(panelNovoCpf, BoxLayout.X_AXIS));
        panelNovoCpf.add(new JLabel("Novo CPF: "));
        panelNovoCpf.add(Box.createRigidArea(new Dimension(10, 0)));
        txfcpf = new JTextField(20);
        txfcpf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        panelNovoCpf.add(txfcpf);
        painelForm.add(panelNovoCpf);
        painelForm.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel panelNovoNome = new JPanel();
        panelNovoNome.setLayout(new BoxLayout(panelNovoNome, BoxLayout.X_AXIS));
        panelNovoNome.add(new JLabel("Novo Nome: "));
        panelNovoNome.add(Box.createRigidArea(new Dimension(10, 0)));
        txfnome = new JTextField(20);
        txfnome.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        panelNovoNome.add(txfnome);
        painelForm.add(panelNovoNome);
        
        txfcpf.setEnabled(false);
        txfnome.setEnabled(false);
        
        add(painelForm, BorderLayout.CENTER);
        
        painelBotoes = new JPanel();
        painelBotoes.setLayout(new BoxLayout(painelBotoes, BoxLayout.X_AXIS));
        painelBotoes.add(Box.createHorizontalGlue());
        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.addActionListener(e -> atualizarPaciente());
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
    
    private void buscarPaciente() {
        try {
            long id = Long.parseLong(txfid.getText());
            Paciente paciente = pacService.localizarPacientePorId(id);
            
            if (paciente != null) {
                lblCpfAtual.setText(paciente.getCpf());
                lblNomeAtual.setText(paciente.getNome());
                
                txfcpf.setText(paciente.getCpf());
                txfnome.setText(paciente.getNome());
                
                txfcpf.setEnabled(true);
                txfnome.setEnabled(true);
                btnAtualizar.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Paciente não encontrado com o ID: " + id,
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "ID inválido. Por favor, insira um número válido.",
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar paciente: " + e.getMessage(),
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void atualizarPaciente() {
    	String cpf = txfcpf.getText().trim();

		Paciente pacienteExistente = pacService.localizarPacientePorCpf(cpf);
		if (pacienteExistente != null) {
			JOptionPane.showMessageDialog(this, 
				"Já existe um paciente cadastrado com o CPF: " + cpf, 
				"CPF duplicado", 
				JOptionPane.ERROR_MESSAGE);
			return;
		}
    	try {
            long id = Long.parseLong(txfid.getText());
            Paciente p = pacService.localizarPacientePorId(id);
            
            if (p != null) {
                p.setCpf(txfcpf.getText());
                p.setNome(txfnome.getText());
                
                pacService.atualizarPaciente(p);
                JOptionPane.showMessageDialog(this, "Paciente atualizado com sucesso!");
                dispose();
                main.loadTablePaciente();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao atualizar paciente: " + e.getMessage(),
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}