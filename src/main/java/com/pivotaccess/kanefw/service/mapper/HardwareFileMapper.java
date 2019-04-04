package com.pivotaccess.kanefw.service.mapper;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pivotaccess.kanefw.domain.Hardware;
import com.pivotaccess.kanefw.domain.HardwareFile;
import com.pivotaccess.kanefw.domain.enumeration.FileCategory;
import com.pivotaccess.kanefw.service.HardwareFileService;

@Service
public class HardwareFileMapper {
   private final Logger log = LoggerFactory.getLogger(HardwareFileMapper.class);
   private String version;   
   private Hardware hardware;
   private final HardwareFileService hardwareFileService;
   
   public HardwareFileMapper(HardwareFileService hardwareFileService){
	   this.hardwareFileService = hardwareFileService;
   }
   
   public Set<HardwareFile> multiPartFilesToHardwareFiles(Hardware hardware,List<MultipartFile> files, String version){
       this.version = version;
       this.hardware = hardware;
	   return files.stream()
           .map((this::multiPartFileToHardwareFile))
           .collect(Collectors.toSet());
   }

   public HardwareFile multiPartFileToHardwareFile(MultipartFile file) {
       HardwareFile hardwareFile = new HardwareFile();
       hardwareFile.setTitle(file.getOriginalFilename());
       hardwareFile.setSize(file.getSize());
       hardwareFile.setMimeType(file.getContentType());
       hardwareFile.setVersion(version);
       hardwareFile.setDateUploaded(LocalDate.now());
       hardwareFile.setCategory(FileCategory.FIRMWARE);
       hardwareFile.setHardware(hardware);
       try {
           hardwareFile.addContent(file.getBytes());
           HardwareFile copy = hardwareFileService.getHardwareFile(hardwareFile);
           log.debug("FILE EXISTS: {}", copy);
           if(copy == null){
        	   hardwareFileService.save(hardwareFile, file);
           } else {
        	   hardwareFile.setId(copy.getId());
        	   hardwareFile.setContent(copy.getContent());
           }
           
       } catch (IOException e) {
           log.error(e.getMessage());
       }

       return hardwareFile;
   }
}
