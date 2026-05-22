package archive.backend.domain.agora.service.impl;

import archive.backend.domain.agora.service.AgoraVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AgoraDAO {

    void insertAgora(AgoraVO agora);

    void updateAgora(AgoraVO agora);

    void deleteAgora(Long agoraNumber);

    List<AgoraVO> selectPostList();

    AgoraVO selectAgoraByNumber(Long agoraNumber);

    List<AgoraVO> selectCommentsByRootAgoraNumber(Long rootAgoraNumber);

    Long findUserNumberByLoginId(String loginId);

    int countAgoraLike(@Param("agoraNumber") Long agoraNumber, @Param("userNumber") Long userNumber);

    void insertAgoraLike(@Param("agoraNumber") Long agoraNumber, @Param("userNumber") Long userNumber);

    void increaseLikeCount(Long agoraNumber);
}
