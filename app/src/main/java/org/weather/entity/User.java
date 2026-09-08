package org.weather.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Users", indexes = {
        @Index(columnList = "login", unique = true, name = "user_login_index")
})
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, length = 100)
    private String login;

    @Column(nullable = false, updatable = false, length = 100)
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
