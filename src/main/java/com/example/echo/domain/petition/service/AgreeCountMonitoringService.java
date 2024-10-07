package com.example.echo.domain.petition.service;

import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.repository.PetitionRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgreeCountMonitoringService {

    private final PetitionRepository petitionRepository;
    private final PetitionCrawlService petitionCrawlService;
    private final PetitionService petitionService;

    @Scheduled(fixedRate = 600000) // 10분 마다 업데이트
    public void updateAgreeCountFromWeb() {
        List<Petition> petitions = petitionRepository.findAll();

        for (Petition petition : petitions) {
            try {
                // 만료된 것은 넘기기
                if (petitionService.isExpired(petition)) {
                    continue;
                }
                // 현재 웹의 동의자 수
                int currentAgreeCount = petitionCrawlService.fetchAgreeCount(petition.getOriginalUrl());

                // 동의 수 증가 시 변경
                if (currentAgreeCount > petition.getAgreeCount()) {
                    System.out.println("웹의 동의자 수 : " + currentAgreeCount + "\n" + "기존 동의 수"  + petition.getAgreeCount());
                    petition.changeAgreeCount(currentAgreeCount);
                    petitionRepository.save(petition);
                    System.out.println("동의 수 업데이트 청원 번호 : " + petition.getPetitionId());
                }
            } catch (Exception e) {
                System.err.println("동의자 수 업데이트 실패 청원: " + petition.getPetitionId());
                e.printStackTrace();
            }
        }
    }
}
