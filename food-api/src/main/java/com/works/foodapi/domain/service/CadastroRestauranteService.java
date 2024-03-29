package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.RestauranteNaoEncontradoException;
import com.works.foodapi.domain.model.*;
import com.works.foodapi.domain.repository.CozinhaRepository;
import com.works.foodapi.domain.repository.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private CadastroUsuarioService cadastroUsuario;

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
    public void ativar(final List<Long> restaurantIds) {
        restaurantIds.forEach(this::ativar);
    }

    @Transactional
    public void inativar(final List<Long> restaurantIds) {
        restaurantIds.forEach(this::inativar);
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

    @Transactional
    public void desassociarResponsavel(final Long restauranteId, final Long usuarioId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);

        restaurante.removerResponsavel(usuario);
    }

    @Transactional
    public void associarResponsavel(final Long restauranteId, final Long usuarioId) {
        Restaurante restaurante = buscarOuFalhar(restauranteId);
        Usuario usuario = cadastroUsuario.buscarOuFalhar(usuarioId);

        restaurante.adicionarResponsavel(usuario);
    }

    public Restaurante buscarOuFalhar(final Long restauranteId) {
        return restauranteRepository
                .findById(restauranteId)
                .orElseThrow(() -> new RestauranteNaoEncontradoException(restauranteId));
    }

}
