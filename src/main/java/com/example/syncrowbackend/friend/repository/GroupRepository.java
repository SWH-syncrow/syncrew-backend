package com.example.syncrowbackend.friend.repository;

import com.example.syncrowbackend.friend.entity.Group;
import com.example.syncrowbackend.friend.enums.GroupCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    Page<Group> findByCategory(GroupCategory category, Pageable pageable);
    Optional<Group> findById(Long id);
}
