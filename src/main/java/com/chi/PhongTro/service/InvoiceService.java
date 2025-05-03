package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.InvoiceCreationRequest;
import com.chi.PhongTro.dto.Request.InvoiceDetailCreationRequest;
import com.chi.PhongTro.dto.Request.InvoiceUpdateRequest;
import com.chi.PhongTro.dto.Response.InvoiceResponse;
import com.chi.PhongTro.dto.Response.RoomResponse;
import com.chi.PhongTro.entity.*;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.InvoiceMapper;
import com.chi.PhongTro.repository.InvoiceRepository;
import com.chi.PhongTro.repository.RenterRepository;
import com.chi.PhongTro.repository.RoomRepository;
import com.chi.PhongTro.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InvoiceService {
    InvoiceRepository invoiceRepository;

    UsersRepository usersRepository;

    RoomRepository roomRepository;

    RenterRepository renterRepository;

    InvoiceMapper invoiceMapper;

    BuildingService buildingService;


    @Transactional
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public InvoiceResponse createInvoice(InvoiceCreationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Users owner = usersRepository.findByPhone(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Rooms room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        Renters renter = renterRepository.findById(request.getRenterId())
                .orElseThrow(() -> new AppException(ErrorCode.RENTER_NOT_FOUND));

        // Kiểm tra quyền: Chỉ Admin hoặc người tạo phòng được phép
        if (!checkInvoiceCreationPermission(owner, room, authentication)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // Kiểm tra trạng thái hợp lệ
        if (!List.of("pending", "paid", "overdue").contains(request.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        boolean isRenterInRoom = renter.getRoomRenters().stream()
                .anyMatch(roomRenter -> {
                    return roomRenter.getRenter().getRenterId().equals(renter.getRenterId())
                    && (roomRenter.getEndDate() == null || roomRenter.getEndDate().isAfter(LocalDate.now()));
                });

        // Kiểm tra xem renter có đang thuê phòng này không
        if (!isRenterInRoom) {
            throw new AppException(ErrorCode.RENTER_NOT_IN_ROOM);
        }

        Invoices invoice = Invoices.builder()
                .owner(owner)
                .room(room)
                .renter(renter)
                .totalAmount(0.0)
                .status(request.getStatus())
                .dueDate(request.getDueDate())
                .createdAt(LocalDate.now())
                .details(new ArrayList<>())
                .build();

        // Tạo chi tiết hóa đơn
        List<InvoiceDetails> details = new ArrayList<>();
        if (request.getDetails() != null && !request.getDetails().isEmpty()) {
            // Nếu người dùng cung cấp chi tiết hóa đơn
            for (InvoiceDetailCreationRequest detailRequest : request.getDetails()) {
                InvoiceDetails detail = InvoiceDetails.builder()
                        .invoice(invoice)
                        .description(detailRequest.getDescription())
                        .amount(detailRequest.getAmount())
                        .build();
                details.add(detail);
            }
        } else {
            // Nếu không cung cấp chi tiết, tự động tạo dựa trên giá phòng và các khoản phí cố định
            // 1. Tiền thuê phòng (dựa trên Rooms.price)
            InvoiceDetails rentDetail = InvoiceDetails.builder()
                    .invoice(invoice)
                    .description("Tiền thuê phòng")
                    .amount(room.getPrice())
                    .build();
            details.add(rentDetail);

            // 2. Tiền điện (giả định 200,000 VNĐ)
            InvoiceDetails electricityDetail = InvoiceDetails.builder()
                    .invoice(invoice)
                    .description("Tiền điện")
                    .amount(200000.0)
                    .build();
            details.add(electricityDetail);

            // 3. Tiền nước (giả định 100,000 VNĐ)
            InvoiceDetails waterDetail = InvoiceDetails.builder()
                    .invoice(invoice)
                    .description("Tiền nước")
                    .amount(100000.0)
                    .build();
            details.add(waterDetail);
        }

        // Thêm chi tiết vào hóa đơn
        invoice.getDetails().addAll(details);

        // Lưu hóa đơn
        invoice = invoiceRepository.save(invoice);

        // Cập nhật totalAmount dựa trên các chi tiết
        double totalAmount = invoice.getDetails().stream()
                .mapToDouble(InvoiceDetails::getAmount)
                .sum();
        invoice.setTotalAmount(totalAmount);
        invoice = invoiceRepository.save(invoice);

        return invoiceMapper.toInvoiceResponse(invoice);
    }

    // Lấy tất cả hóa đơn (public)
    public List<InvoiceResponse> getAllInvoices() {
        List<Invoices> invoices = invoiceRepository.findAll();
        return invoices.stream()
                .map(invoiceMapper::toInvoiceResponse)
                .collect(Collectors.toList());
    }

    public List<InvoiceResponse> getAllInvoicesByOwner() {
        var context = SecurityContextHolder.getContext();
        List<Invoices> invoices = invoiceRepository.findAllByOwnerPhone(context.getAuthentication().getName());
        return invoices.stream()
                .map(invoiceMapper::toInvoiceResponse)
                .collect(Collectors.toList());
    }

    // Lấy hóa đơn theo ID (public)
    public InvoiceResponse getInvoiceById(String invoiceId) {
        Invoices invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
        return invoiceMapper.toInvoiceResponse(invoice);
    }

    // Cập nhật hóa đơn (Admin hoặc người tạo)
    @Transactional
    @PreAuthorize("@invoiceService.checkInvoicePermission(#invoiceId, authentication)")
    public InvoiceResponse updateInvoice(String invoiceId, InvoiceUpdateRequest request) {
        Invoices invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        if (request.getTotalAmount() != null) {
            invoice.setTotalAmount(request.getTotalAmount());
        }

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            if (!List.of("pending", "paid", "overdue").contains(request.getStatus())) {
                throw new AppException(ErrorCode.INVALID_STATUS);
            }
            invoice.setStatus(request.getStatus());
        }

        if (request.getDueDate() != null) {
            invoice.setDueDate(request.getDueDate());
        }

        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceResponse(invoice);
    }

    // Xóa hóa đơn (Admin hoặc người tạo)
    @Transactional
    @PreAuthorize("@invoiceService.checkInvoicePermission(#invoiceId, authentication)")
    public void deleteInvoice(String invoiceId) {
        Invoices invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
        invoiceRepository.delete(invoice);
    }

    // Kiểm tra quyền tạo hóa đơn
    private boolean checkInvoiceCreationPermission(Users owner, Rooms room, Authentication authentication) {
        String currentUserPhone = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = owner.getPhone().equals(currentUserPhone);
        boolean hasBuildingPermission = buildingService.checkBuildingPermission(String.valueOf(room.getBuilding().getBuildingId()), authentication);

        return isAdmin || isOwner || hasBuildingPermission;
    }

    // Kiểm tra quyền chỉnh sửa/xóa hóa đơn
    public boolean checkInvoicePermission(String invoiceId, Authentication authentication) {
        Invoices invoice = invoiceRepository.findById(invoiceId)
                .orElse(null);
        if (invoice == null) return false;

        return checkInvoiceCreationPermission(invoice.getOwner(), invoice.getRoom(), authentication);
    }

}
