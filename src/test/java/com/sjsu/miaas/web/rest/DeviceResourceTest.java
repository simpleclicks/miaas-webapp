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
import com.sjsu.miaas.domain.Device;
import com.sjsu.miaas.repository.DeviceRepository;

/**
 * Test class for the DeviceResource REST controller.
 *
 * @see DeviceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class })
public class DeviceResourceTest {
    
    private static final Long DEFAULT_ID = new Long(1L);
    
    private static final String DEFAULT_DEVICE_ID = "SAMPLE_TEXT";
    private static final String UPDATED_DEVICE_ID = "UPDATED_TEXT";
        
    private static final String DEFAULT_DEVICE_IMAGE_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_DEVICE_IMAGE_NAME = "UPDATED_TEXT";
        
    private static final String DEFAULT_DEVICE_STATUS = "SAMPLE_TEXT";
    private static final String UPDATED_DEVICE_STATUS = "UPDATED_TEXT";
        
    private static final String DEFAULT_DEVICE_TYPE = "SAMPLE_TEXT";
    private static final String UPDATED_DEVICE_TYPE = "UPDATED_TEXT";
        
    private static final String DEFAULT_DEVICE_VERSION = "SAMPLE_TEXT";
    private static final String UPDATED_DEVICE_VERSION = "UPDATED_TEXT";
        
    private static final String DEFAULT_DEVICE_MEMORY = "SAMPLE_TEXT";
    private static final String UPDATED_DEVICE_MEMORY = "UPDATED_TEXT";
        
    @Inject
    private DeviceRepository deviceRepository;

    private MockMvc restDeviceMockMvc;

    private Device device;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DeviceResource deviceResource = new DeviceResource();
        ReflectionTestUtils.setField(deviceResource, "deviceRepository", deviceRepository);

        this.restDeviceMockMvc = MockMvcBuilders.standaloneSetup(deviceResource).build();

        device = new Device();
        device.setId(DEFAULT_ID);

        device.setDeviceId(DEFAULT_DEVICE_ID);
        device.setDeviceImageName(DEFAULT_DEVICE_IMAGE_NAME);
        device.setDeviceStatus(DEFAULT_DEVICE_STATUS);
        device.setDeviceType(DEFAULT_DEVICE_TYPE);
        device.setDeviceVersion(DEFAULT_DEVICE_VERSION);
        device.setDeviceMemory(DEFAULT_DEVICE_MEMORY);
    }

    @Test
    public void testCRUDDevice() throws Exception {

        // Create Device
        restDeviceMockMvc.perform(post("/app/rest/devices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(device)))
                .andExpect(status().isOk());

        // Read Device
        restDeviceMockMvc.perform(get("/app/rest/devices/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.deviceId").value(DEFAULT_DEVICE_ID.toString()))
                .andExpect(jsonPath("$.deviceImageName").value(DEFAULT_DEVICE_IMAGE_NAME.toString()))
                .andExpect(jsonPath("$.deviceStatus").value(DEFAULT_DEVICE_STATUS.toString()))
                .andExpect(jsonPath("$.deviceType").value(DEFAULT_DEVICE_TYPE.toString()))
                .andExpect(jsonPath("$.deviceVersion").value(DEFAULT_DEVICE_VERSION.toString()))
                .andExpect(jsonPath("$.deviceMemory").value(DEFAULT_DEVICE_MEMORY.toString()));

        // Update Device
        device.setDeviceId(UPDATED_DEVICE_ID);
        device.setDeviceImageName(UPDATED_DEVICE_IMAGE_NAME);
        device.setDeviceStatus(UPDATED_DEVICE_STATUS);
        device.setDeviceType(UPDATED_DEVICE_TYPE);
        device.setDeviceVersion(UPDATED_DEVICE_VERSION);
        device.setDeviceMemory(UPDATED_DEVICE_MEMORY);

        restDeviceMockMvc.perform(post("/app/rest/devices")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(device)))
                .andExpect(status().isOk());

        // Read updated Device
        restDeviceMockMvc.perform(get("/app/rest/devices/{id}", DEFAULT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(DEFAULT_ID.intValue()))
                .andExpect(jsonPath("$.deviceId").value(UPDATED_DEVICE_ID.toString()))
                .andExpect(jsonPath("$.deviceImageName").value(UPDATED_DEVICE_IMAGE_NAME.toString()))
                .andExpect(jsonPath("$.deviceStatus").value(UPDATED_DEVICE_STATUS.toString()))
                .andExpect(jsonPath("$.deviceType").value(UPDATED_DEVICE_TYPE.toString()))
                .andExpect(jsonPath("$.deviceVersion").value(UPDATED_DEVICE_VERSION.toString()))
                .andExpect(jsonPath("$.deviceMemory").value(UPDATED_DEVICE_MEMORY.toString()));

        // Delete Device
        restDeviceMockMvc.perform(delete("/app/rest/devices/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Read nonexisting Device
        restDeviceMockMvc.perform(get("/app/rest/devices/{id}", DEFAULT_ID)
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());

    }
}
