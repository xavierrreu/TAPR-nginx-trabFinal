package com.example.auth_service.domain.user.vo;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistoricoProfissional {
    private String empresa;
    private String cargo;
    private int anoInicio;
    private int anoFim;
    private String descricao;
}