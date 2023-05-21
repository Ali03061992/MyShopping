package com.tn.shopping.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.tn.shopping.IntegrationTest;
import com.tn.shopping.domain.ObjectContainingImage;
import com.tn.shopping.repository.EntityManager;
import com.tn.shopping.repository.ObjectContainingImageRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ObjectContainingImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ObjectContainingImageResourceIT {

    private static final byte[] DEFAULT_NAME = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_NAME = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_NAME_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_NAME_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/object-containing-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectContainingImageRepository objectContainingImageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ObjectContainingImage objectContainingImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ObjectContainingImage createEntity(EntityManager em) {
        ObjectContainingImage objectContainingImage = new ObjectContainingImage()
            .name(DEFAULT_NAME)
            .nameContentType(DEFAULT_NAME_CONTENT_TYPE);
        return objectContainingImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ObjectContainingImage createUpdatedEntity(EntityManager em) {
        ObjectContainingImage objectContainingImage = new ObjectContainingImage()
            .name(UPDATED_NAME)
            .nameContentType(UPDATED_NAME_CONTENT_TYPE);
        return objectContainingImage;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ObjectContainingImage.class).block();
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
        objectContainingImage = createEntity(em);
    }

    @Test
    void createObjectContainingImage() throws Exception {
        int databaseSizeBeforeCreate = objectContainingImageRepository.findAll().collectList().block().size();
        // Create the ObjectContainingImage
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(objectContainingImage))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeCreate + 1);
        ObjectContainingImage testObjectContainingImage = objectContainingImageList.get(objectContainingImageList.size() - 1);
        assertThat(testObjectContainingImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testObjectContainingImage.getNameContentType()).isEqualTo(DEFAULT_NAME_CONTENT_TYPE);
    }

    @Test
    void createObjectContainingImageWithExistingId() throws Exception {
        // Create the ObjectContainingImage with an existing ID
        objectContainingImage.setId(1L);

        int databaseSizeBeforeCreate = objectContainingImageRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(objectContainingImage))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllObjectContainingImagesAsStream() {
        // Initialize the database
        objectContainingImageRepository.save(objectContainingImage).block();

        List<ObjectContainingImage> objectContainingImageList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ObjectContainingImage.class)
            .getResponseBody()
            .filter(objectContainingImage::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(objectContainingImageList).isNotNull();
        assertThat(objectContainingImageList).hasSize(1);
        ObjectContainingImage testObjectContainingImage = objectContainingImageList.get(0);
        assertThat(testObjectContainingImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testObjectContainingImage.getNameContentType()).isEqualTo(DEFAULT_NAME_CONTENT_TYPE);
    }

    @Test
    void getAllObjectContainingImages() {
        // Initialize the database
        objectContainingImageRepository.save(objectContainingImage).block();

        // Get all the objectContainingImageList
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
            .value(hasItem(objectContainingImage.getId().intValue()))
            .jsonPath("$.[*].nameContentType")
            .value(hasItem(DEFAULT_NAME_CONTENT_TYPE))
            .jsonPath("$.[*].name")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_NAME)));
    }

    @Test
    void getObjectContainingImage() {
        // Initialize the database
        objectContainingImageRepository.save(objectContainingImage).block();

        // Get the objectContainingImage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, objectContainingImage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(objectContainingImage.getId().intValue()))
            .jsonPath("$.nameContentType")
            .value(is(DEFAULT_NAME_CONTENT_TYPE))
            .jsonPath("$.name")
            .value(is(Base64Utils.encodeToString(DEFAULT_NAME)));
    }

    @Test
    void getNonExistingObjectContainingImage() {
        // Get the objectContainingImage
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingObjectContainingImage() throws Exception {
        // Initialize the database
        objectContainingImageRepository.save(objectContainingImage).block();

        int databaseSizeBeforeUpdate = objectContainingImageRepository.findAll().collectList().block().size();

        // Update the objectContainingImage
        ObjectContainingImage updatedObjectContainingImage = objectContainingImageRepository
            .findById(objectContainingImage.getId())
            .block();
        updatedObjectContainingImage.name(UPDATED_NAME).nameContentType(UPDATED_NAME_CONTENT_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedObjectContainingImage.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedObjectContainingImage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeUpdate);
        ObjectContainingImage testObjectContainingImage = objectContainingImageList.get(objectContainingImageList.size() - 1);
        assertThat(testObjectContainingImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjectContainingImage.getNameContentType()).isEqualTo(UPDATED_NAME_CONTENT_TYPE);
    }

    @Test
    void putNonExistingObjectContainingImage() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingImageRepository.findAll().collectList().block().size();
        objectContainingImage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, objectContainingImage.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(objectContainingImage))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchObjectContainingImage() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingImageRepository.findAll().collectList().block().size();
        objectContainingImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(objectContainingImage))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamObjectContainingImage() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingImageRepository.findAll().collectList().block().size();
        objectContainingImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(objectContainingImage))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateObjectContainingImageWithPatch() throws Exception {
        // Initialize the database
        objectContainingImageRepository.save(objectContainingImage).block();

        int databaseSizeBeforeUpdate = objectContainingImageRepository.findAll().collectList().block().size();

        // Update the objectContainingImage using partial update
        ObjectContainingImage partialUpdatedObjectContainingImage = new ObjectContainingImage();
        partialUpdatedObjectContainingImage.setId(objectContainingImage.getId());

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedObjectContainingImage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedObjectContainingImage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeUpdate);
        ObjectContainingImage testObjectContainingImage = objectContainingImageList.get(objectContainingImageList.size() - 1);
        assertThat(testObjectContainingImage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testObjectContainingImage.getNameContentType()).isEqualTo(DEFAULT_NAME_CONTENT_TYPE);
    }

    @Test
    void fullUpdateObjectContainingImageWithPatch() throws Exception {
        // Initialize the database
        objectContainingImageRepository.save(objectContainingImage).block();

        int databaseSizeBeforeUpdate = objectContainingImageRepository.findAll().collectList().block().size();

        // Update the objectContainingImage using partial update
        ObjectContainingImage partialUpdatedObjectContainingImage = new ObjectContainingImage();
        partialUpdatedObjectContainingImage.setId(objectContainingImage.getId());

        partialUpdatedObjectContainingImage.name(UPDATED_NAME).nameContentType(UPDATED_NAME_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedObjectContainingImage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedObjectContainingImage))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeUpdate);
        ObjectContainingImage testObjectContainingImage = objectContainingImageList.get(objectContainingImageList.size() - 1);
        assertThat(testObjectContainingImage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testObjectContainingImage.getNameContentType()).isEqualTo(UPDATED_NAME_CONTENT_TYPE);
    }

    @Test
    void patchNonExistingObjectContainingImage() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingImageRepository.findAll().collectList().block().size();
        objectContainingImage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, objectContainingImage.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(objectContainingImage))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchObjectContainingImage() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingImageRepository.findAll().collectList().block().size();
        objectContainingImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(objectContainingImage))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamObjectContainingImage() throws Exception {
        int databaseSizeBeforeUpdate = objectContainingImageRepository.findAll().collectList().block().size();
        objectContainingImage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(objectContainingImage))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ObjectContainingImage in the database
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteObjectContainingImage() {
        // Initialize the database
        objectContainingImageRepository.save(objectContainingImage).block();

        int databaseSizeBeforeDelete = objectContainingImageRepository.findAll().collectList().block().size();

        // Delete the objectContainingImage
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, objectContainingImage.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ObjectContainingImage> objectContainingImageList = objectContainingImageRepository.findAll().collectList().block();
        assertThat(objectContainingImageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
