package com.sjsu.miaas.web.rest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

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
import com.sjsu.miaas.domain.AmazonInstance;
import com.sjsu.miaas.repository.AmazonInstanceRepository;

/**
 * Test class for the AmazonInstanceResource REST controller.
 *
 * @see AmazonInstanceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
public class AmazonInstanceResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);
    
    private static final String DEFAULT_INSTANCE_ID = "SAMPLE_TEXT";
    private static final String UPDATED_INSTANCE_ID = "UPDATED_TEXT";
        
    private static final String DEFAULT_INSTANCE_IMAGE_ID = "SAMPLE_TEXT";
    private static final String UPDATED_INSTANCE_IMAGE_ID = "UPDATED_TEXT";
        
    private static final String DEFAULT_INSTANCE_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_INSTANCE_TYPE = "UPDATED_TEXT";
        
    private static final String DEFAULT_INSTANCE_REGION = "SAMPLE_TEXT";
    private static final String UPDATED_INSTANCE_REGION = "UPDATED_TEXT";
        
    private static final String DEFAULT_INSTANCE_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_INSTANCE_STATUS = "UPDATED_TEXT";
        
    @Inject
    private AmazonInstanceRepository amazoninstanceRepository;

    private MockMvc restAmazonInstanceMockMvc;

    private AmazonInstance amazoninstance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AmazonInstanceResource amazoninstanceResource = new AmazonInstanceResource();
        ReflectionTestUtils.setField(amazoninstanceResource, "amazoninstanceRepository", amazoninstanceRepository);

        this.restAmazonInstanceMockMvc = MockMvcBuilders.standaloneSetup(amazoninstanceResource).build();

        amazoninstance = new AmazonInstance();
        amazoninstance.setId(DEFAULT_ID);

        amazoninstance.setInstanceId(DEFAULT_INSTANCE_ID);
        amazoninstance.setInstanceImageId(DEFAULT_INSTANCE_IMAGE_ID);
        amazoninstance.setInstanceType(DEFAULT_INSTANCE_TYPE);
        amazoninstance.setInstanceRegion(DEFAULT_INSTANCE_REGION);
        amazoninstance.setInstanceStatus(DEFAULT_INSTANCE_STATUS);
    }

    @Test
    public void testCRUDAmazonInstance() throws Exception {

        // Create AmazonInstance
        restAmazonInstanceMockMvc.perform(post("/app/rest/amazoninstances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(amazoninstance)))
                .andExpect(status().isOk());

        // Read AmazonInstance
        restAmazonInstanceMockMvc.perform(get("/app/rest/amazoninstances/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.instanceId").value(DEFAULT_INSTANCE_ID.toString()))
                .andExpect(jsonPath("$.instanceImageId").value(DEFAULT_INSTANCE_IMAGE_ID.toString()))
                .andExpect(jsonPath("$.instanceType").value(DEFAULT_INSTANCE_TYPE.toString()))
                .andExpect(jsonPath("$.instanceRegion").value(DEFAULT_INSTANCE_REGION.toString()))
                .andExpect(jsonPath("$.instanceStatus").value(DEFAULT_INSTANCE_STATUS.toString()));

        // Update AmazonInstance
        amazoninstance.setInstanceId(UPDATED_INSTANCE_ID);
        amazoninstance.setInstanceImageId(UPDATED_INSTANCE_IMAGE_ID);
        amazoninstance.setInstanceType(UPDATED_INSTANCE_TYPE);
        amazoninstance.setInstanceRegion(UPDATED_INSTANCE_REGION);
        amazoninstance.setInstanceStatus(UPDATED_INSTANCE_STATUS);

        restAmazonInstanceMockMvc.perform(post("/app/rest/amazoninstances")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(amazoninstance)))
                .andExpect(status().isOk());

        // Read updated AmazonInstance
        restAmazonInstanceMockMvc.perform(get("/app/rest/amazoninstances/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.instanceId").value(UPDATED_INSTANCE_ID.toString()))
                .andExpect(jsonPath("$.instanceImageId").value(UPDATED_INSTANCE_IMAGE_ID.toString()))
                .andExpect(jsonPath("$.instanceType").value(UPDATED_INSTANCE_TYPE.toString()))
                .andExpect(jsonPath("$.instanceRegion").value(UPDATED_INSTANCE_REGION.toString()))
                .andExpect(jsonPath("$.instanceStatus").value(UPDATED_INSTANCE_STATUS.toString()));

        // Delete AmazonInstance
        restAmazonInstanceMockMvc.perform(delete("/app/rest/amazoninstances/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Read nonexisting AmazonInstance
        restAmazonInstanceMockMvc.perform(get("/app/rest/amazoninstances/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
