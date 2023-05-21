package com.tn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ImagePresentation.
 */
@Table("image_presentation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImagePresentation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("image")
    private byte[] image;

    @Column("image_content_type")
    private String imageContentType;

    @Transient
    @JsonIgnoreProperties(value = { "images", "productCategories" }, allowSetters = true)
    private Product imagesPresentation;

    @Column("images_presentation_id")
    private Long imagesPresentationId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ImagePresentation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getImage() {
        return this.image;
    }

    public ImagePresentation image(byte[] image) {
        this.setImage(image);
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public ImagePresentation imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Product getImagesPresentation() {
        return this.imagesPresentation;
    }

    public void setImagesPresentation(Product product) {
        this.imagesPresentation = product;
        this.imagesPresentationId = product != null ? product.getId() : null;
    }

    public ImagePresentation imagesPresentation(Product product) {
        this.setImagesPresentation(product);
        return this;
    }

    public Long getImagesPresentationId() {
        return this.imagesPresentationId;
    }

    public void setImagesPresentationId(Long product) {
        this.imagesPresentationId = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ImagePresentation)) {
            return false;
        }
        return id != null && id.equals(((ImagePresentation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImagePresentation{" +
            "id=" + getId() +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
