package org.weather.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Locations")
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false, length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    //Параметр precision(точность) указывает на максимальное количество цифр, которые может хранить число.
    //Параметр scale(масштаб) представляет максимальное количество цифр, которые может содержать число после запятой.
    //Например, для числа 23.5141 precision равно 6, а scale 4.
    //Если масштаб значения, которое нужно сохранить, превышает объявленный масштаб столбца(scale), система округлит его до заданного количества цифр после точки(просто откинет числа после scale)
    //Если же после этого количество цифр слева в сумме с масштабом(scale) превысит объявленную точность, произойдёт ошибка.
    @Column(precision = 10, scale = 5, nullable = false, updatable = false)
    private BigDecimal latitude;

    @Column(precision = 10, scale = 5, nullable = false, updatable = false)
    private BigDecimal longitude;

    public Location(String name, User user, BigDecimal latitude, BigDecimal longitude) {
        this.name = name;
        this.user = user;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
