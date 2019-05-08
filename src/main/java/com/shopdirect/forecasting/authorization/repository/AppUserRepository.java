package com.shopdirect.forecasting.authorization.repository;

import com.shopdirect.forecasting.authorization.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
}
