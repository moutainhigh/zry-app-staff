package com.zhongmei.yunfu.context.util;

import com.zhongmei.yunfu.context.util.constants.MemoryConstants;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;



public final class FileIOUtils {

    private FileIOUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static final String LINE_SEP = System.getProperty("line.separator");


    public static boolean writeFileFromIS(String filePath, final InputStream is) {
        return writeFileFromIS(FileUtils.getFileByPath(filePath), is, false);
    }


    public static boolean writeFileFromIS(String filePath, final InputStream is, boolean append) {
        return writeFileFromIS(FileUtils.getFileByPath(filePath), is, append);
    }


    public static boolean writeFileFromIS(File file, final InputStream is) {
        return writeFileFromIS(file, is, false);
    }


    public static boolean writeFileFromIS(File file, final InputStream is, boolean append) {
        if (!FileUtils.createOrExistsFile(file) || is == null) return false;
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[1024];
            int len;
            while ((len = is.read(data, 0, 1024)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseUtils.closeIO(is, os);
        }
    }


    public static boolean writeFileFromBytesByStream(String filePath, final byte[] bytes) {
        return writeFileFromBytesByStream(FileUtils.getFileByPath(filePath), bytes, false);
    }


    public static boolean writeFileFromBytesByStream(String filePath, final byte[] bytes, boolean append) {
        return writeFileFromBytesByStream(FileUtils.getFileByPath(filePath), bytes, append);
    }


    public static boolean writeFileFromBytesByStream(File file, final byte[] bytes) {
        return writeFileFromBytesByStream(file, bytes, false);
    }


    public static boolean writeFileFromBytesByStream(File file, final byte[] bytes, boolean append) {
        if (bytes == null || !FileUtils.createOrExistsFile(file)) return false;
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(file, append));
            bos.write(bytes);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseUtils.closeIO(bos);
        }
    }


    public static boolean writeFileFromBytesByChannel(String filePath, final byte[] bytes, boolean isForce) {
        return writeFileFromBytesByChannel(FileUtils.getFileByPath(filePath), bytes, false, isForce);
    }


    public static boolean writeFileFromBytesByChannel(String filePath, final byte[] bytes, boolean append, boolean isForce) {
        return writeFileFromBytesByChannel(FileUtils.getFileByPath(filePath), bytes, append, isForce);
    }


    public static boolean writeFileFromBytesByChannel(File file, final byte[] bytes, boolean isForce) {
        return writeFileFromBytesByChannel(file, bytes, false, isForce);
    }


    public static boolean writeFileFromBytesByChannel(File file, final byte[] bytes, boolean append, boolean isForce) {
        if (bytes == null) return false;
        FileChannel fc = null;
        try {
            fc = new FileOutputStream(file, append).getChannel();
            fc.position(fc.size());
            fc.write(ByteBuffer.wrap(bytes));
            if (isForce) fc.force(true);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseUtils.closeIO(fc);
        }
    }


    public static boolean writeFileFromBytesByMap(String filePath, final byte[] bytes, boolean isForce) {
        return writeFileFromBytesByMap(filePath, bytes, false, isForce);
    }


    public static boolean writeFileFromBytesByMap(String filePath, final byte[] bytes, boolean append, boolean isForce) {
        return writeFileFromBytesByMap(FileUtils.getFileByPath(filePath), bytes, append, isForce);
    }


    public static boolean writeFileFromBytesByMap(File file, final byte[] bytes, boolean isForce) {
        return writeFileFromBytesByMap(file, bytes, false, isForce);
    }


    public static boolean writeFileFromBytesByMap(File file, final byte[] bytes, boolean append, boolean isForce) {
        if (bytes == null || !FileUtils.createOrExistsFile(file)) return false;
        FileChannel fc = null;
        try {
            fc = new FileOutputStream(file, append).getChannel();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bytes.length);
            mbb.put(bytes);
            if (isForce) mbb.force();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseUtils.closeIO(fc);
        }
    }


    public static boolean writeFileFromString(String filePath, String content) {
        return writeFileFromString(FileUtils.getFileByPath(filePath), content, false);
    }


    public static boolean writeFileFromString(String filePath, String content, boolean append) {
        return writeFileFromString(FileUtils.getFileByPath(filePath), content, append);
    }


    public static boolean writeFileFromString(File file, String content) {
        return writeFileFromString(file, content, false);
    }


    public static boolean writeFileFromString(File file, String content, boolean append) {
        if (file == null || content == null) return false;
        if (!FileUtils.createOrExistsFile(file)) return false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            CloseUtils.closeIO(bw);
        }
    }



    public static List<String> readFile2List(String filePath) {
        return readFile2List(FileUtils.getFileByPath(filePath), null);
    }


    public static List<String> readFile2List(String filePath, String charsetName) {
        return readFile2List(FileUtils.getFileByPath(filePath), charsetName);
    }


    public static List<String> readFile2List(File file) {
        return readFile2List(file, 0, 0x7FFFFFFF, null);
    }


    public static List<String> readFile2List(File file, String charsetName) {
        return readFile2List(file, 0, 0x7FFFFFFF, charsetName);
    }


    public static List<String> readFile2List(String filePath, int st, int end) {
        return readFile2List(FileUtils.getFileByPath(filePath), st, end, null);
    }


    public static List<String> readFile2List(String filePath, int st, int end, String charsetName) {
        return readFile2List(FileUtils.getFileByPath(filePath), st, end, charsetName);
    }


    public static List<String> readFile2List(File file, int st, int end) {
        return readFile2List(file, st, end, null);
    }


    public static List<String> readFile2List(File file, int st, int end, String charsetName) {
        if (!FileUtils.isFileExists(file)) return null;
        if (st > end) return null;
        BufferedReader reader = null;
        try {
            String line;
            int curLine = 1;
            List<String> list = new ArrayList<>();
            if (isSpace(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            }
            while ((line = reader.readLine()) != null) {
                if (curLine > end) break;
                if (st <= curLine && curLine <= end) list.add(line);
                ++curLine;
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(reader);
        }
    }


    public static String readFile2String(String filePath) {
        return readFile2String(FileUtils.getFileByPath(filePath), null);
    }


    public static String readFile2String(String filePath, String charsetName) {
        return readFile2String(FileUtils.getFileByPath(filePath), charsetName);
    }


    public static String readFile2String(File file) {
        return readFile2String(file, null);
    }


    public static String readFile2String(File file, String charsetName) {
        if (!FileUtils.isFileExists(file)) return null;
        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();
            if (isSpace(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(LINE_SEP);
            }
                        return sb.delete(sb.length() - LINE_SEP.length(), sb.length()).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(reader);
        }
    }


    public static byte[] readFile2BytesByStream(String filePath) {
        return readFile2BytesByStream(FileUtils.getFileByPath(filePath));
    }


    public static byte[] readFile2BytesByStream(File file) {
        if (!FileUtils.isFileExists(file)) return null;
        FileInputStream fis = null;
        ByteArrayOutputStream os = null;
        try {
            fis = new FileInputStream(file);
            os = new ByteArrayOutputStream();
            byte[] b = new byte[MemoryConstants.KB];
            int len;
            while ((len = fis.read(b, 0, MemoryConstants.KB)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(fis, os);
        }
    }


    public static byte[] readFile2BytesByChannel(String filePath) {
        return readFile2BytesByChannel(FileUtils.getFileByPath(filePath));
    }


    public static byte[] readFile2BytesByChannel(File file) {
        if (!FileUtils.isFileExists(file)) return null;
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fc.size());
            while (true) {
                if (!((fc.read(byteBuffer)) > 0)) break;
            }
            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(fc);
        }
    }


    public static byte[] readFile2BytesByMap(String filePath) {
        return readFile2BytesByMap(FileUtils.getFileByPath(filePath));
    }


    public static byte[] readFile2BytesByMap(File file) {
        if (!FileUtils.isFileExists(file)) return null;
        FileChannel fc = null;
        try {
            fc = new RandomAccessFile(file, "r").getChannel();
            int size = (int) fc.size();
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
            byte[] result = new byte[size];
            mbb.get(result, 0, size);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseUtils.closeIO(fc);
        }
    }

    private static boolean isSpace(String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
