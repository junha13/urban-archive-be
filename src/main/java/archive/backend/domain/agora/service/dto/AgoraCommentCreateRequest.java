package archive.backend.domain.agora.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgoraCommentCreateRequest {

    private String content;
    private Long parentAgoraNumber;
}
