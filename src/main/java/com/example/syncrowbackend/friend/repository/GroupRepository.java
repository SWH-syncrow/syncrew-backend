package com.example.syncrowbackend.friend.repository;

import com.example.syncrowbackend.friend.entity.Group;
import com.example.syncrowbackend.friend.enums.GroupCategory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Long> {
    List<Group> findByCategory(GroupCategory category);
    Optional<Group> findById(Long id);
}
