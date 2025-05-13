package com.works.foodapi.infrastructure.service.storage;

import com.works.foodapi.core.storage.StorageProperties;
import com.works.foodapi.domain.service.FotoStorageService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.util.FileCopyUtils;

import java.nio.file.Files;
import java.nio.file.Path;

@RequiredArgsConstructor
public class LocalFotoStorageService implements FotoStorageService {

    private final StorageProperties storageProperties;

    @Override
    public FotoRecuperada recuperar(String nomeArquivo) {
        try {
            Path arquivoPath = getArquivoPath(nomeArquivo);
            return FotoRecuperada.builder()
                    .inputStream(Files.newInputStream(arquivoPath))
                    .build();
        } catch (Exception e) {
            throw new StorageException("Não foi possível recuperar arquivo.", e);
        }
    }

    @Override
    public void armazenar(NovaFoto novaFoto) {

        try {
            val arquivoPath = getArquivoPath(novaFoto.getNomeArquivo());
            FileCopyUtils.copy(novaFoto.getInputStream(), Files.newOutputStream(arquivoPath));
        } catch (Exception e) {
            throw new StorageException("Não foi possivel armazenar arquivo. ", e);
        }
    }

    @Override
    public void remover(String nomeArquivo) {

        try {
            val arquivoPath = getArquivoPath(nomeArquivo);
            Files.deleteIfExists(arquivoPath);
        } catch (Exception e) {
            throw new StorageException("Não foi possivel excluir arquivo. ", e);
        }
    }

    private Path getArquivoPath(String nomeArquivo) {
        return storageProperties.getLocal()
                .getDiretorioFotos()
                .resolve(Path.of(nomeArquivo));
    }
}
