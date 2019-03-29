package com.pivotaccess.kanefw.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pivotaccess.kanefw.domain.Device;
import com.pivotaccess.kanefw.domain.HardwareFile;
import com.pivotaccess.kanefw.domain.enumeration.FileCategory;
import com.pivotaccess.kanefw.repository.HardwareFileRepository;



/**
 * Service class for managing hardware files.
 */

@Service
@Transactional

public class HardwareFileService {
	
	private final Logger log = LoggerFactory.getLogger(HardwareFileService.class);

	private final HardwareFileRepository hardwareFileRepository;
	
	public HardwareFileService(HardwareFileRepository hardwareFileRepository) {
		// TODO Auto-generated constructor stub
		this.hardwareFileRepository = hardwareFileRepository;
	}
	
	public HardwareFile getLatestFirmware(Device device){
		
		HardwareFile hardwareFile = hardwareFileRepository.findOneByHardwareAndCategoryOrderByVersionDateUploadedDesc( device.getHardware(), FileCategory.FIRMWARE);
		
		return hardwareFile;
	} 

}
