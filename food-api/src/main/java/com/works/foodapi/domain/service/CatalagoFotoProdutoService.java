package com.works.foodapi.domain.service;

import com.works.foodapi.domain.model.FotoProduto;
import com.works.foodapi.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@RequiredArgsConstructor
@Service
public class CatalagoFotoProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FotoStorageService fotoStorageService;

    @Transactional
    public FotoProduto salvar(final FotoProduto foto, final InputStream dadosArquivo) {
        val restauranteId = foto.getRestauranteId();
        val produtoId = foto.getProduto().getId();
        val nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(foto.getNomeArquivo());

        val fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);

        fotoExistente.ifPresent(produtoRepository::delete);

        foto.setNomeArquivo(nomeNovoArquivo);
        val fotoSalva = produtoRepository.save(foto);
        produtoRepository.flush(); // flush garante que tudo que tiver na fila da jpa vai ser executado

        val novaFoto = FotoStorageService
                .NovaFoto.builder()
                .nomeArquivo(foto.getNomeArquivo())
                .inputStream(dadosArquivo)
                .build();

        fotoStorageService.armazenar(novaFoto);

        return fotoSalva;
    }
}
