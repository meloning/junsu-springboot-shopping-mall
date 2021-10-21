package com.meloning.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "item_image")
public class ItemImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_image_id")
    private Long id;

    private String imageName;
    private String originalImageName;
    private String imageUrl;
    private Boolean representativeImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImage(String originalImageName, String imageName, String imageUrl) {
        this.originalImageName = originalImageName;
        this.imageName = imageName;
        this.imageUrl = imageUrl;
    }
}
