package com.chi.PhongTro.service;

import com.chi.PhongTro.dto.Request.CommentCreationRequest;
import com.chi.PhongTro.dto.Request.CommentUpdateRequest;
import com.chi.PhongTro.dto.Response.CommentResponse;
import com.chi.PhongTro.entity.Comments;
import com.chi.PhongTro.entity.Posts;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.CommentMapper;
import com.chi.PhongTro.repository.CommentRepository;
import com.chi.PhongTro.repository.PostRepository;
import com.chi.PhongTro.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service("commentService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentService {
    UsersRepository usersRepository;
    CommentRepository commentRepository;
    PostRepository postRepository;

    CommentMapper commentMapper;


    @Transactional
    @PreAuthorize("isAuthenticated()")
    public CommentResponse createComment(String postId, CommentCreationRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findByPhone(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comments comment = Comments.builder()
                .post(post)
                .user(user)
                .content(request.getContent())
                .createdAt(LocalDate.now())
                .build();

        comment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public List<CommentResponse> getCommentsByPostId(long postId) {
        List<Comments> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    @PreAuthorize("@commentService.checkCommentPermission(#commentId, authentication)")
    public CommentResponse updateComment(String commentId, CommentUpdateRequest request) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (request.getContent() != null && !request.getContent().isEmpty()) {
            comment.setContent(request.getContent());
        }

        comment = commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    @Transactional
    @PreAuthorize("@commentService.checkCommentPermission(#commentId, authentication)")
    public void deleteComment(String commentId) {
        Comments comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        commentRepository.delete(comment);
    }

    public boolean checkCommentPermission(String commentId, Authentication authentication) {
        Comments comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) return false;
        String currentUserPhone = authentication.getName();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")) ||
                comment.getUser().getPhone().equals(currentUserPhone);
    }

}
