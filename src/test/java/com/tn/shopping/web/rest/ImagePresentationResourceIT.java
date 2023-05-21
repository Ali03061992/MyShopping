package com.tn.shopping.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import com.tn.shopping.IntegrationTest;
import com.tn.shopping.domain.ImagePresentation;
import com.tn.shopping.repository.EntityManager;
import com.tn.shopping.repository.ImagePresentationRepository;
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
 * Integration tests for the {@link ImagePresentationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureWebTestClient(timeout = IntegrationTest.DEFAULT_ENTITY_TIMEOUT)
@WithMockUser
class ImagePresentationResourceIT {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/image-presentations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImagePresentationRepository imagePresentationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private WebTestClient webTestClient;

    private ImagePresentation imagePresentation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImagePresentation createEntity(EntityManager em) {
        ImagePresentation imagePresentation = new ImagePresentation().image(DEFAULT_IMAGE).imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return imagePresentation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImagePresentation createUpdatedEntity(EntityManager em) {
        ImagePresentation imagePresentation = new ImagePresentation().image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return imagePresentation;
    }

    public static void deleteEntities(EntityManager em) {
        try {
            em.deleteAll(ImagePresentation.class).block();
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
        imagePresentation = createEntity(em);
    }

    @Test
    void createImagePresentation() throws Exception {
        int databaseSizeBeforeCreate = imagePresentationRepository.findAll().collectList().block().size();
        // Create the ImagePresentation
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imagePresentation))
            .exchange()
            .expectStatus()
            .isCreated();

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeCreate + 1);
        ImagePresentation testImagePresentation = imagePresentationList.get(imagePresentationList.size() - 1);
        assertThat(testImagePresentation.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testImagePresentation.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    void createImagePresentationWithExistingId() throws Exception {
        // Create the ImagePresentation with an existing ID
        imagePresentation.setId(1L);

        int databaseSizeBeforeCreate = imagePresentationRepository.findAll().collectList().block().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        webTestClient
            .post()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imagePresentation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllImagePresentationsAsStream() {
        // Initialize the database
        imagePresentationRepository.save(imagePresentation).block();

        List<ImagePresentation> imagePresentationList = webTestClient
            .get()
            .uri(ENTITY_API_URL)
            .accept(MediaType.APPLICATION_NDJSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentTypeCompatibleWith(MediaType.APPLICATION_NDJSON)
            .returnResult(ImagePresentation.class)
            .getResponseBody()
            .filter(imagePresentation::equals)
            .collectList()
            .block(Duration.ofSeconds(5));

        assertThat(imagePresentationList).isNotNull();
        assertThat(imagePresentationList).hasSize(1);
        ImagePresentation testImagePresentation = imagePresentationList.get(0);
        assertThat(testImagePresentation.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testImagePresentation.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    void getAllImagePresentations() {
        // Initialize the database
        imagePresentationRepository.save(imagePresentation).block();

        // Get all the imagePresentationList
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
            .value(hasItem(imagePresentation.getId().intValue()))
            .jsonPath("$.[*].imageContentType")
            .value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE))
            .jsonPath("$.[*].image")
            .value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    void getImagePresentation() {
        // Initialize the database
        imagePresentationRepository.save(imagePresentation).block();

        // Get the imagePresentation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, imagePresentation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .jsonPath("$.id")
            .value(is(imagePresentation.getId().intValue()))
            .jsonPath("$.imageContentType")
            .value(is(DEFAULT_IMAGE_CONTENT_TYPE))
            .jsonPath("$.image")
            .value(is(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    void getNonExistingImagePresentation() {
        // Get the imagePresentation
        webTestClient
            .get()
            .uri(ENTITY_API_URL_ID, Long.MAX_VALUE)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNotFound();
    }

    @Test
    void putExistingImagePresentation() throws Exception {
        // Initialize the database
        imagePresentationRepository.save(imagePresentation).block();

        int databaseSizeBeforeUpdate = imagePresentationRepository.findAll().collectList().block().size();

        // Update the imagePresentation
        ImagePresentation updatedImagePresentation = imagePresentationRepository.findById(imagePresentation.getId()).block();
        updatedImagePresentation.image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, updatedImagePresentation.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(updatedImagePresentation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeUpdate);
        ImagePresentation testImagePresentation = imagePresentationList.get(imagePresentationList.size() - 1);
        assertThat(testImagePresentation.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImagePresentation.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    void putNonExistingImagePresentation() throws Exception {
        int databaseSizeBeforeUpdate = imagePresentationRepository.findAll().collectList().block().size();
        imagePresentation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, imagePresentation.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imagePresentation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchImagePresentation() throws Exception {
        int databaseSizeBeforeUpdate = imagePresentationRepository.findAll().collectList().block().size();
        imagePresentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imagePresentation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamImagePresentation() throws Exception {
        int databaseSizeBeforeUpdate = imagePresentationRepository.findAll().collectList().block().size();
        imagePresentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .put()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(TestUtil.convertObjectToJsonBytes(imagePresentation))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateImagePresentationWithPatch() throws Exception {
        // Initialize the database
        imagePresentationRepository.save(imagePresentation).block();

        int databaseSizeBeforeUpdate = imagePresentationRepository.findAll().collectList().block().size();

        // Update the imagePresentation using partial update
        ImagePresentation partialUpdatedImagePresentation = new ImagePresentation();
        partialUpdatedImagePresentation.setId(imagePresentation.getId());

        partialUpdatedImagePresentation.image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedImagePresentation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedImagePresentation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeUpdate);
        ImagePresentation testImagePresentation = imagePresentationList.get(imagePresentationList.size() - 1);
        assertThat(testImagePresentation.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImagePresentation.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    void fullUpdateImagePresentationWithPatch() throws Exception {
        // Initialize the database
        imagePresentationRepository.save(imagePresentation).block();

        int databaseSizeBeforeUpdate = imagePresentationRepository.findAll().collectList().block().size();

        // Update the imagePresentation using partial update
        ImagePresentation partialUpdatedImagePresentation = new ImagePresentation();
        partialUpdatedImagePresentation.setId(imagePresentation.getId());

        partialUpdatedImagePresentation.image(UPDATED_IMAGE).imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, partialUpdatedImagePresentation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(partialUpdatedImagePresentation))
            .exchange()
            .expectStatus()
            .isOk();

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeUpdate);
        ImagePresentation testImagePresentation = imagePresentationList.get(imagePresentationList.size() - 1);
        assertThat(testImagePresentation.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testImagePresentation.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    void patchNonExistingImagePresentation() throws Exception {
        int databaseSizeBeforeUpdate = imagePresentationRepository.findAll().collectList().block().size();
        imagePresentation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, imagePresentation.getId())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(imagePresentation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchImagePresentation() throws Exception {
        int databaseSizeBeforeUpdate = imagePresentationRepository.findAll().collectList().block().size();
        imagePresentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL_ID, count.incrementAndGet())
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(imagePresentation))
            .exchange()
            .expectStatus()
            .isBadRequest();

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamImagePresentation() throws Exception {
        int databaseSizeBeforeUpdate = imagePresentationRepository.findAll().collectList().block().size();
        imagePresentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        webTestClient
            .patch()
            .uri(ENTITY_API_URL)
            .contentType(MediaType.valueOf("application/merge-patch+json"))
            .bodyValue(TestUtil.convertObjectToJsonBytes(imagePresentation))
            .exchange()
            .expectStatus()
            .isEqualTo(405);

        // Validate the ImagePresentation in the database
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteImagePresentation() {
        // Initialize the database
        imagePresentationRepository.save(imagePresentation).block();

        int databaseSizeBeforeDelete = imagePresentationRepository.findAll().collectList().block().size();

        // Delete the imagePresentation
        webTestClient
            .delete()
            .uri(ENTITY_API_URL_ID, imagePresentation.getId())
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus()
            .isNoContent();

        // Validate the database contains one less item
        List<ImagePresentation> imagePresentationList = imagePresentationRepository.findAll().collectList().block();
        assertThat(imagePresentationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
