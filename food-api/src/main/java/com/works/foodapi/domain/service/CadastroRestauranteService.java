package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.RestauranteNaoEncontradoException;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.model.Restaurante;
import com.works.foodapi.domain.repository.CozinhaRepository;
import com.works.foodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroRestauranteService {

    private static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Não existe cadastro de cozinha com o código %d";

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    public Restaurante salvar(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);

        restaurante.setCozinha(cozinha);
        return restauranteRepository.save(restaurante);
    }

    public Restaurante buscarOuFalhar(final Long restauranteId) {
        return restauranteRepository
                .findById(restauranteId)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
    }

}
