package com.chi.PhongTro.controller;


import com.chi.PhongTro.dto.Request.*;
import com.chi.PhongTro.dto.Response.PostResponse;
import com.chi.PhongTro.service.PostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {
    PostService postService;

    @PostMapping(consumes = "multipart/form-data")
    ApiResponse<PostResponse> createPost(@ModelAttribute @Valid PostCreationRequest request) throws IOException{

        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.createPost(request));
        return apiResponse;
    }

//    @GetMapping
//    ApiResponse<List<PostResponse>> getAllPost(){
//        ApiResponse<List<PostResponse>> apiResponse = new ApiResponse<>();
//        apiResponse.setResult(postService.getAllPost());
//        return apiResponse;
//    }

    @GetMapping("/{postId}")
    ApiResponse<PostResponse> getPostById(@PathVariable String postId){
        return ApiResponse.<PostResponse>builder()
                .code(1000)
                .result(postService.getPost(postId))
                .build();

    }


    @GetMapping("/my_post")
    ApiResponse<List<PostResponse>> getMyPost(){
        ApiResponse<List<PostResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.getPostByUserId());
        return apiResponse;
    }

    @GetMapping
    ApiResponse<Page<PostResponse>> getPosts(@ModelAttribute PostFilterRequest request){
        return ApiResponse.<Page<PostResponse>>builder()
                .result(postService.getPostsWithFilter(request))
                .build();
    }

    @DeleteMapping("/{postId}")
    ApiResponse<String> deletePost(@PathVariable String postId){
        postService.deletePost(postId);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Đã xóa bài đăng")
                .build();
    }


    @PutMapping("/{postId}")
    ApiResponse<PostResponse> updatePost(@PathVariable String postId,@ModelAttribute @Valid PostUpdateRequest request) throws IOException{
        return ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(postId, request))
                .build();
    }

    @PatchMapping("/{postId}/status")
    ApiResponse<String> updateStatusPost(@PathVariable String postId, @RequestBody @Valid PostStatusUpdateRequest request){
        postService.updateStatusPost(postId, request);
        return ApiResponse.<String>builder()
                .result("Cập nhật trạng thái thành công")
                .build();
    }
}
