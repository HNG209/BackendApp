package com.demo.backendapp.repository;

import com.demo.backendapp.entity.LogoutToken;
import org.antlr.v4.runtime.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogoutTokenRepository extends JpaRepository<LogoutToken, String> {
}
