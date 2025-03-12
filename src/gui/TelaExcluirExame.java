package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import model.Exame;
import service.ExameService;

public class TelaExcluirExame extends JDialog {

    private static final long serialVersionUID = 1L;
    
    private ExameService exameServ;
    private TelaPrincipal main;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JPanel painelResultado;
    private JButton btnExcluir;
    private JButton btnSair;
    private JButton btnBuscar;
    private JLabel lblIdExame;
    private JTextField txfIdExame;
    private JLabel lblDescricao;
    private JLabel lblDescricaoValor;
    private JLabel lblPaciente;
    private JLabel lblPacienteValor;
    private Exame exameAtual;

    public TelaExcluirExame(ExameService exameServ, TelaPrincipal main) {
        this.exameServ = exameServ;
        this.main = main;
        setSize(460, 300);
        setResizable(false);
        setTitle("Tela de Exclusão de Exame");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Painel de busca
        painelForm = new JPanel(new FlowLayout(FlowLayout.CENTER));
        painelForm.setBorder(new EmptyBorder(20, 10, 10, 10));
        lblIdExame = new JLabel("ID do Exame:");
        txfIdExame = new JTextField(10);
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarExame());
        painelForm.add(lblIdExame);
        painelForm.add(txfIdExame);
        painelForm.add(btnBuscar);
        add(painelForm, BorderLayout.NORTH);
        
        // Painel de resultado
        painelResultado = new JPanel(new GridLayout(2, 2, 10, 10));
        painelResultado.setBorder(new EmptyBorder(10, 20, 20, 20));
        lblDescricao = new JLabel("Descrição:");
        lblDescricaoValor = new JLabel("-");
        lblPaciente = new JLabel("ID do Paciente:");
        lblPacienteValor = new JLabel("-");
        painelResultado.add(lblDescricao);
        painelResultado.add(lblDescricaoValor);
        painelResultado.add(lblPaciente);
        painelResultado.add(lblPacienteValor);
        add(painelResultado, BorderLayout.CENTER);
        
        // Painel de botões
        painelBotoes = new JPanel();
        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirExame());
        btnExcluir.setEnabled(false);
        btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> fecharTela());
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnSair);
        add(painelBotoes, BorderLayout.SOUTH);
        
        setModal(true);
        setVisible(true);
    }
    
    private void fecharTela() {
        this.dispose();
    }
    
    private void buscarExame() {
        String idStr = txfIdExame.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, insira o ID do exame.", 
                "Campo vazio", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Long id = Long.parseLong(idStr);
            exameAtual = exameServ.localizarExamePorId(id);
            
            lblDescricaoValor.setText(exameAtual.getDescricao());
            
            lblPacienteValor.setText(String.valueOf(exameAtual.getPacienteId()));
           
            btnExcluir.setEnabled(true);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "ID inválido. Por favor, insira um número válido.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            limparCamposResultado();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar exame: Falha no banco de dados - " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            limparCamposResultado();
        } catch (exception.ExameNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            limparCamposResultado();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro inesperado ao buscar exame: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            limparCamposResultado();
        }
    }
    
    private void limparCamposResultado() {
        lblDescricaoValor.setText("-");
        lblPacienteValor.setText("-");
        btnExcluir.setEnabled(false);
        exameAtual = null;
    }
    
    private void excluirExame() {
        if (exameAtual == null) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, busque um exame primeiro.", 
                "Nenhum exame selecionado", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int escolha = JOptionPane.showConfirmDialog(this, 
                "Deseja excluir o exame com ID " + exameAtual.getId() + "?", 
                "Confirmação", 
                JOptionPane.YES_NO_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                exameServ.removerExame(exameAtual.getId());
                JOptionPane.showMessageDialog(this, "Exame excluído com sucesso!");
                txfIdExame.setText(""); // Limpa o campo após exclusão
                limparCamposResultado();
                if (main != null) {
                    main.loadTableExame();
                }
                fecharTela();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao excluir exame: Falha no banco de dados - " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao excluir exame: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erro inesperado ao excluir exame: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}