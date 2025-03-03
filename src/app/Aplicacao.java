package app;

import javax.swing.SwingUtilities;

import dao.PacienteDAO;
import db.MySQLDatabaseConnection;
import gui.TelaPrincipal;
import service.PacienteService;

public class Aplicacao {

	public static void main(String[] args) {
		
		PacienteService pacServ = 
				new PacienteService(new PacienteDAO(new MySQLDatabaseConnection()));
		
		SwingUtilities.invokeLater(() 
				-> new TelaPrincipal(pacServ).setVisible(true));
	}
}
