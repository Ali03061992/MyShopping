package com.tn.shopping.service;

import com.tn.shopping.domain.ImagePresentation;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ImagePresentation}.
 */
public interface ImagePresentationService {
    /**
     * Save a imagePresentation.
     *
     * @param imagePresentation the entity to save.
     * @return the persisted entity.
     */
    Mono<ImagePresentation> save(ImagePresentation imagePresentation);

    /**
     * Updates a imagePresentation.
     *
     * @param imagePresentation the entity to update.
     * @return the persisted entity.
     */
    Mono<ImagePresentation> update(ImagePresentation imagePresentation);

    /**
     * Partially updates a imagePresentation.
     *
     * @param imagePresentation the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ImagePresentation> partialUpdate(ImagePresentation imagePresentation);

    /**
     * Get all the imagePresentations.
     *
     * @return the list of entities.
     */
    Flux<ImagePresentation> findAll();

    /**
     * Returns the number of imagePresentations available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" imagePresentation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ImagePresentation> findOne(Long id);

    /**
     * Delete the "id" imagePresentation.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
