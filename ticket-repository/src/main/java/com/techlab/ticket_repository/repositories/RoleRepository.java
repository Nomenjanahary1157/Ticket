package com.techlab.ticket_repository.repositories;

import com.techlab.ticket_repository.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer>{
}
