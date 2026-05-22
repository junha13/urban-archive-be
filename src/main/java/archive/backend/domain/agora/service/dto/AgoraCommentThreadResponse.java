package archive.backend.domain.agora.service.dto;

import archive.backend.domain.agora.service.AgoraVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgoraCommentThreadResponse {

    private AgoraVO comment;
    private List<AgoraVO> replies;
}
