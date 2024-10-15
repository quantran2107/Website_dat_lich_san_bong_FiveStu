package com.example.DATN_WebFiveTus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name="Account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ma")
    private String ma;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name="passwords")
    private String passwords;

    @Column(name="enableds")
    private boolean enableds;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_roles",  // Tên bảng trung gian
            joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "ma"),  // Cột khóa chính của bảng Account
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")  // Cột khóa chính của bảng Roles
    )
    private List<Roles> roles;

}