package com.devsu.hackerearth.backend.client.mapper;

import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.model.dto.ClientDtoCreate;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.BeanMapping;
import java.util.List;

@Mapper(
    componentModel = "spring"
)
public interface ClientMapper {

    ClientDto entityToDTO(Client client);
    Client dtoToEntity(ClientDto clientDto);

    @Mapping(target = "id", ignore = true)
    ClientDto createToDTO(ClientDtoCreate ClientDtoCreate);

    List<ClientDto> toDtoList(List<Client> clientList);

    @Mapping(target = "id", ignore = true)
    void updateClientFromDto(ClientDto dto, @MappingTarget Client entity);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClientFromPartialDto(PartialClientDto partialDto, @MappingTarget Client entity);
}