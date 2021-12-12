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

import java.util.ArrayList;
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

        User findUserEntity = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원"));

//        crewDto.builder()
//                .crewLeaderId(userDto.getId())
//                .build();

        Crew crewEntity = convertDtoToEntity(crewDto);
        findUserEntity.setCrew(crewEntity); // setCrew안 양방향은 제거
        return crewRepository.save(crewEntity).getId();  // Entity로 저장
    }

//    private void crewSaveViaDto(CrewDto crewDto){   // Dto -> Entity 변환 대신
//
//        Crew crewEntity = crewRepository.findById(crewDto.getId())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));
//
//
//        for (UserDto userDto : crewDto.getUserDtos()) {
//             crewEntity.getUsers().add(userDto.toEntity());
//        }
//
//    }

    private CrewDto convertEntityToDto(Crew crew) {
        CrewDto crewDto= CrewDto.builder()
                .id(crew.getId())
                .crewLeaderId(crew.getCrewLeaderId())
                .openChat(crew.getOpenChat())
                .crewName(crew.getCrewName())
                .crewRegion(crew.getCrewRegion())
                .requests(crew.getRequests())
                .userDtos(new ArrayList<>()) //  빈 그릇 생성
                .build();

        for (User user : crew.getUsers()) {     // crewDto 빈 그릇에 담는다.
            crewDto.getUserDtos().add(user.toDto());
        }
        return crewDto;
    }

    private Crew convertDtoToEntity(CrewDto crewDto){
        Crew crew = Crew.builder()
                .id(crewDto.getId())
                .crewLeaderId(crewDto.getCrewLeaderId())
                .openChat(crewDto.getOpenChat())
                .crewName(crewDto.getCrewName())
                .crewRegion(crewDto.getCrewRegion())
                .requests(crewDto.getRequests())
//                .users(userDtos)
                .build();

        for (UserDto userDto : crewDto.getUserDtos()) { // crewDto 그릇에 담긴걸 꺼낸다
            crew.getUsers().add(userDto.toEntity());
        }
        return crew;
    }

    public CrewDto getCrewByName(String crewName) {

        Crew crew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        return convertEntityToDto(crew);
    }

    @Transactional
    public Long saveRequest(String crewName, String userNickname){
        Crew crew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        RequestUserToCrew request = new RequestUserToCrew(userNickname);
        crew.getRequests().add(request);

        return request.getId();
    }

}