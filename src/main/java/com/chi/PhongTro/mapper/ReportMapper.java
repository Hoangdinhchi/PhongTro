package com.chi.PhongTro.mapper;

import com.chi.PhongTro.dto.Response.ReportResponse;
import com.chi.PhongTro.entity.Reports;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    @Mapping(target = "reporterId", source = "reporter.user_id")
    @Mapping(target = "reporterPhone", source = "reporter.phone")
    @Mapping(target = "toUserId", source = "toUser.user_id")
    @Mapping(target = "toUserPhone", source = "toUser.phone")
    ReportResponse toReportResponse(Reports report);
}