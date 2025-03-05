package dao;

import db.DatabaseConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Paciente;

public class PacienteDAO implements GenericDAO<Paciente, Long>{

	private DatabaseConnection db;
	
	public PacienteDAO(DatabaseConnection db) {
        if (db == null) {
            throw new IllegalArgumentException("Conexão com o banco de dados não pode ser nula");
        }
        this.db = db;
    }
	
	@Override
    public void add(Paciente obj) throws SQLException, IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException("O paciente não pode ser nulo");
        }
        if (obj.getCpf() == null || obj.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do paciente não pode ser nulo ou vazio");
        }
        if (obj.getNome() == null || obj.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do paciente não pode ser nulo ou vazio");
        }

        try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("INSERT INTO PACIENTES (id, cpf, nome) VALUES (?,?,?)")) {
            pstm.setLong(1, 0);
            pstm.setString(2, obj.getCpf());
            pstm.setString(3, obj.getNome());
            pstm.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Erro ao adicionar paciente: " + e.getMessage(), e);
        }
    }

	@Override
    public Paciente findByID(Long id) throws SQLException, IllegalArgumentException {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou negativo");
        }

        try (PreparedStatement pstm = db.getConnection().prepareStatement("SELECT * FROM PACIENTES WHERE id = ?")) {
            pstm.setLong(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new Paciente(rs.getLong("id"), rs.getString("cpf"), rs.getString("nome"));
                }
            }
            return null;
        } catch (SQLException e) {
            throw new SQLException("Erro ao buscar paciente por ID: " + e.getMessage(), e);
        }
    }

	@Override
    public void delete(Paciente obj) throws SQLException, IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException("O paciente não pode ser nulo");
        }
        if (obj.getId() == null || obj.getId() < 0) {
            throw new IllegalArgumentException("O ID do paciente não pode ser nulo ou negativo");
        }

        try (PreparedStatement pstm = db.getConnection().prepareStatement("DELETE FROM PACIENTES WHERE id = ?")) {
            pstm.setLong(1, obj.getId());
            int affectedRows = pstm.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Nenhum paciente encontrado com o ID: " + obj.getId());
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao excluir paciente: " + e.getMessage(), e);
        }
    }

	@Override
    public void update(Paciente obj) throws SQLException, IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException("O paciente não pode ser nulo");
        }
        if (obj.getId() == null || obj.getId() < 0) {
            throw new IllegalArgumentException("O ID do paciente não pode ser nulo ou negativo");
        }
        if (obj.getCpf() == null || obj.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("O CPF do paciente não pode ser nulo ou vazio");
        }
        if (obj.getNome() == null || obj.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do paciente não pode ser nulo ou vazio");
        }

        try (PreparedStatement pstm = db.getConnection().prepareStatement("UPDATE PACIENTES SET cpf = ?, nome = ? WHERE id = ?")) {
            pstm.setString(1, obj.getCpf());
            pstm.setString(2, obj.getNome());
            pstm.setLong(3, obj.getId());
            int affectedRows = pstm.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Nenhum paciente encontrado com o ID: " + obj.getId());
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao atualizar paciente: " + e.getMessage(), e);
        }
    }

	@Override
    public List<Paciente> getAll() throws SQLException {
        List<Paciente> temp = new ArrayList<>();

        try (PreparedStatement pstm = db.getConnection().prepareStatement("SELECT * FROM PACIENTES");
             ResultSet rs = pstm.executeQuery()) {
            while (rs.next()) {
                Paciente p = new Paciente(rs.getLong("id"), rs.getString("cpf"), rs.getString("nome"));
                temp.add(p);
            }
        } catch (SQLException e) {
            throw new SQLException("Erro ao listar pacientes: " + e.getMessage(), e);
        }
        return temp;
    }
}
