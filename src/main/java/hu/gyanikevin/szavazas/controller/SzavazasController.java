package hu.gyanikevin.szavazas.controller;

import hu.gyanikevin.szavazas.dto.request.SzavazasRequestDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasResponseDto;
import hu.gyanikevin.szavazas.service.SzavazasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/szavazasok")
@RequiredArgsConstructor
public class SzavazasController {

    private final SzavazasService service;

    @PostMapping("/szavazas")
    public ResponseEntity<SzavazasResponseDto> szavazasMentese(@Valid @RequestBody SzavazasRequestDto dto) {

        return new ResponseEntity<>(service.szavazasMentese(dto), HttpStatus.CREATED);

    }
}
