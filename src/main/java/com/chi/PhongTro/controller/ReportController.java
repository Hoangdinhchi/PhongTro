package com.chi.PhongTro.controller;


import com.chi.PhongTro.dto.Request.ApiResponse;
import com.chi.PhongTro.dto.Request.ReportCreationRequest;
import com.chi.PhongTro.dto.Request.ReportFilterRequest;
import com.chi.PhongTro.dto.Request.ReportUpdateRequest;
import com.chi.PhongTro.dto.Response.PageResponse;
import com.chi.PhongTro.dto.Response.ReportResponse;
import com.chi.PhongTro.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportController {
    ReportService reportService;


    @PostMapping
    public ApiResponse<ReportResponse> createReport(@RequestBody ReportCreationRequest request) {
        ReportResponse response = reportService.createReport(request);
        return ApiResponse.<ReportResponse>builder()
                .code(1000)
                .result(response)
                .build();
    }


    @GetMapping
    public ApiResponse<List<ReportResponse>> getAllReports() {
        List<ReportResponse> reports = reportService.getAllReports();
        return ApiResponse.<List<ReportResponse>>builder()
                .code(1000)
                .result(reports)
                .build();
    }

    @GetMapping("/reporters")
    public ApiResponse<PageResponse<ReportResponse>> getAllReportsByReporter(@ModelAttribute ReportFilterRequest request) {
        return ApiResponse.<PageResponse<ReportResponse>>builder()
                .code(1000)
                .result(reportService.getReportsByReporter(request))
                .build();
    }

    @GetMapping("/to-user")
    public ApiResponse<PageResponse<ReportResponse>> getAllReportsByToUser(ReportFilterRequest request) {
        return ApiResponse.<PageResponse<ReportResponse>>builder()
                .code(1000)
                .result(reportService.getReportByFilter(request))
                .build();
    }


    @GetMapping("/admin")
    public ApiResponse<PageResponse<ReportResponse>> getAllReportsByAdmin(@ModelAttribute ReportFilterRequest request) {
        return ApiResponse.<PageResponse<ReportResponse>>builder()
                .code(1000)
                .result(reportService.getReportByFilter(request))
                .build();
    }

    @PutMapping("/{reportId}")
    public ApiResponse<ReportResponse> updateReport(
            @PathVariable String reportId,
            @RequestBody ReportUpdateRequest request) {
        ReportResponse response = reportService.updateReport(reportId, request);
        return ApiResponse.<ReportResponse>builder()
                .code(1000)
                .result(response)
                .build();
    }

    @DeleteMapping("/{reportId}")
    public ApiResponse<String> deleteReport(@PathVariable String reportId) {
        reportService.deleteReport(reportId);
        return ApiResponse.<String>builder()
                .code(1000)
                .result("Deleted report with id: " + reportId + " successfully")
                .build();
    }
}
