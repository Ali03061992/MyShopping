package com.tn.shopping.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A ObjectContainingImage.
 */
@Table("object_containing_image")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ObjectContainingImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column("id")
    private Long id;

    @Column("name")
    private byte[] name;

    @Column("name_content_type")
    private String nameContentType;

    @Transient
    @JsonIgnoreProperties(value = { "images", "productCategories" }, allowSetters = true)
    private Product json;

    @Column("json_id")
    private Long jsonId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ObjectContainingImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getName() {
        return this.name;
    }

    public ObjectContainingImage name(byte[] name) {
        this.setName(name);
        return this;
    }

    public void setName(byte[] name) {
        this.name = name;
    }

    public String getNameContentType() {
        return this.nameContentType;
    }

    public ObjectContainingImage nameContentType(String nameContentType) {
        this.nameContentType = nameContentType;
        return this;
    }

    public void setNameContentType(String nameContentType) {
        this.nameContentType = nameContentType;
    }

    public Product getJson() {
        return this.json;
    }

    public void setJson(Product product) {
        this.json = product;
        this.jsonId = product != null ? product.getId() : null;
    }

    public ObjectContainingImage json(Product product) {
        this.setJson(product);
        return this;
    }

    public Long getJsonId() {
        return this.jsonId;
    }

    public void setJsonId(Long product) {
        this.jsonId = product;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ObjectContainingImage)) {
            return false;
        }
        return id != null && id.equals(((ObjectContainingImage) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ObjectContainingImage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", nameContentType='" + getNameContentType() + "'" +
            "}";
    }
}
