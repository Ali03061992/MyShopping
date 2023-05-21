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
 * A ProductCategory.
 */
@Table("product_category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @NotNull(message = "must not be null")
    @Column("category_name")
    private String categoryName;

    @Transient
    @JsonIgnoreProperties(value = { "images", "productCategories" }, allowSetters = true)
    private Set<Product> productNames = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProductCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public ProductCategory categoryName(String categoryName) {
        this.setCategoryName(categoryName);
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Set<Product> getProductNames() {
        return this.productNames;
    }

    public void setProductNames(Set<Product> products) {
        if (this.productNames != null) {
            this.productNames.forEach(i -> i.removeProductCategory(this));
        }
        if (products != null) {
            products.forEach(i -> i.addProductCategory(this));
        }
        this.productNames = products;
    }

    public ProductCategory productNames(Set<Product> products) {
        this.setProductNames(products);
        return this;
    }

    public ProductCategory addProductName(Product product) {
        this.productNames.add(product);
        product.getProductCategories().add(this);
        return this;
    }

    public ProductCategory removeProductName(Product product) {
        this.productNames.remove(product);
        product.getProductCategories().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductCategory)) {
            return false;
        }
        return id != null && id.equals(((ProductCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCategory{" +
            "id=" + getId() +
            ", categoryName='" + getCategoryName() + "'" +
            "}";
    }
}
