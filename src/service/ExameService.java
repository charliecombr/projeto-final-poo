package service;

import dao.ExameDAO;
import exception.ExameNaoEncontradoException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import model.Exame;

public class ExameService {
	
    private ExameDAO daoExame;
    
    public ExameService(ExameDAO daoExame) {
        if (daoExame == null) {
            throw new IllegalArgumentException("O DAO do exame não pode ser nulo");
        }
        this.daoExame = daoExame;
    }
    
    public void adicionarExame(Exame e) throws SQLException, IllegalArgumentException {
        if (e == null) {
            throw new IllegalArgumentException("O exame não pode ser nulo");
        }
        daoExame.add(e);
    }

    public void atualizarExame(Exame exame) throws SQLException, IllegalArgumentException {
        if (exame == null || exame.getId() == null) {
            throw new IllegalArgumentException("O exame ou seu ID não pode ser nulo");
        }
        daoExame.update(exame);
    }
    
    public boolean removerExame(Long id) throws SQLException, IllegalArgumentException {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou negativo");
        }
        return daoExame.excluirPorId(id);
    }
    
    public Exame localizarExamePorId(Long id) throws SQLException, ExameNaoEncontradoException {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou negativo");
        }
        Exame exame = daoExame.findByID(id);
        if (exame == null) {
            throw new ExameNaoEncontradoException("Exame com ID " + id + " não encontrado");
        }
        return exame;
    }
    
    public List<Exame> getExames() throws SQLException {
        return daoExame.getAll();
    }
    
    public List<Exame> getExamesPorPaciente(Long pacienteId) throws SQLException, IllegalArgumentException {
        if (pacienteId == null || pacienteId < 0) {
            throw new IllegalArgumentException("O ID do paciente não pode ser nulo ou negativo");
        }
        return daoExame.getExamesPorPaciente(pacienteId);
    }
    
    public List<Exame> getExamesPorData(Date data) throws SQLException, IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("A data não pode ser nula");
        }
        return daoExame.getExamesPorData(data);
    }
    
    public boolean verificarExameExistente(Long pacienteId, String descricao, Date data) throws SQLException, IllegalArgumentException {
        if (pacienteId == null || pacienteId < 0) {
            throw new IllegalArgumentException("O ID do paciente não pode ser nulo ou negativo");
        }
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição não pode ser nula ou vazia");
        }
        if (data == null) {
            throw new IllegalArgumentException("A data não pode ser nula");
        }
        
        List<Exame> examesDoPaciente = getExamesPorPaciente(pacienteId);
        
        for (Exame exame : examesDoPaciente) {
            if (exame.getDescricao().equalsIgnoreCase(descricao) && 
                exame.getDataExame().equals(new java.text.SimpleDateFormat("dd/MM/yyyy").format(data))) { 
                return true;
            }
        }
        
        return false;
    }
    
    public void limparExames() throws SQLException {
        daoExame.limparLista();
    }
}