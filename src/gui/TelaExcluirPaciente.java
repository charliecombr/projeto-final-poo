package gui;

import java.awt.BorderLayout;
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
	private JButton btnExcluir;
	private JButton btnSair;
	private JTextField txfid;
	
   
    
    public TelaExcluirPaciente(PacienteService pacService, TelaPrincipal main) {
        this.pacService = pacService;
        this.main = main;
        setSize(360,200);
        setResizable(false);
        setTitle("Tela de Excluir Paciente");    
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        painelForm = new JPanel();    
        txfid = new JTextField(24);
        painelForm.add(new JLabel("ID: "));
        painelForm.add(txfid);    
        add(painelForm, BorderLayout.CENTER);
        painelBotoes = new JPanel();    
        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(e -> excluirPaciente());    
        btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> dispose());    
        painelBotoes.add(btnExcluir);
        painelBotoes.add(btnSair);
        add(painelBotoes, BorderLayout.SOUTH);
        setModal(true);
		setVisible(true);
    }



		private void excluirPaciente() {
        Paciente p = pacService.localizarPacientePorId(Long.parseLong(txfid.getText()));
		if (p != null) {
			pacService.excluir(p);
			JOptionPane.showMessageDialog(this, "Paciente excluido com sucesso!");
			dispose();
			main.loadTablePaciente();
		} else {
			JOptionPane.showMessageDialog(this, "Paciente não encontrado!");
		}
	}
	


}
