package gui;

import java.awt.BorderLayout;

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

    private ExameService exaService;
	private TelaPrincipal main;
	private JPanel painelForm;
	private JPanel painelBotoes;
	private JButton btnExcluir;
	private JButton btnSair;
	private JTextField txfid;
	
   
    
    public TelaExcluirExame(ExameService exaService ,TelaPrincipal main) {

        this.exaService = exaService;
        this.main = main;
        setSize(360,200);
        setResizable(false);
        setTitle("Tela de Excluir Exame");    
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        painelForm = new JPanel();    
        txfid = new JTextField(24);
        painelForm.add(new JLabel("ID do Exame: "));
        painelForm.add(txfid);    
        add(painelForm, BorderLayout.CENTER);
        painelBotoes = new JPanel();    
        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirExame());    
        btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> dispose());    
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnSair);
        add(painelBotoes, BorderLayout.SOUTH);
        setModal(true);
		setVisible(true);
    }



		private void excluirExame() {
        Exame e = exaService.localizarExamePorId(Long.parseLong(txfid.getText()));
		if (e != null) {
			exaService.excluirExame(e);
			JOptionPane.showMessageDialog(this, "Exame excluido com sucesso!");
			dispose();
			main.loadTableExame();
		} else {
			JOptionPane.showMessageDialog(this, "Exame não encontrado!");
		}
	}
}
