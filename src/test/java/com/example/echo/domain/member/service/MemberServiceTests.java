package com.example.echo.domain.member.service;

import com.example.echo.domain.member.entity.Member;
import com.example.echo.domain.member.entity.Role;
import com.example.echo.domain.member.repository.MemberRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberServiceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member member;

    @BeforeEach
    void setUp() {
        // 테스트용 회원 데이터 초기화
        member = new Member();
        member.setUserId("testUser");
        member.setName("테스트 사용자");
        member.setEmail("test@example.com");
        member.setPassword(passwordEncoder.encode("1111"));
        member.setPhone("010-1111-2222");
        member.setAvatarImage("/images/avatar-default.png");
        member.setRole(Role.USER);
        memberRepository.save(member);
    }

    // 로그인 후 role에 따라 토큰 발급
    private String loginAndGetToken(Role role) throws Exception {
        String userId = role == Role.USER ? "testUser" : "testAdmin";
        String requestJson = "{"
                + "\"userId\": \"" + userId + "\","
                + "\"password\": \"1111\""
                + "}";

        String response = mockMvc.perform(post("/api/members/login") // 로그인 API 호출
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return JsonPath.parse(response).read("data.accessToken"); // Json 객체에서 accessToken 반환
    }

    //회원 생성 테스트
    @Test
    void testCreateMember() throws Exception {
        // MemberCreateRequest 객체 생성 및 JSON 문자열로 변환
        String requestJson = "{"
                + "\"userId\": \"newUser\","
                + "\"name\": \"새로운 사용자\","
                + "\"email\": \"new@example.com\","
                + "\"password\": \"1111\","
                + "\"phone\": \"010-1234-5678\","
                + "\"avatarImage\": \"/images/avatar-default.png\","
                + "\"role\": \"USER\""
                + "}";

        mockMvc.perform(post("/api/members/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("새로운 사용자"))
                .andExpect(jsonPath("$.data.userId").value("newUser"))
                .andExpect(jsonPath("$.data.email").value("new@example.com"));
    }

    //회원 수정 테스트
    @Test
    void testUpdateMember() throws Exception {
        // MemberUpdateRequest 객체 생성 및 JSON 문자열로 변환
        String updateJson = "{"
                + "\"name\": \"업데이트된 사용자\","
                + "\"email\": \"update@example.com\","
                + "\"phone\": \"010-9876-5432\""
                + "}";
        String accessToken = loginAndGetToken(Role.USER);

        mockMvc.perform(put("/api/members/" + member.getMemberId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("업데이트된 사용자"))
                .andExpect(jsonPath("$.data.email").value("update@example.com"))
                .andExpect(jsonPath("$.data.phone").value("010-9876-5432"));
    }

    @Nested
    class AdminRole {
        @BeforeEach
        void setUp() {
            // 관리자 테스트용 회원 데이터 초기화
            member = new Member();
            member.setUserId("testAdmin");
            member.setName("테스트 관리자");
            member.setEmail("testAdmin@example.com");
            member.setPassword(passwordEncoder.encode("1111"));
            member.setPhone("010-1111-2222");
            member.setAvatarImage("/images/avatar-default.png");
            member.setRole(Role.ADMIN);
            memberRepository.save(member);
        }

        //회원 삭제 테스트
        @Test
        void testDeleteMember() throws Exception {
            String accessToken = loginAndGetToken(Role.ADMIN);
            mockMvc.perform(delete("/api/members/" + member.getMemberId())
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isOk());

            // 회원 삭제 후 조회 시 예외가 발생해야 함
            mockMvc.perform(get("/api/members/" + member.getMemberId())
                            .header("Authorization", "Bearer " + accessToken))
                    .andExpect(status().isNotFound());
        }
    }

    //회원 아바타 이미지 조회 테스트
    @Test
    void testGetAvatar() throws Exception {
        String accessToken = loginAndGetToken(Role.USER);
        mockMvc.perform(get("/api/members/" + member.getMemberId() + "/avatar")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(member.getAvatarImage()));
    }

    //회원 아바타 업로드 테스트
    @Test
    void testUpdateAvatar() throws Exception {
        MockMultipartFile mockFile = new MockMultipartFile("avatarImage", "test-avatar.png", MediaType.IMAGE_PNG_VALUE, "test image content".getBytes());
        String accessToken = loginAndGetToken(Role.USER);

        mockMvc.perform(multipart("/api/members/" + member.getMemberId() + "/avatar")
                        .file(mockFile)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.avatarImage").isNotEmpty());
    }


}
