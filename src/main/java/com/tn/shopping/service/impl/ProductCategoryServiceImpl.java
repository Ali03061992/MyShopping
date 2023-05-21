package com.tn.shopping.service.impl;

import com.tn.shopping.domain.ProductCategory;
import com.tn.shopping.repository.ProductCategoryRepository;
import com.tn.shopping.service.ProductCategoryService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link ProductCategory}.
 */
@Service
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final Logger log = LoggerFactory.getLogger(ProductCategoryServiceImpl.class);

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @Override
    public Mono<ProductCategory> save(ProductCategory productCategory) {
        log.debug("Request to save ProductCategory : {}", productCategory);
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public Mono<ProductCategory> update(ProductCategory productCategory) {
        log.debug("Request to update ProductCategory : {}", productCategory);
        return productCategoryRepository.save(productCategory);
    }

    @Override
    public Mono<ProductCategory> partialUpdate(ProductCategory productCategory) {
        log.debug("Request to partially update ProductCategory : {}", productCategory);

        return productCategoryRepository
            .findById(productCategory.getId())
            .map(existingProductCategory -> {
                if (productCategory.getCategoryName() != null) {
                    existingProductCategory.setCategoryName(productCategory.getCategoryName());
                }

                return existingProductCategory;
            })
            .flatMap(productCategoryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<ProductCategory> findAll() {
        log.debug("Request to get all ProductCategories");
        return productCategoryRepository.findAll();
    }

    public Mono<Long> countAll() {
        return productCategoryRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<ProductCategory> findOne(Long id) {
        log.debug("Request to get ProductCategory : {}", id);
        return productCategoryRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete ProductCategory : {}", id);
        return productCategoryRepository.deleteById(id);
    }
}
