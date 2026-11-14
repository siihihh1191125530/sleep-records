package cn.local.psqirecord;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/** Portable, unencrypted backup with a SHA-256 checksum for accidental corruption. */
public final class BackupFile {
 public static final int MAX=64*1024*1024;
 private static final String MAGIC="PSQI-BACKUP-1\n";
 public static String hash(String text)throws Exception{byte[] digest=MessageDigest.getInstance("SHA-256").digest(text.getBytes(StandardCharsets.UTF_8));StringBuilder s=new StringBuilder();for(byte b:digest)s.append(String.format("%02x",b&255));return s.toString();}
 public static byte[] encode(String content)throws Exception{byte[] out=(MAGIC+hash(content)+"\n"+content).getBytes(StandardCharsets.UTF_8);if(out.length>MAX)throw new IOException("备份超过64 MB限制");return out;}
 public static String decode(byte[] data)throws Exception{if(data.length>MAX)throw new IOException("备份超过64 MB限制");String text=new String(data,StandardCharsets.UTF_8);if(!text.startsWith(MAGIC))throw new IOException("不是受支持的备份文件");int split=text.indexOf('\n',MAGIC.length());if(split<0)throw new IOException("备份文件不完整");String checksum=text.substring(MAGIC.length(),split),content=text.substring(split+1);if(!checksum.matches("[0-9a-f]{64}")||!MessageDigest.isEqual(checksum.getBytes(StandardCharsets.US_ASCII),hash(content).getBytes(StandardCharsets.US_ASCII)))throw new IOException("备份完整性校验失败");return content;}
}
