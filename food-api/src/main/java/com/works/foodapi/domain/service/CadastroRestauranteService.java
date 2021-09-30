package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.RestauranteNaoEncontradoException;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.model.Restaurante;
import com.works.foodapi.domain.repository.CozinhaRepository;
import com.works.foodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroRestauranteService {

    private static final String MSG_RESTAURANTE_NAO_ENCONTRADO = "Não existe cadastro de cozinha com o código %d";

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private CadastroCozinhaService cadastroCozinha;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Transactional
    public Restaurante salvar(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);

        restaurante.setCozinha(cozinha);
        return restauranteRepository.save(restaurante);
    }

    // JPA esta gerenciando ela sincroniza com banco de dados (update) com o setAtivo
    @Transactional
    public void ativar(final Long restaurantId) {
        Restaurante restauranteAtual = buscarOuFalhar(restaurantId);

        restauranteAtual.ativar();

    }

    @Transactional
    public void inativar(final Long restaurantId) {
        Restaurante restauranteAtual = buscarOuFalhar(restaurantId);

        restauranteAtual.inativar();

    }

    public Restaurante buscarOuFalhar(final Long restauranteId) {
        return restauranteRepository
                .findById(restauranteId)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
    }

}
