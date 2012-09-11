package de.kp.ames.web.core.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

	private static String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
     * Compress the given directory with all its files.
     */
    public static byte[] zipFiles(String scmRootFolderName, List<String> files, String semanticResearchReport) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream zos = new ZipOutputStream(baos);
        //File scmRootFolder = new File(scmRootFolderName);
        
        /*
         * write checkout report in root folder of zip
         * 	SemanticResearchReport.html
         */
        zos.putNextEntry(new ZipEntry("SemanticResearchReport.html"));
        zos.write(semanticResearchReport.getBytes());
        zos.closeEntry();
        
        
        byte bytes[] = new byte[2048];
        
        scmRootFolderName = makeOSDependantAbsoluteFileName(scmRootFolderName);
 
        for (String fileName : files) {
        	
        	System.out.println("========> ZipUtil.zipFiles: " + fileName);
        	
        	fileName = makeOSDependantAbsoluteFileName(fileName);
        	
            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
 
            zos.putNextEntry(new ZipEntry(getRelativeFileName(fileName, scmRootFolderName)));
 
            int bytesRead;
            while ((bytesRead = bis.read(bytes)) != -1) {
                zos.write(bytes, 0, bytesRead);
            }
            zos.closeEntry();
            bis.close();
            fis.close();
        }
        zos.flush();
        baos.flush();
        zos.close();
        baos.close();
 
        return baos.toByteArray();
    }


	private static String makeOSDependantAbsoluteFileName(String fileName) {
		return fileName.replace("file:/", "").replace("/", FILE_SEPARATOR);
	}
	
	private static String getRelativeFileName(String fileName, String scmRootFolderName) {
		String relativeFileName = fileName.replace(scmRootFolderName, "");
		
		System.out.println("========> ZipUtil.zipFiles:getrelative " + relativeFileName);
		if (relativeFileName.startsWith(FILE_SEPARATOR))
			relativeFileName = relativeFileName.substring(1);

		return relativeFileName;
	}

}
