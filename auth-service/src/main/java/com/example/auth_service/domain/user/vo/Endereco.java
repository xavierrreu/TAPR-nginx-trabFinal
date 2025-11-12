package com.example.auth_service.domain.user.vo;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {
    private String rua;
    private String cidade;
    private String estado;
    private String cep;
    private String pais;
}
