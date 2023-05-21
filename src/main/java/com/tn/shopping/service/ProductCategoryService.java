package com.tn.shopping.service;

import com.tn.shopping.domain.ProductCategory;
import java.util.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Interface for managing {@link ProductCategory}.
 */
public interface ProductCategoryService {
    /**
     * Save a productCategory.
     *
     * @param productCategory the entity to save.
     * @return the persisted entity.
     */
    Mono<ProductCategory> save(ProductCategory productCategory);

    /**
     * Updates a productCategory.
     *
     * @param productCategory the entity to update.
     * @return the persisted entity.
     */
    Mono<ProductCategory> update(ProductCategory productCategory);

    /**
     * Partially updates a productCategory.
     *
     * @param productCategory the entity to update partially.
     * @return the persisted entity.
     */
    Mono<ProductCategory> partialUpdate(ProductCategory productCategory);

    /**
     * Get all the productCategories.
     *
     * @return the list of entities.
     */
    Flux<ProductCategory> findAll();

    /**
     * Returns the number of productCategories available.
     * @return the number of entities in the database.
     *
     */
    Mono<Long> countAll();

    /**
     * Get the "id" productCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Mono<ProductCategory> findOne(Long id);

    /**
     * Delete the "id" productCategory.
     *
     * @param id the id of the entity.
     * @return a Mono to signal the deletion
     */
    Mono<Void> delete(Long id);
}
