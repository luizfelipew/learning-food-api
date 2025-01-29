package com.works.foodapi.domain.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class FotoProduto {

    @EqualsAndHashCode.Include
    @Id
    @Column(name = "produto_id") // é utilizado pelo @MapsId
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // essa notação utliliza o produto_id para fazer select na tabela produto
    private Produto produto;
    private String nomeArquivo;
    private String descricao;
    private String contentType;
    private Long tamanho;

}
