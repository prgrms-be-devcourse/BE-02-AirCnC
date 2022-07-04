package com.gurudev.aircnc.infrastructure.mail.repository;

import com.gurudev.aircnc.infrastructure.mail.entity.EmailAuthKey;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailAuthKeyRepository extends JpaRepository<EmailAuthKey, Long> {

    List<EmailAuthKey> findByEmail(String email, Sort sort);

}
