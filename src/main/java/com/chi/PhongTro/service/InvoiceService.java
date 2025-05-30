package com.chi.PhongTro.service;


import com.chi.PhongTro.dto.Request.*;
import com.chi.PhongTro.dto.Response.InvoiceResponse;
import com.chi.PhongTro.dto.Response.PageResponse;
import com.chi.PhongTro.dto.Response.RoomResponse;
import com.chi.PhongTro.entity.*;
import com.chi.PhongTro.exception.AppException;
import com.chi.PhongTro.exception.ErrorCode;
import com.chi.PhongTro.mapper.InvoiceMapper;
import com.chi.PhongTro.repository.InvoiceRepository;
import com.chi.PhongTro.repository.RenterRepository;
import com.chi.PhongTro.repository.RoomRepository;
import com.chi.PhongTro.repository.UsersRepository;
import com.chi.PhongTro.specification.InvoiceSpecification;
import jakarta.transaction.Transactional;
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
        if (!List.of("pending", "paid", "overdue", "cancel").contains(request.getStatus())) {
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
            InvoiceDetails rentDetail = InvoiceDetails.builder()
                    .invoice(invoice)
                    .description("Tiền thuê phòng")
                    .amount(room.getPrice())
                    .build();
            details.add(rentDetail);
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
//
//    public List<InvoiceResponse> getAllInvoicesByOwner() {
//        var context = SecurityContextHolder.getContext();
//        List<Invoices> invoices = invoiceRepository.findAllByOwnerPhone(context.getAuthentication().getName());
//        return invoices.stream()
//                .map(invoiceMapper::toInvoiceResponse)
//                .collect(Collectors.toList());
//    }


    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public PageResponse<InvoiceResponse> getAllInvoicesByOwner(InvoiceFilterRequest request) {
        var context = SecurityContextHolder.getContext();
        Users user = usersRepository.findByPhone(context.getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Specification<Invoices> spec = InvoiceSpecification.filterInvoice(
                request.getStatus(),
                request.getRoomName(),
                request.getNameRenter(),
                request.getPhoneRenter(),
                request.getFromDate(),
                request.getToDate(),
                String.valueOf(user.getUser_id()),
                null
        );

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending()
        );

        Page<Invoices> invoicesPage = invoiceRepository.findAll(spec, pageable);
        Page<InvoiceResponse> responsePage = invoicesPage.map(invoiceMapper::toInvoiceResponse);
        return new PageResponse<>(responsePage);
    }

    public PageResponse<InvoiceResponse> getAllInvoicesByRenter(InvoiceFilterRequest request) {
        var context = SecurityContextHolder.getContext();
        Users user = usersRepository.findByPhone(context.getAuthentication().getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Specification<Invoices> spec = InvoiceSpecification.filterInvoice(
                request.getStatus(),
                request.getRoomName(),
                null,
                null,
                request.getFromDate(),
                request.getToDate(),
                null,
                user.getPhone()

        );

        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by("createdAt").descending()
        );

        Page<Invoices> invoicesPage = invoiceRepository.findAll(spec, pageable);
        Page<InvoiceResponse> responsePage = invoicesPage.map(invoiceMapper::toInvoiceResponse);
        return new PageResponse<>(responsePage);

    }


    @PreAuthorize("@invoiceService.checkInvoicePermission(#invoiceId, authentication)")
    public InvoiceResponse getInvoiceById(String invoiceId) {
        Invoices invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
        return invoiceMapper.toInvoiceResponse(invoice);
    }

    public List<InvoiceResponse> getInvoicesByRoomId(String roomId) {
        return invoiceRepository.findAllByRoomId(roomId)
                .stream()
                .map(invoiceMapper::toInvoiceResponse)
                .collect(Collectors.toList());
    }



    public List<InvoiceResponse> getInvoicesByRoomIdAndRenterPhone (String roomId, String phone){
        return invoiceRepository.findAllByRoomIdAndRenterPhone(roomId, phone)
                .stream()
                .map(invoiceMapper::toInvoiceResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    @PreAuthorize("@invoiceService.checkInvoicePermission(#invoiceId, authentication)")
    public InvoiceResponse updateInvoice(String invoiceId, InvoiceUpdateRequest request) {
        Invoices invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        if (request.getTotalAmount() != null) {
            invoice.setTotalAmount(request.getTotalAmount());
        }

        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            if (!List.of("pending", "paid", "overdue", "cancel").contains(request.getStatus())) {
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

    @Transactional

    public InvoiceResponse updateStatus(String invoiceId, InvoiceUpdateStatusRequest request) {
        Invoices invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));

        if (!List.of("pending", "paid", "overdue", "cancel").contains(request.getStatus())) {
            throw new AppException(ErrorCode.INVALID_STATUS);
        }

        invoice.setStatus(request.getStatus());
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

    public Invoices getInvoiceId(String invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_FOUND));
    }

}
