package com.works.foodapi.infrastructure.service.storage;

import com.works.foodapi.domain.service.FotoStorageService;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class LocalFotoStorageService implements FotoStorageService {

    @Value("${algofood.storage.local.diretorio-fotos}")
    private Path diretorioFotos;

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
        return diretorioFotos.resolve(Path.of(nomeArquivo));
    }
}
