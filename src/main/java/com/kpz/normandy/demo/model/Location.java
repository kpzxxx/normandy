package com.kpz.normandy.demo.model;

import lombok.Data;

import java.sql.Date;


@Data
public class Location {
    private Long id;

    private Double lng;

    private Double lat;

    private String name;

    private Date gmtCreate;

    private Date gmtModified;
}
