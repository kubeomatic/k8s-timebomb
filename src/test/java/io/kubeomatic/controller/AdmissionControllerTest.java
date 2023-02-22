package io.kubeomatic.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;



import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AdmissionControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @DisplayName("Test VALIDATE CREATE POD WITHOUT VALIDTY DATA")
    @Test
    public void testValidatePodCreationWithNoValidityData() throws Exception {
        String podAdmissionReviewData = ValidationServiceTest.podAdmissionBuilder(
                "awh",
                "pod-test",
                "pods",
                "CREATE",
                List.of("Test-01","Test-02"),
                60l,
                "1m",
                "enabled");

        mockMvc.perform(post("/api/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(podAdmissionReviewData)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status.code").value("403"));
    }
    @DisplayName("Test VALIDATE CREATE POD")
    @Test
    public void testValidatePodCreation() throws Exception {
        String podAdmissionReviewData = ValidationServiceTest.podAdmissionBuilder(
                "awh",
                "pod-test",
                "pods",
                "CREATE",
                List.of("Test-01","Test-02"),
                60l,
                "1m",
                "enabled");

        mockMvc.perform(post("/api/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(podAdmissionReviewData)
                        .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status.code").value("403"));
    }
    @DisplayName("Test VALIDATE DELETE POD")
    @Test
    public void testValidatePodDeletion() throws Exception {
        String podAdmissionReviewData = ValidationServiceTest.podAdmissionBuilder(
                "awh",
                "pod-test",
                "pods",
                "DELETE",
                List.of("Test-01","Test-02"),
                60L,
                "1m",
                "enabled");

        mockMvc.perform(post("/api/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(podAdmissionReviewData)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());

    }
    @DisplayName("TEST VALIDATE DEPLOYMENT CREATE DISABLED")
    @Test
    public void testValidateDeploymentCreateDisabled() throws Exception {
        String podAdmissionReviewData = ValidationServiceTest.podAdmissionBuilder(
                "awh",
                "pod-test",
                "deployments",
                "CREATE",
                List.of("Test-01","Test-02"),
                60l,
                "1m",
                "disabled"
        );
        mockMvc.perform(post("/api/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(podAdmissionReviewData)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status.code").value("403"));

    }
    @DisplayName("TEST VALIDATE DEPLOYMENT CREATE EXPIRED")
    @Test
    public void testValidateDeploymentCreateExpired() throws Exception {
        String podAdmissionReviewData = ValidationServiceTest.podAdmissionBuilder(
                "awh",
                "pod-test",
                "deployments",
                "CREATE",
                List.of("Test-01","Test-02"),
                -600l,
                "1m",
                "disabled"
        );
        mockMvc.perform(post("/api/validation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(podAdmissionReviewData)
                        .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status.code").value("403"));

    }
    @DisplayName("Test VALIDATE CREATE DEPLOYMENT")
    @Test
    public void testValidateDeploymentCreation() throws Exception {
        String podAdmissionReviewData = ValidationServiceTest.podAdmissionBuilder(
                "awh",
                "pod-test",
                "deployments",
                "CREATE",
                List.of("Test-01","Test-02"),
                60l,
                "1m",
                "enabled");

        mockMvc.perform(post("/api/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(podAdmissionReviewData)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status.code").value("200"));
    }
    @DisplayName("Test VALIDATE DELETE DEPLOYMENT")
    @Test
    public void testValidateDeploymentDeletion() throws Exception {
        String podAdmissionReviewData = ValidationServiceTest.podAdmissionBuilder(
                "awh",
                "pod-test",
                "deployments",
                "DELETE",
                List.of("Test-01","Test-02"),
                60l,
                "1m",
                "enabled");

        mockMvc.perform(post("/api/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(podAdmissionReviewData)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest());
    }
    @DisplayName("Test Health Liveness")
    @Test
    public void testHealthLiveness() throws Exception {

        mockMvc.perform(get("/actuator/health/liveness")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(content().json("{\"status\":\"UP\"}"));
    }
    @DisplayName("Test Health Readiness")
    @Test
    public void testHealthReadiness() throws Exception {

        mockMvc.perform(get("/actuator/health/readiness")
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk()).andExpect(content().json("{\"status\":\"UP\"}"));
    }
}
