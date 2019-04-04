package com.pivotaccess.kanefw.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.pivotaccess.kanefw.domain.Device;
import com.pivotaccess.kanefw.domain.HardwareFile;
import com.pivotaccess.kanefw.domain.enumeration.FileCategory;
import com.pivotaccess.kanefw.repository.HardwareFileRepository;



/**
 * Service class for managing hardware files.
 */

@Service
public class HardwareFileService {
	
	private final Logger log = LoggerFactory.getLogger(HardwareFileService.class);

	private final HardwareFileRepository hardwareFileRepository;
	
	public HardwareFileService(HardwareFileRepository hardwareFileRepository){
		this.hardwareFileRepository = hardwareFileRepository;
	}
	
	//TEST ENV
    private String baseLocation = "src/main/webapp/uploads";
    
    //PRODUCTION ENV
    //private String baseLocation = "/data/";
	    
    public String getBaseLocation() {
        return baseLocation;
    }
    
    private synchronized void createDirectory(String directoryPath)
            throws IOException {
        File dir = new File(directoryPath);

        if (!dir.exists()) {
            try {
                dir.mkdirs();
                return;
            } catch (SecurityException e) {
                log.error(e.getMessage(), e);
                throw new IOException(e);
            }
        } else {
            if (!dir.isDirectory()) {
                /**
                 * It exists but is a file?
                 */
                throw new IOException(String.format(
                        "File name exists and is not a directory: %s",
                        directoryPath));
            }
        }
    }
    
    public HardwareFile save(HardwareFile hardwareFile, MultipartFile file){
    	
    	try {
			
    		saveToDisk(hardwareFile, file);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("FILE creation error: {} {}", hardwareFile, hardwareFile.getHardware());
		}
    	return hardwareFile;
    }
    
    private HardwareFile saveToDisk(HardwareFile hardwareFile,
            MultipartFile file) throws IOException {
        File f = getFile(hardwareFile);
        log.debug("FILE: {}",f );
        FileOutputStream output = new FileOutputStream(f, true);
        log.debug("FILE PATH: {}", f.getAbsolutePath());
        try {
            output.write(hardwareFile.retrieveContent());
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
        	
		} finally {
            output.close();
        }
        log.debug("OUTPUT FILE: {} {}", output, f.getAbsolutePath());
        return hardwareFile;
    }
    
    private File getFile(HardwareFile hardwareFile) {
        return new File(getDirectoryPath(hardwareFile) + hardwareFile.getTitle());
    }
    
    private String getDirectoryPath(HardwareFile hardwareFile) {
    	try {
			createDirectory(baseLocation + File.separator + hardwareFile.getHardware().getModel()
	                + File.separator + hardwareFile.getVersion());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return baseLocation + File.separator + hardwareFile.getHardware().getModel()
                + File.separator + hardwareFile.getVersion() + File.separator;
    }

	public HardwareFile getLatestFirmware(Device device){
		
		HardwareFile hardwareFile = hardwareFileRepository.findOneByHardwareAndCategoryOrderByVersionDateUploadedDesc( device.getHardware(), FileCategory.FIRMWARE);
		
		return hardwareFile;
	} 
	
	public HardwareFile getHardwareFile(HardwareFile hardwareFile){
		
		log.debug("DETAILS: {} {} {}", 	hardwareFile.getHardware(),
										hardwareFile.getVersion(),
										hardwareFile.getTitle());
		
		HardwareFile hardwareFileRecord = null;
		try {
					
			if (hardwareFile.getHardware().getId() != null){
				log.debug("HARDWARE : {}", hardwareFile.getHardware());
				hardwareFileRecord = hardwareFileRepository.findOneByHardwareAndVersionAndTitle(
															hardwareFile.getHardware(),
															hardwareFile.getVersion(),
															hardwareFile.getTitle());
				log.debug("HARDWARE FILE RECORD: {}", hardwareFileRecord);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.debug("HARDWARE OR HARDWARE FILE NOT EXISTS");
			e.printStackTrace();
		}
		
		return hardwareFileRecord;
	}

}
