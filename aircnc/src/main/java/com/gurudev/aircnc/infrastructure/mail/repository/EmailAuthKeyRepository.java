package com.gurudev.aircnc.infrastructure.mail.repository;

import com.gurudev.aircnc.infrastructure.mail.entity.EmailAuthKey;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAuthKeyRepository extends JpaRepository<EmailAuthKey, Long> {

  Optional<EmailAuthKey> findTopByEmail(String email, Sort sort);

}
