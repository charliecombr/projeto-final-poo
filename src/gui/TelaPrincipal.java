package gui;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import model.Exame;
import model.Paciente;
import service.ExameService;
import service.PacienteService;

public class TelaPrincipal extends JFrame{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private JMenuBar barraMenu;
    private JMenu menuPaciente;
    private JMenu menuExame;
    private JMenuItem menuItemAdicionarPaciente;
    private JMenuItem menuItemAtualizarPaciente;
    private JMenuItem menuItemExcluirPaciente;
    private JMenuItem menuItemAdicionarExame;
    private JMenuItem menuItemAtualizarExame;
    private JMenuItem menuItemExcluirExame;
    private JScrollPane scrollPanePacientes;
    private JScrollPane scrollPaneExames;
    private JTable tablePacientes;
    private JTable tableExames;
    private PacienteService pacService;
    private ExameService exameService;
    private JTabbedPane tabbed;
    
    
    public TelaPrincipal(PacienteService pacService, ExameService exameService) {
        this.pacService = pacService;
        this.exameService = exameService;
        setTitle("Gerência de Prontuários");
        setSize(480,360);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        barraMenu = new JMenuBar();
        
        menuPaciente = new JMenu("Paciente");
        barraMenu.add(menuPaciente);
        
        menuItemAdicionarPaciente = new JMenuItem("Adicionar");
        menuItemAdicionarPaciente.addActionListener(e -> new TelaCadastrarPaciente(pacService, this));
        
        menuItemAtualizarPaciente = new JMenuItem("Atualizar");
        menuItemAtualizarPaciente.addActionListener(e -> new TelaAtualizarPaciente(pacService, this));
        
        menuItemExcluirPaciente = new JMenuItem("Excluir");
        menuItemExcluirPaciente.addActionListener(e -> new TelaExcluirPaciente(pacService, this));
        
        menuPaciente.add(menuItemAdicionarPaciente);
        menuPaciente.add(menuItemAtualizarPaciente);
        menuPaciente.add(menuItemExcluirPaciente);
        
        menuExame = new JMenu("Exame");
        barraMenu.add(menuExame);
        
        menuItemAdicionarExame = new JMenuItem("Adicionar");
        menuItemAdicionarExame.addActionListener(e -> new TelaCadastrarExame(exameService,pacService, this));
        
        menuItemAtualizarExame = new JMenuItem("Atualizar");
        menuItemAtualizarExame.addActionListener(e -> new TelaAtualizarExame(exameService, pacService, this));
        
        menuItemExcluirExame = new JMenuItem("Excluir");
        menuItemExcluirExame.addActionListener(e -> new TelaExcluirExame(exameService, this));
        
        menuExame.add(menuItemAdicionarExame);
        menuExame.add(menuItemAtualizarExame);
        menuExame.add(menuItemExcluirExame);
        
        add(barraMenu, BorderLayout.NORTH);
   
        tabbed = new JTabbedPane();
        

        tablePacientes = new JTable();
        scrollPanePacientes = new JScrollPane(tablePacientes);
        tabbed.addTab("Pacientes", scrollPanePacientes);
        
        tableExames = new JTable();
        scrollPaneExames = new JScrollPane(tableExames);
        tabbed.addTab("Exames", scrollPaneExames);
        
        add(tabbed, BorderLayout.CENTER);
        
        loadTablePaciente();
		loadTableExame();
    }
    
    protected void loadTablePaciente() {
        List<Paciente> itens = pacService.getPacientes();
        tablePacientes.setModel(new TabelaPacienteModel(itens));
    }
    
	protected void loadTableExame() {
       List<Exame> exames = exameService.getExames();
       tableExames.setModel(new TabelaExameModel(exames));
    }
}