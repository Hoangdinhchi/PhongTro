package com.chi.PhongTro.controller;


import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.CommentCreationRequest;
import com.chi.PhongTro.dto.Request.CommentUpdateRequest;
import com.chi.PhongTro.dto.Response.CommentResponse;
import com.chi.PhongTro.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    CommentService commentService;

    @PostMapping
    public ApiResponse<CommentResponse> createComment(
            @PathVariable String postId,
            @RequestBody CommentCreationRequest request) {
        CommentResponse response = commentService.createComment(postId, request);
        return ApiResponse.<CommentResponse>builder()
                .code(1000)
                .result(response)
                .build();
    }

    // Lấy tất cả bình luận của bài đăng
    @GetMapping
    public ApiResponse<List<CommentResponse>> getCommentsByPostId(@PathVariable long postId) {
        List<CommentResponse> comments = commentService.getCommentsByPostId(postId);
        return ApiResponse.<List<CommentResponse>>builder()
                .code(1000)
                .result(comments)
                .build();
    }

    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponse> updateComment(
            @PathVariable String postId,
            @PathVariable String commentId,
            @RequestBody CommentUpdateRequest request) {
        CommentResponse response = commentService.updateComment(commentId, request);
        return ApiResponse.<CommentResponse>builder()
                .code(1000)
                .result(response)
                .build();
    }

    // Xóa bình luận
    @DeleteMapping("/{commentId}")
    public ApiResponse<String> deleteComment(
            @PathVariable String postId,
            @PathVariable String commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Đã xóa bình luận")
                .build();
    }
}
