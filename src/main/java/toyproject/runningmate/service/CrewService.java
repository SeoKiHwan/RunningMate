package toyproject.runningmate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.domain.request.RequestUserToCrew;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.CrewRepository;
import toyproject.runningmate.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final UserRepository userRepository;
    private final RequestService requestService;
    @Transactional
    public Long save(UserDto userDto, CrewDto crewDto) { // Dto로 받아서

        User user = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        // crewDto의 crewLeaderId 설정
        crewDto.setCrewLeaderId(userDto.getId());

        Crew crew = crewDto.toEntity();
        user.setCrew(crew); // 연관관계 편의 메서드
        return crewRepository.save(crew).getId();  // Entity로 저장
    }


    public CrewDto getCrewByName(String crewName) {

        Crew crew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        return crew.toDto();
    }


    public List<UserDto> getCrewMembersByCrewName(String crewName){
        Crew crew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        return crew.userEntityListToDtoList();
    }

    @Transactional
    public Long saveRequest(String crewName, String userNickname){
        Crew crew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        RequestUserToCrew request = new RequestUserToCrew(userNickname);    // request.getId() NULL 발생
        crew.getRequests().add(request);
        crewRepository.save(crew);
        return request.getId();
    }

}