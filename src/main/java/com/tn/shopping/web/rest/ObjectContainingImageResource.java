package com.tn.shopping.web.rest;

import com.tn.shopping.domain.ObjectContainingImage;
import com.tn.shopping.repository.ObjectContainingImageRepository;
import com.tn.shopping.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.tn.shopping.domain.ObjectContainingImage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ObjectContainingImageResource {

    private final Logger log = LoggerFactory.getLogger(ObjectContainingImageResource.class);

    private static final String ENTITY_NAME = "objectContainingImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ObjectContainingImageRepository objectContainingImageRepository;

    public ObjectContainingImageResource(ObjectContainingImageRepository objectContainingImageRepository) {
        this.objectContainingImageRepository = objectContainingImageRepository;
    }

    /**
     * {@code POST  /object-containing-images} : Create a new objectContainingImage.
     *
     * @param objectContainingImage the objectContainingImage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new objectContainingImage, or with status {@code 400 (Bad Request)} if the objectContainingImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/object-containing-images")
    public Mono<ResponseEntity<ObjectContainingImage>> createObjectContainingImage(
        @RequestBody ObjectContainingImage objectContainingImage
    ) throws URISyntaxException {
        log.debug("REST request to save ObjectContainingImage : {}", objectContainingImage);
        if (objectContainingImage.getId() != null) {
            throw new BadRequestAlertException("A new objectContainingImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return objectContainingImageRepository
            .save(objectContainingImage)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/object-containing-images/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /object-containing-images/:id} : Updates an existing objectContainingImage.
     *
     * @param id the id of the objectContainingImage to save.
     * @param objectContainingImage the objectContainingImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objectContainingImage,
     * or with status {@code 400 (Bad Request)} if the objectContainingImage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the objectContainingImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/object-containing-images/{id}")
    public Mono<ResponseEntity<ObjectContainingImage>> updateObjectContainingImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ObjectContainingImage objectContainingImage
    ) throws URISyntaxException {
        log.debug("REST request to update ObjectContainingImage : {}, {}", id, objectContainingImage);
        if (objectContainingImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objectContainingImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return objectContainingImageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return objectContainingImageRepository
                    .save(objectContainingImage)
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(result ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                            .body(result)
                    );
            });
    }

    /**
     * {@code PATCH  /object-containing-images/:id} : Partial updates given fields of an existing objectContainingImage, field will ignore if it is null
     *
     * @param id the id of the objectContainingImage to save.
     * @param objectContainingImage the objectContainingImage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated objectContainingImage,
     * or with status {@code 400 (Bad Request)} if the objectContainingImage is not valid,
     * or with status {@code 404 (Not Found)} if the objectContainingImage is not found,
     * or with status {@code 500 (Internal Server Error)} if the objectContainingImage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/object-containing-images/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ObjectContainingImage>> partialUpdateObjectContainingImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ObjectContainingImage objectContainingImage
    ) throws URISyntaxException {
        log.debug("REST request to partial update ObjectContainingImage partially : {}, {}", id, objectContainingImage);
        if (objectContainingImage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, objectContainingImage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return objectContainingImageRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ObjectContainingImage> result = objectContainingImageRepository
                    .findById(objectContainingImage.getId())
                    .map(existingObjectContainingImage -> {
                        if (objectContainingImage.getName() != null) {
                            existingObjectContainingImage.setName(objectContainingImage.getName());
                        }
                        if (objectContainingImage.getNameContentType() != null) {
                            existingObjectContainingImage.setNameContentType(objectContainingImage.getNameContentType());
                        }

                        return existingObjectContainingImage;
                    })
                    .flatMap(objectContainingImageRepository::save);

                return result
                    .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                    .map(res ->
                        ResponseEntity
                            .ok()
                            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, res.getId().toString()))
                            .body(res)
                    );
            });
    }

    /**
     * {@code GET  /object-containing-images} : get all the objectContainingImages.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of objectContainingImages in body.
     */
    @GetMapping("/object-containing-images")
    public Mono<List<ObjectContainingImage>> getAllObjectContainingImages() {
        log.debug("REST request to get all ObjectContainingImages");
        return objectContainingImageRepository.findAll().collectList();
    }

    /**
     * {@code GET  /object-containing-images} : get all the objectContainingImages as a stream.
     * @return the {@link Flux} of objectContainingImages.
     */
    @GetMapping(value = "/object-containing-images", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ObjectContainingImage> getAllObjectContainingImagesAsStream() {
        log.debug("REST request to get all ObjectContainingImages as a stream");
        return objectContainingImageRepository.findAll();
    }

    /**
     * {@code GET  /object-containing-images/:id} : get the "id" objectContainingImage.
     *
     * @param id the id of the objectContainingImage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the objectContainingImage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/object-containing-images/{id}")
    public Mono<ResponseEntity<ObjectContainingImage>> getObjectContainingImage(@PathVariable Long id) {
        log.debug("REST request to get ObjectContainingImage : {}", id);
        Mono<ObjectContainingImage> objectContainingImage = objectContainingImageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(objectContainingImage);
    }

    /**
     * {@code DELETE  /object-containing-images/:id} : delete the "id" objectContainingImage.
     *
     * @param id the id of the objectContainingImage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/object-containing-images/{id}")
    public Mono<ResponseEntity<Void>> deleteObjectContainingImage(@PathVariable Long id) {
        log.debug("REST request to delete ObjectContainingImage : {}", id);
        return objectContainingImageRepository
            .deleteById(id)
            .then(
                Mono.just(
                    ResponseEntity
                        .noContent()
                        .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
                        .build()
                )
            );
    }
}
