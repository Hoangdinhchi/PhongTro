package com.chi.PhongTro.controller;


import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.PostCreationRequest;
import com.chi.PhongTro.dto.Response.PostResponse;
import com.chi.PhongTro.entity.Posts;
import com.chi.PhongTro.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    ApiResponse<PostResponse> createPost(@ModelAttribute PostCreationRequest request) throws IOException{

        ApiResponse<PostResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.createPost(request));
        return apiResponse;
    }

    @GetMapping
    ApiResponse<List<PostResponse>> getAllPost(){
        ApiResponse<List<PostResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(postService.getAllPost());
        return apiResponse;
    }
}
