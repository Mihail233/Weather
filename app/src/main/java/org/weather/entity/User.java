package org.weather.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Users", indexes = {
        @Index(columnList = "login", unique = true, name = "user_login_index")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Это стоит делать в том случае, если ответственность за создание или обновление сущности, на которую ссылается столбец, лежит не на текущей сущности, а на другой.
    //это если updatable = false, insertable = false
    @Column(nullable = false, updatable = false, length = 100)
    private String login;

    @Column(nullable = false, updatable = false, length = 100)
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
