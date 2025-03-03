package app;

import dao.ExameDAO;
import dao.PacienteDAO;
import db.MySQLDatabaseConnection;
import gui.TelaPrincipal;
import javax.swing.SwingUtilities;
import service.ExameService;
import service.PacienteService;

public class Aplicacao {

	public static void main(String[] args) {
        MySQLDatabaseConnection dbConnection = new MySQLDatabaseConnection();
        
        ExameDAO exameDAO = new ExameDAO(dbConnection);
        PacienteDAO pacienteDAO = new PacienteDAO(dbConnection);
        
        ExameService exameServ = new ExameService(exameDAO);
        PacienteService pacServ = new PacienteService(pacienteDAO);
        
        SwingUtilities.invokeLater(() 
                -> new TelaPrincipal(pacServ, exameServ).setVisible(true));
    }
}
