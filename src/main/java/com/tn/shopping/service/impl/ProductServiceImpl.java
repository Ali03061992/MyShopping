package com.tn.shopping.service.impl;

import com.tn.shopping.domain.Product;
import com.tn.shopping.repository.ProductRepository;
import com.tn.shopping.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<Product> save(Product product) {
        log.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> update(Product product) {
        log.debug("Request to update Product : {}", product);
        return productRepository.save(product);
    }

    @Override
    public Mono<Product> partialUpdate(Product product) {
        log.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                if (product.getProductName() != null) {
                    existingProduct.setProductName(product.getProductName());
                }
                if (product.getProductPrice() != null) {
                    existingProduct.setProductPrice(product.getProductPrice());
                }

                return existingProduct;
            })
            .flatMap(productRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<Product> findAll(Pageable pageable) {
        log.debug("Request to get all Products");
        return productRepository.findAllBy(pageable);
    }

    public Flux<Product> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable);
    }

    public Mono<Long> countAll() {
        return productRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Mono<Product> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public Mono<Void> delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        return productRepository.deleteById(id);
    }
}
