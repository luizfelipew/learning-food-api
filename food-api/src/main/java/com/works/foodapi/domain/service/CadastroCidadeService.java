package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.model.Cidade;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.model.Estado;
import com.works.foodapi.domain.model.Restaurante;
import com.works.foodapi.domain.repository.CidadeRepository;
import com.works.foodapi.domain.repository.CozinhaRepository;
import com.works.foodapi.domain.repository.EstadoRepository;
import com.works.foodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CadastroCidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public Cidade salvar(Cidade cidade) {
        Long estadoId = cidade.getEstado().getId();
        Estado estado = estadoRepository.buscar(estadoId);

        if (Objects.isNull(estado)){
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe cadastro de estado com o código %d", estadoId)
            );
        }

        cidade.setEstado(estado);
        return cidadeRepository.salvar(cidade);
    }

}
