package com.devsu.hackerearth.backend.account.mapper;

import com.devsu.hackerearth.backend.account.model.Account;
import com.devsu.hackerearth.backend.account.model.dto.AccountDto;
import com.devsu.hackerearth.backend.account.model.dto.PartialAccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;

import java.util.List;

@Mapper(
    componentModel = "spring"
)
public interface AccountMapper {

    AccountDto entityToDTO(Account account);
    Account dtoToEntity(AccountDto accountDto);

    List<AccountDto> toDtoList(List<Account> accountList);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    void updateAccountFromDto(AccountDto dto, @MappingTarget Account entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccountFromPartialDto(PartialAccountDto partialDto, @MappingTarget Account entity);
}