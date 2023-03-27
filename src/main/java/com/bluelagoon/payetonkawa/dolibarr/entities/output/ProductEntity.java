package com.bluelagoon.payetonkawa.dolibarr.entities.output;

import lombok.Data;

@Data
public class ProductEntity {
    private String id;
    private String label;
    private String description;
    private String price;
    private String stock_reel;
    private String url;
}
