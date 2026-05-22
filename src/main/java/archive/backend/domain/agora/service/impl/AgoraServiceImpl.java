package archive.backend.domain.agora.service.impl;

import archive.backend.domain.agora.service.AgoraService;
import archive.backend.domain.agora.service.AgoraVO;
import archive.backend.domain.agora.service.dto.AgoraCommentCreateRequest;
import archive.backend.domain.agora.service.dto.AgoraCommentThreadResponse;
import archive.backend.domain.agora.service.dto.AgoraDetailResponse;
import archive.backend.domain.agora.service.dto.AgoraPostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
public class AgoraServiceImpl implements AgoraService {

    private static final String POST_TYPE = "POST";
    private static final String COMMENT_TYPE = "COMMENT";
    private static final String ACTIVE_STATUS = "ACTIVE";
    private static final int COMMENT_DEPTH = 0;
    private static final int REPLY_DEPTH = 1;

    private final AgoraDAO agoraDAO;

    // Creates a discussion post and allows only admins to perform the action.
    @Override
    @Transactional
    public boolean insertPost(String loginId, boolean admin, AgoraPostCreateRequest request) {
        if (!admin) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can create agora posts.");
        }
        validatePostRequest(request);

        AgoraVO agora = new AgoraVO();
        agora.setUserNumber(findUserNumberByLoginId(loginId));
        agora.setTitle(request.getTitle().trim());
        agora.setContent(request.getContent().trim());
        agora.setNodeType(POST_TYPE);
        agora.setDepth(COMMENT_DEPTH);
        agora.setStatus(ACTIVE_STATUS);

