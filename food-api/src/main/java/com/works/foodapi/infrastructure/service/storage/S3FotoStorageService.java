package com.works.foodapi.infrastructure.service.storage;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.works.foodapi.core.storage.StorageProperties;
import com.works.foodapi.domain.service.FotoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S3FotoStorageService implements FotoStorageService {

    private final AmazonS3 amazonS3;
    private final StorageProperties storageProperties;

    @Override
    public FotoRecuperada recuperar(String nomeArquivo) {
        var caminhoArquivo = getCaminhoArquivo(nomeArquivo);
        var url = amazonS3.getUrl(storageProperties.getS3().getBucket(), caminhoArquivo);

        return FotoRecuperada.builder()
                .url(url.toString())
                .build();
    }

    @Override
    public void armazenar(NovaFoto novaFoto) {
        try {
            var caminhoArquivo = getCaminhoArquivo(novaFoto.getNomeArquivo());
            var objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(novaFoto.getContentType());

            var putObjectRequest = new PutObjectRequest(
                    storageProperties.getS3().getBucket(),
                    caminhoArquivo,
                    novaFoto.getInputStream(),
                    objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(putObjectRequest);
        } catch (Exception ex) {
            throw new StorageException("Não foi possível enviar o arquivo para a Amazon S3", ex);
        }

    }

    private String getCaminhoArquivo(String nomeArquivo) {
        return String.format(
                "%s/%s", storageProperties.getS3().getDiretorioFotos(),
                nomeArquivo
        );
    }

    @Override
    public void remover(String nomeArquivo) {
        try {
            var caminhoArquivo = getCaminhoArquivo(nomeArquivo);
            var deleteObjectRequest = new DeleteObjectRequest(storageProperties.getS3().getBucket(), caminhoArquivo);

            amazonS3.deleteObject(deleteObjectRequest);
        } catch (Exception ex) {
            throw new StorageException("Não foi possível excluir arquivo na Amazon S3.", ex);
        }
    }

}
