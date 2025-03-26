package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.ReportCreationRequest;
import com.chi.PhongTro.dto.Request.ReportUpdateRequest;
import com.chi.PhongTro.dto.Response.ReportResponse;
import com.chi.PhongTro.entity.Posts;
import com.chi.PhongTro.entity.Reports;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.ReportMapper;
import com.chi.PhongTro.repository.PostRepository;
import com.chi.PhongTro.repository.ReportRepository;
import com.chi.PhongTro.repository.UsersRepository;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
    ReportRepository reportRepository;

    UsersRepository usersRepository;

    PostRepository postRepository;

    ReportMapper reportMapper;

    @PreAuthorize("isAuthenticated()")
    public ReportResponse createReport(ReportCreationRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users reporter = usersRepository.findByPhone(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Users reportedUser = usersRepository.findById(request.getReportedUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Posts post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Reports report = Reports.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .post(post)
                .reason(request.getReason())
                .status("pending")
                .createdAt(LocalDate.now())
                .build();
        report = reportRepository.save(report);
        return reportMapper.toReportResponse(report);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ReportResponse> getAllReports(){
        List<Reports> reports = reportRepository.findAll();
        return reports.stream()
                .map(reportMapper::toReportResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ReportResponse updateReport(String reportId, ReportUpdateRequest request) {
        Reports report = reportRepository.findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND));

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {

            if (!List.of("pending", "resolved").contains(request.getStatus())) {
                throw new AppException(ErrorCode.INVALID_STATUS);
            }
            report.setStatus(request.getStatus());
        }

        report = reportRepository.save(report);
        return reportMapper.toReportResponse(report);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteReport(String reportId) {
        Reports report = reportRepository.findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND));
        reportRepository.delete(report);
    }
}
