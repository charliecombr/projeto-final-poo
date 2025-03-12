package model;

public class Exame {
    private Long id;
    private String descricao;
    private String dataExame;
    private long pacienteId;
    
    public Exame() {
    }
    
    public Exame(Long id, String descricao, String dataExame, Long pacienteId) {
        this.id = id;
        this.descricao = descricao;
        this.dataExame = dataExame;
        this.pacienteId = pacienteId;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        if (id != null && id < 0) {
            throw new IllegalArgumentException("O ID do exame não pode ser negativo");
        }
        this.id = id;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição do exame não pode ser nula ou vazia");
        }
        this.descricao = descricao;
    }
    
    public String getDataExame() {
        return dataExame;
    }
    
    public void setDataExame(String dataExame) {
        if (dataExame == null || dataExame.trim().isEmpty()) {
            throw new IllegalArgumentException("A data do exame não pode ser nula ou vazia");
        }
        this.dataExame = dataExame;
    }
    
    public long getPacienteId() {
        return pacienteId;
    }
    
    public void setPacienteId(long pacienteId) {
        if (pacienteId < 0) {
            throw new IllegalArgumentException("O ID do paciente não pode ser negativo");
        }
        this.pacienteId = pacienteId;
    }
    
    @Override
    public String toString() {
        return "Exame [id=" + id + ", descricao=" + descricao + ", dataExame=" + dataExame + ", pacienteId=" + pacienteId + "]";
    }
}
