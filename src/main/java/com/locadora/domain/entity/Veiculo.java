package com.locadora.domain.entity;

import com.locadora.domain.enums.StatusVeiculo;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "veiculos")
public class Veiculo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marca;
    private String modelo;
    @Column(unique = true)
    private String placa;
    private Integer ano;

    @Enumerated(EnumType.STRING)
    private StatusVeiculo status;


    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Locacao> locacoes = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public Integer getAno() { return ano; }
    public void setAno(Integer ano) { this.ano = ano; }

    public StatusVeiculo getStatus() { return status; }
    public void setStatus(StatusVeiculo status) { this.status = status; }

    public List<Locacao> getLocacoes() { return locacoes; }

    public void addLocacao(Locacao locacao) {
        if (locacao == null) return;
        locacoes.add(locacao);
        locacao.setVeiculo(this);
    }

    public void removeLocacao(Locacao locacao) {
        if (locacao == null) return;
        locacoes.remove(locacao);
        locacao.setVeiculo(null);
    }

    public void atualizarVeiculoFromDTO(com.locadora.application.dto.request.VeiculoRequest dto) {
        if (dto.getMarca() != null) setMarca(dto.getMarca());
        if (dto.getModelo() != null) setModelo(dto.getModelo());
        if (dto.getPlaca() != null) setPlaca(dto.getPlaca());
        if (dto.getAno() != null) setAno(dto.getAno());
        if (dto.getStatus() != null) setStatus(dto.getStatus());
    }
}
