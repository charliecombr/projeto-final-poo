package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Paciente {

	private Long id;
	private String cpf;
	private String nome;
	private List<Exame> exames = new ArrayList<>();

	public Paciente(Long id, String cpf, String nome) {
		this.id = id;
		this.cpf = cpf;
		this.nome = nome;
	}
	
	public Paciente() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
        if (id != null && id < 0) {
            throw new IllegalArgumentException("O ID do paciente não pode ser negativo");
        }
        this.id = id;
    }

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do paciente não pode ser nulo ou vazio");
        }
        this.cpf = cpf;
    }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do paciente não pode ser nulo ou vazio");
        }
        this.nome = nome;
    }

	public List<Exame> getExames() {
		return exames;
	}

	public void setExames(List<Exame> exames) {
        if (exames == null) {
            throw new IllegalArgumentException("A lista de exames não pode ser nula");
        }
        this.exames = exames;
    }

	@Override
	public int hashCode() {
		return Objects.hash(cpf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Paciente other = (Paciente) obj;
		return Objects.equals(cpf, other.cpf);
	}

	@Override
	public String toString() {
		return "Paciente [id=" + id + ", cpf=" + cpf + ", nome=" + nome + ", exames=" + exames + "]";
	}
	
	
	
	
	
}
