package gui;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Paciente;

public class TabelaPacienteModel extends AbstractTableModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Paciente> pacientes;
	private String[] colunas = {"ID", "CPF", "Nome"};
	
	public TabelaPacienteModel(List<Paciente> itens) {
		this.pacientes = itens;
	}
	
	@Override
	public int getRowCount() {
		return pacientes.size();
	}

	@Override
	public int getColumnCount() {
		return colunas.length;
	}
	
	@Override
	public String getColumnName(int index) {
		return colunas[index];
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Paciente p = pacientes.get(rowIndex);
		return switch(columnIndex) {
		case 0 -> p.getId();
		case 1 -> p.getCpf();
		case 2 -> p.getNome();
		default -> null;
		};
	}

}
