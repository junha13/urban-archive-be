package archive.backend.domain.agora.service;

import archive.backend.domain.agora.service.dto.AgoraCommentCreateRequest;
import archive.backend.domain.agora.service.dto.AgoraDetailResponse;
import archive.backend.domain.agora.service.dto.AgoraPostCreateRequest;

import java.util.List;

public interface AgoraService {

    boolean insertPost(String loginId, boolean admin, AgoraPostCreateRequest request);

    boolean updatePost(String loginId, boolean admin, Long agoraNumber, AgoraPostCreateRequest request);

    boolean deletePost(String loginId, boolean admin, Long agoraNumber);

    List<AgoraVO> selectPostList();

    AgoraDetailResponse selectPostDetail(Long agoraNumber);

    boolean insertComment(String loginId, Long postAgoraNumber, AgoraCommentCreateRequest request);

    boolean updateComment(String loginId, boolean admin, Long agoraNumber, AgoraCommentCreateRequest request);

    boolean deleteComment(String loginId, boolean admin, Long agoraNumber);

    int recommendAgora(String loginId, Long agoraNumber);
}
