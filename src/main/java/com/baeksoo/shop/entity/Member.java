
package com.baeksoo.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

    @Entity
    @ToString
    @Setter
    @Getter
    public class Member {

        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(unique=true)
        private String username;

        @Column(length = 60, unique = true)
        private String password;

        @Column
        private String displayName;

    }



