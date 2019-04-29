package co.edu.uniandes.xrepo.service.dto;

import java.io.Serializable;

/**
 * A DTO for the SamplesFiles entity.
 */
public class SamplesFilesParametersDTO implements Serializable {

    private String filePath;

    private long fileSize;

    private int totalLines;

    private int processedLines;

    private int processedLinesOk;

    /**
     * @return the filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @return the fileSize
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the processedLinesOk
     */
    public int getProcessedLinesOk() {
        return processedLinesOk;
    }

    /**
     * @param processedLinesOk the processedLinesOk to set
     */
    public void setProcessedLinesOk(int processedLinesOk) {
        this.processedLinesOk = processedLinesOk;
    }

    /**
     * @return the processedLines
     */
    public int getProcessedLines() {
        return processedLines;
    }

    /**
     * @param processedLines the processedLines to set
     */
    public void setProcessedLines(int processedLines) {
        this.processedLines = processedLines;
    }

    /**
     * @return the totalLines
     */
    public int getTotalLines() {
        return totalLines;
    }

    /**
     * @param totalLines the totalLines to set
     */
    public void setTotalLines(int totalLines) {
        this.totalLines = totalLines;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    
}
