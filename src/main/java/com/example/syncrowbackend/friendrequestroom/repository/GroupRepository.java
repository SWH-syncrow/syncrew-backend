package com.example.syncrowbackend.friendrequestroom.repository;

import com.example.syncrowbackend.friendrequestroom.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findById(Long id);

}
