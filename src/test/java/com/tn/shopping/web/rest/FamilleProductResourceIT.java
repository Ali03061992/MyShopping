package com.tn.shopping.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.tn.shopping.IntegrationTest;
import com.tn.shopping.domain.FamilleProduct;
import com.tn.shopping.repository.EntityManager;
import com.tn.shopping.repository.FamilleProductRepository;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Integration tests for the {@link FamilleProductResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class FamilleProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/famille-products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FamilleProductRepository familleProductRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private FamilleProduct familleProduct;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilleProduct createEntity(EntityManager em) {
        FamilleProduct familleProduct = new FamilleProduct().name(DEFAULT_NAME).type(DEFAULT_TYPE);
        return familleProduct;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FamilleProduct createUpdatedEntity(EntityManager em) {
        FamilleProduct familleProduct = new FamilleProduct().name(UPDATED_NAME).type(UPDATED_TYPE);
        return familleProduct;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(FamilleProduct.class).block();
        } catch (Exception e) {
            // It can fail, if other entities are still referring this - it will be removed later.
        }
    }

    @AfterEach
    public void cleanup() {
        deleteEntities(em);
    }

    @BeforeEach
    public void initTest() {
        deleteEntities(em);
        familleProduct = createEntity(em);
    }

    @Test
    void createFamilleProduct() throws Exception {
        int databaseSizeBeforeCreate = familleProductRepository.findAll().collectList().block().size();
        // Create the FamilleProduct
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(familleProduct))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeCreate + 1);
        FamilleProduct testFamilleProduct = familleProductList.get(familleProductList.size() - 1);
        assertThat(testFamilleProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamilleProduct.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void createFamilleProductWithExistingId() throws Exception {
        // Create the FamilleProduct with an existing ID
        familleProduct.setId(1L);

        int databaseSizeBeforeCreate = familleProductRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(familleProduct))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = familleProductRepository.findAll().collectList().block().size();
        // set the field null
        familleProduct.setName(null);

        // Create the FamilleProduct, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(familleProduct))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = familleProductRepository.findAll().collectList().block().size();
        // set the field null
        familleProduct.setType(null);

        // Create the FamilleProduct, which fails.

        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(familleProduct))
            .exchange()
            .expectStatus()
            .isBadRequest();

        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllFamilleProductsAsStream() {
        // Initialize the database
        familleProductRepository.save(familleProduct).block();

        List<FamilleProduct> familleProductList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(FamilleProduct.class)
            .getResponseBody()
            .filter(familleProduct::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(familleProductList).isNotNull();
        assertThat(familleProductList).hasSize(1);
        FamilleProduct testFamilleProduct = familleProductList.get(0);
        assertThat(testFamilleProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamilleProduct.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    void getAllFamilleProducts() {
        // Initialize the database
        familleProductRepository.save(familleProduct).block();

        // Get all the familleProductList
        webTestClient
            .get()
            .uri(ENTITY_API_URL + "?sort=id,desc")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.[*].id")
            .value(hasItem(familleProduct.getId().intValue()))
            .jsonPath("$.[*].name")
            .value(hasItem(DEFAULT_NAME))
            .jsonPath("$.[*].type")
            .value(hasItem(DEFAULT_TYPE));
    }

    @Test
    void getFamilleProduct() {
        // Initialize the database
        familleProductRepository.save(familleProduct).block();

        // Get the familleProduct
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, familleProduct.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(familleProduct.getId().intValue()))
            .jsonPath("$.name")
            .value(is(DEFAULT_NAME))
            .jsonPath("$.type")
            .value(is(DEFAULT_TYPE));
    }

    @Test
    void getNonExistingFamilleProduct() {
        // Get the familleProduct
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingFamilleProduct() throws Exception {
        // Initialize the database
        familleProductRepository.save(familleProduct).block();

        int databaseSizeBeforeUpdate = familleProductRepository.findAll().collectList().block().size();

        // Update the familleProduct
        FamilleProduct updatedFamilleProduct = familleProductRepository.findById(familleProduct.getId()).block();
        updatedFamilleProduct.name(UPDATED_NAME).type(UPDATED_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedFamilleProduct.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedFamilleProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeUpdate);
        FamilleProduct testFamilleProduct = familleProductList.get(familleProductList.size() - 1);
        assertThat(testFamilleProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilleProduct.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void putNonExistingFamilleProduct() throws Exception {
        int databaseSizeBeforeUpdate = familleProductRepository.findAll().collectList().block().size();
        familleProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, familleProduct.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(familleProduct))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchFamilleProduct() throws Exception {
        int databaseSizeBeforeUpdate = familleProductRepository.findAll().collectList().block().size();
        familleProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(familleProduct))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamFamilleProduct() throws Exception {
        int databaseSizeBeforeUpdate = familleProductRepository.findAll().collectList().block().size();
        familleProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(familleProduct))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateFamilleProductWithPatch() throws Exception {
        // Initialize the database
        familleProductRepository.save(familleProduct).block();

        int databaseSizeBeforeUpdate = familleProductRepository.findAll().collectList().block().size();

        // Update the familleProduct using partial update
        FamilleProduct partialUpdatedFamilleProduct = new FamilleProduct();
        partialUpdatedFamilleProduct.setId(familleProduct.getId());

        partialUpdatedFamilleProduct.type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFamilleProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilleProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeUpdate);
        FamilleProduct testFamilleProduct = familleProductList.get(familleProductList.size() - 1);
        assertThat(testFamilleProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFamilleProduct.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void fullUpdateFamilleProductWithPatch() throws Exception {
        // Initialize the database
        familleProductRepository.save(familleProduct).block();

        int databaseSizeBeforeUpdate = familleProductRepository.findAll().collectList().block().size();

        // Update the familleProduct using partial update
        FamilleProduct partialUpdatedFamilleProduct = new FamilleProduct();
        partialUpdatedFamilleProduct.setId(familleProduct.getId());

        partialUpdatedFamilleProduct.name(UPDATED_NAME).type(UPDATED_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedFamilleProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedFamilleProduct))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeUpdate);
        FamilleProduct testFamilleProduct = familleProductList.get(familleProductList.size() - 1);
        assertThat(testFamilleProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFamilleProduct.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    void patchNonExistingFamilleProduct() throws Exception {
        int databaseSizeBeforeUpdate = familleProductRepository.findAll().collectList().block().size();
        familleProduct.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, familleProduct.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(familleProduct))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchFamilleProduct() throws Exception {
        int databaseSizeBeforeUpdate = familleProductRepository.findAll().collectList().block().size();
        familleProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(familleProduct))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamFamilleProduct() throws Exception {
        int databaseSizeBeforeUpdate = familleProductRepository.findAll().collectList().block().size();
        familleProduct.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(familleProduct))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the FamilleProduct in the database
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteFamilleProduct() {
        // Initialize the database
        familleProductRepository.save(familleProduct).block();

        int databaseSizeBeforeDelete = familleProductRepository.findAll().collectList().block().size();

        // Delete the familleProduct
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, familleProduct.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<FamilleProduct> familleProductList = familleProductRepository.findAll().collectList().block();
        assertThat(familleProductList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
