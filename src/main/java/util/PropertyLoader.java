package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Propertiesファイルを指定クラスを基準に読み込むユーティリティ.
 *
 */
public class PropertyLoader {
    /** 基準クラス. */
    private final Class<?> clazz_;
    /** 読み込んだPropertiesの内容. */
    private final Properties prop_;

    /**
     * 基準クラス付の初期化.
     *
     * @param clazz
     *            基準クラス
     */
    public PropertyLoader(Class<?> clazz) {
        this.clazz_ = clazz;
        this.prop_ = new Properties();
    }

    /**
     * 値の読み込み.
     *
     * @param key
     *            キー
     * @return 値(存在しない場合null)
     */
    public String getValue(String key) {
        return prop_.getProperty(key);
    }

    /**
     * ディフォルトつき読み込み.
     *
     * @param key
     *            キー
     * @param defaultValue
     *            存在しなかった場合の戻り値
     * @return 取得した値(存在しない場合はdefaultValue)
     */
    public String getValue(String key, String defaultValue) {
        return prop_.getProperty(key, defaultValue);
    }

    /**
     * クラスと指定名称からリソースを開いてPropertiesを読み込む.
     *
     * @param name
     *            指定名称(コンストラクタで指定したクラスからの相対位置)
     */
    protected void load(String name) {
        InputStream is = null;
        try {
            is = findStream(name);
            if (is != null) {
                try {
                    prop_.load(is);
                } finally {
                    IOUtil.close(is);
                }
            }
        } catch (IOException e) {
            return;
        }
    }

    /**
     * リソースロード処理.
     *
     * @param name
     *            コンストラクタで指定したクラスからの相対ファイル名
     * @return 入力ストリーム
     */
    protected InputStream findStream(String name) {
        return clazz_.getResourceAsStream(name);
    }

    /**
     * ファイルの読み込み処理.
     *
     * 初期指定クラスから、親に向かってクラス名+.propertiesのPropertiesファイルを読み込む
     */
    public void load() {
        Class<?> c = clazz_;
        do {
            load(c.getSimpleName() + ".properties");
            c = c.getSuperclass();
        } while (c != null);
    }
}
