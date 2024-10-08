package com.example.DATN_WebFiveTus.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
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