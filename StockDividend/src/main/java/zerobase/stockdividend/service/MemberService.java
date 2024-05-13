package zerobase.stockdividend.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.stockdividend.exception.impl.AlreadyExistUserException;
import zerobase.stockdividend.exception.impl.NotFoundUserIdException;
import zerobase.stockdividend.exception.impl.WrongPasswordException;
import zerobase.stockdividend.model.Auth;
import zerobase.stockdividend.persist.entity.MemberEntity;
import zerobase.stockdividend.persist.repository.MemberEntityRepository;

@Service
@Slf4j
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberEntityRepository memberEntityRepository;
    private final PasswordEncoder passwordEncoder;

    // 해당 이름(아이디)으로 등록되어 있는 user 찾기
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "could not find user with username -> " + username)
                );
    }

    //회원가입
    public MemberEntity register(Auth.SignUp member) {
        boolean exists = memberEntityRepository.existsByUsername(member.getUsername());
        if(exists) {
            throw new AlreadyExistUserException();
        }

        member.setPassword(passwordEncoder.encode(member.getPassword()));
        MemberEntity savedMember = memberEntityRepository.save(member.toMemberEntity());

        return savedMember;
    }

    //로그인 시 검증
    public MemberEntity authenticate(Auth.LogIn member) {
        MemberEntity user = memberEntityRepository.findByUsername(member.getUsername())
                .orElseThrow(NotFoundUserIdException::new);

        if (!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }

        return user;
    }


}
