package ru.bbcv.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Limit limit;

    public void setLimit(Limit limit) {
        this.limit = limit;
        if (limit != null) {
            limit.setUser(this);
        }
    }
}
