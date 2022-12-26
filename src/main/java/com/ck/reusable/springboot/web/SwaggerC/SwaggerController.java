package com.ck.reusable.springboot.web.SwaggerC;

import com.ck.reusable.springboot.domain.Swagger.SwaggerUserDoc;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import retrofit2.http.POST;

@RestController
@RequiredArgsConstructor
public class SwaggerController {

    @Operation(summary = "test", description = "테스트", tags = {"Member API"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = SwaggerUserDoc.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/api/save")
    public SwaggerUserDoc.SwaggerUserSignDto swaggerUserSignDto()
    {
        return new SwaggerUserDoc.SwaggerUserSignDto();
    }

}
