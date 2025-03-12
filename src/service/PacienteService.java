package service;

import dao.GenericDAO;
import exception.PacienteExameVinculadoException;
import exception.PacienteNaoEncontradoException;
import exception.ValidarCpfException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import model.Exame;
import model.Paciente;

public class PacienteService {

	private GenericDAO<Paciente, Long> daoPaciente;
    private ExameService exameService; 

    
	
	public PacienteService(GenericDAO<Paciente, Long> dao) {
        if (dao == null) {
            throw new IllegalArgumentException("O DAO do paciente não pode ser nulo");
        }
        this.daoPaciente = dao;
        this.exameService = null;
    }
	
	public void adicionarPaciente(Paciente p) throws SQLException, IllegalArgumentException, InputMismatchException {
        if (p == null) {
            throw new IllegalArgumentException("O paciente não pode ser nulo");
        }
        daoPaciente.add(p);
    }
	
	public Paciente localizarPacientePorId(Long id) throws SQLException, PacienteNaoEncontradoException {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou negativo");
        }
        Paciente p = daoPaciente.findByID(id);
        if (p == null) {
            throw new PacienteNaoEncontradoException("Paciente com ID " + id + " não encontrado");
        }
        return p;
    }
	
	public Paciente localizarPacientePorCpf(String cpf) throws SQLException, PacienteNaoEncontradoException {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF não pode ser nulo ou vazio");
        }
        List<Paciente> pacientes = daoPaciente.getAll();
        return pacientes.stream()
                .filter(p -> cpf.equals(p.getCpf()))
                .findFirst()
                .orElseThrow(() -> new PacienteNaoEncontradoException("Paciente com CPF " + cpf + " não encontrado"));
    }
	
    public void deletarPaciente(Paciente p) throws SQLException, IllegalArgumentException, PacienteExameVinculadoException {
        if (p == null) {
            throw new IllegalArgumentException("O paciente não pode ser nulo");
        }
        
        // Verificar se o exameService foi configurado
        if (exameService == null) {
            // Se não tiver ExameService, manter o comportamento original
            try {
                daoPaciente.delete(p);
            } catch (SQLException e) {
                String errorMsg = e.getMessage().toLowerCase();
                if (errorMsg.contains("foreign key") || errorMsg.contains("constraint")) {
                    throw new PacienteExameVinculadoException("Paciente possui exame vinculado. Apague o exame primeiro.");
                }
                throw e;
            }
        } else {
            try {
                List<Exame> exames = exameService.getExamesPorPaciente(p.getId());
                
                for (Exame exame : exames) {
                    exameService.removerExame(exame.getId());
                }
                daoPaciente.delete(p);
                
            } catch (SQLException e) {
                throw e;
            }
        }
    }
	
	public List<Paciente> getPacientes() throws SQLException {
        return daoPaciente.getAll();
    }
	
	public void atualizarPaciente(Paciente p) throws SQLException, IllegalArgumentException, ValidarCpfException {
        if (p == null || p.getId() == null) {
            throw new IllegalArgumentException("O paciente ou seu ID não pode ser nulo");
        }
        daoPaciente.update(p);
    }
    public void setExameService(ExameService exameService) {
        this.exameService = exameService;
    }
}
