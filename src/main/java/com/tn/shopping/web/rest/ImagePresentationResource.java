package com.tn.shopping.web.rest;

import com.tn.shopping.domain.ImagePresentation;
import com.tn.shopping.repository.ImagePresentationRepository;
import com.tn.shopping.service.ImagePresentationService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.reactive.ResponseUtil;

/**
 * REST controller for managing {@link com.tn.shopping.domain.ImagePresentation}.
 */
@RestController
@RequestMapping("/api")
public class ImagePresentationResource {

    private final Logger log = LoggerFactory.getLogger(ImagePresentationResource.class);

    private static final String ENTITY_NAME = "imagePresentation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImagePresentationService imagePresentationService;

    private final ImagePresentationRepository imagePresentationRepository;

    public ImagePresentationResource(
        ImagePresentationService imagePresentationService,
        ImagePresentationRepository imagePresentationRepository
    ) {
        this.imagePresentationService = imagePresentationService;
        this.imagePresentationRepository = imagePresentationRepository;
    }

    /**
     * {@code POST  /image-presentations} : Create a new imagePresentation.
     *
     * @param imagePresentation the imagePresentation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imagePresentation, or with status {@code 400 (Bad Request)} if the imagePresentation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/image-presentations")
    public Mono<ResponseEntity<ImagePresentation>> createImagePresentation(@RequestBody ImagePresentation imagePresentation)
        throws URISyntaxException {
        log.debug("REST request to save ImagePresentation : {}", imagePresentation);
        if (imagePresentation.getId() != null) {
            throw new BadRequestAlertException("A new imagePresentation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return imagePresentationService
            .save(imagePresentation)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/image-presentations/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /image-presentations/:id} : Updates an existing imagePresentation.
     *
     * @param id the id of the imagePresentation to save.
     * @param imagePresentation the imagePresentation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagePresentation,
     * or with status {@code 400 (Bad Request)} if the imagePresentation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imagePresentation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/image-presentations/{id}")
    public Mono<ResponseEntity<ImagePresentation>> updateImagePresentation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImagePresentation imagePresentation
    ) throws URISyntaxException {
        log.debug("REST request to update ImagePresentation : {}, {}", id, imagePresentation);
        if (imagePresentation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagePresentation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return imagePresentationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return imagePresentationService
                    .update(imagePresentation)
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
     * {@code PATCH  /image-presentations/:id} : Partial updates given fields of an existing imagePresentation, field will ignore if it is null
     *
     * @param id the id of the imagePresentation to save.
     * @param imagePresentation the imagePresentation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagePresentation,
     * or with status {@code 400 (Bad Request)} if the imagePresentation is not valid,
     * or with status {@code 404 (Not Found)} if the imagePresentation is not found,
     * or with status {@code 500 (Internal Server Error)} if the imagePresentation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/image-presentations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<ImagePresentation>> partialUpdateImagePresentation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImagePresentation imagePresentation
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImagePresentation partially : {}, {}", id, imagePresentation);
        if (imagePresentation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagePresentation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return imagePresentationRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<ImagePresentation> result = imagePresentationService.partialUpdate(imagePresentation);

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
     * {@code GET  /image-presentations} : get all the imagePresentations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imagePresentations in body.
     */
    @GetMapping("/image-presentations")
    public Mono<List<ImagePresentation>> getAllImagePresentations() {
        log.debug("REST request to get all ImagePresentations");
        return imagePresentationService.findAll().collectList();
    }

    /**
     * {@code GET  /image-presentations} : get all the imagePresentations as a stream.
     * @return the {@link Flux} of imagePresentations.
     */
    @GetMapping(value = "/image-presentations", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ImagePresentation> getAllImagePresentationsAsStream() {
        log.debug("REST request to get all ImagePresentations as a stream");
        return imagePresentationService.findAll();
    }

    /**
     * {@code GET  /image-presentations/:id} : get the "id" imagePresentation.
     *
     * @param id the id of the imagePresentation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imagePresentation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/image-presentations/{id}")
    public Mono<ResponseEntity<ImagePresentation>> getImagePresentation(@PathVariable Long id) {
        log.debug("REST request to get ImagePresentation : {}", id);
        Mono<ImagePresentation> imagePresentation = imagePresentationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imagePresentation);
    }

    /**
     * {@code DELETE  /image-presentations/:id} : delete the "id" imagePresentation.
     *
     * @param id the id of the imagePresentation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/image-presentations/{id}")
    public Mono<ResponseEntity<Void>> deleteImagePresentation(@PathVariable Long id) {
        log.debug("REST request to delete ImagePresentation : {}", id);
        return imagePresentationService
            .delete(id)
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
