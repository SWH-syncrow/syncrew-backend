package com.example.syncrowbackend.friend.repository;

import com.example.syncrowbackend.friend.entity.Group;
import com.example.syncrowbackend.friend.entity.UserGroup;
import com.example.syncrowbackend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    boolean existsByUserAndGroup(User user, Group group);

    List<UserGroup> findByUser(User user);
}
