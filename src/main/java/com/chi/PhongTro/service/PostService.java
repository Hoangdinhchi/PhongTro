package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.PostCreationRequest;
import com.chi.PhongTro.dto.Request.PostFilterRequest;
import com.chi.PhongTro.dto.Request.PostStatusUpdateRequest;
import com.chi.PhongTro.dto.Request.PostUpdateRequest;
import com.chi.PhongTro.dto.Response.PageResponse;
import com.chi.PhongTro.dto.Response.PostResponse;
import com.chi.PhongTro.entity.Media;
import com.chi.PhongTro.entity.Posts;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.PostMapper;
import com.chi.PhongTro.repository.*;
import com.chi.PhongTro.specification.PostSpecification;
import com.chi.PhongTro.util.TypeMedia;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service("postService")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    UsersRepository usersRepository;
    MediaRepository mediaRepository;
    RoomTypesRepository roomTypesRepository;
    UtilitiesRepository utilitiesRepository;
    PostMapper postMapper;

    @NonFinal
    @Value("${file.uploadDir}")
    String uploadDir;

    @NonFinal
    @Value("${file.base-url}")
    String baseUrl;

    @Transactional
    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
    public PostResponse createPost(PostCreationRequest request) throws IOException {
        var context = SecurityContextHolder.getContext();

        Posts posts = postMapper.toPost(request);

        posts.setUser(usersRepository.findByPhone(context.getAuthentication().getName())
                      .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
        posts.setType(roomTypesRepository.findById(String.valueOf(request.getTypeId())).
                        orElseThrow(() -> new AppException(ErrorCode.ROOM_TYPE_NOT_EXISTED)));
        posts.setUtilities(request.getUtilityIds().stream()
                        .map(id -> utilitiesRepository.findById(String.valueOf(id))
                                .orElseThrow(() -> new AppException(ErrorCode.UTILITY_NOT_EXISTED)))
                                .collect(Collectors.toSet()));
        posts.setStatus("display");
        posts.setLatitude(request.getLatitude());
        posts.setLongitude(request.getLongitude());
        posts.setCreatedAt(LocalDate.now());
        posts.setView_count(0);
        posts.setSave_count(0);


        List<Media> mediaList = handleFile(request.getMedia(), posts);

        Posts savePost = postRepository.save(posts);
        if(mediaList != null){
            savePost.setMedia(mediaList);
            mediaRepository.saveAll(mediaList);
        }
        return new PostResponse(savePost);
    }

    @Transactional(rollbackOn = IOException.class)
    @PreAuthorize("@postService.checkDeletePermission(#postId, authentication)")
    public PostResponse updatePost(String postId, PostUpdateRequest request) throws IOException {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        postMapper.updatePost(post, request);

        if (request.getTypeId() != null) {
            post.setType(roomTypesRepository.findById(String.valueOf(request.getTypeId()))
                    .orElseThrow(() -> new AppException(ErrorCode.ROOM_TYPE_NOT_EXISTED)));
        }
        if (request.getUtilityIds() != null && !request.getUtilityIds().isEmpty()) {
            post.setUtilities(request.getUtilityIds().stream()
                    .map(id -> utilitiesRepository.findById(String.valueOf(id))
                            .orElseThrow(() -> new AppException(ErrorCode.UTILITY_NOT_EXISTED)))
                    .collect(Collectors.toSet()));
        }


        if (request.getMedia() != null && !request.getMedia().isEmpty()) {

            deleteFile(post.getMedia());


            List<Media> mediaList = new ArrayList<>(handleFile(request.getMedia(), post));
            post.setMedia(mediaList);
            mediaRepository.saveAll(mediaList);
        }

        Posts updatedPost = postRepository.save(post);
        return new PostResponse(updatedPost);
    }

    private void deleteFile(List<Media> mediaList){
        if (mediaList != null) {
            for (Media media : mediaList) {
                Path filePath = Paths.get(uploadDir, media.getFile_url().substring(uploadDir.length() + 1));
                try {
                    Files.deleteIfExists(filePath);
                } catch (IOException e) {
                    log.error("File do not exists: " + filePath);
                }
            }
            mediaRepository.deleteAll(mediaList);
        }
    }

    private List<Media> handleFile(List<MultipartFile> files, Posts posts){
        List<Media> mediaList = null;
        if (files != null && !files.isEmpty()){
            mediaList = files.stream()
                    .filter(file -> file != null && !file.isEmpty())
                    .map(file -> {
                        try {
                            return processFile(file, posts);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        }
        return mediaList;
    }

    private Media processFile(MultipartFile file, Posts post) throws IOException {
        validateFile(file);
        String fileName = saveFile(file);
        String fileUrl = baseUrl + "/files/" + fileName;
        return Media.builder()
                .post(post)
                .file_url(fileUrl)
                .file_type(determineFileType(file))
                .created_at(LocalDate.now())
                .build();
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("File is empty or null");
        }

        if (file.getSize() > 50 * 1024 * 1024) {
            throw new IOException("File size exceeds 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !(contentType.startsWith("image/") || contentType.startsWith("video/"))) {
            throw new IOException("Only image and video files are allowed");
        }
    }

    private String saveFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }


        String contentType = file.getContentType();
        assert contentType != null;
        if (!contentType.startsWith("image/") && !contentType.startsWith("video/")) {
            throw new IOException("Only image and video files are allowed");
        }

        // Tạo thư mục nếu chưa tồn tại
        Path folder = Paths.get(uploadDir);
        if (!Files.exists(folder)) {
            Files.createDirectories(folder);
        }


        String originalFileName = file.getOriginalFilename();
        String fileExtension = originalFileName != null && originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : "";


        String fileName = UUID.randomUUID().toString() + fileExtension;

        Path filePath = folder.resolve(fileName).normalize().toAbsolutePath();

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

    private TypeMedia determineFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            return TypeMedia.image;
        }

        if (contentType.startsWith("image/")) {
            return TypeMedia.image;
        } else if (contentType.startsWith("video/")) {
            return TypeMedia.video;
        } else {
            return TypeMedia.image;
        }
    }

    public List<PostResponse> getAllPost(){
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

//    public PostResponse getPostById(String postId){
//        return new PostResponse(postRepository.findById(postId)
//                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND)));
//    }

    public List<PostResponse> getPostByUserId(){

        var context = SecurityContextHolder.getContext().getAuthentication();
        Users user = usersRepository.findByPhone(context.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return postRepository.findByUserId(user.getUser_id()).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public PostResponse getPost(String postId){

        incrementViewCount(postId);
        return new PostResponse(postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND)));
    }


    public PageResponse<PostResponse> getPostsWithFilter(PostFilterRequest request) {
        Specification<Posts> spec = PostSpecification.filterPost(
                request.getTypeId(),
                request.getStatus(),
                request.getCity(),
                request.getDistrict(),
                request.getAddress(),
                request.getMinPrice(),
                request.getMaxPrice(),
                request.getMinArea(),
                request.getMaxArea()
        );

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending()
        );
        Page<Posts> postsPage = postRepository.findAll(spec, pageable);
        Page<PostResponse> responsePage = postsPage.map(PostResponse::new);

        return new PageResponse<>(responsePage);
    }

    public void updateStatusPost(PostStatusUpdateRequest request){



        Posts post = postRepository.findById(request.getPostId()).orElseThrow(
                () -> new AppException(ErrorCode.POST_NOT_FOUND)
        );
        if (!List.of("display", "hidden", "pending").contains(request.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }
        post.setStatus(request.getStatus());
        postRepository.save(post);
    }



    @Transactional
    @PreAuthorize("@postService.checkDeletePermission(#postId, authentication)")
    public void deletePost(String postId){
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));
        postRepository.delete(post);
    }

    public boolean checkDeletePermission(String postId, Authentication authentication) {
        Posts post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return false;
        }
        String currentUserPhone = authentication.getName();
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")) ||
                post.getUser().getPhone().equals(currentUserPhone);
    }

    public PostResponse incrementViewCount(String postId) {
        Posts post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        post.setView_count(post.getView_count() + 1);
        Posts updatedPost = postRepository.save(post);
        return new PostResponse(updatedPost);
    }




}
