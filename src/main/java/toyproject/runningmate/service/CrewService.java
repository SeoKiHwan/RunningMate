package toyproject.runningmate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.domain.request.RequestUserToCrew;
import toyproject.runningmate.domain.user.User;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.dto.UserDto;
import toyproject.runningmate.repository.CrewRepository;
import toyproject.runningmate.repository.RequestRepository;
import toyproject.runningmate.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Transactional
    public Long save(UserDto userDto, CrewDto crewDto) { // Dto로 받아서

        User findUserEntity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        // crewDto의 crewLeaderId 설정
        crewDto.setCrewLeaderId(userDto.getId());

        Crew crewEntity = crewDto.toEntity();
        findUserEntity.addCrew(crewEntity);

        crewRepository.save(crewEntity);
        return crewEntity.getId();  // Entity로 저장
    }

    public CrewDto getCrewByName(String crewName) {

        Crew crew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));
        return crew.toCrewDto();
    }

    //crew 중복

    public List<UserDto> getCrewMembersByCrewName(String crewName){
        Crew crew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        return crew.userEntityListToDtoList();
    }

    @Transactional
    public Long saveRequest(UserDto userDto, CrewDto crewDto) {
        RequestUserToCrew requestUserToCrew = RequestUserToCrew.builder()
                .nickName(userDto.getNickName())
                .build();

        System.out.println("--------1------");

        requestRepository.save(requestUserToCrew);

        Crew crew = crewRepository.findById(crewDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        requestUserToCrew.addCrew(crew);

        return requestUserToCrew.getId();
    }

    @Transactional
    public void rejectUser(String userNickName){
        RequestUserToCrew requestUserToCrew = requestRepository.findByNickName(userNickName)
                .orElseThrow(() -> new IllegalArgumentException("요청한 적 없음"));

        requestRepository.delete(requestUserToCrew);

    }

    @Transactional
    public void addmitUser(String userNickName) {
        RequestUserToCrew requestUserToCrew = requestRepository.findByNickName(userNickName)
                .orElseThrow(() -> new IllegalArgumentException("요청한 적 없음"));

        //가입할 크루
        Crew crew = requestUserToCrew.getCrew();

        //가입할 회원
        User findUser = userRepository.findByNickName(userNickName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

        findUser.addCrew(crew);
    }

    public List<String> getRequestList(String crewName) {
        Crew findCrew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재X"));

        List<String> requests = new ArrayList<>();
        for (RequestUserToCrew request : findCrew.getRequests()) {
            String nickName = request.getNickName();
            requests.add(nickName);
        }

        return requests;
    }

}