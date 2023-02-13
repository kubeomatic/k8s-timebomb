package br.com.clusterlab.controller;

import br.com.clusterlab.dto.validation.pod.*;
import br.com.clusterlab.dto.validation.pod.Object;
import br.com.clusterlab.service.ValidationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc
public class ValidationControllerTest {

    Logger logger = LoggerFactory.getLogger(ValidationController.class);
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    ObjectMapper om = new ObjectMapper();
    @DisplayName("Test CREATE POD")
    @Test
    public void testPodCreation() throws Exception {
        String uuid = String.valueOf(UUID.randomUUID());

        PodAdmissionReview podAdmissionReview = new PodAdmissionReview();
        Request request = new Request();
        Resource resource = new Resource();
        Object object = new Object();
        Spec spec = new Spec();
        ArrayList<Container> containerArrayList = new ArrayList<Container>();

        resource.setResource("pods");
        for ( String containerName : List.of("Test-01","Test-02")){
            Container container = new Container();
            container.setName(containerName);
            containerArrayList.add(container);
        }

        spec.setContainers(containerArrayList);
        object.setSpec(spec);

        request.setObject(object);
        request.setResource(resource);
        request.setOperation("CREATE");
        request.setUid(uuid);
        request.setDryRun(false);
        request.setNamespace("awh");
        request.setName("pod-create-test");

        podAdmissionReview.setRequest(request);

        String podAdmissionReviewData = om.writeValueAsString(podAdmissionReview);

        mockMvc.perform(post("/validate/pods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(podAdmissionReviewData)
                .accept(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk());
    }
}
