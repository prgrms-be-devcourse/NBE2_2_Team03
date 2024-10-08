package com.example.echo.domain.petition.service;

import com.example.echo.domain.petition.entity.Petition;
import com.example.echo.domain.petition.repository.PetitionRepository;
import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgreeCountMonitoringService {

    private final PetitionRepository petitionRepository;
    private final PetitionCrawlService petitionCrawlService;

    @Transactional
    @Scheduled(fixedRate = 600000) // 10분 마다 업데이트
    public void updateAgreeCountFromWeb() {
        List<Petition> petitions = petitionRepository.findAllActive(); // 만료 전만 가져오기

        for (Petition petition : petitions) {
            try {
                System.out.println(petition.getPetitionId() + " " + petition.getEndDate());

                // 현재 웹의 동의자 수
                int currentAgreeCount = petitionCrawlService.fetchAgreeCount(petition.getOriginalUrl());

                // 동의 수 증가 시 변경
                if (currentAgreeCount > petition.getAgreeCount()) {
                    log.info("웹의 동의자 수 : {}, 기존 동의 수 : {}", currentAgreeCount, petition.getAgreeCount());
                    petition.changeAgreeCount(currentAgreeCount);
                    petitionRepository.save(petition);
                    log.info("동의 수 업데이트 성공 청원 : {}", petition.getPetitionId());
                } else if (currentAgreeCount < petition.getAgreeCount()) {
                    log.info("동의 수 업데이트 실패");
                } else {
                    log.info("동의 수 동일");
                }
            } catch (Exception e) {
                log.info("동의자 수 업데이트 실패 청원 : {}", petition.getPetitionId());
                e.printStackTrace();
            }
        }
    }
}
