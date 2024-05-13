package zerobase.stockdividend.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.stockdividend.model.Auth;
import zerobase.stockdividend.persist.entity.MemberEntity;
import zerobase.stockdividend.persist.repository.MemberEntityRepository;
import zerobase.stockdividend.security.TokenProvider;
import zerobase.stockdividend.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final MemberEntityRepository memberEntityRepository;
    private final TokenProvider tokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @RequestBody Auth.SignUp request
            ) {
        MemberEntity member = memberService.register(request);
        return ResponseEntity.ok(member);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody Auth.LogIn request
    ) {
        MemberEntity user = memberService.authenticate(request);
        String token = tokenProvider.generateToken(user.getUsername(), user.getRoles());
        log.info("user login -> " + request.getUsername());
        return ResponseEntity.ok(token);
    }
}
