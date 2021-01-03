package com.study.es.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Program Name: es-study-api
 * Created by yanlp on 2021-01-03
 *
 * @author yanlp
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    private String img;
    private String title;
    private String price;
}
