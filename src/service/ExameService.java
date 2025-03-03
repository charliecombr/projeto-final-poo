package service;

import dao.ExameDAO;
import java.util.Date;
import java.util.List;
import model.Exame;

public class ExameService {
    private ExameDAO daoExame;
    
    public ExameService(ExameDAO daoExame) {
        this.daoExame = daoExame;
    }
    
    public void adicionarExame(Exame e) {
        daoExame.add(e);
    }

    public void atualizarExame(Exame exame) {
        daoExame.update(exame);
    }
    
    public boolean removerExame(long id) {
        return daoExame.excluirPorId(id);
    }
    
    public Exame localizarExamePorId(long id) {
        return daoExame.findByID(id);
    }
    
    public List<Exame> getExames() {
        return daoExame.getAll();
    }
    
    public List<Exame> getExamesPorPaciente(long pacienteId) {
        return daoExame.getExamesPorPaciente(pacienteId);
    }
    
    public List<Exame> getExamesPorData(Date data) {
        return daoExame.getExamesPorData(data);
    }
    
    public boolean verificarExameExistente(long pacienteId, String descricao, Date data) {
        List<Exame> examesDoPaciente = getExamesPorPaciente(pacienteId);
        
        for (Exame exame : examesDoPaciente) {
            if (exame.getDescricao().equalsIgnoreCase(descricao) && 
                exame.getDataExame().equals(data)) { 
                return true;
            }
        }
        
        return false;
    }
    
    public void limparExames() {
        daoExame.limparLista();
    }
    
    public void excluirExame(Exame exame) {
        daoExame.delete(exame);
    }
}