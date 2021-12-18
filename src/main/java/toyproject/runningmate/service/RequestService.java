package toyproject.runningmate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.runningmate.domain.request.RequestUserToCrew;
import toyproject.runningmate.repository.RequestRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;

    @Transactional
    public Long save(RequestUserToCrew requestUserToCrew){
        return requestRepository.save(requestUserToCrew).getId();
    }


}
