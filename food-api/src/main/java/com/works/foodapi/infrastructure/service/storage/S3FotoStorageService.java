package com.works.foodapi.infrastructure.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.works.foodapi.domain.service.FotoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
public class S3FotoStorageService implements FotoStorageService {

    private final AmazonS3 amazonS3;

    @Override
    public InputStream recuperar(String nomeArquivo) {
        return null;
    }

    @Override
    public void armazenar(NovaFoto novaFoto) {

    }

    @Override
    public void remover(String nomeArquivo) {

    }
}
