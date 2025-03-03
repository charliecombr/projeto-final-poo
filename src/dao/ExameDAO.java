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
    public void add(Exame obj) {
        if (db != null) {
            // Modo banco de dados
            try(PreparedStatement pstm = db.getConnection()
                .prepareStatement("INSERT INTO EXAMES (id, descricao, data, pacientes_id) VALUES (?,?,?,?)",
                                  PreparedStatement.RETURN_GENERATED_KEYS)) {
                // ID - geralmente autoincremento no banco
                pstm.setLong(1, 0);
                
                // Descrição do exame
                pstm.setString(2, obj.getDescricao());
                
                // Data do exame (como string)
                pstm.setString(3, obj.getDataExame());
                
                // ID do paciente
                pstm.setLong(4, obj.getPacienteId());
                
                pstm.executeUpdate();
                
                // Obter ID gerado
                try (ResultSet rs = pstm.getGeneratedKeys()) {
                    if (rs.next()) {
                        obj.setId(rs.getLong(1));
                    }
                }
                
                // Adicionar à lista em memória para consistência
                exameList.add(obj);
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Modo memória
            lastId++;
            obj.setId(lastId);
            exameList.add(obj);
        }
    }

    @Override
    public Exame findByID(Long id) {
        if (db != null) {
            // Implementar busca no banco
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
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            // Busca em memória
            for (Exame exame : exameList) {
                if (exame.getId() == id) {
                    return exame;
                }
            }
            return null;
        }
    }

    @Override
    public void delete(Exame obj) {
        if (obj != null) {
            if (db != null) {
                // Modo banco de dados
                try (PreparedStatement pstm = db.getConnection()
                    .prepareStatement("DELETE FROM EXAMES WHERE id = ?")) {
                    pstm.setLong(1, obj.getId());
                    pstm.executeUpdate();
                    
                    // Remover da lista em memória também
                    exameList.removeIf(e -> e.getId() == obj.getId());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                // Modo memória
                exameList.removeIf(e -> e.getId() == obj.getId());
            }
        }
    }
    
    public boolean excluirPorId(long id) {
        if (db != null) {
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("DELETE FROM EXAMES WHERE id = ?")) {
                pstm.setLong(1, id);
                int affectedRows = pstm.executeUpdate();
                
                // Remover da lista em memória também
                exameList.removeIf(e -> e.getId() == id);
                
                return affectedRows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return exameList.removeIf(e -> e.getId() == id);
        }
    }

    @Override
    public void update(Exame obj) {
        if (db != null) {
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("UPDATE EXAMES SET descricao = ?, data = ?, pacientes_id = ? WHERE id = ?")) {
                pstm.setString(1, obj.getDescricao());
                pstm.setString(2, obj.getDataExame());
                pstm.setLong(3, obj.getPacienteId());
                pstm.setLong(4, obj.getId());
                
                pstm.executeUpdate();
                
                // Atualizar na lista em memória
                for (int i = 0; i < exameList.size(); i++) {
                    if (exameList.get(i).getId() == obj.getId()) {
                        exameList.set(i, obj);
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Modo memória
            for (int i = 0; i < exameList.size(); i++) {
                if (exameList.get(i).getId() == obj.getId()) {
                    exameList.set(i, obj);
                    break;
                }
            }
        }
    }

    @Override
    public List<Exame> getAll() {
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
                
                // Atualiza a lista em memória
                this.exameList = result;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            return result;
        } else {
            return new ArrayList<>(exameList);
        }
    }
    
    public List<Exame> getExamesPorPaciente(long pacienteId) {
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
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            return result;
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
    
    public List<Exame> getExamesPorData(Date data) {
        // Formatar a data para comparação com String
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
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
            return result;
        } else {
            List<Exame> examesDaData = new ArrayList<>();
            
            for (Exame exame : exameList) {
                // Comparando strings de data
                if (exame.getDataExame().equals(dataStr)) {
                    examesDaData.add(exame);
                }
            }
            
            return examesDaData;
        }
    }
    
    public void limparLista() {
        if (db != null) {
            try (PreparedStatement pstm = db.getConnection()
                .prepareStatement("DELETE FROM EXAMES")) {
                pstm.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        exameList.clear();
        lastId = 0;
    }
}