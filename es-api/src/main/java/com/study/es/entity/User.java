package com.study.es.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Program Name: es-study-api
 * Created by yanlp on 2021-01-03
 *
 * @author yanlp
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Builder
public class User {
    private String name;
    private int age;
}
