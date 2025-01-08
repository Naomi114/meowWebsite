package tw.com.ispan.projfinal_back.domain;

import java.io.Serializable;

import javax.swing.text.html.HTML.Tag;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "productTag")
public class ProductTag {

    @EmbeddedId
    private ProductTagId id; // Change from ProductTagBean to ProductTagId

    @ManyToOne
    @MapsId("productId")
    private ProductBean product;

    @ManyToOne
    @MapsId("tagId")
    private Tag tag;

    // Getters and Setters
}

