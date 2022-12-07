package com.tobias.reviewcudservice.domain;

import java.io.Serializable;
import javax.persistence.Embeddable;
import org.springframework.data.mongodb.core.mapping.Field;

@Embeddable
public class UserVo implements Serializable {

    @Field("userid")
    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Field("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Field("weight")
    private String weight;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    @Field("height")
    private String height;

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
