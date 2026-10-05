package hu.gyanikevin.szavazas.controller;

import hu.gyanikevin.szavazas.dto.request.SzavazasRequestDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazasEredmenyResponseDto;
import hu.gyanikevin.szavazas.dto.response.SzavazatResponseDto;
import hu.gyanikevin.szavazas.service.SzavazasService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/szavazat")
    public ResponseEntity<SzavazatResponseDto> szavazatLekerdezese(@RequestParam("szavazas") String szavazas, @RequestParam("kepviselo") String kepviselo) {
        return new ResponseEntity<>(service.szavazatLekerdezese(szavazas, kepviselo), HttpStatus.OK);
    }

    @GetMapping("/eredmeny")
    public ResponseEntity<SzavazasEredmenyResponseDto> eredmenyLekerdezese(@RequestParam("szavazas") String szavazas) {
        return new ResponseEntity<>(service.eredmenyLekerdezese(szavazas), HttpStatus.OK);
    }
}
