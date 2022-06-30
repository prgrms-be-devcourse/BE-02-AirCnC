package com.gurudev.aircnc.infrastructure.mail.repository;

import com.gurudev.aircnc.infrastructure.mail.entity.AuthenticationKey;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthKeyRepository extends JpaRepository<AuthenticationKey, Long> {

  Optional<AuthenticationKey> findByEmail(String email);

}
