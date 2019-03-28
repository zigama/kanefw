package com.pivotaccess.kanefw.service.mapper;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.pivotaccess.kanefw.domain.HardwareFile;

@Service
public class HardwareFileMapper {
   private final Logger log = LoggerFactory.getLogger(HardwareFileMapper.class);
   public Set<HardwareFile> multiPartFilesToHardwareFiles(List<MultipartFile> files){
       return files.stream()
           .map((this::multiPartFileToHardwareFile))
           .collect(Collectors.toSet());
   }

   public HardwareFile multiPartFileToHardwareFile(MultipartFile file) {
       HardwareFile hardwareFile = new HardwareFile();
       hardwareFile.setTitle(file.getOriginalFilename());
       hardwareFile.setSize(file.getSize());
       hardwareFile.setMimeType(file.getContentType());
       try {
           hardwareFile.addContent(file.getBytes());
       } catch (IOException e) {
           log.error(e.getMessage());
       }

       return hardwareFile;
   }
}
