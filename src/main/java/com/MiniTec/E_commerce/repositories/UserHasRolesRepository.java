package com.MiniTec.E_commerce.repositories;

import com.MiniTec.E_commerce.models.UserHasRoles;
import com.MiniTec.E_commerce.models.id.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserHasRolesRepository extends JpaRepository<UserHasRoles, UserRoleId> {
}
