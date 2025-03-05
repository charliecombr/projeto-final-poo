package service;

import dao.GenericDAO;
import exception.PacienteNaoEncontradoException;

import java.sql.SQLException;
import java.util.List;
import model.Paciente;

public class PacienteService {

	private GenericDAO<Paciente, Long> daoPaciente;
	
	public PacienteService(GenericDAO<Paciente, Long> dao) {
        if (dao == null) {
            throw new IllegalArgumentException("O DAO do paciente não pode ser nulo");
        }
        this.daoPaciente = dao;
    }
	
	public void adicionarPaciente(Paciente p) throws SQLException, IllegalArgumentException {
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
	
	public void deletarPaciente(Paciente p) throws SQLException, IllegalArgumentException {
        if (p == null) {
            throw new IllegalArgumentException("O paciente não pode ser nulo");
        }
        daoPaciente.delete(p);
    }
	
	public List<Paciente> getPacientes() throws SQLException {
        return daoPaciente.getAll();
    }
	
	public void atualizarPaciente(Paciente p) throws SQLException, IllegalArgumentException {
        if (p == null || p.getId() == null) {
            throw new IllegalArgumentException("O paciente ou seu ID não pode ser nulo");
        }
        daoPaciente.update(p);
    }
	
	public void excluir(Paciente p) throws SQLException, IllegalArgumentException {
        if (p == null) {
            throw new IllegalArgumentException("O paciente não pode ser nulo");
        }
        daoPaciente.delete(p);
    }
}
