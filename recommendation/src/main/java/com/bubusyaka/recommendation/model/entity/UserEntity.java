package com.bubusyaka.recommendation.model.entity;

import com.bubusyaka.recommendation.model.enums.Role;
import com.bubusyaka.recommendation.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Table(name="users_table")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends BaseEntity {

    @Column(name = "user_name")
    private String username;

    @Column(name = "age")
    private Long age;

    @Column(name = "city")
    private String city;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "user_password")
    private String userPassword;
}
