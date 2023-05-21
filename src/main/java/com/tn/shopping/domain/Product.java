package com.tn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A Product.
 */
@Table("product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("product_name")
    private String productName;

    @NotNull(message = "must not be null")
    @Column("product_price")
    private Integer productPrice;

    @Transient
    @JsonIgnoreProperties(value = { "imagesPresentation" }, allowSetters = true)
    private Set<ImagePresentation> images = new HashSet<>();

    @Transient
    @JsonIgnoreProperties(value = { "productNames" }, allowSetters = true)
    private Set<ProductCategory> productCategories = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return this.productName;
    }

    public Product productName(String productName) {
        this.setProductName(productName);
        return this;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductPrice() {
        return this.productPrice;
    }

    public Product productPrice(Integer productPrice) {
        this.setProductPrice(productPrice);
        return this;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public Set<ImagePresentation> getImages() {
        return this.images;
    }

    public void setImages(Set<ImagePresentation> imagePresentations) {
        if (this.images != null) {
            this.images.forEach(i -> i.setImagesPresentation(null));
        }
        if (imagePresentations != null) {
            imagePresentations.forEach(i -> i.setImagesPresentation(this));
        }
        this.images = imagePresentations;
    }

    public Product images(Set<ImagePresentation> imagePresentations) {
        this.setImages(imagePresentations);
        return this;
    }

    public Product addImages(ImagePresentation imagePresentation) {
        this.images.add(imagePresentation);
        imagePresentation.setImagesPresentation(this);
        return this;
    }

    public Product removeImages(ImagePresentation imagePresentation) {
        this.images.remove(imagePresentation);
        imagePresentation.setImagesPresentation(null);
        return this;
    }

    public Set<ProductCategory> getProductCategories() {
        return this.productCategories;
    }

    public void setProductCategories(Set<ProductCategory> productCategories) {
        this.productCategories = productCategories;
    }

    public Product productCategories(Set<ProductCategory> productCategories) {
        this.setProductCategories(productCategories);
        return this;
    }

    public Product addProductCategory(ProductCategory productCategory) {
        this.productCategories.add(productCategory);
        productCategory.getProductNames().add(this);
        return this;
    }

    public Product removeProductCategory(ProductCategory productCategory) {
        this.productCategories.remove(productCategory);
        productCategory.getProductNames().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", productPrice=" + getProductPrice() +
            "}";
    }
}
