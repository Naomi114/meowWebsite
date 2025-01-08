package tw.com.ispan.projfinal_back.domain;

import java.io.Serializable;

import jakarta.persistence.Embeddable;

@Embeddable
public class ProductTagId implements Serializable {  // Changed name to ProductTagId
    private Integer productId;
    private Integer tagId;

    // Getters, Setters, equals, hashCode
}
