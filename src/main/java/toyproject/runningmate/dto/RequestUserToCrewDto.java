package toyproject.runningmate.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.runningmate.domain.request.RequestUserToCrew;
@Getter
@NoArgsConstructor
@Builder
public class RequestUserToCrewDto {

    private Long id;
    private String nickName;


    @Builder
    public RequestUserToCrewDto(Long id, String nickName) {
        this.id=id;
        this.nickName=nickName;
    }


    public RequestUserToCrew toEntity(){
        return RequestUserToCrew.builder()
                .id(id)
                .nickName(nickName)
                .build();
    }
}
