package toyproject.runningmate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final EntityManager em;

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

    //크루 삭제면 UserDto에 있는 crewName, User에 있는 isCrewLeader 변경
    @Transactional
    public void deleteCrew(String crewName) {
        Crew deleteCrew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재X"));

        Long crewLeaderId = deleteCrew.getCrewLeaderId();
        User leader = userRepository.findById(crewLeaderId)
                .orElseThrow(() -> new IllegalArgumentException("존재X"));

        leader.setCrewLeader(!leader.isCrewLeader());
        deleteCrew.setDeleteFlag(true);
        deleteCrew.changeCrewName("DUMMYCREWNAME" + deleteCrew.getId());

        //크루 멤버들의 crew 연관관계를 깨야함
        for (User user : deleteCrew.getUsers()) {
            user.deleteCrew();
        }

        deleteCrew.getRequests().clear();
    }

    //위임 현재 유저(토큰가지고 있는 얘가 리더), 파라미터로 들어오는 얘가 리더가 될 얘
    @Transactional
    public void changeCrewLeader(String leaderName, String userName) {
        User leader = userRepository.findByNickName(leaderName)
                .orElseThrow(() -> new IllegalArgumentException("존재X"));
        User user = userRepository.findByNickName(userName)
                .orElseThrow(() -> new IllegalArgumentException("존재X"));

        leader.setCrewLeader(false);
        user.setCrewLeader(true);

        String crewName = user.getCrew().getCrewName();

        Crew crew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재X"));

        crew.changeCrewLeaderId(user.getId());
    }

    //crewName 변경
    @Transactional
    public void changeCrewName(String crewName, String newName) {
        Crew crew = crewRepository.findByCrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재X"));

        List<Crew> duplicated = em.createQuery("select c from Crew c where c.crewName = :newName", Crew.class)
                .setParameter("newName", newName)
                .getResultList();

        if(duplicated.size() > 0){
            throw new IllegalStateException("이미 존재하는 이름입니다");
        }

        crew.changeCrewName(newName);
    }

    @Transactional
    public void changeMember(String userName){
        User user = userRepository.findByNickName(userName)
                .orElseThrow(() -> new IllegalArgumentException("존재X"));

        user.deleteCrew();

    }

    public List<CrewDto> findByPage(PageRequest pageRequest) {
        return crewRepository.findAll(pageRequest).stream()
                .map(Crew::toCrewDto)
                .collect(Collectors.toList());
    }


}