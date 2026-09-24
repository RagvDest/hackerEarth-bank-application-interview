package com.devsu.hackerearth.backend.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.devsu.hackerearth.backend.client.controller.ClientController;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.model.dto.ClientDtoCreate;
import com.devsu.hackerearth.backend.client.service.ClientService;
import com.devsu.hackerearth.backend.client.mapper.ClientMapper;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
public class sampleTest {

	@Autowired
    private ClientService clientService;
    private ClientService clientServiceMock = mock(ClientService.class);
    private ClientMapper clientMapper = mock(ClientMapper.class);
	private ClientController clientController = new ClientController(clientServiceMock, clientMapper);

    @BeforeEach
    void setup(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test 
    void createClientTest() {
        // Arrange
        ClientDto newClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        ClientDtoCreate newClientCreate = new ClientDtoCreate("Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        ClientDto createdClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        when(clientMapper.createToDTO(newClientCreate)).thenReturn(newClient);
        when(clientServiceMock.create(any(ClientDto.class))).thenReturn(createdClient);

        // Act
        ResponseEntity<ClientDto> response = clientController.create(newClientCreate);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdClient, response.getBody());
    }

    @Test
    void updatePartialClientTest(){
        // Arrange
        PartialClientDto dto = new PartialClientDto(false);
        ClientDto updatedClient = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", false);
        when(clientServiceMock.partialUpdate(1L,dto)).thenReturn(updatedClient);

        //Act
        ResponseEntity<ClientDto> response = clientController.partialUpdate(1L,dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedClient, response.getBody());
    }
}
