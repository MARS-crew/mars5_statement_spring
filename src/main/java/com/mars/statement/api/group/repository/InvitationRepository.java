package com.mars.statement.api.group.repository;

import com.mars.statement.api.group.domain.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    List<Invitation> findByEmail(String email);
}
