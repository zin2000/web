package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * IO処理ユーティリティ.
 *
 */
public class IOUtil {
    /** 読み込みバッファ長. */
    private static final int READ_BUF_LEN = 2048;

    /**
     * IOUtilダミーコンストラクタ.
     */
    protected IOUtil() {
    }

    /**
     * エラー発生を抑止したclose処理.
     *
     * @param is
     *            閉じたい入力ストリーム
     */
    public static void close(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                return;
            }
        }
    }

    /**
     * エラー発生を抑止したclose処理.
     *
     * @param r
     *            閉じたいReader
     */
    public static void close(Reader r) {
        if (r != null) {
            try {
                r.close();
            } catch (IOException e) {
                return;
            }
        }
    }

    /**
     * エラー発生を抑止したclose処理.
     *
     * @param w
     *            閉じたいWriter
     */
    public static void close(Writer w) {
        if (w != null) {
            try {
                w.close();
            } catch (IOException e) {
                return;
            }
        }
    }

    /**
     * エラー発生を抑止したclose処理.
     *
     * @param os
     *            閉じたい出力ストリーム
     */
    public static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                return;
            }
        }
    }

    /**
     * 文字列を読み込む.
     *
     * @param r
     *            読み込み元Reader
     * @return 読み込んだ文字列
     * @throws IOException
     *             読み込み中IO例外
     */
    public static String read(Reader r) throws IOException {
        final StringBuilder buf = new StringBuilder();
        int len = 0;
        final char[] cb = new char[READ_BUF_LEN];
        do {
            len = r.read(cb);
            if (len > 0) {
                buf.append(cb, 0, len);
            }
        } while (len >= 0);
        return buf.toString();
    }

    /**
     * バイト列を読み込む.
     *
     * @param is
     *            読み込み元InputStream
     * @return 読み込んだバッファ列
     * @throws IOException
     *             読み込み中IO例外
     */
    public static byte[] read(InputStream is) throws IOException {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            int len = 0;
            final byte[] buf = new byte[READ_BUF_LEN];
            do {
                len = is.read(buf);
                if (len > 0) {
                    baos.write(buf, 0, len);
                }
            } while (len >= 0);
            return baos.toByteArray();
        } finally {
            close(baos);
        }
    }
}
