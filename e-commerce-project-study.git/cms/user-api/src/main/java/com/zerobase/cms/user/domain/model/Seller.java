package com.zerobase.cms.user.domain.model;

import com.zerobase.cms.user.domain.SignupForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Seller {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;
    private String name;
    private String password;
    private String phone; //휴대폰번호 validation?
    private LocalDate birth;
    private Integer balance;

    private LocalDateTime verifyExpiredAt;
    private String verificationCode;
    private boolean verify;

    public static Seller from(SignupForm signupForm) {
        return  Seller.builder()
                .email(signupForm.getEmail().toLowerCase(Locale.ROOT))
                .name(signupForm.getName())
                .password(signupForm.getPassword())
                .birth(signupForm.getBirth())
                .phone(signupForm.getPhone())
                .verify(false )
                .build();
    }
}
