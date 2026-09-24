package com.devsu.hackerearth.backend.account.mapper;

import com.devsu.hackerearth.backend.account.model.Transaction;
import com.devsu.hackerearth.backend.account.model.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;

import java.util.List;

@Mapper(
    componentModel = "spring"
)
public interface TransactionMapper {

    TransactionDto entityToDTO(Transaction transaction);
    Transaction dtoToEntity(TransactionDto transactionDto);

    List<TransactionDto> toDtoList(List<Transaction> transactionList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    void updateTransactionFromDto(TransactionDto dto, @MappingTarget Transaction entity);
}