package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.ReportCreationRequest;
import com.chi.PhongTro.dto.Request.ReportFilterRequest;
import com.chi.PhongTro.dto.Request.ReportUpdateRequest;
import com.chi.PhongTro.dto.Response.PageResponse;
import com.chi.PhongTro.dto.Response.ReportResponse;
import com.chi.PhongTro.entity.Posts;
import com.chi.PhongTro.entity.Reports;
import com.chi.PhongTro.entity.TypeReports;
import com.chi.PhongTro.entity.Users;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.ReportMapper;
import com.chi.PhongTro.repository.PostRepository;
import com.chi.PhongTro.repository.ReportRepository;
import com.chi.PhongTro.repository.TypeReportsRepository;
import com.chi.PhongTro.repository.UsersRepository;
import com.chi.PhongTro.specification.ReportSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    TypeReportsRepository typeReportsRepository;

    ReportMapper reportMapper;

    @PreAuthorize("isAuthenticated()")
    public ReportResponse createReport(ReportCreationRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users reporter = usersRepository.findByPhone(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Users toUser;

        TypeReports typeReports = typeReportsRepository.findById(request.getTypeReportId())
                .orElseThrow(() -> new AppException(ErrorCode.TYPE_REPORT_NOT_EXISTED));
        if (typeReports.getName().equals("OWNER") || typeReports.getName().equals("POST")) {
            toUser = usersRepository.findById("3")
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        }
        else{
            toUser = usersRepository.findByPhone(request.getToUserId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        }


        Reports report = Reports.builder()
                .reporter(reporter)
                .toUser(toUser)
                .reportedId(Long.valueOf(request.getReportedId()))
                .reason(request.getReason())
                .content(request.getContent() != null ? request.getContent() : "" )
                .status("pending")
                .typeReport(typeReports)
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

    @PreAuthorize("hasRole('ADMIN') || hasRole('OWNER')")
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

    public PageResponse<ReportResponse> getReportsByReporter(ReportFilterRequest request){

        var context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Users user = usersRepository.findByPhone(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        Specification<Reports> specification = ReportSpecification.filterReport(
                request.getTypeId(),
                request.getStatus(),
                request.getPhoneReporter(),
                request.getReason(),
                null,
                String.valueOf(user.getUser_id())
        );

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending()
        );

        Page<Reports> reportsPage = reportRepository.findAll(specification, pageable) ;

        Page<ReportResponse> responsePage = reportsPage.map(reportMapper::toReportResponse);

        return new PageResponse<>(responsePage);
    }


    public PageResponse<ReportResponse> getReportByFilter(ReportFilterRequest request){

        var context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Users user = usersRepository.findByPhone(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        Specification<Reports> specification = ReportSpecification.filterReport(
                request.getTypeId(),
                request.getStatus(),
                request.getPhoneReporter(),
                request.getReason(),
                String.valueOf(user.getUser_id()),
                null
        );

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending()
        );

        Page<Reports> reportsPage = reportRepository.findAll(specification, pageable) ;

        Page<ReportResponse> responsePage = reportsPage.map(reportMapper::toReportResponse);

        return new PageResponse<>(responsePage);
    }


    public List<ReportResponse> getReportsByToUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return reportRepository.findAllByToUserPhone(authentication.getName())
                .stream()
                .map(reportMapper::toReportResponse)
                .collect(Collectors.toList());
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteReport(String reportId) {
        Reports report = reportRepository.findById(reportId)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND));
        reportRepository.delete(report);
    }
}
