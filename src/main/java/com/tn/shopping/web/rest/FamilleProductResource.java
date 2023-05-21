package com.tn.shopping.web.rest;

import com.tn.shopping.domain.FamilleProduct;
import com.tn.shopping.repository.FamilleProductRepository;
import com.tn.shopping.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.tn.shopping.domain.FamilleProduct}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class FamilleProductResource {

    private final Logger log = LoggerFactory.getLogger(FamilleProductResource.class);

    private static final String ENTITY_NAME = "familleProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FamilleProductRepository familleProductRepository;

    public FamilleProductResource(FamilleProductRepository familleProductRepository) {
        this.familleProductRepository = familleProductRepository;
    }

    /**
     * {@code POST  /famille-products} : Create a new familleProduct.
     *
     * @param familleProduct the familleProduct to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new familleProduct, or with status {@code 400 (Bad Request)} if the familleProduct has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/famille-products")
    public Mono<ResponseEntity<FamilleProduct>> createFamilleProduct(@Valid @RequestBody FamilleProduct familleProduct)
        throws URISyntaxException {
        log.debug("REST request to save FamilleProduct : {}", familleProduct);
        if (familleProduct.getId() != null) {
            throw new BadRequestAlertException("A new familleProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        return familleProductRepository
            .save(familleProduct)
            .map(result -> {
                try {
                    return ResponseEntity
                        .created(new URI("/api/famille-products/" + result.getId()))
                        .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                        .body(result);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * {@code PUT  /famille-products/:id} : Updates an existing familleProduct.
     *
     * @param id the id of the familleProduct to save.
     * @param familleProduct the familleProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familleProduct,
     * or with status {@code 400 (Bad Request)} if the familleProduct is not valid,
     * or with status {@code 500 (Internal Server Error)} if the familleProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/famille-products/{id}")
    public Mono<ResponseEntity<FamilleProduct>> updateFamilleProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FamilleProduct familleProduct
    ) throws URISyntaxException {
        log.debug("REST request to update FamilleProduct : {}, {}", id, familleProduct);
        if (familleProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familleProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return familleProductRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                return familleProductRepository
                    .save(familleProduct)
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
     * {@code PATCH  /famille-products/:id} : Partial updates given fields of an existing familleProduct, field will ignore if it is null
     *
     * @param id the id of the familleProduct to save.
     * @param familleProduct the familleProduct to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated familleProduct,
     * or with status {@code 400 (Bad Request)} if the familleProduct is not valid,
     * or with status {@code 404 (Not Found)} if the familleProduct is not found,
     * or with status {@code 500 (Internal Server Error)} if the familleProduct couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/famille-products/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public Mono<ResponseEntity<FamilleProduct>> partialUpdateFamilleProduct(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FamilleProduct familleProduct
    ) throws URISyntaxException {
        log.debug("REST request to partial update FamilleProduct partially : {}, {}", id, familleProduct);
        if (familleProduct.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, familleProduct.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        return familleProductRepository
            .existsById(id)
            .flatMap(exists -> {
                if (!exists) {
                    return Mono.error(new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound"));
                }

                Mono<FamilleProduct> result = familleProductRepository
                    .findById(familleProduct.getId())
                    .map(existingFamilleProduct -> {
                        if (familleProduct.getName() != null) {
                            existingFamilleProduct.setName(familleProduct.getName());
                        }
                        if (familleProduct.getType() != null) {
                            existingFamilleProduct.setType(familleProduct.getType());
                        }

                        return existingFamilleProduct;
                    })
                    .flatMap(familleProductRepository::save);

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
     * {@code GET  /famille-products} : get all the familleProducts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of familleProducts in body.
     */
    @GetMapping("/famille-products")
    public Mono<List<FamilleProduct>> getAllFamilleProducts() {
        log.debug("REST request to get all FamilleProducts");
        return familleProductRepository.findAll().collectList();
    }

    /**
     * {@code GET  /famille-products} : get all the familleProducts as a stream.
     * @return the {@link Flux} of familleProducts.
     */
    @GetMapping(value = "/famille-products", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<FamilleProduct> getAllFamilleProductsAsStream() {
        log.debug("REST request to get all FamilleProducts as a stream");
        return familleProductRepository.findAll();
    }

    /**
     * {@code GET  /famille-products/:id} : get the "id" familleProduct.
     *
     * @param id the id of the familleProduct to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the familleProduct, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/famille-products/{id}")
    public Mono<ResponseEntity<FamilleProduct>> getFamilleProduct(@PathVariable Long id) {
        log.debug("REST request to get FamilleProduct : {}", id);
        Mono<FamilleProduct> familleProduct = familleProductRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(familleProduct);
    }

    /**
     * {@code DELETE  /famille-products/:id} : delete the "id" familleProduct.
     *
     * @param id the id of the familleProduct to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/famille-products/{id}")
    public Mono<ResponseEntity<Void>> deleteFamilleProduct(@PathVariable Long id) {
        log.debug("REST request to delete FamilleProduct : {}", id);
        return familleProductRepository
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
