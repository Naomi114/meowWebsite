package tw.com.ispan.projfinal_back.domain;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "productTag")
public class ProductTagBean<Tag> {
    @EmbeddedId
    private ProductTagBean id;

    @ManyToOne
    @MapsId("productId")
    private ProductBean product;

    @ManyToOne
    @MapsId("tagId")
    private Tag tag;

    // Getters and Setters
}

@Embeddable
public class ProductTagBean implements Serializable {
    private Integer productId;
    private Integer tagId;

    // Getters, Setters, equals, hashCode
}

