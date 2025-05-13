package com.works.foodapi.core.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.works.foodapi.domain.service.FotoStorageService;
import com.works.foodapi.infrastructure.service.storage.LocalFotoStorageService;
import com.works.foodapi.infrastructure.service.storage.S3FotoStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.works.foodapi.core.storage.StorageProperties.*;

@Configuration
@RequiredArgsConstructor
public class StorageConfig {

    private final StorageProperties storageProperties;

    @Bean
    public AmazonS3 amazonS3() {
        var credentials = new BasicAWSCredentials(
                storageProperties.getS3().getIdChaveAcesso(),
                storageProperties.getS3().getChaveAcessoSecreta()
        );

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(storageProperties.getS3().getRegiao())
                .build();
    }

    @Bean
//    @ConditionalOnProperty(name = "algofood.storage.tipo", havingValue = "S3")
    public FotoStorageService fotoStorageService() {
        if (TipoStorage.S3.equals(storageProperties.getTipo())){
            return new S3FotoStorageService(amazonS3(), storageProperties);
        } else {
            return new LocalFotoStorageService(storageProperties);
        }
    }
}
