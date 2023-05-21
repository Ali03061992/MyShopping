package com.tn.shopping.repository;

import com.tn.shopping.domain.ObjectContainingImage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ObjectContainingImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ObjectContainingImageRepository
    extends ReactiveCrudRepository<ObjectContainingImage, Long>, ObjectContainingImageRepositoryInternal {
    @Query("SELECT * FROM object_containing_image entity WHERE entity.json_id = :id")
    Flux<ObjectContainingImage> findByJson(Long id);

    @Query("SELECT * FROM object_containing_image entity WHERE entity.json_id IS NULL")
    Flux<ObjectContainingImage> findAllWhereJsonIsNull();

    @Override
    <S extends ObjectContainingImage> Mono<S> save(S entity);

    @Override
    Flux<ObjectContainingImage> findAll();

    @Override
    Mono<ObjectContainingImage> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ObjectContainingImageRepositoryInternal {
    <S extends ObjectContainingImage> Mono<S> save(S entity);

    Flux<ObjectContainingImage> findAllBy(Pageable pageable);

    Flux<ObjectContainingImage> findAll();

    Mono<ObjectContainingImage> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ObjectContainingImage> findAllBy(Pageable pageable, Criteria criteria);

}
