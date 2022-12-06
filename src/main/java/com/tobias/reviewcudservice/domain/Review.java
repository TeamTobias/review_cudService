package com.tobias.reviewcudservice.domain;

import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.relational.core.mapping.Embedded;

/**
 * A Review.
 */
@Document(collection = "review")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Review implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("img_name")
    private String imgName;

    @Field("description")
    private String description;

    @Field("created_at")
    private Instant createdAt;

    @Embedded(onEmpty = null)
    private UserVo userVo;

    @Field("catalogid")
    private String catalogid;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Review id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgName() {
        return this.imgName;
    }

    public Review imgName(String imgName) {
        this.setImgName(imgName);
        return this;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getDescription() {
        return this.description;
    }

    public Review description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Review createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserid() {
        return this.userVo.getUserid();
    }

    public Review userid(String userid) {
        this.setUserid(userid);
        return this;
    }

    public void setUserid(String userid) {
        this.userVo.setUserid(userid);
    }

    public String getName() {
        return this.userVo.getName();
    }

    public Review name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.userVo.setName(name);
    }

    public String getWeight() {
        return this.userVo.getWeight();
    }

    public Review weight(String weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(String weight) {
        this.userVo.setWeight(weight);
    }

    public String getHeight() {
        return this.userVo.getHeight();
    }

    public Review height(String height) {
        this.setHeight(height);
        return this;
    }

    public void setHeight(String height) {
        this.userVo.setHeight(height);
    }

    public String getCatalogid() {
        return this.catalogid;
    }

    public Review catalogid(String catalogid) {
        this.setCatalogid(catalogid);
        return this;
    }

    public void setCatalogid(String catalogid) {
        this.catalogid = catalogid;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Review)) {
            return false;
        }
        return id != null && id.equals(((Review) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Review{" +
            "id=" + getId() +
            ", imgName='" + getImgName() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", userid='" + getUserid() + "'" +
            ", name='" + getName() + "'" +
            ", weight='" + getWeight() + "'" +
            ", height='" + getHeight() + "'" +
            ", catalogid='" + getCatalogid() + "'" +
            "}";
    }
}
