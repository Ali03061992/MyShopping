package com.tn.shopping.repository;

import com.tn.shopping.domain.FamilleProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the FamilleProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FamilleProductRepository extends ReactiveCrudRepository<FamilleProduct, Long>, FamilleProductRepositoryInternal {
    @Override
    <S extends FamilleProduct> Mono<S> save(S entity);

    @Override
    Flux<FamilleProduct> findAll();

    @Override
    Mono<FamilleProduct> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface FamilleProductRepositoryInternal {
    <S extends FamilleProduct> Mono<S> save(S entity);

    Flux<FamilleProduct> findAllBy(Pageable pageable);

    Flux<FamilleProduct> findAll();

    Mono<FamilleProduct> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<FamilleProduct> findAllBy(Pageable pageable, Criteria criteria);

}
