package zerobase.stockdividend.model;

import lombok.*;
import zerobase.stockdividend.persist.entity.MemberEntity;

import java.util.List;

@Builder
@AllArgsConstructor
public class Auth {


    @Data
    public static class LogIn {
        private String username;
        private String password;
    }

    @Data
    public static class SignUp {
        private String username;
        private String password;
        List<String> roles;

        public MemberEntity toMemberEntity() {
            return MemberEntity.builder()
                    .username(username)
                    .password(password)
                    .roles(roles)
                    .build();
        }
    }
}
