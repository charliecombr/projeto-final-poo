package service;

import dao.GenericDAO;
import java.util.List;
import model.Paciente;

public class PacienteService {

	private GenericDAO<Paciente, Long> daoPaciente;
	
	public PacienteService(GenericDAO<Paciente, Long> dao) {
		this.daoPaciente = dao;
	}
	
	public void adicionarPaciente(Paciente p) {
		daoPaciente.add(p);
	}
	
	public Paciente localizarPacientePorId(Long id) {
		return daoPaciente.findByID(id);
	}
	
	public Paciente localizarPacientePorCpf(String cpf) {
		return daoPaciente.getAll()
				  .stream()
				  .filter(p -> p.getCpf().equals(cpf))
				  .findFirst()
				  .orElse(null);
	}
	
	public void deletarPaciente(Paciente p) {
		daoPaciente.delete(p);
	}
	
	public List<Paciente> getPacientes(){
		return daoPaciente.getAll();
	}
	
	public void atualizarPaciente(Paciente p) {
		daoPaciente.update(p);
	}

	public void excluir(Paciente p) {
		daoPaciente.delete(p);
	}
}
