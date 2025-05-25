package org.myprojects.simple_shop_app.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.myprojects.simple_shop_app.cart.model.Cart;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String middleName;

    private String phoneNumber;

    private LocalDate birthDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "customer")
    private Cart cart;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(nullable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private LocalDate updatedAt;

    @PrePersist
    private void prePersist() {
        var now = LocalDate.now();

        createdAt = now;
        updatedAt = now;

        status = UserStatus.ACTIVE;
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = LocalDate.now();
    }
}
