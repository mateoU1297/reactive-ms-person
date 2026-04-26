package com.pragma.ms_person.infrastructure.input.rest;

import com.pragma.ms_person.application.dto.EnrollmentRequest;
import com.pragma.ms_person.application.dto.EnrollmentResponse;
import com.pragma.ms_person.application.dto.PersonRequest;
import com.pragma.ms_person.application.dto.PersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class PersonRouter {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/persons",
                    method = RequestMethod.POST,
                    beanClass = PersonRestHandler.class,
                    beanMethod = "save",
                    operation = @Operation(
                            operationId = "savePerson",
                            summary = "Register a new person",
                            tags = {"Person"},
                            parameters = {},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = PersonRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201",
                                            content = @Content(
                                                    schema = @Schema(implementation = PersonResponse.class)
                                            )),
                                    @ApiResponse(responseCode = "400", description = "Invalid field"),
                                    @ApiResponse(responseCode = "409",
                                            description = "Person already exists")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/persons/{personId}/enrollments",
                    method = RequestMethod.POST,
                    beanClass = PersonRestHandler.class,
                    beanMethod = "enroll",
                    operation = @Operation(
                            operationId = "enrollPerson",
                            summary = "Enroll a person in a bootcamp",
                            tags = {"Person"},
                            parameters = {
                                    @Parameter(name = "personId", in = ParameterIn.PATH,
                                            required = true,
                                            schema = @Schema(type = "integer", format = "int64"))
                            },
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = EnrollmentRequest.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(responseCode = "201",
                                            content = @Content(
                                                    schema = @Schema(implementation = EnrollmentResponse.class)
                                            )),
                                    @ApiResponse(responseCode = "400",
                                            description = "Max enrollments exceeded or date conflict"),
                                    @ApiResponse(responseCode = "404",
                                            description = "Person or bootcamp not found"),
                                    @ApiResponse(responseCode = "409",
                                            description = "Already enrolled")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> personRoutes(PersonRestHandler handler) {
        return RouterFunctions.route()
                .POST("/api/v1/persons", handler::save)
                .POST("/api/v1/persons/{personId}/enrollments", handler::enroll)
                .build();
    }
}
