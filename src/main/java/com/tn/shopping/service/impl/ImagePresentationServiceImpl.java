package com.tn.shopping.service.impl;

import com.tn.shopping.domain.ImagePresentation;
import com.tn.shopping.repository.ImagePresentationRepository;
import com.tn.shopping.service.ImagePresentationService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ImagePresentation}.
 */
@Service
@Transactional
public class ImagePresentationServiceImpl implements ImagePresentationService {

    private final Logger log = LoggerFactory.getLogger(ImagePresentationServiceImpl.class);

    private final ImagePresentationRepository imagePresentationRepository;

    public ImagePresentationServiceImpl(ImagePresentationRepository imagePresentationRepository) {
        this.imagePresentationRepository = imagePresentationRepository;
    }

    @Override
    public Mono<ImagePresentation> save(ImagePresentation imagePresentation) {
        log.debug("Request to save ImagePresentation : {}", imagePresentation);
        return imagePresentationRepository.save(imagePresentation);
    }

    @Override
    public Mono<ImagePresentation> update(ImagePresentation imagePresentation) {
        log.debug("Request to update ImagePresentation : {}", imagePresentation);
        return imagePresentationRepository.save(imagePresentation);
    }

    @Override
    public Mono<ImagePresentation> partialUpdate(ImagePresentation imagePresentation) {
        log.debug("Request to partially update ImagePresentation : {}", imagePresentation);

        return imagePresentationRepository
            .findById(imagePresentation.getId())
            .map(existingImagePresentation -> {
                if (imagePresentation.getImage() != null) {
                    existingImagePresentation.setImage(imagePresentation.getImage());
                }
                if (imagePresentation.getImageContentType() != null) {
                    existingImagePresentation.setImageContentType(imagePresentation.getImageContentType());
                }

                return existingImagePresentation;
            })
            .flatMap(imagePresentationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ImagePresentation> findAll() {
        log.debug("Request to get all ImagePresentations");
        return imagePresentationRepository.findAll();
    }

    public Mono<Long> countAll() {
        return imagePresentationRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ImagePresentation> findOne(Long id) {
        log.debug("Request to get ImagePresentation : {}", id);
        return imagePresentationRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ImagePresentation : {}", id);
        return imagePresentationRepository.deleteById(id);
    }
}
