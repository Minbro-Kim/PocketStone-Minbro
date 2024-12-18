package com.pocketstone.team_sync.auth;
import java.time.Duration;
import java.time.LocalDate;

import com.pocketstone.team_sync.exception.CredentialsInvalidException;
import com.pocketstone.team_sync.exception.CredentialsNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pocketstone.team_sync.config.jwt.RefreshTokenService;
import com.pocketstone.team_sync.config.jwt.TokenProvider;
import com.pocketstone.team_sync.entity.Company;
import com.pocketstone.team_sync.entity.User;
import com.pocketstone.team_sync.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofHours(2);

    //회원가입
    @Transactional
    public Long save(AddUserRequestDto dto) {
        
        // 회사 객체 생성
        Company company = new Company();
        System.out.println("d여기기ㅣㄱ");
        company.setCompanyName(dto.getCompanyName());
        
        User user = User.builder()
                            .loginId(dto.getLoginId())
                            .email(dto.getEmail())
                            .password(bCryptPasswordEncoder.encode(dto.getPassword()))//비밀번호 암호화 저장
                            .company(company)
                            .joinDate(LocalDate.now())
                            .build();
        System.out.println("d여기기ㅣehehehehheㄱ");
        // 회사의 유저 필드도 설정
        company.setUser(user); // 회사와 유저의 양방향 연결
        System.out.println("d여기기ㅣㄱsdfsdfsdfsd");
        System.out.println(user.getId()+user.getEmail()+user.getLoginId()+user.getPassword()+user.getCompany().getCompanyName());
        userRepository.save(user);
        System.out.println("ddddddddddddddddddddd여기기ㅣㄱsdfsdfsdfsd");
        return user.getId();

    }

    //로그인
    @Transactional
    public LoginTokenResponseDto login(String loginId, String password) {

        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CredentialsNotFoundException(loginId));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new CredentialsInvalidException();
        }
        

        String refreshToken = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        String accessToken = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        refreshTokenService.saveRefreshToken(user.getId(),refreshToken);
        LoginTokenResponseDto loginToken = new LoginTokenResponseDto("Bearer", accessToken, refreshToken);
        return loginToken;
    }
}
