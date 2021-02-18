package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.model.Restaurante;
import com.works.foodapi.domain.repository.CozinhaRepository;
import com.works.foodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CadastroRestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    public Restaurante salvar(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cozinhaRepository.buscar(cozinhaId);

        if (Objects.isNull(cozinha)){
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe cadastro de cozinha com o código %d", cozinhaId)
            );
        }

        restaurante.setCozinha(cozinha);
        return restauranteRepository.salvar(restaurante);
    }

}
