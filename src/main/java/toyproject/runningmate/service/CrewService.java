package toyproject.runningmate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.domain.request.RequestUserToCrew;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.dto.RequestUserToCrewDto;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.CrewRepository;
import toyproject.runningmate.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long save(UserDto userDto, CrewDto crewDto) { // Dto로 받아서

        User findUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));


        crewDto.builder()
                .crewLeaderId(userDto.getId())
                .build();

        Crew crewEntity = crewDto.toEntity();
        findUser.addCrew(crewEntity);
        return crewRepository.save(crewEntity).getId();  // Entity로 저장
    }

    private CrewDto convertEntityToDto(Crew crew) {
        return CrewDto.builder()
                .id(crew.getId())
                .crewLeaderId(crew.getCrewLeaderId())
                .openChat(crew.getOpenChat())
                .crewName(crew.getCrewName())
                .crewRegion(crew.getCrewRegion())
                .requests(crew.getRequests())
                .build();
    }

    public CrewDto getCrewByName(String crewName) {

        Crew crew = crewRepository.findBycrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        return convertEntityToDto(crew);
    }

    @Transactional
    public Long saveRequest(String crewName, String userNickname){
        Crew crew = crewRepository.findBycrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        RequestUserToCrew request = new RequestUserToCrew(userNickname);
        crew.getRequests().add(request);

        return request.getId();
    }

}