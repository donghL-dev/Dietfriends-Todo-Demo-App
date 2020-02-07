package com.donghun.todo.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "idx")
@Entity @Table @Builder
public class Token implements Serializable {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idx;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 500)
    private String token;
}
