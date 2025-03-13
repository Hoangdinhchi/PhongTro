package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.PostCreationRequest;
import com.chi.PhongTro.dto.Response.PostResponse;
import com.chi.PhongTro.entity.Media;
import com.chi.PhongTro.entity.Posts;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.repository.*;
import com.chi.PhongTro.util.TypeMedia;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostService {
    PostRepository postRepository;
    UsersRepository usersRepository;
    MediaRepository mediaRepository;
    RoomTypesRepository roomTypesRepository;
    UtilitiesRepository utilitiesRepository;

    @NonFinal
    @Value("${file.uploadDir}")
    String uploadDir;

    public PostResponse createPost(PostCreationRequest request) throws IOException {
        Posts posts = Posts.builder()
                .user(usersRepository.findById(String.valueOf(request.getUserId()))
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))
                .type(roomTypesRepository.findById(String.valueOf(request.getTypeId())).
                        orElseThrow(() -> new AppException(ErrorCode.ROOM_TYPE_NOT_EXISTED)))
                .title(request.getTitle())
                .description(request.getDescription())
                .address(request.getAddress())
                .city(request.getCity())
                .district(request.getDistrict())
                .price(request.getPrice())
                .area(request.getArea())
                .utilities(request.getUtilityIds().stream()
                        .map(id -> utilitiesRepository.findById(String.valueOf(id))
                                .orElseThrow(() -> new AppException(ErrorCode.UTILITY_NOT_EXISTED)))
                        .collect(Collectors.toSet()))
                .status("display")
                .created_at(LocalDate.now())
                .view_count(0)
                .save_count(0)
                .build();
        Posts savePost = postRepository.save(posts);

        if (request.getMedia() != null && !request.getMedia().isEmpty()){
            List<Media> mediaList = request.getMedia().stream()
                    .map(mediaRequest -> {
                        MultipartFile file = mediaRequest.getFile();
                        String fileName = null;
                        try {
                            fileName = saveFile(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        String fileUrl = uploadDir + "/" + fileName;
                        return Media.builder()
                                .post(savePost)
                                .file_url(fileUrl)
                                .file_type(determineFileType(file))
                                .created_at(LocalDate.now())
                                .build();

                    })
                    .toList();
            savePost.setMedia(mediaList);
            mediaRepository.saveAll(mediaList);
        }

        return new PostResponse(savePost);
    }

    private String saveFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IOException("File is empty");
        }


        String contentType = file.getContentType();
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
            return TypeMedia.image; // Giá trị mặc định nếu không xác định được
        }

        if (contentType.startsWith("image/")) {
            return TypeMedia.image;
        } else if (contentType.startsWith("video/")) {
            return TypeMedia.video;
        } else {
            return TypeMedia.image; // Hoặc ném ngoại lệ nếu không cho phép loại khác
        }
    }

    public List<PostResponse> getAllPost(){
        return postRepository.findAll().stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
}
