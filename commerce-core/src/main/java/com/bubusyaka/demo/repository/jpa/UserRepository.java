package com.bubusyaka.demo.repository.jpa;

import com.bubusyaka.demo.model.entity.ItemEntity;
import com.bubusyaka.demo.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = """
select * from users_table
""", nativeQuery = true)
    List<UserEntity> findAll();


    @Query(value = """
select * from users_table ut where ut.user_name = :user_name
""", nativeQuery = true)
    List<UserEntity> findByUserName(@Param("user_name") String userName);
}
