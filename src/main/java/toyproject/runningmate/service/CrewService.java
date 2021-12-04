package toyproject.runningmate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.runningmate.domain.crew.Crew;
import toyproject.runningmate.dto.CrewDto;
import toyproject.runningmate.repository.CrewRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CrewService {

    private final CrewRepository crewRepository;

    @Transactional
    public Long save(Crew crew) {
        return crewRepository.save(crew).getId();
    }

    public List<Crew> findCrews(){
        return crewRepository.findAll();
    }

    public CrewDto findOne(Long crewId){

        Crew crew = crewRepository.findById(crewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하는 크루가 없다."));

        CrewDto crewDto = convertEntityToDto(crew);
        return crewDto;
    }


    private CrewDto convertEntityToDto(Crew crew) {
        return CrewDto.builder()
                .id(crew.getId())
                .users(crew.getUsers())
                .crewLeaderId(crew.getCrewLeaderId())
                .openChat(crew.getOpenChat())
                .crewName(crew.getCrewName())
                .crewRegion(crew.getCrewRegion())
                .build();
    }

    public CrewDto getCrewByName(String crewName) {

        CrewDto crewDto = crewRepository.findBycrewName(crewName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 크루"));

        return crewDto;
    }

}