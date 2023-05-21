package com.tn.shopping.repository;

import com.tn.shopping.domain.ImagePresentation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Spring Data R2DBC repository for the ImagePresentation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImagePresentationRepository extends ReactiveCrudRepository<ImagePresentation, Long>, ImagePresentationRepositoryInternal {
    @Query("SELECT * FROM image_presentation entity WHERE entity.images_presentation_id = :id")
    Flux<ImagePresentation> findByImagesPresentation(Long id);

    @Query("SELECT * FROM image_presentation entity WHERE entity.images_presentation_id IS NULL")
    Flux<ImagePresentation> findAllWhereImagesPresentationIsNull();

    @Override
    <S extends ImagePresentation> Mono<S> save(S entity);

    @Override
    Flux<ImagePresentation> findAll();

    @Override
    Mono<ImagePresentation> findById(Long id);

    @Override
    Mono<Void> deleteById(Long id);
}

interface ImagePresentationRepositoryInternal {
    <S extends ImagePresentation> Mono<S> save(S entity);

    Flux<ImagePresentation> findAllBy(Pageable pageable);

    Flux<ImagePresentation> findAll();

    Mono<ImagePresentation> findById(Long id);
    // this is not supported at the moment because of https://github.com/jhipster/generator-jhipster/issues/18269
    // Flux<ImagePresentation> findAllBy(Pageable pageable, Criteria criteria);

}
