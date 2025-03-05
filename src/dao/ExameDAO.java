package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import db.DatabaseConnection;
import model.Exame;

public class ExameDAO implements GenericDAO<Exame, Long> {
    
    private DatabaseConnection db;
    private List<Exame> exameList;
    private long lastId;
    
    // Construtor para modo banco de dados
    public ExameDAO(DatabaseConnection db) {
        if (db == null) {
            throw new IllegalArgumentException("Conexão com o banco de dados não pode ser nula");
        }
        this.db = db;
        this.exameList = new ArrayList<>();
        this.lastId = 0;
    }
    
    // Construtor para modo memória
    public ExameDAO() {
        this.db = null;
        this.exameList = new ArrayList<>();
        this.lastId = 0;
    }
    
    @Override
    public void add(Exame obj) throws SQLException, IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException("O exame não pode ser nulo");
        }
        if (obj.getDescricao() == null || obj.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição do exame não pode ser nula ou vazia");
        }
        if (obj.getDataExame() == null || obj.getDataExame().trim().isEmpty()) {
            throw new IllegalArgumentException("A data do exame não pode ser nula ou vazia");
        }
        if (obj.getPacienteId() < 0) {
            throw new IllegalArgumentException("O ID do paciente não pode ser negativo");
        }

        if (db != null) {
            // Modo banco de dados
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("INSERT INTO EXAMES (id, descricao, data, pacientes_id) VALUES (?,?,?,?)",
                                  PreparedStatement.RETURN_GENERATED_KEYS)) {
                pstm.setLong(1, 0); // ID autoincremento
                pstm.setString(2, obj.getDescricao());
                pstm.setString(3, obj.getDataExame());
                pstm.setLong(4, obj.getPacienteId());
                
                pstm.executeUpdate();
                
                // Obter ID gerado
                try (ResultSet rs = pstm.getGeneratedKeys()) {
                    if (rs.next()) {
                        obj.setId(rs.getLong(1));
                    } else {
                        throw new SQLException("Falha ao obter ID gerado para o exame");
                    }
                }
                exameList.add(obj);
            } catch (SQLException e) {
                throw new SQLException("Erro ao adicionar exame no banco de dados: " + e.getMessage(), e);
            }
        } else {
            // Modo memória
            lastId++;
            obj.setId(lastId);
            exameList.add(obj);
        }
    }

    @Override
    public Exame findByID(Long id) throws SQLException, IllegalArgumentException {
        if (id == null || id < 0) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou negativo");
        }

        if (db != null) {
            // Modo banco de dados
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("SELECT * FROM EXAMES WHERE id = ?")) {
                pstm.setLong(1, id);
                
                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        Exame exame = new Exame();
                        exame.setId(rs.getLong("id"));
                        exame.setDescricao(rs.getString("descricao"));
                        exame.setDataExame(rs.getString("data"));
                        exame.setPacienteId(rs.getLong("pacientes_id"));
                        return exame;
                    }
                    return null; // Retorna null se não encontrado (tratamento pode ser na camada de serviço)
                }
            } catch (SQLException e) {
                throw new SQLException("Erro ao buscar exame por ID: " + e.getMessage(), e);
            }
        } else {
            // Modo memória
            for (Exame exame : exameList) {
                if (exame.getId() == id) {
                    return exame;
                }
            }
            return null; // Retorna null se não encontrado
        }
    }

    @Override
    public void delete(Exame obj) throws SQLException, IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException("O exame não pode ser nulo");
        }
        if (obj.getId() == null || obj.getId() < 0) {
            throw new IllegalArgumentException("O ID do exame não pode ser nulo ou negativo");
        }

        if (db != null) {
            // Modo banco de dados
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("DELETE FROM EXAMES WHERE id = ?")) {
                pstm.setLong(1, obj.getId());
                int affectedRows = pstm.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Nenhum exame encontrado com o ID: " + obj.getId());
                }
                exameList.removeIf(e -> e.getId() == obj.getId());
            } catch (SQLException e) {
                throw new SQLException("Erro ao excluir exame: " + e.getMessage(), e);
            }
        } else {
            // Modo memória
            if (!exameList.removeIf(e -> e.getId() == obj.getId())) {
                throw new IllegalArgumentException("Nenhum exame encontrado com o ID: " + obj.getId());
            }
        }
    }
    
    public boolean excluirPorId(long id) throws SQLException, IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("O ID não pode ser negativo");
        }

        if (db != null) {
            // Modo banco de dados
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("DELETE FROM EXAMES WHERE id = ?")) {
                pstm.setLong(1, id);
                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    exameList.removeIf(e -> e.getId() == id);
                    return true;
                }
                return false; // Retorna false se nenhum exame foi excluído
            } catch (SQLException e) {
                throw new SQLException("Erro ao excluir exame por ID: " + e.getMessage(), e);
            }
        } else {
            // Modo memória
            int initialSize = exameList.size();
            exameList.removeIf(e -> e.getId() == id);
            return exameList.size() < initialSize; // Retorna true se algo foi removido
        }
    }

    @Override
    public void update(Exame obj) throws SQLException, IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException("O exame não pode ser nulo");
        }
        if (obj.getId() == null || obj.getId() < 0) {
            throw new IllegalArgumentException("O ID do exame não pode ser nulo ou negativo");
        }
        if (obj.getDescricao() == null || obj.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição do exame não pode ser nula ou vazia");
        }
        if (obj.getDataExame() == null || obj.getDataExame().trim().isEmpty()) {
            throw new IllegalArgumentException("A data do exame não pode ser nula ou vazia");
        }
        if (obj.getPacienteId() < 0) {
            throw new IllegalArgumentException("O ID do paciente não pode ser negativo");
        }

        if (db != null) {
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("UPDATE EXAMES SET descricao = ?, data = ?, pacientes_id = ? WHERE id = ?")) {
                pstm.setString(1, obj.getDescricao());
                pstm.setString(2, obj.getDataExame());
                pstm.setLong(3, obj.getPacienteId());
                pstm.setLong(4, obj.getId());
                
                int affectedRows = pstm.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Nenhum exame encontrado com o ID: " + obj.getId());
                }
                
                // Atualizar na lista em memória
                for (int i = 0; i < exameList.size(); i++) {
                    if (exameList.get(i).getId() == obj.getId()) {
                        exameList.set(i, obj);
                        break;
                    }
                }
            } catch (SQLException e) {
                throw new SQLException("Erro ao atualizar exame: " + e.getMessage(), e);
            }
        } else {
            // Modo memória
            for (int i = 0; i < exameList.size(); i++) {
                if (exameList.get(i).getId() == obj.getId()) {
                    exameList.set(i, obj);
                    return;
                }
            }
            throw new IllegalArgumentException("Nenhum exame encontrado com o ID: " + obj.getId());
        }
    }

    @Override
    public List<Exame> getAll() throws SQLException {
        if (db != null) {
            List<Exame> result = new ArrayList<>();
            
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("SELECT * FROM EXAMES");
                ResultSet rs = pstm.executeQuery()) {
                
                while (rs.next()) {
                    Exame exame = new Exame();
                    exame.setId(rs.getLong("id"));
                    exame.setDescricao(rs.getString("descricao"));
                    exame.setDataExame(rs.getString("data"));
                    exame.setPacienteId(rs.getLong("pacientes_id"));
                    result.add(exame);
                }
                this.exameList = result;
                return result;
            } catch (SQLException e) {
                throw new SQLException("Erro ao listar exames: " + e.getMessage(), e);
            }
        } else {
            return new ArrayList<>(exameList);
        }
    }
    
    public List<Exame> getExamesPorPaciente(long pacienteId) throws SQLException {
        if (pacienteId < 0) {
            throw new IllegalArgumentException("O ID do paciente não pode ser negativo");
        }

        if (db != null) {
            List<Exame> result = new ArrayList<>();
            
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("SELECT * FROM EXAMES WHERE pacientes_id = ?")) {
                pstm.setLong(1, pacienteId);
                
                try (ResultSet rs = pstm.executeQuery()) {
                    while (rs.next()) {
                        Exame exame = new Exame();
                        exame.setId(rs.getLong("id"));
                        exame.setDescricao(rs.getString("descricao"));
                        exame.setDataExame(rs.getString("data"));
                        exame.setPacienteId(rs.getLong("pacientes_id"));
                        result.add(exame);
                    }
                    return result;
                }
            } catch (SQLException e) {
                throw new SQLException("Erro ao listar exames por paciente: " + e.getMessage(), e);
            }
        } else {
            List<Exame> examesDoPaciente = new ArrayList<>();
            for (Exame exame : exameList) {
                if (exame.getPacienteId() == pacienteId) {
                    examesDoPaciente.add(exame);
                }
            }
            return examesDoPaciente;
        }
    }
    
    public List<Exame> getExamesPorData(Date data) throws SQLException, IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("A data não pode ser nula");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dataStr = sdf.format(data);

        if (db != null) {
            List<Exame> result = new ArrayList<>();
            
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("SELECT * FROM EXAMES WHERE data = ?")) {
                pstm.setString(1, dataStr);
                
                try (ResultSet rs = pstm.executeQuery()) {
                    while (rs.next()) {
                        Exame exame = new Exame();
                        exame.setId(rs.getLong("id"));
                        exame.setDescricao(rs.getString("descricao"));
                        exame.setDataExame(rs.getString("data"));
                        exame.setPacienteId(rs.getLong("pacientes_id"));
                        result.add(exame);
                    }
                    return result;
                }
            } catch (SQLException e) {
                throw new SQLException("Erro ao listar exames por data: " + e.getMessage(), e);
            }
        } else {
            List<Exame> examesDaData = new ArrayList<>();
            for (Exame exame : exameList) {
                if (exame.getDataExame().equals(dataStr)) {
                    examesDaData.add(exame);
                }
            }
            return examesDaData;
        }
    }
    
    public void limparLista() throws SQLException {
        if (db != null) {
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("DELETE FROM EXAMES")) {
                pstm.executeUpdate();
            } catch (SQLException e) {
                throw new SQLException("Erro ao limpar lista de exames: " + e.getMessage(), e);
            }
        }
        exameList.clear();
        lastId = 0;
    }
}