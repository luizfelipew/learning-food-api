package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.RestauranteNaoEncontradoException;
import com.works.foodapi.domain.model.Cidade;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.model.FormaPagamento;
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
    private CadastroCidadeService cadastroCidade;

    @Autowired
    private CozinhaRepository cozinhaRepository;

    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamento;

    @Transactional
    public Restaurante salvar(Restaurante restaurante) {
        final Long cozinhaId = restaurante.getCozinha().getId();
        final Long cidadeId = restaurante.getEndereco().getCidade().getId();

        final Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);
        final Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);

        restaurante.setCozinha(cozinha);
        restaurante.getEndereco().setCidade(cidade);
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

    @Transactional
    public void desassociarFormaPagamento(final Long restauranteId, final Long formaPagamentoId){
        final Restaurante restaurante = buscarOuFalhar(restauranteId);
        final FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);

        restaurante.removerFormaPagamento(formaPagamento);
    }

    @Transactional
    public void associarFormaPagamento(final Long restauranteId, final Long formaPagamentoId){
        final Restaurante restaurante = buscarOuFalhar(restauranteId);
        final FormaPagamento formaPagamento = cadastroFormaPagamento.buscarOuFalhar(formaPagamentoId);

        restaurante.adicionarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void abrir(final Long restauranteId){
        final Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
        restauranteAtual.abrir();
    }

    @Transactional
    public void fechar(final Long restauranteId){
        final Restaurante restauranteAtual = buscarOuFalhar(restauranteId);
        restauranteAtual.fechar();
    }

    public Restaurante buscarOuFalhar(final Long restauranteId) {
        return restauranteRepository
                .findById(restauranteId)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
    }

}
