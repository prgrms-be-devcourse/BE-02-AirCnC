package com.gurudev.aircnc.domain.member.service.command;

import static com.gurudev.aircnc.exception.Preconditions.checkArgument;
import static com.gurudev.aircnc.exception.Preconditions.checkNotNull;
import static lombok.AccessLevel.PRIVATE;
import static org.springframework.util.StringUtils.hasText;

import com.gurudev.aircnc.domain.member.entity.Email;
import com.gurudev.aircnc.domain.member.entity.Member;
import com.gurudev.aircnc.domain.member.entity.Password;
import com.gurudev.aircnc.domain.member.entity.PhoneNumber;
import com.gurudev.aircnc.domain.member.entity.Role;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class MemberCommand {

  @Getter
  public static class MemberRegisterCommand {

    private final Email email;
    private final Password password;
    private final String name;
    private final LocalDate birthDate;
    private final PhoneNumber phoneNumber;
    private final Role role;

    public MemberRegisterCommand(String email, String password, String name,
        LocalDate birthDate, String phoneNumber, String role) {

      checkArgument(hasText(name), "이름은 공백이 될 수 없습니다");
      checkNotNull(birthDate, "생일은 null 이 될 수 없습니다");

      this.email = new Email(email);
      this.password = new Password(password);
      this.name = name;
      this.birthDate = birthDate;
      this.phoneNumber = new PhoneNumber(phoneNumber);
      this.role = Role.valueOf(role);
    }

    public Password getPassword() {
      return new Password(Password.toString(password));
    }

    public Member toEntity() {
      return new Member(email, password, name, birthDate, phoneNumber, role);
    }
  }
}
