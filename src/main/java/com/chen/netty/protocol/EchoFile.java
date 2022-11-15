package com.chen.netty.protocol;

import org.msgpack.annotation.Index;

import java.io.Serializable;

/**
 * @Author @Chenxc
 * @Date 2022/2/21 16:56
 */
public class EchoFile implements Serializable {
    private static final long serialVersionUID = 1L;
    @Index(0)
    private String clientFilePath;// 文件在客户端上面的位置
    @Index(1)
    private String fileName;//文件名称
    @Index(2)
    private String file_md5;// 文件MD5
    @Index(3)
    private long starPos;// 开始位置
    @Index(4)
    private byte[] bytes;// 文件字节数组
    @Index(5)
    private long readSize;// 结尾位置
    @Index(6)
    private long fileSize;//文件总大小
    @Index(7)
    private boolean check;//刚开始上传时候用于检测文件

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getClientFilePath() {
        return clientFilePath;
    }

    public void setClientFilePath(String clientFilePath) {
        this.clientFilePath = clientFilePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFile_md5() {
        return file_md5;
    }

    public void setFile_md5(String file_md5) {
        this.file_md5 = file_md5;
    }

    public long getStarPos() {
        return starPos;
    }

    public void setStarPos(long starPos) {
        this.starPos = starPos;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public long getReadSize() {
        return readSize;
    }

    public void setReadSize(long readSize) {
        this.readSize = readSize;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public String toString() {
        return "EchoFile{" + "fileName=" + fileName + ", file_md5=" + file_md5 + ", starPos=" + starPos + ", readSize=" + readSize + ", fileSize=" + fileSize + ", check=" + check + '}';
    }

}
