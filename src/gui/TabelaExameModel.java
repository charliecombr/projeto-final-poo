package gui;

import java.util.List;
import javax.swing.table.AbstractTableModel;

import model.Exame;

public class TabelaExameModel extends AbstractTableModel {
    
    private static final long serialVersionUID = 1L;
    private List<Exame> exames;
    private String[] colunas = {"ID", "Descrição", "Data", "Paciente"};
    
    public TabelaExameModel(List<Exame> itens) {
        this.exames = itens;
    }
    
    @Override
    public int getRowCount() {
        return exames.size();
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
        Exame e = exames.get(rowIndex);
        try {
            switch(columnIndex) {
                case 0: return e.getId();
                case 1: return e.getDescricao();
                case 2: return e.getDataExame();
                case 3: return e.getPacienteId();
                default: return null;
            }
        } catch (Exception ex) {
            System.err.println("Erro ao acessar propriedade do exame: " + ex.getMessage());
            return "ERRO";
        }
    }
    
    public Exame getExame(int rowIndex) {
        return exames.get(rowIndex);
    }
}