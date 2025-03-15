package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.PostCreationRequest;
import com.chi.PhongTro.dto.Response.PostResponse;
import com.chi.PhongTro.entity.Media;
import com.chi.PhongTro.entity.Posts;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.PostMapper;
import com.chi.PhongTro.repository.*;
import com.chi.PhongTro.util.TypeMedia;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        posts.setCreated_at(LocalDate.now());
        posts.setView_count(0);
        posts.setSave_count(0);


        List<Media> mediaList = null;
        if (request.getMedia() != null && !request.getMedia().isEmpty()){
            mediaList = request.getMedia().stream()
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
        Posts savePost = postRepository.save(posts);
        if(mediaList != null){
            savePost.setMedia(mediaList);
            mediaRepository.saveAll(mediaList);
        }

        return new PostResponse(savePost);
    }

    private Media processFile(MultipartFile file, Posts post) throws IOException {
        validateFile(file);
        String fileName = saveFile(file);
        String fileUrl = uploadDir + "/" + fileName;
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

    @Transactional
    @PreAuthorize("@postService.checkDeletePermission(#postId, principal)")
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
}
