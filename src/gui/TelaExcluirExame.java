package gui;

import java.awt.BorderLayout;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.Exame;
import service.ExameService;

public class TelaExcluirExame extends JDialog {

    private static final long serialVersionUID = 1L;
    
    private ExameService exameServ;
    private TelaPrincipal main;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JButton btnExcluir;
    private JButton btnSair;
    private JLabel lblIdExame;
    private JTextField txfIdExame;

    public TelaExcluirExame(ExameService exameServ, TelaPrincipal main) {
        this.exameServ = exameServ;
        this.main = main;
        setSize(360,200);
        setResizable(false);
        setTitle("Tela de Exclusão de Exame");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        painelForm = new JPanel();
        lblIdExame = new JLabel("ID do Exame:");
        txfIdExame = new JTextField(15);
        painelForm.add(lblIdExame);
        painelForm.add(txfIdExame);
        add(painelForm, BorderLayout.CENTER);
        
        painelBotoes = new JPanel();
        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirExame());
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
    
    private void excluirExame() {
        String idStr = txfIdExame.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, insira o ID do exame.", 
                "Campos vazios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Long id = Long.parseLong(idStr);
            Exame exame = exameServ.localizarExamePorId(id); // Busca para confirmar existência
            int escolha = JOptionPane.showConfirmDialog(this, 
                "Deseja excluir o exame com ID " + id + "?", 
                "Confirmação", 
                JOptionPane.YES_NO_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                exameServ.excluirExame(exame);
                JOptionPane.showMessageDialog(this, "Exame excluído com sucesso!");
                txfIdExame.setText(""); // Limpa o campo após exclusão
                if (main != null) {
                    main.loadTableExame();
                }
                fecharTela();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "ID inválido. Por favor, insira um número válido.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao excluir exame: Falha no banco de dados - " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (exception.ExameNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
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