package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    List<User> findByIdIn(List<Long> userIds);

    @Modifying
    @Query("UPDATE User u SET u.status = :status WHERE u.id IN :ids")
    void updateAllUserStatus(@Param("status") UserStatus blocked, @Param("ids") List<Long> userIds);

    default User findUserById(Long id) {
        return findById(id).orElseThrow(() -> new IllegalArgumentException("해당 ID에 맞는 값이 존재하지 않습니다."));
    }
}
