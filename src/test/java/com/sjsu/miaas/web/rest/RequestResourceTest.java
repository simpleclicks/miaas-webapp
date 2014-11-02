package com.sjsu.miaas.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;
import org.joda.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.sjsu.miaas.Application;
import com.sjsu.miaas.domain.Request;
import com.sjsu.miaas.repository.RequestRepository;

/**
 * Test class for the RequestResource REST controller.
 *
 * @see RequestResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
public class RequestResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);
    
    private static final String DEFAULT_REQUEST_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_REQUEST_TYPE = "UPDATED_TEXT";
        
    private static final LocalDate DEFAULT_REQUEST_START_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_REQUEST_START_DATE = new LocalDate();
        
    private static final LocalDate DEFAULT_REQUEST_END_DATE = new LocalDate(0L);
    private static final LocalDate UPDATED_REQUEST_END_DATE = new LocalDate();
        
    private static final Integer DEFAULT_RESOURCE_QUANTITY = 0;
    private static final Integer UPDATED_RESOURCE_QUANTITY = 1;
        
    private static final String DEFAULT_RESOURCE_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_RESOURCE_TYPE = "UPDATED_TEXT";
        
    private static final String DEFAULT_RESOURCE_VERSION = "SAMPLE_TEXT";
    private static final String UPDATED_RESOURCE_VERSION = "UPDATED_TEXT";
        
    private static final String DEFAULT_RESOURCE_MEMORY = "SAMPLE_TEXT";
    private static final String UPDATED_RESOURCE_MEMORY = "UPDATED_TEXT";
        
    private static final Boolean DEFAULT_RESOURCE_BACKUP = false;
    private static final Boolean UPDATED_RESOURCE_BACKUP = true;
    @Inject
    private RequestRepository requestRepository;

    private MockMvc restRequestMockMvc;

    private Request request;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RequestResource requestResource = new RequestResource();
        ReflectionTestUtils.setField(requestResource, "requestRepository", requestRepository);

        this.restRequestMockMvc = MockMvcBuilders.standaloneSetup(requestResource).build();

        request = new Request();
        request.setId(DEFAULT_ID);

        request.setRequestType(DEFAULT_REQUEST_TYPE);
        request.setRequestStartDate(DEFAULT_REQUEST_START_DATE);
        request.setRequestEndDate(DEFAULT_REQUEST_END_DATE);
        request.setResourceQuantity(DEFAULT_RESOURCE_QUANTITY);
        request.setResourceType(DEFAULT_RESOURCE_TYPE);
        request.setResourceVersion(DEFAULT_RESOURCE_VERSION);
        request.setResourceMemory(DEFAULT_RESOURCE_MEMORY);
        request.setResourceBackup(DEFAULT_RESOURCE_BACKUP);
    }

    @Test
    public void testCRUDRequest() throws Exception {

        // Create Request
        restRequestMockMvc.perform(post("/app/rest/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isOk());

        // Read Request
        restRequestMockMvc.perform(get("/app/rest/requests/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.requestType").value(DEFAULT_REQUEST_TYPE.toString()))
                .andExpect(jsonPath("$.requestStartDate").value(DEFAULT_REQUEST_START_DATE.toString()))
                .andExpect(jsonPath("$.requestEndDate").value(DEFAULT_REQUEST_END_DATE.toString()))
                .andExpect(jsonPath("$.resourceQuantity").value(DEFAULT_RESOURCE_QUANTITY))
                .andExpect(jsonPath("$.resourceType").value(DEFAULT_RESOURCE_TYPE.toString()))
                .andExpect(jsonPath("$.resourceVersion").value(DEFAULT_RESOURCE_VERSION.toString()))
                .andExpect(jsonPath("$.resourceMemory").value(DEFAULT_RESOURCE_MEMORY.toString()))
                .andExpect(jsonPath("$.resourceBackup").value(DEFAULT_RESOURCE_BACKUP.booleanValue()));

        // Update Request
        request.setRequestType(UPDATED_REQUEST_TYPE);
        request.setRequestStartDate(UPDATED_REQUEST_START_DATE);
        request.setRequestEndDate(UPDATED_REQUEST_END_DATE);
        request.setResourceQuantity(UPDATED_RESOURCE_QUANTITY);
        request.setResourceType(UPDATED_RESOURCE_TYPE);
        request.setResourceVersion(UPDATED_RESOURCE_VERSION);
        request.setResourceMemory(UPDATED_RESOURCE_MEMORY);
        request.setResourceBackup(UPDATED_RESOURCE_BACKUP);

        restRequestMockMvc.perform(post("/app/rest/requests")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(request)))
                .andExpect(status().isOk());

        // Read updated Request
        restRequestMockMvc.perform(get("/app/rest/requests/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.requestType").value(UPDATED_REQUEST_TYPE.toString()))
                .andExpect(jsonPath("$.requestStartDate").value(UPDATED_REQUEST_START_DATE.toString()))
                .andExpect(jsonPath("$.requestEndDate").value(UPDATED_REQUEST_END_DATE.toString()))
                .andExpect(jsonPath("$.resourceQuantity").value(UPDATED_RESOURCE_QUANTITY))
                .andExpect(jsonPath("$.resourceType").value(UPDATED_RESOURCE_TYPE.toString()))
                .andExpect(jsonPath("$.resourceVersion").value(UPDATED_RESOURCE_VERSION.toString()))
                .andExpect(jsonPath("$.resourceMemory").value(UPDATED_RESOURCE_MEMORY.toString()))
                .andExpect(jsonPath("$.resourceBackup").value(UPDATED_RESOURCE_BACKUP.booleanValue()));

        // Delete Request
        restRequestMockMvc.perform(delete("/app/rest/requests/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Read nonexisting Request
        restRequestMockMvc.perform(get("/app/rest/requests/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