        agoraDAO.insertAgora(agora);
        return true;
    }

    // Updates an existing discussion post and keeps the admin-only rule.
    @Override
    @Transactional
    public boolean updatePost(String loginId, boolean admin, Long agoraNumber, AgoraPostCreateRequest request) {
        if (!admin) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can update agora posts.");
        }
        validatePostRequest(request);

        AgoraVO post = findAgoraOrThrow(agoraNumber);
        validatePostNode(post);

        post.setUserNumber(findUserNumberByLoginId(loginId));
        post.setTitle(request.getTitle().trim());
        post.setContent(request.getContent().trim());

        agoraDAO.updateAgora(post);
        return true;
    }

    // Deletes a discussion post and cascades to its comments and likes through the schema.
    @Override
    @Transactional
    public boolean deletePost(String loginId, boolean admin, Long agoraNumber) {
        if (!admin) {
            throw new ResponseStatusException(FORBIDDEN, "Only admins can delete agora posts.");
        }

        AgoraVO post = findAgoraOrThrow(agoraNumber);
        validatePostNode(post);
        agoraDAO.deleteAgora(post.getAgoraNumber());
        return true;
    }

    // Returns the latest agora post list for the board screen.
    @Override
    @Transactional(readOnly = true)
    public List<AgoraVO> selectPostList() {
        return agoraDAO.selectPostList();
    }

    // Returns a post with its comment and reply tree grouped by top-level comments.
    @Override
    @Transactional(readOnly = true)
    public AgoraDetailResponse selectPostDetail(Long agoraNumber) {
        AgoraVO post = findAgoraOrThrow(agoraNumber);
        validatePostNode(post);

        List<AgoraVO> comments = agoraDAO.selectCommentsByRootAgoraNumber(agoraNumber);
        List<AgoraCommentThreadResponse> threads = comments.stream()
                .filter(comment -> comment.getDepth() == COMMENT_DEPTH)
                .map(comment -> new AgoraCommentThreadResponse(
                        comment,
                        comments.stream()
                                .filter(reply -> reply.getDepth() == REPLY_DEPTH)
                                .filter(reply -> Objects.equals(reply.getParentAgoraNumber(), comment.getAgoraNumber()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        return new AgoraDetailResponse(post, threads);
    }

    // Creates either a comment or a reply while enforcing the maximum reply depth of one.
    @Override
    @Transactional
    public boolean insertComment(String loginId, Long postAgoraNumber, AgoraCommentCreateRequest request) {
        validateCommentRequest(request);

        AgoraVO post = findAgoraOrThrow(postAgoraNumber);
        validatePostNode(post);

        AgoraVO agora = new AgoraVO();
        agora.setUserNumber(findUserNumberByLoginId(loginId));
        agora.setRootAgoraNumber(postAgoraNumber);
        agora.setContent(request.getContent().trim());
        agora.setNodeType(COMMENT_TYPE);
        agora.setStatus(ACTIVE_STATUS);

        if (request.getParentAgoraNumber() == null) {
            agora.setDepth(COMMENT_DEPTH);
        } else {
            AgoraVO parentComment = findAgoraOrThrow(request.getParentAgoraNumber());
            validateReplyParent(postAgoraNumber, parentComment);
            agora.setParentAgoraNumber(parentComment.getAgoraNumber());
            agora.setDepth(REPLY_DEPTH);
        }

        agoraDAO.insertAgora(agora);
        return true;
    }

    // Updates a comment or reply and allows the author or an admin to perform the action.
    @Override
    @Transactional
    public boolean updateComment(String loginId, boolean admin, Long agoraNumber, AgoraCommentCreateRequest request) {
        validateCommentRequest(request);

        AgoraVO comment = findAgoraOrThrow(agoraNumber);
        validateCommentNode(comment);
        validateCommentOwner(comment, loginId, admin);

        comment.setContent(request.getContent().trim());
        agoraDAO.updateAgora(comment);
        return true;
    }

    // Deletes a comment or reply and allows the author or an admin to perform the action.
    @Override
    @Transactional
    public boolean deleteComment(String loginId, boolean admin, Long agoraNumber) {
        AgoraVO comment = findAgoraOrThrow(agoraNumber);
        validateCommentNode(comment);
        validateCommentOwner(comment, loginId, admin);

        agoraDAO.deleteAgora(comment.getAgoraNumber());
        return true;
    }

    // Applies a recommendation to a post or comment and blocks duplicates or self-like on comments.
    @Override
    @Transactional
    public int recommendAgora(String loginId, Long agoraNumber) {
        Long userNumber = findUserNumberByLoginId(loginId);
        AgoraVO target = findAgoraOrThrow(agoraNumber);

        if (COMMENT_TYPE.equals(target.getNodeType()) && Objects.equals(target.getUserNumber(), userNumber)) {
            throw new ResponseStatusException(BAD_REQUEST, "You cannot recommend your own comment.");
        }

        if (agoraDAO.countAgoraLike(agoraNumber, userNumber) > 0) {
            throw new ResponseStatusException(CONFLICT, "You already recommended this item.");
        }

        agoraDAO.insertAgoraLike(agoraNumber, userNumber);
        agoraDAO.increaseLikeCount(agoraNumber);
        return findAgoraOrThrow(agoraNumber).getLikeCount();
    }

    // Resolves the authenticated loginId into the persisted user number.
    private Long findUserNumberByLoginId(String loginId) {
        Long userNumber = agoraDAO.findUserNumberByLoginId(loginId);
        if (userNumber == null) {
            throw new ResponseStatusException(UNAUTHORIZED, "User information could not be resolved.");
        }
        return userNumber;
    }

    // Validates the required fields for a discussion post request.
    private void validatePostRequest(AgoraPostCreateRequest request) {
        if (request == null || isBlank(request.getTitle()) || isBlank(request.getContent())) {
            throw new ResponseStatusException(BAD_REQUEST, "Post title and content are required.");
        }
    }

    // Validates the required fields for a comment or reply request.
    private void validateCommentRequest(AgoraCommentCreateRequest request) {
        if (request == null || isBlank(request.getContent())) {
            throw new ResponseStatusException(BAD_REQUEST, "Comment content is required.");
        }
    }

    // Loads an agora row and fails when the row does not exist or is inactive.
    private AgoraVO findAgoraOrThrow(Long agoraNumber) {
        AgoraVO agora = agoraDAO.selectAgoraByNumber(agoraNumber);
        if (agora == null || !ACTIVE_STATUS.equals(agora.getStatus())) {
            throw new ResponseStatusException(NOT_FOUND, "Agora item not found.");
        }
        return agora;
    }

    // Ensures that the selected row is a post before a post-only operation proceeds.
    private void validatePostNode(AgoraVO agora) {
        if (!POST_TYPE.equals(agora.getNodeType())) {
            throw new ResponseStatusException(BAD_REQUEST, "This operation is only available for posts.");
        }
    }

    // Ensures that the selected row is a comment or reply before a comment-only operation proceeds.
    private void validateCommentNode(AgoraVO agora) {
        if (!COMMENT_TYPE.equals(agora.getNodeType())) {
            throw new ResponseStatusException(BAD_REQUEST, "This operation is only available for comments.");
        }
    }

    // Checks whether the current user is the comment owner or an admin.
    private void validateCommentOwner(AgoraVO comment, String loginId, boolean admin) {
        if (admin) {
            return;
        }

        Long userNumber = findUserNumberByLoginId(loginId);
        if (!Objects.equals(comment.getUserNumber(), userNumber)) {
            throw new ResponseStatusException(FORBIDDEN, "Only the comment owner can modify this item.");
        }
    }

    // Validates that a reply targets a top-level comment within the same post.
    private void validateReplyParent(Long postAgoraNumber, AgoraVO parentComment) {
        validateCommentNode(parentComment);

        if (parentComment.getDepth() != COMMENT_DEPTH) {
            throw new ResponseStatusException(BAD_REQUEST, "Replies cannot be nested deeper than one level.");
        }
        if (!Objects.equals(parentComment.getRootAgoraNumber(), postAgoraNumber)) {
            throw new ResponseStatusException(BAD_REQUEST, "Replies must belong to the same post.");
        }
    }

    // Returns whether a string is null, empty, or only whitespace.
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
