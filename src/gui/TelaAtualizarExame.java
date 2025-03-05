package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
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
    private JButton btnBuscar;
    private JButton btnSalvar;
    private JButton btnLimpar;
    private JButton btnSair;
    private JLabel lblIdExame;
    private JLabel lblDescricaoAtual;
    private JLabel lblCpfAtual;
    private JLabel lblNovoCpf;
    private JLabel lblNovaDescricao;
    private JTextField txfIdExame;
    private JTextField txfDescricaoAtual;
    private JTextField txfCpfAtual;
    private JTextField txfNovoCpf;
    private JTextField txfNovaDescricao;
    private Exame exame;

    public TelaAtualizarExame(ExameService exameServ, PacienteService pacService, TelaPrincipal main) {
        this.exameServ = exameServ;
        this.pacService = pacService;
        this.main = main;
        setSize(360, 300);
        setResizable(false);
        setTitle("Tela de Atualização de Exame");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        painelForm = new JPanel(new GridLayout(5, 2, 5, 5));
        lblIdExame = new JLabel("ID do Exame:");
        txfIdExame = new JTextField(15);
        lblDescricaoAtual = new JLabel("Descrição Atual:");
        txfDescricaoAtual = new JTextField(15);
        txfDescricaoAtual.setEditable(false); // Apenas visualização
        lblCpfAtual = new JLabel("CPF Atual:");
        txfCpfAtual = new JTextField(15);
        txfCpfAtual.setEditable(false); // Apenas visualização
        lblNovoCpf = new JLabel("Novo CPF:");
        txfNovoCpf = new JTextField(15);
        lblNovaDescricao = new JLabel("Nova Descrição:");
        txfNovaDescricao = new JTextField(15);
        
        painelForm.add(lblIdExame);
        painelForm.add(txfIdExame);
        painelForm.add(lblDescricaoAtual);
        painelForm.add(txfDescricaoAtual);
        painelForm.add(lblCpfAtual);
        painelForm.add(txfCpfAtual);
        painelForm.add(lblNovoCpf);
        painelForm.add(txfNovoCpf);
        painelForm.add(lblNovaDescricao);
        painelForm.add(txfNovaDescricao);
        add(painelForm, BorderLayout.CENTER);
        
        painelBotoes = new JPanel();
        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarExame());
        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(e -> atualizarExame());
        btnSalvar.setEnabled(false); // Desativado até buscar um exame
        btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(e -> limparCampos());
        btnSair = new JButton("Sair");
        btnSair.addActionListener(e -> fecharTela());
        painelBotoes.add(btnBuscar);
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        painelBotoes.add(btnSair);
        add(painelBotoes, BorderLayout.SOUTH);
        
        setModal(true);
        setVisible(true);
    }
    
    private void fecharTela() {
        this.dispose();
    }
    
    private void limparCampos() {
        txfIdExame.setText("");
        txfDescricaoAtual.setText("");
        txfCpfAtual.setText("");
        txfNovoCpf.setText("");
        txfNovaDescricao.setText("");
        exame = null;
        btnSalvar.setEnabled(false);
    }
    
    private void buscarExame() {
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
            exame = exameServ.localizarExamePorId(id);
            txfDescricaoAtual.setText(exame.getDescricao());
            Paciente paciente = pacService.localizarPacientePorId(exame.getPacienteId());
            txfCpfAtual.setText(paciente.getCpf());
            txfNovoCpf.setText(paciente.getCpf());
            txfNovaDescricao.setText(exame.getDescricao());
            btnSalvar.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Exame encontrado com sucesso!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "ID inválido. Por favor, insira um número válido.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Erro ao buscar exame: Falha no banco de dados - " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
        } catch (ExameNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            limparCampos();
        } catch (PacienteNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, 
                "Paciente associado ao exame não encontrado: " + e.getMessage(), 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            limparCampos();
        }
    }
    
    private void atualizarExame() {
        if (exame == null) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum exame selecionado para atualização. Busque um exame primeiro.", 
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String novoCpf = txfNovoCpf.getText().trim();
        String novaDescricao = txfNovaDescricao.getText().trim();

        if (novoCpf.isEmpty() || novaDescricao.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, preencha todos os campos de novo CPF e nova descrição.", 
                "Campos vazios", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Paciente novoPaciente = pacService.localizarPacientePorCpf(novoCpf);
            if (novoPaciente == null) {
                throw new PacienteNaoEncontradoException("Paciente com CPF " + novoCpf + " não encontrado");
            }

            // Define a data atual automaticamente
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String dataAtual = sdf.format(new Date());

            exame.setDescricao(novaDescricao);
            exame.setPacienteId(novoPaciente.getId());
            exame.setDataExame(dataAtual); // Data atualizada automaticamente
            exameServ.atualizarExame(exame);
            JOptionPane.showMessageDialog(this, "Exame atualizado com sucesso!");
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
        } catch (PacienteNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, 
                "Paciente não encontrado com o novo CPF fornecido: " + e.getMessage(), 
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
}