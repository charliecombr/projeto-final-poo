package model;

public class Exame {
    private long id;
    private String descricao;
    private String dataExame;
    private long pacienteId;
    
    // Construtor padrão
    public Exame() {
    }
    
    // Construtor com parâmetros
    public Exame(long id, String descricao, String dataExame, long pacienteId) {
        this.id = id;
        this.descricao = descricao;
        this.dataExame = dataExame;
        this.pacienteId = pacienteId;
    }
    
    // Getters e Setters
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDataExame() {
        return dataExame;
    }
    
    public void setDataExame(String dataExame) {
        this.dataExame = dataExame;
    }
    
    public long getPacienteId() {
        return pacienteId;
    }
    
    public void setPacienteId(long pacienteId) {
        this.pacienteId = pacienteId;
    }
    
    @Override
    public String toString() {
        return "Exame [id=" + id + ", descricao=" + descricao + ", dataExame=" + dataExame + ", pacienteId=" + pacienteId + "]";
    }
}
