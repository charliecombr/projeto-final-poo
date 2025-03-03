package gui;

import java.awt.BorderLayout;
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
import java.util.Date;

public class TelaCadastrarExame extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    private ExameService exaService;
    private PacienteService pacService;
    private TelaPrincipal main;
    private JPanel painelForm;
    private JPanel painelBotoes;
    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnSair;
    private JLabel lblDesc;
    private JTextField txfDesc;
    private JLabel lblCpf;
    private JTextField txfCpf;

    public TelaCadastrarExame(ExameService exaService, PacienteService pacService, TelaPrincipal main) {
        this.exaService = exaService;
        this.pacService = pacService;
        this.main = main;
        setSize(360, 250);
        setResizable(false);
        setTitle("Tela de Cadastro de Exame");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        painelForm = new JPanel();
        
        lblDesc = new JLabel("Descrição do Exame: ");
        txfDesc = new JTextField(25);
        painelForm.add(lblDesc);
        painelForm.add(txfDesc);
        
        lblCpf = new JLabel("CPF do Paciente:      ");
        txfCpf = new JTextField(25);
        painelForm.add(lblCpf);
        painelForm.add(txfCpf);
        
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
    
    private void fecharTela() {
        this.hide();
    }
    
    private void limparCampos() {
        txfDesc.setText("");
        txfCpf.setText("");
    }

    private void addExame() {
        String desc = txfDesc.getText().trim();
        String cpf = txfCpf.getText().trim();
        
        if (desc.isEmpty() || cpf.isEmpty()) {
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
                "Paciente não encontrado", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String dataStr = sdf.format(new Date());
        
        Exame novoExame = new Exame(0L, desc, dataStr, paciente.getId());
        
        exaService.adicionarExame(novoExame);
        
        JOptionPane.showMessageDialog(this, "Exame cadastrado com sucesso!");
        
        limparCampos();
        
         // main.loadTableExame();
    }
}