package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.FotoProdutoNaoEncontradaException;
import com.works.foodapi.domain.model.FotoProduto;
import com.works.foodapi.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class CatalogoFotoProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FotoStorageService fotoStorageService;

    @Transactional
    public FotoProduto salvar(final FotoProduto foto, final InputStream dadosArquivo) {
        val restauranteId = foto.getRestauranteId();
        val produtoId = foto.getProduto().getId();
        val nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());
        String nomeArquivoExistente = null;

        val fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);

//        fotoExistente.ifPresent(produtoRepository::delete);
        if (fotoExistente.isPresent()) {
            nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
            produtoRepository.delete(fotoExistente.get());
        }

        foto.setNomeArquivo(nomeNovoArquivo);
        val fotoSalva = produtoRepository.save(foto);
        produtoRepository.flush(); // flush garante que tudo que tiver na fila da jpa vai ser executado

        val novaFoto = FotoStorageService
                .NovaFoto.builder()
                .nomeArquivo(foto.getNomeArquivo())
                .inputStream(dadosArquivo)
                .build();

        fotoStorageService.substituir(nomeArquivoExistente, novaFoto);

        return fotoSalva;
    }

    public void excluir(Long restauranteId, Long produtoId) {
        val foto = buscarOuFalhar(restauranteId, produtoId);

        produtoRepository.delete(foto);
        produtoRepository.flush();

        fotoStorageService.remover(foto.getNomeArquivo());
    }

    public FotoProduto buscarOuFalhar(Long restauranteId, Long produtoId) {
        return produtoRepository.findFotoById(restauranteId, produtoId)
                .orElseThrow(() -> new FotoProdutoNaoEncontradaException(restauranteId, produtoId));
    }
}
