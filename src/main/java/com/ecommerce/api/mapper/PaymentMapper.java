package com.ecommerce.api.mapper;

import com.ecommerce.api.dtos.payment.PaymentResponseDto;
import com.ecommerce.api.model.PaymentsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final OrderMapper orderMapper;

    public PaymentResponseDto toResponseDto(PaymentsEntity payment) {
        return new PaymentResponseDto(
                payment.getId(),
                orderMapper.toResponseDto(payment.getOrder()),
                payment.getPaymentStatus(),
                payment.getCreatedAt()
        );
    }
}