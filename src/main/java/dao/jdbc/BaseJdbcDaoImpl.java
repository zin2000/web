package dao.jdbc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import util.PropertyLoader;

import dao.BaseDao;

/**
 * DAOの既定処理.
 *
 * 主な機能: Propertiesファイルに定義したSQL文を取得する(実装クラス名と同じ名前のproeprtiesファイルが対象)
 * RENTのレコード作成・更新と履歴レコードの作成
 */
public class BaseJdbcDaoImpl  extends SimpleJdbcDaoSupport implements BaseDao {
    /**
     * datacenter_rent_idをrent_idの更新時に条件としない場合.
     * (予約時やSYSIDベースVLANや回収時でdatacenter_rent_idが不定なケースなど).
     */
    protected static final int UNDEFINE_DATACENTER_RENT_ID = Integer.MIN_VALUE;
    /** 任意のRENT_IDを処理対象としない場合. */
    protected static final int UNDEFINE_RENT_ID = Integer.MIN_VALUE;

    /** 検索結果行が存在しない場合の queryIntの戻り値. */
    protected static final int NULL_QUERY_INT_VALUE = Integer.MIN_VALUE;
    /** 検索結果行が存在しない場合の queryIntの戻り値. */
    protected static final long NULL_QUERY_LONG_VALUE = Long.MIN_VALUE;

    /** プロパティの削除の際にすべてのキーを削除する場合のキー指定値. */
    protected static final String DELETE_ALL_PROPERTIES = null;
    /** 検索結果が0件の場合にエラーを発生しない(検索結果が0件の場合、リスト検索の場合空リスト、1件検索の場合nullを返す). */
    //public static final FaultCode EMPTY_NO_FAULT = null;

    /** SQLのFOR UPDATE文. */
    private static final String SQLQUERYPART_FOR_UPDATE = " FOR UPDATE";
    /** 空文字. */
    private static final String EMPTY_STRING = "";

    /** テーブル名後置:: 在庫. */
    protected static final String TABLENAME_SUFFIX_MASTER = "_MASTER";
    /** テーブル名後置:: 貸出. */
    protected static final String TABLENAME_SUFFIX_RENT = "_RENT";
    /** テーブル名後置:: 履歴. */
    protected static final String TABLENAME_SUFFIX_HISTORY = "_HISTORY";

    /** DBの履歴テーブルの格納処理用のカラム名文字列の生成. */
    private static final HashMap<String, String> RENT_COLUMNNAME_MAP = new HashMap<String, String>();
    /** Propertyファイルの内容保持. */
    private final PropertyLoader pl;

    // IPクエリ検索用のSQLテンプレート名(IP、IDGWで利用)
    /** SQLテンプレート:: IP情報と紐付くメインISCSIストレージのIPアドレス第4オクテット値のSQLテンプレート名. */
    protected static final String TEMPLATENAME_ISCSI_IP_ADDRES_4TH = "ipAddress4th";
    /** SQLテンプレート:: ストレージカテゴリタイプ値のSQLテンプレート名. */
    protected static final String TEMPLATENAME_DATASOURCE_CATEGORY_TYPE_ID = "datasourceCategoryTypeId";
    
    // IPクエリ検索用の定数(IP、IDGWで利用)
    /** API間共通定数:: IP情報と紐付くメインISCSIストレージのIPアドレス第4オクテット値 */
    protected static final int ISCSI_IP_ADDRES_4TH = 200;
    
    /**
     * 既定処理の基本処理定義.
     * SQLの外部読み込みpropertyファイル(クラスと同じディレクトリにあるクラス名+'.properties')のロードを行う
     */
    public BaseJdbcDaoImpl() {
	pl = new PropertyLoader(this.getClass());
	pl.load();
    }

    /**
     * SQL定義Property内容の取得. JDBCのDAO実装から直接の呼び出しのみ.
     *
     * @param name
     *            SQL定義プロパティキー
     * @return SQL定義プロパティに定義されているSQL文字列(存在しない場合NULL)
     */
    protected String findSql(final String name) {
	return pl.getValue(name);
    }

//    /**
//     * 指定したMASTERテーブルの情報を論理削除する.
//     *
//     * 指定したPKのIDの行をDELETE_FLAG=TRUE/UPD_DT=NOW()/UPD_OP=[opId]で1行更新する
//     * RENTテーブルの場合は履歴の記録が必要なのでこのメソッドでなく
//     * {@link #deleteRentAndHistory(String, long, int, FaultCode)}を利用してください
//     *
//     * <dl>
//     * <dt>deleteError</dt>
//     * <dd>指定したID値のレコードが存在しない・複数論理削除対象があった場合・JDBC処理例外(SQL文の例外エラー・引数不足など)
//     * ・引数NULLなどの他例外(SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param srcTableName
//     *            論理削除対象のテーブル名
//     * @param id
//     *            マスターテーブルのPKのID値
//     * @param deleteError
//     *            削除失敗時の例外
//     * @return 処理成否(失敗時は例外になるのでfalseがかえるケースはない)
//     * @throws WebApiCommandException
//     *             GENERAL_DB_OTHER_ERROR[DB処理の例外],
//     */
//    protected boolean deleteMasterRecord(final long opId, final String srcTableName, final int id,
//	    final FaultCode deleteError) throws WebApiCommandException {
//	final MapSqlParameterSource map = new MapSqlParameterSource(TEMPLATENAME_OP_ID, opId).addValue("srcId", id);
//	final StringBuilder buf = new StringBuilder().append("UPDATE ").append(srcTableName)
//		.append(" SET delete_flag=TRUE, upd_dt=NOW(), upd_op=:opId WHERE ").append(srcTableName)
//		.append("_id=:srcId");
//	return updateOneRecord(buf.toString(), map, deleteError);
//    }
//
//    /**
//     * RENT/MASTERの要素行を両方論理削除する.
//     * RENT行はRENT_IDで指定された行・履歴は削除後の状態を追加・MASTERはMASTER_IDで指定された行が対象
//     *
//     * <dl>
//     * <dt>masterError</dt>
//     * <dd>在庫行論理削除エラー</dd>
//     * <dt>rentError</dt>
//     * <dd>貸出行論理削除エラー</dd>
//     * <dt>histError</dt>
//     * <dd>変更履歴行作成エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param baseTableName
//     *            操作テーブルベース名
//     * @param masterId
//     *            在庫テーブルのID
//     * @param rentId
//     *            貸出テーブルのID
//     * @param masterError
//     *            削除失敗時エラーコード(在庫)
//     * @param rentError
//     *            削除失敗時エラーコード(貸出)
//     * @param histError
//     *            削除失敗時エラーコード(履歴)
//     * @return 処理成否（falseは返らず、Exceptionとなる）
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public boolean deleteRentAndHistoryAndMaster(final long opId, final String baseTableName, final int masterId,
//	    final int rentId, final FaultCode masterError, final FaultCode rentError, final FaultCode histError)
//	    throws WebApiCommandException {
//	deleteRentAndHistory(opId, baseTableName, rentId, rentError, histError);
//	deleteMasterRecord(opId, baseTableName + TABLENAME_SUFFIX_MASTER, masterId, masterError);
//	return true;
//    }
//
//    /**
//     * 汎用カウンターの値取得.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_COUNTER_UPDATE_ERROR</dt>
//     * <dd>カウンタ行更新(インクリメント)失敗</dd>
//     * <dt>FaultCode.GENERAL_COUNTER_INSERT_ERROR</dt>
//     * <dd>非存在カウンタ行作成失敗</dd>
//     * <dt>FaultCode.GENERAL_COUNTER_NO_SUCH_ERROR</dt>
//     * <dd>カウンタ行が存在しないため値取得に失敗</dd>
//     * </dl>
//     *
//     * @param name
//     *            カウンタ名称
//     * @return 次のカウンタID
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected int nextCounter(final String name) throws WebApiCommandException {
//	final MapSqlParameterSource map = new MapSqlParameterSource("name", name);
//	final int row = updateDB(findSql("UPDATE_COUNTER"), map, FaultCode.GENERAL_COUNTER_UPDATE_ERROR);
//	if (row == 0) {
//	    insertOneRecord(findSql("INSERT_COUNTER"), map, FaultCode.GENERAL_COUNTER_INSERT_ERROR);
//	}
//	return queryForInt(findSql("SELECT_COUNTER"), map, FaultCode.GENERAL_COUNTER_NO_SUCH_ERROR,
//		FaultCode.GENERAL_COUNTER_SELECT_ERROR);
//    }
//
//    /**
//     * 指定したRENT_IDの要素の履歴行を追加する. 対象のRENT行はrent_idのみが条件(論理削除でも履歴行を作成)
//     * RENTテーブルの更新した行の内容をHISTORYに登録する
//     *
//     * <dl>
//     * <dt>insertError</dt>
//     * <dd>履歴行の登録結果行数が1でない(通常発生しない)・履歴用のカラム名称取得に失敗</dd>
//     * </dl>
//     *
//     * @param rentTableName
//     *            RENTテーブル名
//     * @param historyTableName
//     *            履歴テーブル名
//     * @param rentId
//     *            RENTの内容をコピーするレコードのPK
//     * @param insertError
//     *            INSERT文実行時のFaultCode
//     * @return 履歴登録可否(trueしか帰らない/失敗はすべて例外発生)
//     * @throws WebApiCommandException
//     *             履歴行の追加時のDBエラー
//     */
//    protected boolean insertHistoryByRentId(final String rentTableName, final String historyTableName,
//	    final int rentId, final FaultCode insertError) throws WebApiCommandException {
//	final String columns = findHistoryColumnNames(historyTableName, insertError);
//	final StringBuilder sql = new StringBuilder().append("INSERT INTO ").append(historyTableName).append("(")
//		.append(columns).append(") SELECT ").append(columns).append(" FROM ").append(rentTableName)
//		.append(" WHERE ").append(rentTableName).append("_id=:rentId");
//	insertOneRecord(sql.toString(), new MapSqlParameterSource("rentId", rentId), insertError);
//	return true;
//    }
//
//    /**
//     * RENTレコード作成と対応する履歴レコードの作成.
//     *
//     * DELETE_FLAG/REG_OP/REG_DT/UPD_OP/UPD_DTは固定設定する
//     * 実行するとRENTとHISTORYに1レコードずつINSERTされる。
//     *
//     * <dl>
//     * <dt>insertRentError</dt>
//     * <dd>貸出行の1行追加に失敗</dd>
//     * <dt>insertHistError</dt>
//     * <dd>履歴行の1行追加に失敗</dd>
//     * </dl>
//     *
//     * @param opId
//     *            OP_ID
//     * @param baseTableName
//     *            テーブル基幹名(_RENTや_HISTORYをはずしたテーブル名)
//     * @param insertColumnValueMap
//     *            レコード作成時にセットする要素マップ(キー:カラム名)
//     * @param insertRentError
//     *            RENT行作成エラー
//     * @param insertHistError
//     *            HISTORY行作成エラー
//     *
//     * @return 作成した貸出ID
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public int insertRentAndHistoryForRentId(final long opId, final String baseTableName,
//	    final MapSqlParameterSource insertColumnValueMap, final FaultCode insertRentError,
//	    final FaultCode insertHistError) throws WebApiCommandException {
//	final String rentTableName = baseTableName + TABLENAME_SUFFIX_RENT;
//	final String historyTableName = baseTableName + TABLENAME_SUFFIX_HISTORY;
//
//	// 貸出テーブルのINSERT処理
//	final int rentId = insertRecord(opId, rentTableName, insertColumnValueMap.getValues(), insertRentError);
//	// 対応する履歴のINSERT処理
//	insertHistoryByRentId(rentTableName, historyTableName, rentId, insertHistError);
//	return rentId;
//    }
//
//    /**
//     * メインレコード作成と対応する履歴レコードの作成.
//     *
//     * DELETE_FLAG/REG_OP/REG_DT/UPD_OP/UPD_DTは固定設定する
//     * 実行するとRENTとHISTORYに1レコードずつINSERTされる。
//     *
//     * <dl>
//     * <dt>insertRentError</dt>
//     * <dd>貸出行の1行追加に失敗</dd>
//     * <dt>insertHistError</dt>
//     * <dd>履歴行の1行追加に失敗</dd>
//     * </dl>
//     *
//     * @param opId
//     *            OP_ID
//     * @param mainTableName
//     *            テーブル基幹名(メインのテーブル名)
//     * @param historyTableName
//     *            テーブル基幹名(履歴のテーブル名)
//     * @param insertColumnValueMap
//     *            レコード作成時にセットする要素マップ(キー:カラム名)
//     * @param insertRentError
//     *            RENT行作成エラー
//     * @param insertHistError
//     *            HISTORY行作成エラー
//     *
//     * @return 作成した貸出ID
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public int insertTableAndHistory(final long opId, final String mainTableName, final String historyTableName,
//	    final MapSqlParameterSource insertColumnValueMap, final FaultCode insertRentError,
//	    final FaultCode insertHistError) throws WebApiCommandException {
//	// 貸出テーブルのINSERT処理
//	final int mainId = insertRecord(opId, mainTableName, insertColumnValueMap.getValues(), insertRentError);
//	// 対応する履歴のINSERT処理
//	insertHistoryByRentId(mainTableName, historyTableName, mainId, insertHistError);
//	return mainId;
//    }
//
//    /**
//     * RENTレコード作成と対応する履歴レコードの作成.
//     *
//     * DELETE_FLAG/REG_OP/REG_DT/UPD_OP/UPD_DTは固定設定する
//     * 実行するとRENTとHISTORYに1レコードずつINSERTされる。
//     *
//     * <dl>
//     * <dt>insertRentError</dt>
//     * <dd>貸出行の1行追加に失敗</dd>
//     * <dt>insertHistError</dt>
//     * <dd>履歴行の1行追加に失敗</dd>
//     * </dl>
//     *
//     * @param opId
//     *            OP_ID
//     * @param baseTableName
//     *            テーブル基幹名(_RENTや_HISTORYをはずしたテーブル名)
//     * @param insertColumnValueMap
//     *            レコード作成時にセットする要素マップ(キー:カラム名)
//     * @param insertRentError
//     *            RENT行生成結果行数エラー
//     * @param insertHistError
//     *            HISTORY行生成結果行数エラー
//     *
//     * @return 作成したステータステーブルのID
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public boolean insertRentAndHistory(final long opId, final String baseTableName,
//	    final MapSqlParameterSource insertColumnValueMap, final FaultCode insertRentError,
//	    final FaultCode insertHistError) throws WebApiCommandException {
//	insertRentAndHistoryForRentId(opId, baseTableName, insertColumnValueMap, insertRentError, insertHistError);
//	return true;
//    }
//
//    /**
//     * 行を１行追加する. 対象行はAUTO_INCREMENT対象であること
//     * <dl>
//     * <dt>insertError</dt>
//     * <dd>1行追加に失敗/JDBC処理例外(SQL文の例外エラー・引数不足など)/引数NULLなどの他例外(
//     * SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param tableName
//     *            テーブル名
//     * @param insertColumnValueMap
//     *            行作成引数マップ
//     * @param insertError
//     *            追加処理時の例外
//     *
//     * @return 追加した行のRENT_ID
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    private int insertRecord(final long opId, final String tableName, final Map<String, Object> insertColumnValueMap,
//	    final FaultCode insertError) throws WebApiCommandException {
//	final MapSqlParameterSource map = new MapSqlParameterSource(); // パラメータの組立
//	for (final Map.Entry<String, Object> e : insertColumnValueMap.entrySet()) {
//	    map.addValue(e.getKey(), e.getValue());
//	}
//	map.addValue(TEMPLATENAME_OP_ID, opId);
//	return insertOneRecord(TypeHDbUtil.makeInsertSql(tableName, insertColumnValueMap), map, insertError);
//    }
//
//    /**
//     * 在庫行を１行追加する.
//     *
//     * <dl>
//     * <dt>insertError</dt>
//     * <dd>在庫行の1行追加に失敗・AUTO_INCREMENT結果が不正(結果なし・複数)・JDBC処理例外(SQL文の例外エラー・引数不足など)
//     * ・引数NULLなどの他例外(SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param masterTableName
//     *            在庫テーブル名
//     * @param insertColumnValueMap
//     *            在庫行作成引数マップ
//     * @param insertError
//     *            追加処理時の例外
//     *
//     * @return 追加した在庫行のMASTER_ID
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected int insertMasterRecord(final long opId, final String masterTableName,
//	    final Map<String, Object> insertColumnValueMap, final FaultCode insertError) throws WebApiCommandException {
//	return insertRecord(opId, masterTableName, insertColumnValueMap, insertError);
//    }
//
//    /**
//     * RENT_IDで指定したRENT行のステータスを変更し、HISTORY記録する.
//     *
//     *
//     * <dl>
//     * <dt>rentError</dt>
//     * <dd>貸出行更新エラー</dd>
//     * <dt>histError</dt>
//     * <dd>変更結果履歴行作成エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            コマンド操作ID
//     * @param baseTableName
//     *            _RENTや_HISTORYを外したテーブル名
//     * @param datacenterRentId
//     *            変更対象rent行のdatacenter_rent_idの値(もしチェックしない場合は
//     *            {@link #UNDEFINE_DATACENTER_RENT_ID}を設定する)
//     * @param rentId
//     *            変更対象のXXX_RENT_IDのID
//     * @param nextStatus
//     *            新しく変更するstatusの数値
//     * @param rentError
//     *            貸出更新失敗時のFaultCode(指定した要素が存在しない場合・DB処理全般エラー)
//     * @param histError
//     *            履歴登録失敗時のFasultCode
//     * @return ステータス変更の成否（失敗時は例外が発生するのでtrueのみ）
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     *
//     */
//    public boolean updateRentStatusChange(final long opId, final String baseTableName, final int datacenterRentId,
//	    final int rentId, final StatusTypeDef nextStatus, final FaultCode rentError, final FaultCode histError)
//	    throws WebApiCommandException {
//	return updateRentStatusAndHistory(opId, baseTableName, datacenterRentId, rentId, new MapSqlParameterSource(
//		"status", nextStatus.getCode()), rentError, histError);
//    }
//
//    /**
//     * RENT_IDで指定したRENTを論理削除し、履歴にも登録する.
//     *
//     * 削除の際にdatacenter_rent_idの整合性はすでに確認済みであること。
//     *
//     * delete_flag/upd_op/upd_dtのフィールドが変更される（statusについても変更しない）＋履歴への登録
//     *
//     * <dl>
//     * <dt>deleteRentError</dt>
//     * <dd>貸出行論理削除エラー</dd>
//     * <dt>insertHistError</dt>
//     * <dd>変更履歴行作成エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            操作ID
//     * @param baseTableName
//     *            操作テーブル基底名
//     * @param rentId
//     *            RENT_ID
//     * @param deleteRentError
//     *            貸出行論理削除処理失敗時のエラーコード
//     * @param insertHistError
//     *            履歴行追加処理失敗時のエラーコード
//     * @return 処理成否(falseはかえらない。エラーが発生する)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public boolean deleteRentAndHistory(final long opId, final String baseTableName, final int rentId,
//	    final FaultCode deleteRentError, final FaultCode insertHistError) throws WebApiCommandException {
//	final MapSqlParameterSource updateColumnValueMap = new MapSqlParameterSource("delete_flag", true);
//	return updateRentAndHistoryByRentId(opId, baseTableName, UNDEFINE_DATACENTER_RENT_ID, rentId,
//		updateColumnValueMap, deleteRentError, insertHistError);
//    }
//
//    /**
//     * RENT_IDで指定したRENTの属性を更新する。（変更した情報をそのままHISTORYに記録する).
//     *
//     * <dl>
//     * <dt>updateError</dt>
//     * <dd>貸出行更新エラー(指定したRENT_ID行が存在しない場合含む)</dd>
//     * <dt>updateError</dt>
//     * <dd>変更結果履歴行作成エラー</dd>
//     *
//     * </dl>
//     *
//     * @param opId
//     *            OPID
//     * @param baseTableName
//     *            RENTとHISTORYの名前のベース名
//     * @param datacenterRentId
//     *            更新対象RENT行に設定されているdatacenter_rent_idの値(
//     *            datacenter_rent_idの内容を確認しない場合は
//     *            {@link #UNDEFINE_DATACENTER_RENT_ID}を設定)
//     * @param rentId
//     *            更新対象RENT_ID行
//     * @param updateColumnValueMap
//     *            RENT行の更新カラムマップ
//     * @param updateRentError
//     *            履歴更新失敗時エラー(RENTID指定なのでRENT行がない場合)
//     * @param insertHistError
//     *            履歴作成失敗時エラー
//     * @return 更新成否
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public boolean updateRentStatusAndHistory(final long opId, final String baseTableName, final int datacenterRentId,
//	    final int rentId, final MapSqlParameterSource updateColumnValueMap, final FaultCode updateRentError,
//	    final FaultCode insertHistError) throws WebApiCommandException {
//	return updateRentAndHistoryByRentId(opId, baseTableName.toUpperCase(), datacenterRentId, rentId, updateColumnValueMap,
//		updateRentError, insertHistError);
//    }
//
//    /**
//     * 指定したRENT_ID行の状態テーブル(RENT)のカラムを更新して、履歴(HISTORY)にレコードを追加する.
//     * (このメソッドはFacadeから直接操作しないでください)
//     *
//     * 必ずRENTの1行が対象になる。もしRENT_IDに対応するものがない場合はFaultを発行する
//     *
//     * <dl>
//     * <dt>rentUpdateError</dt>
//     * <dd>貸出行更新エラー</dd>
//     * <dt>histInsertError</dt>
//     * <dd>履歴行作成エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            操作ID
//     * @param baseTable
//     *            テーブル基本名
//     * @param datacenterRentId
//     *            データセンターRENT_ID（0/負数の場合は条件としない）
//     * @param rentId
//     *            処理対象貸出ID
//     * @param updateColumnValueMap
//     *            更新内容
//     * @param rentUpdateError
//     *            貸出行更新失敗時FaultCode
//     * @param histInsertError
//     *            履歴行追加失敗時FaultCode
//     *
//     *
//     * @return 貸出行と履歴行を操作できたらtrue(操作失敗時は例外が発生)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected boolean updateRentAndHistoryByRentId(final long opId, final String baseTable, final int datacenterRentId,
//	    final int rentId, final MapSqlParameterSource updateColumnValueMap, final FaultCode rentUpdateError,
//	    final FaultCode histInsertError) throws WebApiCommandException {
//	final String rentTable = baseTable + TABLENAME_SUFFIX_RENT;
//	// 貸出行の内容を更新する
//	updateRentByRentId(opId, rentTable, datacenterRentId, rentId,
//		updateColumnValueMap.getValues(), rentUpdateError);
//	// 履歴に更新内容を反映
//	insertHistoryByRentId(rentTable, baseTable + TABLENAME_SUFFIX_HISTORY, rentId, histInsertError);
//	return true;
//    }
//
//    /**
//     * 指定したID行のテーブルのカラムを更新して、履歴(HISTORY)にレコードを追加する.
//     * (このメソッドはFacadeから直接操作しないでください)
//     *
//     * 必ず1行が対象になる。もしIDに対応するものがない場合はFaultを発行する
//     *
//     * <dl>
//     * <dt>tableUpdateError</dt>
//     * <dd>メインテーブル更新エラー</dd>
//     * <dt>histInsertError</dt>
//     * <dd>履歴行作成エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            操作ID
//     * @param baseTable
//     *            テーブル名
//     * @param historyTable
//     *            履歴テーブル名
//     * @param mainId
//     *            処理対象貸出ID
//     * @param updateColumnValueMap
//     *            更新内容
//     * @param tableUpdateError
//     *            貸出行更新失敗時FaultCode
//     * @param histInsertError
//     *            履歴行追加失敗時FaultCode
//     *
//     *
//     * @return 貸出行と履歴行を操作できたらtrue(操作失敗時は例外が発生)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected boolean updateTableAndHistoryByMainId(final long opId, final String baseTable, final String historyTable,
//	    final int mainId, final MapSqlParameterSource updateColumnValueMap, final FaultCode tableUpdateError,
//	    final FaultCode histInsertError) throws WebApiCommandException {
//
//	// 貸出行の内容を更新する
//	updateRentByRentId(opId, baseTable, 0, mainId,
//		updateColumnValueMap.getValues(), tableUpdateError);
//	// 履歴に更新内容を反映
//	insertHistoryByRentId(baseTable, historyTable, mainId, histInsertError);
//	return true;
//    }
//
//    /**
//     * 指定したRENT_ID行の状態テーブル(RENT)のカラムを更新する.
//     *
//     * このメソッドは履歴作成を行わないので直接このメソッドを操作しないでください。
//     * 必ず1行(か0の)のRENT行が対象になる。もしDATACENTER_RENT_ID+RENT_IDに対応するものがない場合はFaultを発行する
//     *
//     *
//     * <dl>
//     * <dt>updateError</dt>
//     * <dd>貸出行更新エラー(更新対象なし・更新対象複数)・JDBC処理例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(
//     * SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            操作ID
//     * @param rentTable
//     *            状態テーブル名
//     * @param datacenterRentId
//     *            データセンターRENT_ID（0/負数の場合は条件としない）
//     * @param rentId
//     *            処理対象貸出ID
//     * @param updateColumnValueMap
//     *            更新内容
//     * @param updateError
//     *            処理中にエラーが発生した場合に発行するFaultCode
//     *
//     *
//     * @return 更新成否(trueのみ/失敗時はFaultCodeが発行される)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected boolean updateRentByRentId(final long opId, final String rentTable, final int datacenterRentId,
//	    final int rentId, final Map<String, Object> updateColumnValueMap, final FaultCode updateError)
//	    throws WebApiCommandException {
//	final MapSqlParameterSource map = new MapSqlParameterSource();
//	final StringBuilder buf = new StringBuilder();
//	buf.append("UPDATE ").append(rentTable).append(" SET upd_dt=NOW(), upd_op=:opId");
//	for (final Map.Entry<String, Object> e : updateColumnValueMap.entrySet()) {
//	    buf.append(',').append(e.getKey()).append("=:s").append(e.getKey());
//	    map.addValue("s" + e.getKey(), e.getValue());
//	}
//	buf.append(" WHERE ").append(rentTable).append("_id=:wrentId");
//	if (datacenterRentId > 0) {
//	    buf.append(" AND datacenter_rent_id=:datacenterRentId");
//	    map.addValue("datacenterRentId", datacenterRentId);
//	}
//	map.addValue("wrentId", rentId);
//	map.addValue(TEMPLATENAME_OP_ID, opId);
//
//	// 履歴レコード更新処理
//	return updateOneRecord(buf.toString(), map, updateError);
//    }
//    /**
//     * 指定したMASTER_ID行の在庫テーブル(MASTER)のカラムを更新する.
//     *
//     * <dl>
//     * <dt>updateError</dt>
//     * <dd>在庫行更新エラー(更新対象なし・更新対象複数)・JDBC処理例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(
//     * SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            操作ID
//     * @param baseTableName
//     *            ベーステーブル名
//     * @param masterId
//     *            処理対象在庫ID
//     * @param updateColumnValueMap
//     *            更新内容
//     * @param updateError
//     *            処理中にエラーが発生した場合に発行するFaultCode
//     *
//     *
//     * @return 更新成否(trueのみ/失敗時はFaultCodeが発行される)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public boolean updateMaster(final long opId, final String baseTableName,
//	    final int masterId, final MapSqlParameterSource updateColumnValueMap, final FaultCode updateError)
//	    throws WebApiCommandException {
//    	final String masterTable = baseTableName.toUpperCase() + TABLENAME_SUFFIX_MASTER;
//    	final MapSqlParameterSource map = new MapSqlParameterSource();
//    	final StringBuilder buf = new StringBuilder();
//    	buf.append("UPDATE ").append(masterTable).append(" SET upd_dt=NOW(), upd_op=:opId");
//    	for (final Map.Entry<String, Object> e : updateColumnValueMap.getValues().entrySet()) {
//    	    buf.append(',').append(e.getKey()).append("=:s").append(e.getKey());
//    	    map.addValue("s" + e.getKey(), e.getValue());
//    	}
//    	buf.append(" WHERE ").append(masterTable).append("_id=:wmasterId");
//    	map.addValue("wmasterId", masterId);
//    	map.addValue(TEMPLATENAME_OP_ID, opId);
//
//    	// 在庫レコード更新処理
//    	return updateOneRecord(buf.toString(), map, updateError);
//    }
//
//    /**
//     * RENTテーブル内の同じMASTER_IDの要素を検索する.
//     *
//     * 検索対象は論理削除フラグを除外する
//     *
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_NO_MASTEID_BY_RENTID</dt>
//     * <dd>対象行なし(RENT_ID行なし・削除済み)</dd>
//     * <dt>FaultCode.GENERAL_MASTEID_BY_RENTID_SELECT_ERROR</dt>
//     * <dd>取得エラー(複数行取得)・JDBC処理例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(
//     * SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param baseTableName
//     *            テーブル基幹名
//     * @param rentId
//     *            検索対象の {baseTableName}_rent_idの値
//     * @param isForUpdate
//     *            検索結果のRENT行をFOR UPDATEでロックするか？
//     * @return MASTER_IDの内容
//     * @throws WebApiCommandException
//     *             処理結果エラー
//     */
//    protected int findMasterIdByRentId(final String baseTableName, final int rentId, final boolean isForUpdate)
//	    throws WebApiCommandException {
//	final StringBuilder buf = new StringBuilder().append("SELECT ").append(baseTableName)
//		.append("_master_id FROM ").append(baseTableName).append("_RENT WHERE delete_flag=FALSE AND ")
//		.append(baseTableName).append("_rent_id=:id").append(makeForUpdateQuery(isForUpdate));
//
//	return queryForInt(buf.toString(), new MapSqlParameterSource("id", rentId),
//		FaultCode.GENERAL_NO_MASTEID_BY_RENTID, FaultCode.GENERAL_MASTEID_BY_RENTID_SELECT_ERROR);
//    }
//
//    /**
//     * RENTテーブルでdatacenter_rent_idで検索する(論理削除済みは除外).
//     *
//     * <dl>
//     * <dt>errorCode</dt>
//     * <dd>JDBC処理例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param rentTableName
//     *            貸出テーブル
//     * @param datacenterRentId
//     *            datacenterRentId
//     * @param prevStatus
//     *            検索対象のステータス(null可能/NULLの場合は検索しない)
//     * @param errorCode
//     *            検索中エラー発生
//     * @return 対象のRENT_IDのリスト(値が存在しない場合はからのList)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public List<Integer> findRentIdList(final String rentTableName, final int datacenterRentId,
//	    final StatusTypeDef prevStatus, final FaultCode errorCode) throws WebApiCommandException {
//	final MapSqlParameterSource map = new MapSqlParameterSource();
//	map.addValue("datacenter_rent_id", datacenterRentId);
//	if (prevStatus != StatusTypeDef.ANY) {
//	    map.addValue("status", prevStatus.getCode());
//	}
//	return findRentIdList(rentTableName, map, errorCode);
//    }
//
//    /**
//     * 対象RENTテーブルをtagetMapの条件で検索する(複数件検索). 対象のRENTの行は論理削除されていないことを条件にする.
//     * 検索結果が0件や複数件であっても正常に値を返す。
//     *
//     * <dl>
//     * <dt>errorCode</dt>
//     * <dd>JDBC処理中例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param rentTableName
//     *            RENTのテーブル名
//     * @param targetMap
//     *            検索条件マップ(カラム名と検索値)
//     * @param errorCode
//     *            検索処理中に発生したエラー
//     * @return rent_idのIDのリスト(対象が0行の場合は空のリスト)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public List<Integer> findRentIdList(final String rentTableName, final MapSqlParameterSource targetMap,
//	    final FaultCode errorCode) throws WebApiCommandException {
//	final StringBuilder buf = new StringBuilder().append("SELECT ").append(rentTableName).append("_id FROM ")
//		.append(rentTableName).append(" WHERE delete_flag=FALSE");
//	for (Map.Entry<String, Object> me : targetMap.getValues().entrySet()) {
//	    buf.append(" AND ").append(me.getKey()).append("=:").append(me.getKey());
//	}
//	return queryForIntegerList(buf.toString(), targetMap, errorCode);
//    }
//
//    /**
//     * 指定した条件で1件のRENT_IDの値を取得する.
//     *
//     * @param rentTableName
//     *            検索対象テーブル
//     * @param targetMap
//     *            検索条件
//     * @param emptyCode
//     *            結果が存在しない場合のFaultCode
//     * @param errorCode
//     *            結果が複数件存在する/検索中エラーのFaultCode
//     * @return 正常に検索で1件のRENT_IDの値
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     *
//     */
//    public int findRentId(final String rentTableName, final MapSqlParameterSource targetMap, final FaultCode emptyCode,
//	    final FaultCode errorCode) throws WebApiCommandException {
//	final StringBuilder buf = new StringBuilder().append("SELECT ").append(rentTableName).append("_id FROM ")
//		.append(rentTableName).append(" WHERE delete_flag=FALSE");
//	for (Map.Entry<String, Object> me : targetMap.getValues().entrySet()) {
//	    buf.append(" AND ").append(me.getKey()).append("=:").append(me.getKey());
//	}
//	return queryForInt(buf.toString(), targetMap, emptyCode, errorCode);
//    }
//
//    /**
//     * タイプ名称（name）を指定して1件のTYPE_IDの値を取得する.
//     *
//     * @param typeTableName
//     *            検索対象のTYPEテーブル
//     * @param name
//     *            タイプ名称
//     * @param emptyCode
//     *            結果が存在しない場合のFaultCode
//     * @return 正常に検索で1件のTYPE_IDの値
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     *
//     */
//    protected int findTypeIdFromName(final String typeTableName, final String name,
//	    final FaultCode emptyCode) throws WebApiCommandException {
//	final StringBuilder buf = new StringBuilder().append("SELECT ").append(typeTableName).append("_id FROM ")
//		.append(typeTableName).append(" WHERE name = :name");
//	MapSqlParameterSource targetMap = new MapSqlParameterSource("name", name);
//	return queryForInt(buf.toString(), targetMap, emptyCode, FaultCode.GENERAL_FIND_TYPE_ID_SELECT_ERROR);
//    }
//
//    /**
//     * 指定したテーブルの要素からテーブル名+"_id"を除いたカラム名のクエリを取得する.
//     *
//     * このメソッドはキャッシュ処理を行う 履歴テーブルのAutoIncremtntの要素を除いたカラム文字列作成
//     *
//     *
//     * <dl>
//     * <dt>error</dt>
//     * <dd>履歴用のカラム名称取得に失敗</dd>
//     * </dl>
//     *
//     * @param tableName
//     *            テーブル名
//     * @param error
//     *            履歴表のカラム要素取得時にエラーが発生した場合のFaultCode
//     * @return 対象のテーブルの全要素のカラム名列記文字列
//     * @throws WebApiCommandException
//     *             DBアクセスでカラム情報取得した際のエラー
//     */
//    private String findHistoryColumnNames(final String tableName, final FaultCode error) throws WebApiCommandException {
//	final String cs;
//	synchronized (RENT_COLUMNNAME_MAP) {
//	    if (RENT_COLUMNNAME_MAP.containsKey(tableName)) {
//		cs = RENT_COLUMNNAME_MAP.get(tableName);
//	    } else {
//		cs = findHistoryColumnNamesBySql(tableName, error);
//		RENT_COLUMNNAME_MAP.put(tableName, cs);
//	    }
//	}
//	return cs;
//    }
//
//    /**
//     * 指定履歴テーブルのカラム名をSQLを使って取得する.
//     *
//     * RENTからHISTORYにカラムをコピーする際の検索条件用 ただしコピーできないPKになるテーブル名_idのカラムだけ追加しない
//     *
//     * <dl>
//     * <dt>error</dt>
//     * <dd>履歴用のカラム名称取得に失敗</dd>
//     * </dl>
//     *
//     * @param tableName
//     *            履歴テーブル名
//     * @param error
//     *            履歴テーブルのDB検索時エラーが起きた場合のFaultCode
//     * @return カラム列部分の文字列
//     * @throws WebApiCommandException
//     *             対象テーブルのカラム情報の取得に失敗した場合
//     */
//    protected String findHistoryColumnNamesBySql(final String tableName, final FaultCode error)
//	    throws WebApiCommandException {
//	final String sql = new StringBuilder().append("SELECT * FROM ").append(tableName).append(" LIMIT 0").toString();
//
//	String[] columns;
//	try {
//	    columns = findNamedParameterJdbcTemplate().queryForRowSet(sql, new MapSqlParameterSource()).getMetaData()
//		    .getColumnNames();
//	} catch (Throwable e) {
//	    throw new WebApiCommandException(error, "findHistoryColumnNamesBySql", e);
//	}
//
//	final String hid = tableName + "_id";
//	final StringBuilder buf = new StringBuilder();
//	for (final String c : columns) {
//	    if (!hid.equalsIgnoreCase(c)) {
//		if (buf.length() > 0) {
//		    buf.append(',');
//		}
//		buf.append(c);
//	    }
//	}
//	return buf.toString();
//    }
//
//    /**
//     * 直前にINSERTした要素のAUTO_INCREMENT内容の取得.
//     *
//     * <dl>
//     * <dt>error</dt>
//     * <dd>要素が存在しない場合＋それ以外のエラーコード(複数例外・JDBCエラーなど)</dd>
//     * </dl>
//     *
//     * @param error
//     *            エラー発生時に返すべきFaultCode
//     * @return 直前に追加したAUTO_INCREMENT内容を参照する.
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected long readLastInsertId(final FaultCode error) throws WebApiCommandException {
//	return queryForLong(findSql("S_LAST_INSERT_ID"), new MapSqlParameterSource(), error, error);
//    }
//
//    /**
//     * BASE_SET_IDから内部のDATACENTER_RENT_IDへの対応を確認する.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_NOT_FOUND</dt>
//     * <dd>ベースセット検索結果なし</dd>
//     * <dt>FaultCode.GENERAL_BASESET_SELECT_ERROR</dt>
//     * <dd>ベースセット検索結果が複数件/検索中エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param baseSetId
//     *            ベースセットID
//     * @return datacenterRentId
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public int findDatacenterRentId(final long opId, final String baseSetId) throws WebApiCommandException {
//	return findDatacenterRent(opId, baseSetId).getDatacenterRentId();
//    }
//
//    /**
//     * BASE_SET_IDから内部のDATACENTER_RENT情報を取得する.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_NOT_FOUND</dt>
//     * <dd>ベースセット検索結果なし</dd>
//     * <dt>FaultCode.GENERAL_BASESET_SELECT_ERROR</dt>
//     * <dd>ベースセット検索結果が複数件/検索中エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param baseSetId
//     *            ベースセットID
//     * @return データセンター情報Bean
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public DatacenterRent findDatacenterRent(final long opId, final String baseSetId) throws WebApiCommandException {
//	return queryForObject(findSql("S_DATACENTER_RENT"), new MapSqlParameterSource("baseSetId", baseSetId),
//		TypeHRowMappers.DATACENTER_RENT_ROW_MAPPER, FaultCode.GENERAL_BASESET_NOT_FOUND,
//		FaultCode.GENERAL_BASESET_SELECT_ERROR);
//    }
//
//    /**
//     * 貸出テーブル名と貸出IDからベースセットIDを取得する.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESETID_SEARCH_EMPTY_ERROR</dt>
//     * <dd>ベースセットID検索で、ベースセットIDを取得できませんでした.</dd>
//     * <dt>FaultCode.GENERAL_BASESETID_SEARCH_MULTI_ERROR</dt>
//     * <dd>ベースセットID検索中にエラーが発生しました.</dd>
//     * </dl>
//     *
//     * @param rentTableName
//     *            貸出テーブル名（Full Nameで）
//     * @param rentId
//     *            貸出ID
//     * @return ベースセットID
//     * @throws WebApiCommandException
//     *             メソッド実行時に発生する可能性のあるException
//     */
//    public String findBaseSetIdFromRentId(final String rentTableName, final int rentId)
//	    throws WebApiCommandException {
//	final StringBuilder buf = new StringBuilder().append(findSql("SELECT_BASE_SET_ID_PREFIX")).append(" ")
//		.append(rentTableName).append(" WHERE delete_flag=FALSE AND ")
//		.append(rentTableName).append("_id = ").append(rentId).append(")");
//	return queryForString(buf.toString(), null,
//		FaultCode.GENERAL_BASESETID_SEARCH_EMPTY_ERROR, FaultCode.GENERAL_BASESETID_SEARCH_MULTI_ERROR);
//    }
//
//    /**
//     * テーブルの主キーからそのテーブルに保持されているファシリティタイプIDを取得する。
//     * 
//     * @param opId 操作者ID
//     * @param tableName 完全なテーブル名
//     * @param keyId テーブルの主キーになるID
//     * @return ファシリティタイプID
//     * @throws WebApiCommandException メソッド実行時に発生する可能性のあるException
//     */
//    public int findFacilityTypeId(final long opId, final String tableName,
//	    final int keyId) throws WebApiCommandException {
//
//	final StringBuffer sql = new StringBuffer("SELECT facility_type_id ");
//	sql.append(" FROM ").append(tableName);
//	sql.append(" WHERE ").append(tableName).append("_id = ").append(keyId);
//
//	return queryForInt(sql.toString(), null, 
//		FaultCode.GENERAL_FACILITY_SEARCH_EMPTY_ERROR, FaultCode.GENERAL_FACILITY_SEARCH_MULTI_ERROR);
//    }
//
//    /**
//     * SYDIDからcontract_idの値を検索する.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_SYSID_NOT_FOUND</dt>
//     * <dd>SYSID検索結果なし</dd>
//     * <dt>FaultCode.GENERAL_SYSID_SELECT_ERROR</dt>
//     * <dd>SYSID検索結果が複数件/検索中エラー</dd>
//     * </dl>
//     *
//     * SYSIDの存在確認も副次的に行う.
//     *
//     * @param sysId
//     *            SYSIDの文字列
//     * @return contract_id
//     * @throws WebApiCommandException
//     *             DBエラーや存在しないSYSIDの場合
//     */
//    public int findContractId(final String sysId) throws WebApiCommandException {
//	return queryForInt(findSql("CHECK_SYSID"), new MapSqlParameterSource(TEMPLATENAME_SYS_ID, sysId),
//		FaultCode.GENERAL_SYSID_NOT_FOUND, FaultCode.GENERAL_SYSID_SELECT_ERROR);
//    }
//
//    /**
//     * ベースセット単位の振出の管理ネットワークVLAN+IPアドレス情報の取得.
//     *
//     * 非チェック時
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_QUERY_ERROR</dt>
//     * <dd>DB検索エラー</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_MGMT_VLAN_MULTI_FOUND</dt>
//     * <dd>複数の管理VLANが割り当てられている場合</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_NOT_TYPE</dt>
//     * <dd>1つも管理VLANが割り当てられていない場合</dd>
//     * </dl>
//     *
//     * チェック時
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_QUERY_ERROR</dt>
//     * <dd>DB検索エラー</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_MGMT_VLAN_MULTI_FOUND</dt>
//     * <dd>複数の管理VLANが割り当てられている場合</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_NOT_TYPE</dt>
//     * <dd>複数の管理VLANが割り当てられている場合</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_NOT_RESERVED</dt>
//     * <dd>ベースセットVLANの予約が済んでいない</dd>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_IPADDRESS_FORMAT_ERROR</dt>
//     * <dd>VLANのIPアドレスブロック指定の格納値が正しくない</dd>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_IPADDRESS_EMPTY</dt>
//     * <dd>VLANのIPアドレスブロック指定がされていない</dd>
//     * </dl>
//     *
//     * @param opId
//     *            OPID
//     * @param dr
//     *            DatacenterRent
//     * @param checked
//     *            VLAN中のネットワークブロック情報のチェックをするか
//     * @return ベースセットVLAN情報
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     *
//     */
//    public BaseSetVlanBean findMgmtVlan(final long opId, final DatacenterRent dr, final boolean checked)
//	    throws WebApiCommandException {
//	return findMgmtVlan(opId, dr.getDatacenterRentId(), checked);
//    }
//
//    /**
//     * ベースセット単位の振出の管理ネットワークVLAN+IPアドレス情報の取得.
//     *
//     * 非チェック時
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_QUERY_ERROR</dt>
//     * <dd>DB検索エラー</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_MGMT_VLAN_MULTI_FOUND</dt>
//     * <dd>複数の管理VLANが割り当てられている場合</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_NOT_TYPE</dt>
//     * <dd>管理VLANがこの設備に存在しない場合</dd>
//     * </dl>
//     *
//     * チェック時
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_QUERY_ERROR</dt>
//     * <dd>DB検索エラー</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_MGMT_VLAN_MULTI_FOUND</dt>
//     * <dd>複数の管理VLANが割り当てられている場合</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_NOT_TYPE</dt>
//     * <dd>管理VLANがこの設備に存在しない場合</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_NOT_RESERVED</dt>
//     * <dd>ベースセットVLANの予約が済んでいない</dd>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_IPADDRESS_FORMAT_ERROR</dt>
//     * <dd>VLANのIPアドレスブロック指定の格納値が正しくない</dd>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_IPADDRESS_EMPTY</dt>
//     * <dd>VLANのIPアドレスブロック指定がされていない</dd>
//     * </dl>
//     *
//     * @param opId
//     *            OPID
//     * @param datacenterRentId
//     *            DataCenterRentId
//     * @param checked
//     *            VLAN中のネットワークブロック情報のチェックをするか
//     * @return ベースセットVLAN情報
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public BaseSetVlanBean findMgmtVlan(final long opId, final int datacenterRentId, final boolean checked)
//	    throws WebApiCommandException {
//	BaseSetVlanBean bsvb = null;
//	final List<BaseSetVlanBean> bsvbl = findBaseSetVlanListFromDB(opId, datacenterRentId);
//	for (final BaseSetVlanBean cbsvb : bsvbl) {
//	    if (VlanNetworkType.MANAGEMENT_VLAN.getType().equals(cbsvb.getVlanName())) {
//		if (bsvb != null) {
//		    throw new WebApiCommandException(FaultCode.GENERAL_BASESET_MGMT_VLAN_MULTI_FOUND);
//		}
//		bsvb = cbsvb;
//	    }
//	}
//	if (bsvb == null) { // MANAGEMENT_VLANがこの設備に存在しない時に発生
//	    throw new WebApiCommandException(FaultCode.GENERAL_BASESET_VLAN_NOT_TYPE);
//	}
//	if (checked) {
//	    TypeHDbUtil.checkAndStoreIpAddressBlock(bsvb);
//	} else {
//	    TypeHDbUtil.updateIPAddressBlock(bsvb);
//	}
//	return bsvb;
//    }
//
//    /**
//     * ベースセットVLANが指定されたベースセットに割り当て済みの要素一覧を取得する. もしわりあてされたものが必要な場合は
//     * {@link #findBaseSetVlanList(long, int, boolean)}を利用すること
//     *
//     * チェックなし
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_QUERY_ERROR</dt>
//     * <dd>DB検索エラー</dd>
//     * </dl>
//     *
//     * チェック時
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_QUERY_ERROR</dt>
//     * <dd>DB検索エラー</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_NOT_RESERVED</dt>
//     * <dd>ベースセットVLANの予約が済んでいない</dd>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_IPADDRESS_FORMAT_ERROR</dt>
//     * <dd>VLANのIPアドレスブロック指定の格納値が正しくない</dd>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_IPADDRESS_EMPTY</dt>
//     * <dd>VLANのIPアドレスブロック指定がされていない</dd>
//     * </dl>
//     *
//     * @param opId
//     *            OPID
//     * @param datacenterRentId
//     *            ベースセットのRENT_ID
//     * @param checked
//     *            取得内容のチェックが必要な場合true
//     * @return 割り当て済みベースセットVLAN情報
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public List<BaseSetVlanBean> findVlanEsxi(final long opId, final int datacenterRentId, final boolean checked)
//	    throws WebApiCommandException {
//	final List<BaseSetVlanBean> bsvl = findBaseSetVlanListFromDB(opId, datacenterRentId);
//	final ArrayList<BaseSetVlanBean> al = new ArrayList<BaseSetVlanBean>(bsvl.size());
//	for (final BaseSetVlanBean bsv : bsvl) {
//	    if (bsv.getVlanId() >= 0) {
//		if (checked) {
//		    TypeHDbUtil.checkAndStoreIpAddressBlock(bsv);
//		} else {
//		    TypeHDbUtil.updateIPAddressBlock(bsv);
//		}
//		al.add(bsv);
//	    }
//	}
//	return al;
//    }
//
//    /**
//     * ベースセット単位の振出のVLAN+IPアドレス情報の取得.
//     *
//     * チェック時
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_QUERY_ERROR</dt>
//     * <dd>ベースセットVLAN DB検索エラー</dd>
//     *
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_NOT_RESERVED</dt>
//     * <dd>ベースセットVLANの予約が済んでいない</dd>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_IPADDRESS_FORMAT_ERROR</dt>
//     * <dd>VLANのIPアドレスブロック指定の格納値が正しくない</dd>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_IPADDRESS_EMPTY</dt>
//     * <dd>VLANのIPアドレスブロック指定がされていない</dd>
//     * </dl>
//     *
//     * 非チェック時
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_QUERY_ERROR</dt>
//     * <dd>ベースセットVLAN DB検索エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param datacenterRentId
//     *            データセンター貸出ID
//     * @param isCheck
//     *            チェック処理をするか
//     * @return 指定ベースセットのベースセットVLAN情報リスト
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public List<BaseSetVlanBean> findBaseSetVlanList(final long opId, final int datacenterRentId, final boolean isCheck)
//	    throws WebApiCommandException {
//	final List<BaseSetVlanBean> bsvl = findBaseSetVlanListFromDB(opId, datacenterRentId);
//	for (final BaseSetVlanBean bsvb : bsvl) {
//	    if (isCheck) {
//		TypeHDbUtil.checkAndStoreIpAddressBlock(bsvb);
//	    } else {
//		TypeHDbUtil.updateIPAddressBlock(bsvb);
//	    }
//	}
//	return bsvl;
//    }
//
//    /**
//     * VLANからネットワーク情報の基本情報の検索(DB検索のみ・値の再格納・チェックは行わない).
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_BASESET_VLAN_QUERY_ERROR</dt>
//     * <dd>DB検索エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param datacenterRentId
//     *            データセンター貸出ID
//     * @return VLAN情報のリスト
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected List<BaseSetVlanBean> findBaseSetVlanListFromDB(final long opId, final int datacenterRentId)
//	    throws WebApiCommandException {
//	return queryForList(findSql("S_BASE_SET_VLAN_INFO"), new MapSqlParameterSource("datacenterRentId",
//		datacenterRentId), TypeHRowMappers.BASE_SET_VLAN_BEAN_ROW_MAPPER,
//		FaultCode.GENERAL_BASESET_VLAN_QUERY_ERROR);
//    }
//
//    /**
//     * 設備別の汎用プロパティから値を取得する.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_FACILITY_PROPERTY_NOT_FOUND</dt>
//     * <dd>検索結果が0件</dd>
//     * <dt>FaultCode.GENERAL_FACILITY_PROPERTY_SELECT_ERROR</dt>
//     * <dd>検索結果が複数件・検索中エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            操作ID
//     * @param facilityTypeId
//     *            設備ID
//     * @param fpkey
//     *            プロパティキー名
//     * @return プロパティ値(存在しない場合や複数存在する場合は例外)
//     * @throws WebApiCommandException
//     *             SQLの処理エラー・要素がない場合や複数存在する場合
//     */
//    public String readFacilityProperty(final long opId, final int facilityTypeId, final String fpkey)
//	    throws WebApiCommandException {
//	return queryForString(findSql("S_FACILITY_PROPERTY"),
//		new MapSqlParameterSource("pkey", fpkey).addValue(TEMPLATE_FACILITY_TYPE_ID, facilityTypeId),
//		FaultCode.GENERAL_FACILITY_PROPERTY_NOT_FOUND, FaultCode.GENERAL_FACILITY_PROPERTY_SELECT_ERROR);
//    }
//
//    /**
//     * SYSID別の汎用プロパティから値を取得する.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_CONTRACT_PROPERTY_NOT_FOUND</dt>
//     * <dd>検索結果が0件</dd>
//     * <dt>FaultCode.GENERAL_CONTRACT_PROPERTY_SELECT_ERROR</dt>
//     * <dd>検索結果が複数件・検索中エラー</dd>
//     * </dl>
//     *
//     *
//     * @param opId
//     *            操作ID
//     * @param contractId
//     *            契約者ID(SYSIDと1対1対応)
//     * @param cpkey
//     *            プロパティキー名
//     * @return プロパティ値(要素が存在しない場合や複数存在する場合は例外)
//     * @throws WebApiCommandException
//     *             SQL操作時エラー、要素がないか、複数ある場合
//     */
//    public String readContractProperty(final long opId, final int contractId, final String cpkey)
//	    throws WebApiCommandException {
//	return queryForString(findSql("S_CONTRACT_PROPERTY"),
//		new MapSqlParameterSource("pkey", cpkey).addValue("contractId", contractId),
//		FaultCode.GENERAL_CONTRACT_PROPERTY_NOT_FOUND, FaultCode.GENERAL_CONTRACT_PROPERTY_SELECT_ERROR);
//    }
//
//    /**
//     * BASESETID別の汎用プロパティから値を取得する.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_DATACENTER_PROPERTY_NOT_FOUND</dt>
//     * <dd>検索結果が0件</dd>
//     * <dt>FaultCode.GENERAL_DATACENTER_PROPERTY_SELECT_ERROR</dt>
//     * <dd>検索結果が複数件・検索エラー</dd>
//     * </dl>
//     *
//     *
//     * @param opId
//     *            操作ID
//     * @param datacenterRentId
//     *            データセンターID(BASE_SET_IDと1対1対応)
//     * @param dpkey
//     *            プロパティキー名
//     * @return プロパティ値(要素が存在しない場合や複数存在する場合は例外)
//     * @throws WebApiCommandException
//     *             SQL操作時エラー、要素がないか、複数ある場合
//     */
//    public String readDatacenterProperty(final long opId, final int datacenterRentId, final String dpkey)
//	    throws WebApiCommandException {
//	return queryForString(findSql("S_DATACENTER_PROPERTY"),
//		new MapSqlParameterSource("pkey", dpkey).addValue("datacenterRentId", datacenterRentId),
//		FaultCode.GENERAL_DATACENTER_PROPERTY_NOT_FOUND, FaultCode.GENERAL_DATACENTER_PROPERTY_SELECT_ERROR);
//    }
//
//    /**
//     * 契約者別のプロパティ値を更新する.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_PROPERTY_DELETE_ERROR</dt>
//     * <dd>Property既存項目削除中にエラー発生</dd>
//     * <dt>FaultCode.GENERAL_PROPERTY_INSERT_ERROR</dt>
//     * <dd>Property項目登録中にエラー発生</dd>
//     * </dl>
//     *
//     * @param opId
//     *            操作ID
//     * @param contractId
//     *            契約者ID
//     * @param cpkey
//     *            プロパティキー
//     * @param value
//     *            設定する値
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public void updateContractProperty(final long opId, final int contractId, final String cpkey, final String value)
//	    throws WebApiCommandException {
//	updateProperty(opId, "CONTRACT_PROPERTY", "contract_id", contractId, cpkey, value);
//    }
//
//    /**
//     * データセンター別のプロパティ値を更新する.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_PROPERTY_DELETE_ERROR</dt>
//     * <dd>Property既存項目削除中にエラー発生</dd>
//     * <dt>FaultCode.GENERAL_PROPERTY_INSERT_ERROR</dt>
//     * <dd>Property項目登録中にエラー発生</dd>
//     * </dl>
//     *
//     * @param opId
//     *            操作ID
//     * @param datacenterRentId
//     *            datacenterRentId
//     * @param dpkey
//     *            プロパティキー
//     * @param value
//     *            設定する値
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public void updateDatacenterProperty(final long opId, final int datacenterRentId, final String dpkey,
//	    final String value) throws WebApiCommandException {
//	updateProperty(opId, "DATACENTER_PROPERTY", "datacenter_rent_id", datacenterRentId, dpkey, value);
//    }
//
//    /**
//     * 設備別のプロパティ値を更新する.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_PROPERTY_DELETE_ERROR</dt>
//     * <dd>Property既存項目削除中にエラー発生</dd>
//     * <dt>FaultCode.GENERAL_PROPERTY_INSERT_ERROR</dt>
//     * <dd>Property項目登録中にエラー発生</dd>
//     * </dl>
//     *
//     * @param opId
//     *            操作ID
//     * @param facilityTypeId
//     *            favcilityTypeId
//     * @param fpkey
//     *            プロパティキー
//     * @param value
//     *            設定する値
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public void updateFacilityProperty(final long opId, final int facilityTypeId, final String fpkey,
//	    final String value) throws WebApiCommandException {
//	updateProperty(opId, "FACILITY_TYPE_PROPERTY", "facility_type_id", facilityTypeId, fpkey, value);
//    }
//
//    /**
//     * 在庫情報をMASTER_IDから取得する.
//     *
//     * <dl>
//     * <dt>FaultCode.ADMIN_DB_RENT_MASTER_NOT_FOUND</dt>
//     * <dd>在庫レコードが見つからなかった</dd>
//     * <dt>FaultCode.ADMIN_DB_RENT_MASTER_SELECT_ERROR</dt>
//     * <dd>検索中にエラー発生</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param masterTableName
//     *            在庫テーブル名
//     * @param masterId
//     *            マスターID
//     * @return 在庫情報
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public Map<String, Object> readMasterHistory(final long opId, final String masterTableName, final int masterId)
//	    throws WebApiCommandException {
//	final MapSqlParameterSource map = new MapSqlParameterSource(masterTableName + "_ID", masterId);
//	return queryForObject(
//		new StringBuilder().append("SELECT * FROM ").append(masterTableName).append(" ")
//			.append(TypeHDbUtil.formatAppendWhereQuery(map)).toString(), map,
//		new TypeHRowMappers.MapRowMapper(), FaultCode.ADMIN_DB_RENT_MASTER_NOT_FOUND,
//		FaultCode.ADMIN_DB_RENT_MASTER_NOT_FOUND);
//    }
//
//    /**
//     * 履歴情報をRENT_IDから取得する.
//     *
//     * @param opId
//     *            処理ID
//     * @param baseTableName
//     *            履歴テーブル名
//     * @param rentId
//     *            貸出ID
//     * @return 履歴情報
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    public List<Map<String, Object>> readRentHistoryList(final long opId, final String baseTableName, final int rentId)
//	    throws WebApiCommandException {
//	final MapSqlParameterSource map = new MapSqlParameterSource(baseTableName + "_RENT_ID", rentId);
//	return readRentHistoryList(opId, baseTableName + TABLENAME_SUFFIX_HISTORY,
//		TypeHDbUtil.formatAppendWhereQuery(map), map);
//    }
//
//    /**
//     * 履歴の要素を取得する.
//     *
//     * もしRENT_IDが存在せずに結果行が0の場合でも空の結果を返す
//     * <dl>
//     * <dt>FaultCode.ADMIN_DB_RENT_HISTORY_QUERY_ERROR</dt>
//     * <dd>検索中にエラー発生</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param historyTableName
//     *            履歴テーブル名
//     * @param appendSql
//     *            付加するSQL
//     * @param map
//     *            条件
//     * @return 履歴の要素
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected List<Map<String, Object>> readRentHistoryList(final long opId, final String historyTableName,
//	    final String appendSql, final MapSqlParameterSource map) throws WebApiCommandException {
//	final StringBuilder sql = new StringBuilder().append("SELECT * FROM ").append(historyTableName).append(" ")
//		.append(appendSql).append(" ORDER BY ").append(historyTableName).append("_ID");
//	return queryForList(sql.toString(), map, new TypeHRowMappers.MapRowMapper(),
//		FaultCode.ADMIN_DB_RENT_HISTORY_QUERY_ERROR);
//    }
//
//    /**
//     * 汎用プロパティの更新処理.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_PROPERTY_DELETE_ERROR</dt>
//     * <dd>Property既存項目削除中にエラー発生</dd>
//     * <dt>FaultCode.GENERAL_PROPERTY_INSERT_ERROR</dt>
//     * <dd>Property項目登録中にエラー発生</dd>
//     * </dl>
//     *
//     * @param opId
//     *            操作ID
//     * @param tableName
//     *            プロパティテーブル名
//     * @param idColumnName
//     *            プロパティの区分IDカラム名
//     * @param idValue
//     *            プロパティの区分IDの値
//     * @param key
//     *            設定キー名
//     * @param value
//     *            設定値
//     * @throws WebApiCommandException
//     *             更新処理に失敗
//     */
//    protected void updateProperty(final long opId, final String tableName, final String idColumnName,
//	    final int idValue, final String key, final String value) throws WebApiCommandException {
//	deleteProperty(opId, tableName, idColumnName, idValue, key); // 既存：論理削除
//
//	// 設定値が空以外の場合作成
//	if (StringUtil.notEmpty(value)) { // 新規作成(1要素)
//	    final MapSqlParameterSource cmap = new MapSqlParameterSource().addValue("pkey", key)
//		    .addValue("pvalue", value).addValue(TEMPLATENAME_OP_ID, opId).addValue("id", idValue);
//	    insertOneRecord(
//		    new StringBuilder("INSERT INTO ").append(tableName).append("(").append(idColumnName)
//			    .append(", pkey, pvalue, delete_flag, reg_op, reg_dt, upd_op, upd_dt) ")
//			    .append("VALUES(:id, :pkey, :pvalue, FALSE, :opId, NOW(), :opId, NOW())").toString(), cmap,
//		    FaultCode.GENERAL_PROPERTY_INSERT_ERROR);
//	}
//    }
//
//    /**
//     * プロパティの汎用削除処理.
//     *
//     * <dl>
//     * <dt>FaultCode.GENERAL_PROPERTY_DELETE_ERROR</dt>
//     * <dd>Property既存項目削除中にエラー発生</dd>
//     * </dl>
//     *
//     *
//     * プロパティの要素は何件でもエラーにならない(0,1,複数)、処理中に例外が出た場合だけFaultCode.
//     * GENERAL_PROPERTY_DELETE_ERRORが発生
//     *
//     * @param opId
//     *            操作ID
//     * @param tableName
//     *            プロパティテーブル名
//     * @param idColumnName
//     *            プロパティテーブルの範囲指定用カラム名
//     * @param idValue
//     *            プロパティテーブルの範囲指定カラム値
//     * @param key
//     *            削除対象キー(キーを限定しない場合はnullを指定すること)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected void deleteProperty(final long opId, final String tableName, final String idColumnName,
//	    final int idValue, final String key) throws WebApiCommandException {
//	final StringBuilder sql = new StringBuilder("UPDATE ").append(tableName)
//		.append(" SET delete_flag=TRUE,upd_op=:opId,upd_dt=NOW() WHERE delete_flag=FALSE AND ")
//		.append(idColumnName).append("=:id");
//	final MapSqlParameterSource cmap = new MapSqlParameterSource().addValue(TEMPLATENAME_OP_ID, opId).addValue(
//		"id", idValue);
//	if (StringUtil.notEquals(key, DELETE_ALL_PROPERTIES)) {
//	    sql.append(" AND pkey=:pkey");
//	    cmap.addValue("pkey", key);
//	}
//	// 既存：論理削除「指定内容により複数削除の可能性あり」
//	updateDB(sql.toString(), cmap, FaultCode.GENERAL_PROPERTY_DELETE_ERROR);
//    }
//
//    /**
//     * ファシリティタイプ情報のリストを取得する.
//     *
//     * @param opId 操作者ID
//     * @return ファシリティタイプ情報のリスト
//     * @throws WebApiCommandException 検索時に発生する例外
//     */
//    public List<FacilityTypeBean> findFacilityType(final long opId) throws WebApiCommandException {
//	return queryForList(findSql("SELECT_FACILITY_TYPE"), null,
//		TypeHRowMappers.FACILITY_TYPE_DATA_ROW_MAPPER,
//		FaultCode.GENERAL_FACILITY_TYPE_ERROR);
//    }
//
//    /**
//     * 指定された条件でSSNW NAT IPアドレスを予約する.
//     *
//     * @param opId
//     *            操作ID
//     * @param dr
//     *            データセンタの貸出情報
//     * @param ssnwNatIpType
//     *            SSNW側NAT IPのタイプ
//     * @param unit
//     *            台数
//     * @return 予約した情報一覧
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>FaultCode.SSNW_LACK_REMAIN_IP_ERROR</dt>
//     *             <dd>SSNW 予約 指定された数の予約用のSSNWの空きがありません。</dd>
//     *             <dt>FaultCode.SSNW_RESERVE_UPDATE_RENT_ERROR</dt>
//     *             <dd>SSNW 予約 貸出行変更の処理に失敗しました</dd>
//     *             <dt>FaultCode.SSNW_RESERVE_INSERT_HIST_ERROR</dt>
//     *             <dd>SSNW 予約 履歴行追加の処理に失敗しました</dd>
//     *             </dl>
//     */
//    public List<SsnwNatDataBean> changeReserveSsnwNatIp(final long opId, final DatacenterRent dr,
//	    final SsnwNatIpType ssnwNatIpType, final int unit) throws WebApiCommandException {
//
//	// 指定された数が1より小さい場合は何もしない。
//	if (unit < 1) {
//	    return new ArrayList<SsnwNatDataBean>();
//	}
//
//	// 在庫のものを取得するため、データセンター貸出IDはセットしない
//	List<SsnwNatDataBean> list = findSsnwNatDataList(opId, -1, -1, dr.getFacilityTypeId(), StatusTypeDef.STOCK,
//		unit, true);
//	// 在庫が足りなければエラー
//	if (list.size() != unit) {
//	    throw new WebApiCommandException(FaultCode.GENERAL_SSNW_LACK_REMAIN_IP_ERROR);
//	}
//
//	MapSqlParameterSource map = new MapSqlParameterSource();
//	map.addValue("datacenter_rent_id", dr.getDatacenterRentId());
//	map.addValue("ssnw_nat_ip_type_id", ssnwNatIpType.getCode());
//	map.addValue("status", StatusTypeDef.RESERVE.getCode());
//	for (SsnwNatDataBean bean : list) {
//	    updateRentStatusAndHistory(opId, "SSNW_NAT_IP", UNDEFINE_DATACENTER_RENT_ID, bean.getSsnwNatIpRentId(),
//		    map, FaultCode.GENERAL_SSNW_RESERVE_UPDATE_RENT_ERROR,
//		    FaultCode.GENERAL_SSNW_RESERVE_INSERT_HIST_ERROR);
//	}
//
//	// 予約した貸出IDを保持しておく
//	int[] reservedRents = new int[list.size()];
//	for (int i = 0; i < list.size(); i++) {
//	    reservedRents[i] = list.get(i).getSsnwNatIpRentId();
//	}
//	Arrays.sort(reservedRents); // このあと、binarySearchを使うのでソートしておく
//
//	// 登録した情報を取得する。今回予約したもの以外は除去する
//	List<SsnwNatDataBean> returnList = new ArrayList<SsnwNatDataBean>(); // 返却用のリスト
//	list = findSsnwNatDataList(opId, dr.getDatacenterRentId(), -1, dr.getFacilityTypeId(), StatusTypeDef.RESERVE,
//		-1, false);
//	for (SsnwNatDataBean bean : list) {
//	    if (Arrays.binarySearch(reservedRents, bean.getSsnwNatIpRentId()) >= 0) {
//		returnList.add(bean);
//	    }
//	}
//	return returnList;
//    }
//
//    /**
//     * 指定された条件で、ステータスを進める.
//     *
//     * @param opId
//     *            操作ID
//     * @param dr
//     *            データセンタの貸出情報
//     * @param ssnwNatIpRentId
//     *            SSNW側のNAT IPのID
//     * @param prevStatus
//     *            変更前ステータス（指定しない場合はnullを指定）
//     * @param status
//     *            変更後ステータス（指定しない場合はnullを指定）
//     * @return 変更の成否(0件件以上の変更した場合true)
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>FaultCode.SSNW_CHANGE_STATUS_NO_DATA_ERROR</dt>
//     *             <dd>SSNW ステータス変更 変更対象のデータが存在しません</dd>
//     *             <dt>FaultCode.SSNW_CAHNGE_STATUS_UPDATE_RENT_ERROR</dt>
//     *             <dd>SSNW ステータス変更 貸出行更新の処理に失敗しました</dd>
//     *             <dt>FaultCode.SSNW_CHANGE_STATUS_INSERT_HIST_ERROR</dt>
//     *             <dd>SSNW ステータス変更 履歴行追加の処理に失敗しました</dd>
//     *             </dl>
//     */
//    public boolean changeSsnwNatIpStatus(final long opId, final DatacenterRent dr, final int ssnwNatIpRentId,
//	    final StatusTypeDef prevStatus, final StatusTypeDef status) throws WebApiCommandException {
//
//	final int intPrevStatus = StatusUtil.getValidStatusCode(prevStatus);
//	return changeSsnwNatIpStatus(opId, dr, ssnwNatIpRentId, intPrevStatus, status.getCode());
//    }
//
//    /**
//     * 指定された条件で、ステータスを進める.
//     *
//     * @param opId
//     *            操作ID
//     * @param dr
//     *            データセンタの貸出情報
//     * @param ssnwNatIpRentId
//     *            SSNW側のNAT IPのID
//     * @param prevStatus
//     *            変更前ステータス（指定しない場合はnullを指定）
//     * @param status
//     *            変更後ステータス（指定しない場合はnullを指定）
//     * @return 変更の成否(0件件以上の変更した場合true)
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>FaultCode.SSNW_CHANGE_STATUS_NO_DATA_ERROR</dt>
//     *             <dd>SSNW ステータス変更 変更対象のデータが存在しません</dd>
//     *             <dt>FaultCode.SSNW_CAHNGE_STATUS_UPDATE_RENT_ERROR</dt>
//     *             <dd>SSNW ステータス変更 貸出行更新の処理に失敗しました</dd>
//     *             <dt>FaultCode.SSNW_CHANGE_STATUS_INSERT_HIST_ERROR</dt>
//     *             <dd>SSNW ステータス変更 履歴行追加の処理に失敗しました</dd>
//     *             </dl>
//     */
//    public boolean changeSsnwNatIpStatus(final long opId, final DatacenterRent dr, final int ssnwNatIpRentId,
//	    final int prevStatus, final int status) throws WebApiCommandException {
//
//	// 指定された条件で検索する
//	List<SsnwNatDataBean> list = findSsnwNatDataList(opId, dr.getDatacenterRentId(), ssnwNatIpRentId, -1,
//		prevStatus, -1, true);
//	if (list.size() == 0) {
//	    throw new WebApiCommandException(FaultCode.GENERAL_SSNW_CHANGE_STATUS_NO_DATA_ERROR);
//	}
//
//	for (SsnwNatDataBean bean : list) {
//	    updateRentStatusAndHistory(opId, "SSNW_NAT_IP", dr.getDatacenterRentId(), bean.getSsnwNatIpRentId(),
//		    new MapSqlParameterSource("status", status),
//		    FaultCode.GENERAL_SSNW_CAHNGE_STATUS_UPDATE_RENT_ERROR,
//		    FaultCode.GENERAL_SSNW_CHANGE_STATUS_INSERT_HIST_ERROR);
//	}
//	// ここまで来ればOK
//	return true;
//    }
//
//    /**
//     * SSNW_NAT_IPのリサイクルを行う。 指定された貸出情報を論理削除し、新しい貸出情報を作成する.
//     *
//     * @param opId
//     *            操作ID
//     * @param dr
//     *            データセンタの貸出情報
//     * @param ssnwNatIpRentId
//     *            SSNW側のNAT IPのID
//     * @param prevStatus
//     *            変更前ステータス
//     * @param status
//     *            変更後ステータス
//     * @return 変更の成否(0件件以上の変更した場合true)
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>FaultCode.SSNW_RENEWAL_NO_DATA_ERROR</dt>
//     *             <dd>SSNW 再利用 対象のデータが存在しません</dd>
//     *             <dt>FaultCode.SSNW_RENEWAL_RENT_DELETE_ERROR</dt>
//     *             <dd>SSNW 再利用 貸出行論理削除でエラーが発生しました</dd>
//     *             <dt>FaultCode.SSNW_RENEWAL_HIST_DELETE_ERROR</dt>
//     *             <dd>SSNW 再利用 履歴行作成(貸出論理削除時)でエラーが発生しました</dd>
//     *             <dt>FaultCode.SSNW_RENEWAL_RENT_CREATE_ERROR</dt>
//     *             <dd>SSNW 再利用 貸出行作成でエラーが発生しました</dd>
//     *             <dt>FaultCode.SSNW_RENEWAL_HIST_CREATE_ERROR</dt>
//     *             <dd>SSNW 再利用 履歴行作成(貸出作成時)でエラーが発生しました</dd>
//     *             </dl>
//     */
//    public boolean changeRefreshSsnwNatIpStatus(final long opId, final DatacenterRent dr, final int ssnwNatIpRentId,
//	    final StatusTypeDef prevStatus, final StatusTypeDef status) throws WebApiCommandException {
//
//	// 対応するSSNW_NAT_IPは在庫に存在するかを確認(FOR UPDATEでロック)
//	List<SsnwNatDataBean> ssnwList = findSsnwNatDataList(opId, dr.getDatacenterRentId(), ssnwNatIpRentId,
//		dr.getFacilityTypeId(), prevStatus, -1, true);
//	if (ssnwList.size() == 0) {
//	    throw new WebApiCommandException(FaultCode.GENERAL_SSNW_RENEWAL_NO_DATA_ERROR);
//	}
//
//	for (SsnwNatDataBean ssnw : ssnwList) {
//	    final int masterId = ssnw.getSsnwNatIpMasterId();
//	    final int rentId = ssnw.getSsnwNatIpRentId();
//
//	    // 対応するRENTの情報を論理削除する
//	    deleteRentAndHistory(opId, "SSNW_NAT_IP", rentId, FaultCode.GENERAL_SSNW_RENEWAL_RENT_DELETE_ERROR,
//		    FaultCode.GENERAL_SSNW_RENEWAL_HIST_DELETE_ERROR);
//
//	    // 対応するRENTの情報を作成する
//	    final MapSqlParameterSource newer = new MapSqlParameterSource();
//	    newer.addValue("ssnw_nat_ip_master_id", masterId);
//	    newer.addValue("status", status.getCode());
//	    insertRentAndHistory(opId, "SSNW_NAT_IP", newer, //
//		    FaultCode.GENERAL_SSNW_RENEWAL_RENT_CREATE_ERROR, FaultCode.GENERAL_SSNW_RENEWAL_HIST_CREATE_ERROR);
//	}
//	return true;
//    }
//
//    /**
//     * 条件に合わせて、SSNWデータを取得する.
//     *
//     * @param opId
//     *            貸出ID
//     * @param datacenterRentId
//     *            データセンター貸出ID（指定しない場合は-1を指定）
//     * @param ssnwNatIpRentId
//     *            SSNW NAT IPアドレス貸出ID（指定しない場合は-1を指定）
//     * @param facilityType
//     *            ファシリティタイプ（指定しない場合は-1を指定）
//     * @param prevStatus
//     *            ステータス（指定しない場合はnullを指定）
//     * @param unit
//     *            取得する数（指定しない場合は-1を指定）
//     * @param isForUpdate
//     *            ロックをかけるかどうか
//     * @return SSNWデータの一覧
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>FaultCode.SSNW_QUERY_RENT_ERROR</dt>
//     *             <dd>SSNW 汎用 SSNW_NAT_IPデータ検索中にえらーが発生しました</dd>
//     *             <dt>FaultCode.SSNW_NO_RENT_DATA_ERROR</dt>
//     *             <dd>SSNW 汎用 指定された貸出IDに一致するSSNW NAT IPのデータがありません。</dd>
//     *             </dl>
//     */
//    protected List<SsnwNatDataBean> findSsnwNatDataList(final long opId, final int datacenterRentId,
//	    final int ssnwNatIpRentId, final int facilityType, final StatusTypeDef prevStatus, final int unit,
//	    final boolean isForUpdate) throws WebApiCommandException {
//
//	final int intPrevStatus = StatusUtil.getValidStatusCode(prevStatus);
//	return findSsnwNatDataList(opId, datacenterRentId, ssnwNatIpRentId, facilityType, intPrevStatus, unit,
//		isForUpdate);
//    }
//
//    /**
//     * 条件に合わせて、SSNWデータを取得する.
//     *
//     * @param opId
//     *            貸出ID
//     * @param datacenterRentId
//     *            データセンター貸出ID（指定しない場合は-1を指定）
//     * @param ssnwNatIpRentId
//     *            SSNW NAT IPアドレス貸出ID（指定しない場合は-1を指定）
//     * @param facilityType
//     *            ファシリティタイプ（指定しない場合は-1を指定）
//     * @param prevStatus
//     *            ステータス（指定しない場合はnullを指定）
//     * @param unit
//     *            取得する数（指定しない場合は-1を指定）
//     * @param isForUpdate
//     *            ロックをかけるかどうか
//     * @return SSNWデータの一覧
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>FaultCode.SSNW_QUERY_RENT_ERROR</dt>
//     *             <dd>SSNW 汎用 SSNW_NAT_IPデータ検索中にえらーが発生しました</dd>
//     *             <dt>FaultCode.SSNW_NO_RENT_DATA_ERROR</dt>
//     *             <dd>SSNW 汎用 指定された貸出IDに一致するSSNW NAT IPのデータがありません。</dd>
//     *             </dl>
//     */
//    protected List<SsnwNatDataBean> findSsnwNatDataList(final long opId, final int datacenterRentId,
//	    final int ssnwNatIpRentId, final int facilityType, final int prevStatus, final int unit,
//	    final boolean isForUpdate) throws WebApiCommandException {
//
//	MapSqlParameterSource map = new MapSqlParameterSource();
//
//	final StringBuilder buf = new StringBuilder(findSql("FIND_SSNW_NAT_IP"));
//	if (datacenterRentId > 0) {
//	    // AND =:
//	    buf.append(" AND snir.datacenter_rent_id=:datacenterRentId");
//	    map.addValue("datacenterRentId", datacenterRentId);
//	}
//	if (StatusUtil.isValidStatusCode(prevStatus)) {
//	    buf.append(" AND snir.status=:prevStatus");
//	    map.addValue("prevStatus", prevStatus);
//	}
//	if (ssnwNatIpRentId > 0) {
//	    buf.append(" AND snir.ssnw_nat_ip_rent_id=:ssnwNatIpRentId");
//	    map.addValue("ssnwNatIpRentId", ssnwNatIpRentId);
//	}
//	if (facilityType > 0) {
//	    buf.append(" AND snim.facility_type_id=:facility_type_id");
//	    map.addValue("facility_type_id", facilityType);
//	}
//	// ORDER句を付ける
//	buf.append(" " + findSql("SSNW_OREDER_BY"));
//	if (unit > 0) { // 個数を指定
//	    buf.append(" LIMIT " + unit + " ");
//	}
//	buf.append(makeForUpdateQuery(isForUpdate));
//
//	return queryForList(buf.toString(), map, TypeHRowMappers.SSNW_NAT_DATA_ROW_MAPPER,
//		FaultCode.GENERAL_SSNW_QUERY_RENT_ERROR);
//    }
//
//    /**
//     * 一意な貸出IDからデータセンタの貸出IDを取得する.
//     *
//     * @param opId
//     *            処理ID
//     * @param baseTableName
//     *            対照テーブル名
//     * @param rentId
//     *            検索対象RENT_ID
//     * @param baseSetId
//     *            ベースセットID
//     * @return 貸出データの一覧
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>GENERAL_RENT_ID_SELECT_ERROR</dt>
//     *             <dd>貸出情報の取得に失敗した場合のエラー</dd>
//     *             </dl>
//     */
//    public DatacenterRent findDatacenterRentByRentId(final long opId, final String baseSetId,
//	    final int rentId, final String baseTableName) throws WebApiCommandException {
//	StringBuffer sql = new StringBuffer();
//	MapSqlParameterSource map = new MapSqlParameterSource();
//	sql.append("SELECT bt.datacenter_rent_id, co.facility_type_id, co.sysid, dr.contract_id,"
//	+ " dr.base_set_id, dr.status, dr.delete_flag, co.delete_flag as co_delete_flag ");
//	sql.append("FROM " + baseTableName + " bt ");
//	sql.append("LEFT OUTER JOIN DATACENTER_RENT dr ");
//	sql.append("ON( dr.datacenter_rent_id = bt.datacenter_rent_id ) ");
//	sql.append("LEFT OUTER JOIN CONTRACT co ");
//	sql.append("ON( co.contract_id = dr.contract_id ) ");
//	sql.append("WHERE bt." + baseTableName + "_id =:rentId ");
//	sql.append("  AND bt.delete_flag =false ");
//
//	map.addValue("rentId", rentId);
//
//	DatacenterRent dr = queryForObject(
//		sql.toString(),
//		map,
//		TypeHRowMappers.DATACENTER_RENT_BY_BASE_RENT_ID_ROW_MAPPER,
//		FaultCode.GENERAL_RENT_ID_NOT_FOUND,
//		FaultCode.GENERAL_RENT_ID_SELECT_ERROR);
//
//	//引数と検索されたベースセットIDが空文字かNULLである確認（どちらかNULLの場合にTRUE）
//	//Emptyである場合値を返却
//	if (StringUtil.isEmpty(dr.getBaseSetId()) || StringUtil.isEmpty(baseSetId)) {
//	    return dr;
//	}
//
//	//引数と検索されたベースセットIDを比較（一致しない場合のみTRUE）
//	//不一致の場合エラーとする
//	if (!(baseSetId.equals(dr.getBaseSetId()))) {
//	    throw new WebApiCommandException(FaultCode.GENERAL_BASE_SET_ERROR);
//	}
//	return dr;
//    }
//
//    /**
//     * ステータスコードから貸出データを取得する.
//     *
//     * @param opId
//     *            処理ID
//     * @param baseTableName
//     *            対照テーブル名
//     * @param status
//     *            ステータスコード
//     * @return 貸出データの一覧
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>GENERAL_QUERY_STATUS_RENT_ERROR</dt>
//     *             <dd>貸出情報の取得に失敗した場合のエラー</dd>
//     *             </dl>
//     */
//    public List<BaseQueryStatusBean> findQueryStatusList(final long opId, final String baseTableName,
//	    final int status) throws WebApiCommandException {
//	StringBuffer sql = new StringBuffer();
//	MapSqlParameterSource map = new MapSqlParameterSource();
//
//	sql.append("SELECT co.facility_type_id, co.sysid, dr.base_set_id, bt." + baseTableName + "_id as rent_id, bt.status ");
//	sql.append("FROM " + baseTableName + " bt ");
//	sql.append("LEFT OUTER JOIN DATACENTER_RENT dr ");
//	sql.append("ON( dr.datacenter_rent_id = bt.datacenter_rent_id AND dr.delete_flag = FALSE ) ");
//	sql.append("LEFT OUTER JOIN CONTRACT co ");
//	sql.append("ON( co.contract_id = dr.contract_id AND co.delete_flag = FALSE ) ");
//	sql.append("WHERE bt.status =:status ");
//	sql.append("  AND bt.delete_flag =false ");
//	sql.append("ORDER BY co.facility_type_id IS NULL ASC, co.sysid IS NULL ASC, dr.base_set_id IS NULL ASC, rent_id, status ");
//
//	map.addValue("status", status);
//
//	return queryForList(
//		sql.toString(),
//		map,
//		TypeHRowMappers.BASE_QUERY_STATUS_MAPPER,
//		FaultCode.GENERAL_QUERY_STATUS_RENT_ERROR);
//    }
//
//    /**
//     * ステータスコードから貸出データを取得する.
//     *
//     * @param opId
//     *            処理ID
//     * @param baseTableName
//     *            対照テーブル名
//     * @param uniqueIdName
//     *            RENT_ID以外の一意なカラム
//     * @param status
//     *            ステータスコード
//     * @return 貸出データの一覧
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>GENERAL_QUERY_STATUS_RENT_ERROR</dt>
//     *             <dd>貸出情報の取得に失敗した場合のエラー</dd>
//     *             </dl>
//     */
//    public List<BaseQueryStatusUniqueIdBean> findQueryStatusList(final long opId, final String baseTableName,
//	    final String uniqueIdName, final int status) throws WebApiCommandException {
//	StringBuffer sql = new StringBuffer();
//	MapSqlParameterSource map = new MapSqlParameterSource();
//
//	sql.append("SELECT co.facility_type_id, co.sysid, dr.base_set_id, bt." + uniqueIdName + " as unique_id, bt.status ");
//	sql.append("FROM " + baseTableName + " bt ");
//	sql.append("LEFT OUTER JOIN DATACENTER_RENT dr ");
//	sql.append("ON( dr.datacenter_rent_id = bt.datacenter_rent_id AND dr.delete_flag = FALSE ) ");
//	sql.append("LEFT OUTER JOIN CONTRACT co ");
//	sql.append("ON( co.contract_id = dr.contract_id AND co.delete_flag = FALSE ) ");
//	sql.append("WHERE bt.status =:status ");
//	sql.append("  AND bt.delete_flag =false ");
//	sql.append("ORDER BY co.facility_type_id IS NULL ASC, co.sysid IS NULL ASC, dr.base_set_id IS NULL ASC, unique_id, status ");
//
//	map.addValue("status", status);
//
//	return queryForList(
//		sql.toString(),
//		map,
//		TypeHRowMappers.BASE_QUERY_STATUS_MAPPER_FOR_UNIQUE_ID,
//		FaultCode.GENERAL_QUERY_STATUS_RENT_ERROR);
//    }
//
//    /**
//     * チェック処理:: 指定したfacilityTypeIdが存在するか.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_FACILITY_NOT_FOUND</dt>
//     * <dd>Faility項目行が存在しない</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_FACILITY_SELECT_ERROR</dt>
//     * <dd>検索中にエラー発生(複数結果取得時含む)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param facilityTypeId
//     *            ファシリティタイプID
//     * @throws WebApiCommandException
//     *             存在しないか、エラー(複数存在など)場合例外
//     */
//    protected void checkFacilityTypeId(final long opId, final int facilityTypeId) throws WebApiCommandException {
//	queryForInt(findSql("CHECK_FACILITY_TYPE_ROWS"), new MapSqlParameterSource(TEMPLATE_FACILITY_TYPE_ID,
//		facilityTypeId), FaultCode.DB_COUNT_CHECK_FACILITY_NOT_FOUND,
//		FaultCode.DB_COUNT_CHECK_FACILITY_SELECT_ERROR);
//    }
//
//    /**
//     * チェック処理:: 指定したfacilityTypeId+vlanNetworkTypeidが存在するか.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_CHECK_FACILITY_ON_VLT_NOT_FOUND</dt>
//     * <dd>facilityType項目行が存在しない</dd>
//     * <dt>FaultCode.DB_CHECK_FACILITY_VLAN_TYPE_NOT_FOUND</dt>
//     * <dd>この設備にvlanNetworkTypeid項目行が存在しない</dd>
//     * <dt>FaultCode.DB_CHECK_FACILITY_VLAN_TYPE_SELECT_ERROR</dt>
//     * <dd>検索中にエラー発生(複数結果取得時含む)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param facilityTypeId
//     *            設備ID
//     * @param vlanNetworkTypeId
//     *            VLANネットワークタイプID
//     * @throws WebApiCommandException
//     *             存在しないか、エラー(複数存在など)場合例外
//     */
//    protected void checkFacilityVlanNetworkTypeId(final long opId, final int facilityTypeId,
//	    final int vlanNetworkTypeId) throws WebApiCommandException {
//	// facility_type_id, VLAN_NETWORK_TYPE_ID
//	final Tuple<Integer, Integer> t = queryForObject(findSql("CHECK_FACILITY_VLAN_NETWORK_TYPE_ROWS"),
//		new MapSqlParameterSource(TEMPLATE_VLAN_NETWORK_TYPE_ID, vlanNetworkTypeId).addValue(
//			TEMPLATE_FACILITY_TYPE_ID, facilityTypeId), TypeHRowMappers.TUPLE_INT_INT_ROW_MAPPER,
//		FaultCode.DB_CHECK_FACILITY_ON_VLT_NOT_FOUND, FaultCode.DB_CHECK_FACILITY_VLAN_TYPE_SELECT_ERROR);
//	if (t.getSecound() == null) { // VLAN_NETWORK_TYPEが存在しない場合
//	    throw new WebApiCommandException(FaultCode.DB_CHECK_FACILITY_VLAN_TYPE_NOT_FOUND);
//	}
//    }
//
//    /**
//     * チェック処理:: 指定したnetportTypeIdが存在するか.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_NETPORT_TYPE_NOT_FOUND</dt>
//     * <dd>netportTypeId項目行が存在しない</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_NETPORT_TYPE_SELECT_ERROR</dt>
//     * <dd>検索中にエラー発生(複数結果取得時含む)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param netportTypeId
//     *            VLANネットワークタイプID
//     * @throws WebApiCommandException
//     *             存在しないか、エラー(複数存在など)場合例外
//     */
//    protected void checkNetPortTypeId(final long opId, final int netportTypeId) throws WebApiCommandException {
//	queryForInt(findSql("CHECK_NETPORT_TYPE_ROWS"), new MapSqlParameterSource(TEMPLATE_NETPORT_TYPE_ID,
//		netportTypeId), FaultCode.DB_COUNT_CHECK_NETPORT_TYPE_NOT_FOUND,
//		FaultCode.DB_COUNT_CHECK_NETPORT_TYPE_SELECT_ERROR);
//    }
//
//    /**
//     * チェック処理:: 指定したnetportTypeIdが存在するか.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_VLAN_NETWORK_NOT_FOUND</dt>
//     * <dd>vlanNetworkTypeId項目行が存在しない</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_VLAN_NETWORK_SELECT_ERROR</dt>
//     * <dd>検索中にエラー発生(複数結果取得時含む)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param vlanNetworkTypeId
//     *            VLANネットワークタイプID
//     * @throws WebApiCommandException
//     *             存在しないか、エラー(複数存在など)場合例外
//     */
//    protected void checkVlanNetworkTypeId(final long opId, final int vlanNetworkTypeId) throws WebApiCommandException {
//	queryForInt(findSql("CHECK_VLAN_NETWORK_TYPE_ROWS"), new MapSqlParameterSource(TEMPLATE_VLAN_NETWORK_TYPE_ID,
//		vlanNetworkTypeId), FaultCode.DB_COUNT_CHECK_VLAN_NETWORK_NOT_FOUND,
//		FaultCode.DB_COUNT_CHECK_VLAN_NETWORK_SELECT_ERROR);
//    }
//
//    /**
//     * チェック処理:: 指定したvlanBindingTypeIdが存在するか.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_VLAN_BINDING_NOT_FOUND</dt>
//     * <dd>vlanBindingTypeId項目行が存在しない</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_VLAN_BINDING_MULTI_ERROR</dt>
//     * <dd>検索中にエラー発生(複数結果取得時含む)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param vlanBindingTypeId
//     *            VLAN結合タイプID
//     * @throws WebApiCommandException
//     *             存在しないか、エラー(複数存在など)場合例外
//     */
//    protected void checkVlanBindingTypeId(final long opId, final int vlanBindingTypeId) throws WebApiCommandException {
//	queryForInt(findSql("CHECK_VLAN_BINDING_TYPE_ROWS"), new MapSqlParameterSource(TEMPLATE_VLAN_BIDNING_TYPE_ID,
//		vlanBindingTypeId), FaultCode.DB_COUNT_CHECK_VLAN_BINDING_NOT_FOUND,
//		FaultCode.DB_COUNT_CHECK_VLAN_BINDING_SELECT_ERROR);
//    }
//
//    /**
//     * チェック処理:: 指定したhostTypeId(FCあり・なし)が存在するか.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_HOSTTYPEID_NOT_FOUND</dt>
//     * <dd>hostTypeId項目行が存在しない</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_HOSTTYPEID_SELECT_ERROR</dt>
//     * <dd>検索中にエラー発生(複数結果取得時含む)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param hostTypeId
//     *            ホストタイプID
//     * @throws WebApiCommandException
//     *             存在しないか、エラー(複数存在など)場合例外
//     */
//    protected void checkHostTypeId(final long opId, final int hostTypeId) throws WebApiCommandException {
//    queryForInt(findSql("CHECK_HOST_TYPE_ROWS"), new MapSqlParameterSource(TEMPLATE_HOST_TYPE_ID, hostTypeId),
//        FaultCode.DB_COUNT_CHECK_HOSTTYPEID_NOT_FOUND, FaultCode.DB_COUNT_CHECK_HOSTTYPEID_SELECT_ERROR);
//    }
//    
//    /**
//     * チェック処理:: 指定したhostHardwareTypeIdが存在するか.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_HOST_HARDWARE_TYPE_ID_NOT_FOUND</dt>
//     * <dd>hostHardwareTypeId項目行が存在しない</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_HOST_HARDWARE_TYPE_ID_SELECT_ERROR</dt>
//     * <dd>検索中にエラー発生(複数結果取得時含む)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param hostHardwareTypeId
//     *            ホストの機種コードを一意に決定するID
//     * @throws WebApiCommandException
//     *             存在しないか、エラー(複数存在など)場合例外
//     */
//    protected void checkHostHardwareTypeId(final long opId, final int hostHardwareTypeId) throws WebApiCommandException {
//	queryForInt(findSql("CHECK_HOST_HARDWARE_TYPE_ROWS"), new MapSqlParameterSource(TEMPLATE_HOST_HARDWARE_TYPE_ID, hostHardwareTypeId),
//		FaultCode.DB_COUNT_CHECK_HOST_HARDWARE_TYPE_ID_NOT_FOUND, FaultCode.DB_COUNT_CHECK_HOST_HARDWARE_TYPE_ID_SELECT_ERROR);
//    }
//    /**
//     * チェック処理:: 指定したrackGroupTypeIdが存在するか.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_RACK_GROUP_TYPE_ID_NOT_FOUND</dt>
//     * <dd>rackGroupTypeId項目行が存在しない</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_RACK_GROUP_TYPE_ID_SELECT_ERROR</dt>
//     * <dd>検索中にエラー発生(複数結果取得時含む)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param rackGroupTypeId
//     *            ラックグループタイプID
//     * @throws WebApiCommandException
//     *             存在しないか、エラー(複数存在など)場合例外
//     */
//    protected void checkRackGroupTypeId(final long opId, final int rackGroupTypeId) throws WebApiCommandException {
//    queryForInt(findSql("CHECK_RACK_GROUP_TYPE_ROWS"), new MapSqlParameterSource(TEMPLATE_RACK_GROUP_TYPE_ID, rackGroupTypeId),
//        FaultCode.DB_COUNT_CHECK_RACK_GROUP_TYPE_ID_NOT_FOUND, FaultCode.DB_COUNT_CHECK_RACK_GROUP_TYPE_ID_SELECT_ERROR);
//    }
//
//    /**
//     * チェック処理:: 指定したデータソースタイプ（ストレージタイプ）datasourceTypeIdが存在するか.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_DATASOURCE_NOT_FOUND</dt>
//     * <dd>datasourceTypeId項目行が存在しない</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_DATASOURCE_SELECT_ERROR</dt>
//     * <dd>検索中にエラー発生(複数結果取得時含む)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param datasourceTypeId
//     *            データソースタイプID
//     * @throws WebApiCommandException
//     *             存在しないか、エラー(複数存在など)場合例外
//     */
//    protected void checkDatasourceType(final long opId, final int datasourceTypeId) throws WebApiCommandException {
//	queryForInt(findSql("CHECK_DATASOURCE_TYPE_ROWS"), new MapSqlParameterSource(TEMPLATE_DATASOURCE_TYPE_ID,
//		datasourceTypeId), FaultCode.DB_COUNT_CHECK_DATASOURCE_NOT_FOUND,
//		FaultCode.DB_COUNT_CHECK_DATASOURCE_SELECT_ERROR);
//    }
//
//    /**
//     * 指定したhostId(在庫ホスト名)が存在するか. 対象のhostIdが論理削除済みの場合は存在しない扱いになる.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_HOSTID_NOT_FOUND</dt>
//     * <dd>hostId項目行が存在しない</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_HOSTID_SELECT_ERROR</dt>
//     * <dd>検索中にエラー発生(複数結果取得時含む)</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param hostId
//     *            ホストID
//     * @throws WebApiCommandException
//     *             存在しないか複数存在する場合例外
//     *
//     */
//    protected void checkHostId(final long opId, final String hostId) throws WebApiCommandException {
//	queryForInt(findSql("CHECK_HOST_MASTER_ROWS_BY_HOSTID"),
//		new MapSqlParameterSource(TEMPLATENAME_HOST_ID, hostId), FaultCode.DB_COUNT_CHECK_HOSTID_NOT_FOUND,
//		FaultCode.DB_COUNT_CHECK_HOSTID_SELECT_ERROR);
//    }
//
//    /**
//     * 指定したvCenterのバージョンタイプmgmt_server_version_type_idが存在するか.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_VCENTER_VERSION_NOT_FOUND</dt>
//     * <dd>指定されたVCENTER VERSION TYPE IDは存在しません</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_VCENTER_VERSION_SELECT_ERROR</dt>
//     * <dd>指定されたVCENTER VERSION TYPE IDの検索でエラーが発生しました</dd>
//     * </dl>
//     *
//     * @param opId
//     *            処理ID
//     * @param mgmtServerVersionTypeId
//     *            vCenterバージョンタイプID
//     * @throws WebApiCommandException
//     *             存在しないか、エラー(複数存在など)場合例外
//     */
//    protected void checkMgmtServerVersionType(final long opId, final int mgmtServerVersionTypeId)
//	    throws WebApiCommandException {
//	queryForInt(findSql("CHECK_MGMT_SERVER_VERSION_TYPE_ROWS"), new MapSqlParameterSource(
//		TEMPLATE_MGMT_SERVER_VERSION_TYPE_ID, mgmtServerVersionTypeId),
//		FaultCode.DB_COUNT_CHECK_VCENTER_VERSION_NOT_FOUND,
//		FaultCode.DB_COUNT_CHECK_VCENTER_VERSION_SELECT_ERROR);
//    }
//
//    /**
//     * CONSTRACT_IDから存在確認を行う.
//     *
//     * <dl>
//     * <dt>FaultCode.DB_COUNT_CHECK_CONSTRACT_NOT_FOUND</dt>
//     * <dd>CONSTRACT検索結果なし</dd>
//     * <dt>FaultCode.DB_COUNT_CHECK_CONSTRACT_SELECT_ERROR</dt>
//     * <dd>CONSTRACT検索結果が複数件/検索中エラー</dd>
//     * </dl>
//     *
//     * @param opId
//     *            OPID
//     * @param constractId
//     *            コントラクトID
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected void checkConstractId(final long opId, final int constractId) throws WebApiCommandException {
//	queryForInt(findSql("CHECK_CONSTRACT_ID"), new MapSqlParameterSource(TEMPLATENAME_CONSTRACT_ID, constractId),
//		FaultCode.DB_COUNT_CHECK_CONSTRACT_NOT_FOUND, FaultCode.DB_COUNT_CHECK_CONSTRACT_SELECT_ERROR);
//    }
//
//    /**
//     * LICENCE_TYPE_IDの存在確認を行う.
//     *
//     * @param opId
//     *            操作ID
//     * @param licenseTypeId
//     *            ライセンスタイプID
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>FaultCode.DB_COUNT_CHECK_LICENSE_TYPE_NOT_FOUND</dt>
//     *             <dd>ライセンスタイプ検索結果なし</dd>
//     *             <dt>FaultCode.DB_COUNT_CHECK_LICENSE_TYPE_SELECT_ERROR</dt>
//     *             <dd>ライセンスタイプ検索結果が複数件/検索中エラー</dd>
//     *             </dl>
//     *
//     */
//    protected void checkLicenseTypeId(final long opId, final int licenseTypeId) throws WebApiCommandException {
//	// ConstractIDの検索を行う
//	queryForInt(findSql("CHECK_LICENCE_TYPE_ROWS"), //
//		new MapSqlParameterSource(TEMPLATENAME_LICENSE_TYPE_ID, licenseTypeId), //
//		FaultCode.DB_COUNT_CHECK_LICENSE_TYPE_NOT_FOUND, FaultCode.DB_COUNT_CHECK_LICENSE_TYPE_SELECT_ERROR);
//    }
//
//    /**
//     * SSNW_NAT_IP_RENT_IDの存在確認を行う.
//     *
//     * @param opId
//     *            操作ID
//     * @param dr
//     *            データセンターRENT
//     * @param ssnwNatIpRentId
//     *            SSNW貸出ID
//     * @param ssnwNatIpType
//     *            SSNW種別
//     * @throws WebApiCommandException
//     *             <dl>
//     *             <dt>FaultCode.DB_COUNT_CHECK_SSNW_RENT_DATA_NOT_FOUND</dt>
//     *             <dd>指定されたSSNW貸出IDのクエリ結果行がありません(countなので想定外)</dd>
//     *             <dt>FaultCode.DB_COUNT_CHECK_SSNW_RENT_DATA_SELECT_ERROR</dt>
//     *             <dd>指定されたSSNW貸出IDのクエリ検索でエラーが発生しました</dd>
//     *             <dt>FaultCode.DB_COUNT_CHECK_NO_MUCH_SSNW_RENT_DATA_ERROR</dt>
//     *             <dd>指定されたSSNW貸出IDに一致するSSNWのデータ数が1つでありません</dd>
//     *             </dl>
//     *
//     */
//    protected void checkSsnwNatIpRentId(final long opId, final DatacenterRent dr, final int ssnwNatIpRentId,
//	    final SsnwNatIpType ssnwNatIpType) throws WebApiCommandException {
//	// ssnwNatIpRentIdの存在チェック
//	if (queryForInt(
//		findSql("SELECT_COUNT_SSNW_RENT_ID"), //
//		new MapSqlParameterSource("datacenter_rent_id", dr.getDatacenterRentId()).//
//			addValue("ssnwNatIpRentId", ssnwNatIpRentId).//
//			addValue("ssnwNatIpTypeId", ssnwNatIpType.getCode()),
//		FaultCode.DB_COUNT_CHECK_SSNW_RENT_DATA_NOT_FOUND,
//		FaultCode.DB_COUNT_CHECK_SSNW_RENT_DATA_SELECT_ERROR) != 1) {
//	    throw new WebApiCommandException(FaultCode.DB_COUNT_CHECK_NO_MUCH_SSNW_RENT_DATA_ERROR);
//	}
//    }
//
//    /**
//     * 指定したテーブル、カラムでその値が一意かどうかをチェックする.
//     * 
//     * @param opId 操作者ID
//     * @param tableName テーブル名
//     * @param columnName カラム名
//     * @param uniqueValue 一意であることを確認する値
//     * @param useDeleteFlag delete_flag=falseを利用するかどうか
//     * @throws WebApiCommandException チェック時に発生しうる例外
//     */
//    protected void checkUniqueValue(final long opId, final String tableName, final String columnName,
//	    final String uniqueValue, final boolean useDeleteFlag) throws WebApiCommandException {
//	StringBuffer sql = new StringBuffer();
//	sql.append(" SELECT COUNT(*) FROM ").append(tableName);
//	sql.append(" WHERE ").append(columnName).append(" = :").append(columnName);
//	if (useDeleteFlag) {
//	    sql.append(" AND delete_flag=false ");
//	}
//	final int uniqueCount = queryForInt(sql.toString(), //
//		new MapSqlParameterSource(columnName, uniqueValue), //
//		FaultCode.DB_COUNT_CHECK_UNIQUE_VALUE_NOT_FOUND, FaultCode.DB_COUNT_CHECK_UNIQUE_VALUE_SELECT_ERROR);
//	if (uniqueCount != 0) {
//	    throw new WebApiCommandException(FaultCode.DB_COUNT_CHECK_UNIQUE_VALUE_ERROR);
//	}
//    }
//
//    /**
//     * 更新対象データが外部キーの場合に親テーブルに存在しないデータが指定されていないかチェックするメソッド.
//     *
//     * @param targetTable 更新対象マスタテーブル名
//     * @param targetColumn　更新対象カラム名
//     * @param targetValue　更新データ値
//     * @throws WebApiCommandException 外部キーデータが親テーブルに存在しない場合エラー
//     */
//    protected void checkForeignKeyValue(final String baseTableName, final String targetColumn, final String targetValue) throws WebApiCommandException {
//	final String targetTable = baseTableName + TABLENAME_SUFFIX_MASTER;
//	// 外部キー元データの存在チェック
//	for (ForeignKeyColumnName values :ForeignKeyColumnName.values()) {
//	    if (values.getTableName().equals(targetTable.toUpperCase())
//		    && values.getColumnName().equals(
//			    targetColumn.toUpperCase())) {
//		// 更新対象が外部キーで存在しないデータが指定された場合にエラー
//
//		// 外部キー検索用SQL
//		StringBuffer sql = new StringBuffer();
//		sql.append("SELECT " + targetColumn + " ");
//		sql.append("FROM " + values.getForeignTableName() + " ");
//		sql.append("WHERE " + targetColumn + "='" + targetValue + "' ");
//		if (values.isHaveDeleteFlag()) {
//		    sql.append("AND DELETE_FLAG = false;");
//		}
//
//		// 設定値が親テーブルに存在するかチェックする
//		queryForInt(
//			sql.toString(),
//			new MapSqlParameterSource(),
//			FaultCode.DB_COUNT_CHECK_FOREIGN_KEY_NOT_FOUND,
//			FaultCode.DB_COUNT_CHECK_FOREIGN_KEY_VALUE_SELECT_ERROR);
//		break;
//	    }
//	}
//    }
//
//    // ///////////////////
//    /**
//     * 1項目検索処理.
//     *
//     * <dl>
//     * <dt>emptyCode</dt>
//     * <dd>検索結果が0件(emptyCodeがnull指定で要素0件の場合はエラーでなくnullを返す)</dd>
//     * <dt>otherError</dt>
//     * <dd>
//     * 検索結果が複数件/JDBC処理例外(SQL文の例外エラー・引数不足など)/引数NULLなどの他例外(SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param <T>
//     *            検索結果Object型
//     * @param sql
//     *            検索SQL(NamedParameter)
//     * @param map
//     *            検索引数
//     * @param rowMapper
//     *            結果取得RowMapper
//     * @param emptyError
//     *            結果がない場合のエラーコード 指定がNULLの場合で、要素がNULL結果の場合例外でなくNULLが返る
//     * @param otherError
//     *            それ以外のエラーコード(必ず指定してください)
//     * @return 取得結果(emptyCode==nullの場合はNULLの可能性あり)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected <T> T queryForObject(final String sql, final MapSqlParameterSource map, final RowMapper<T> rowMapper,
//	    final FaultCode emptyError, final FaultCode otherError) throws WebApiCommandException {
//	try {
//	    return findNamedParameterJdbcTemplate().queryForObject(sql, map, rowMapper);
//	} catch (EmptyResultDataAccessException e) { // 結果情報行なし
//	    if (emptyError == EMPTY_NO_FAULT) { // emptyError指定がない場合は単純に結果をNULLで返す
//		return null;
//	    }
//	    throw new WebApiCommandException(emptyError, "queryForObject", e);
//	} catch (IncorrectResultSizeDataAccessException e) { // 結果行数複数
//	    throw new WebApiCommandException(otherError, "queryForObject", e);
//	} catch (DataAccessException e) { // そのほかのJDBCエラー
//	    throw new WebApiCommandException(otherError, "queryForObject", e);
//	} catch (Throwable e) { // そのほかの実行時エラー
//	    throw new WebApiCommandException(otherError, "queryForObject", e);
//	}
//    }
//
//    /**
//     * 結果がリストになる検索処理.
//     *
//     * <dl>
//     * <dt>error</dt>
//     * <dd>JDBC処理例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param <T>
//     *            結果型(Listの内容の型)
//     * @param sql
//     *            検索SQL(NamedParameter)
//     * @param map
//     *            検索引数
//     * @param rowMapper
//     *            結果取得RowMapper
//     * @param error
//     *            発生エラー
//     * @return 取得結果(結果行0の場合は空のリスト)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected <T> List<T> queryForList(final String sql, final MapSqlParameterSource map, final RowMapper<T> rowMapper,
//	    final FaultCode error) throws WebApiCommandException {
//	try {
//	    return findNamedParameterJdbcTemplate().query(sql, map, rowMapper);
//	} catch (DataAccessException e) { // そのほかのJDBCエラー
//	    throw new WebApiCommandException(error, "queryForList", e);
//	} catch (Throwable e) { // JDBC以外のエラー
//	    throw new WebApiCommandException(error, "queryForList", e);
//	}
//    }
//
//    /**
//     * DBのレコード1行登録処理. 作成行にAUTO_INCREMENTがついていないと必ずエラーが発生します.
//     *
//     * <dl>
//     * <dt>error</dt>
//     * <dd>作成行が1行でない（追加処理SQL実行中の例外）/ 作成行のAUTO_INCREMENT結果行が取得できない</dd>
//     * </dl>
//     *
//     * @param sql
//     *            実行SQL
//     * @param map
//     *            実行時パラメータマップ
//     * @param error
//     *            追加処理中にエラーが発生した場合のエラーコード
//     * @return 追加した行のAUTO_INCREMENT要素の値
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected int insertOneRecord(final String sql, final MapSqlParameterSource map, final FaultCode error)
//	    throws WebApiCommandException {
//	final int row;
//	try {
//	    row = findNamedParameterJdbcTemplate().update(sql, map);
//	} catch (DataAccessException e) { // そのほかのJDBCエラー
//	    throw new WebApiCommandException(error, "insertOneRecord", e);
//	} catch (Throwable e) { // JDBC以外の実行中エラー
//	    throw new WebApiCommandException(error, "insertOneRecord", e);
//	}
//	if (row != 1) {
//	    throw new WebApiCommandException(error, "insertOneRecord : Insert row count error:" + row);
//	}
//	return (int) readLastInsertId(error);
//    }
//
//    /**
//     * DB変更処理.
//     *
//     * 一行追加(AUTO_INCREMENTつき)や一行更新は別のメソッドを使用してください。
//     *
//     * <dl>
//     * <dt>error</dt>
//     * <dd>JDBC処理例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param sql
//     *            SQL(NamedPArameter)
//     * @param map
//     *            引数マップ
//     * @param error
//     *            呼び出し中にエラーが発生した場合のFaultCode
//     * @return 変更行数
//     * @throws WebApiCommandException
//     *             SQL実行エラー
//     *
//     */
//    protected int updateDB(final String sql, final MapSqlParameterSource map, final FaultCode error)
//	    throws WebApiCommandException {
//	try {
//	    return findNamedParameterJdbcTemplate().update(sql, map);
//	} catch (DataAccessException e) { // そのほかのJDBCエラー
//	    throw new WebApiCommandException(error, "updateDB", e);
//	} catch (Throwable e) {
//	    throw new WebApiCommandException(error, "updateDB", e);
//	}
//    }
//
//    /**
//     * DB1行変更処理.
//     *
//     * 処理例外はすべてWebApiCommandExceptionで実行時指定のエラーコードになる。 メッセージ部分は発生した内容によって異なる。
//     *
//     * <dl>
//     * <dt>updateError</dt>
//     * <dd>更新時エラー(実行中例外)・更新行数が1以外の場合もこのエラーが発生</dd>
//     * </dl>
//     *
//     * @param sql
//     *            SQL(NamedPArameter)
//     * @param map
//     *            引数マップ
//     * @param updateError
//     *            DB処理時エラー
//     * @return 変更成否
//     * @throws WebApiCommandException
//     *             SQL実行エラー(影響行数1行以外、処理中に別の例外が発生)
//     *
//     */
//    protected boolean updateOneRecord(final String sql, final MapSqlParameterSource map, final FaultCode updateError)
//	    throws WebApiCommandException {
//	final int row;
//	try {
//	    row = findNamedParameterJdbcTemplate().update(sql, map);
//	} catch (DataAccessException e) { // そのほかのJDBCエラー
//	    throw new WebApiCommandException(updateError, "updateOneRecord", e);
//	} catch (Throwable e) { // 更新中のJDBC以外のエラー
//	    throw new WebApiCommandException(updateError, "updateOneRecord", e);
//	}
//	if (row == 1) {
//	    return true;
//	}
//	throw new WebApiCommandException(updateError, "updateOneRecord : Update row count error:" + row);
//    }
//
//    /**
//     * 結果がStringになる検索処理.
//     *
//     * <dl>
//     * <dt>emptyCode</dt>
//     * <dd>要素が存在しない場合(emptyCodeがNULLの場合で結果が存在しない場合<code>NULL</code>)</dd>
//     * <dt>multiCode</dt>
//     * <dd>要素が複数存在する場合・JDBC処理例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(
//     * SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param sql
//     *            検索SQL(NamedParameter)
//     * @param map
//     *            検索引数
//     * @param emptyCode
//     *            結果要素が0件の場合のFaultCode(0件を許す場合はemptyCodeを記述しない)
//     * @param multiCode
//     *            取得結果が複数や実行中に例外が発生した場合のFaultCode
//     * @return 取得結果(取得件数0件やnullの場合はnullを返す)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外 実行時に発生しうる例外
//     */
//    protected String queryForString(final String sql, final MapSqlParameterSource map, final FaultCode emptyCode,
//	    final FaultCode multiCode) throws WebApiCommandException {
//	return queryForObject(sql, map, TypeHRowMappers.STRING_ROW_MAPPER, emptyCode, multiCode);
//    }
//
//    /**
//     * 結果がintになる検索処理.
//     *
//     * <dl>
//     * <dt>emptyCode</dt>
//     * <dd>要素が存在しない場合(emptyCodeがNULLの場合で結果が存在しない場合
//     * <code>NULL_QUERY_INT_VALUE(Integer.MIN_VALUE)</code>)</dd>
//     * <dt>multiCode</dt>
//     * <dd>要素が複数存在する場合・JDBC処理例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(
//     * SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param sql
//     *            検索SQL(NamedParameter)
//     * @param map
//     *            検索引数
//     * @param emptyCode
//     *            結果要素が0件の場合のFaultCode(0件を許す場合はemptyCodeを記述しない)
//     * @param multiCode
//     *            取得結果が複数や実行中に例外が発生した場合のFaultCode
//     * @return 取得結果(取得件数0件やnullの場合はNULL_QUERY_INT_VALUE[Integer.MIN_VALUE]を返す)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected int queryForInt(final String sql, final MapSqlParameterSource map, final FaultCode emptyCode,
//	    final FaultCode multiCode) throws WebApiCommandException {
//	final Integer v = queryForObject(sql, map, TypeHRowMappers.INTEGER_ROW_MAPPER, emptyCode, multiCode);
//	if (v == null) {
//	    return NULL_QUERY_INT_VALUE;
//	} else {
//	    return v.intValue();
//	}
//    }
//
//    /**
//     * 結果がLongになる検索処理.
//     *
//     * <dl>
//     * <dt>emptyCode</dt>
//     * <dd>要素が存在しない場合(emptyCodeがNULLの場合で結果が存在しない場合<code>Long.MIN_VALUE</code>)</dd>
//     * <dt>errorCode</dt>
//     * <dd>それ以外のエラーコード(複数例外・JDBCエラーなど)</dd>
//     * </dl>
//     *
//     * @param sql
//     *            検索SQL(NamedParameter)
//     * @param map
//     *            検索引数
//     * @param emptyCode
//     *            結果要素が0件の場合のFaultCode(0件を許す場合はemptyCodeを記述しない)
//     * @param errorCode
//     *            取得結果が複数や実行中に例外が発生した場合のFaultCode
//     * @return 取得結果(取得件数0件やnullの場合はNULL_QUERY_LONG_VALUE[Long.MIN_VALUE]を返す)
//     * @throws WebApiCommandException
//     *             実行時に発生しうる例外
//     */
//    protected long queryForLong(final String sql, final MapSqlParameterSource map, final FaultCode emptyCode,
//	    final FaultCode errorCode) throws WebApiCommandException {
//	final Long v = queryForObject(sql, map, TypeHRowMappers.LONG_ROW_MAPPER, emptyCode, errorCode);
//	if (v == null) {
//	    return NULL_QUERY_LONG_VALUE;
//	} else {
//	    return v.longValue();
//	}
//    }
//
//    /**
//     * 結果が文字列リストになる検索処理.
//     *
//     * <dl>
//     * <dt>error</dt>
//     * <dd>JDBC処理例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param sql
//     *            検索SQL(NamedParameter)
//     * @param map
//     *            検索引数
//     * @param error
//     *            検索時エラー
//     * @return 取得結果(List<String>)(結果行0の場合は空のリスト)
//     * @throws WebApiCommandException
//     *             SQL実行時エラーなど
//     */
//    protected List<String> queryForStringList(final String sql, final MapSqlParameterSource map, final FaultCode error)
//	    throws WebApiCommandException {
//	return queryForList(sql, map, TypeHRowMappers.STRING_ROW_MAPPER, error);
//    }
//
//    /**
//     * 結果がINTリストになる検索処理.
//     *
//     * <dl>
//     * <dt>error</dt>
//     * <dd>JDBC処理例外(SQL文の例外エラー・引数不足など)・引数NULLなどの他例外(SQLがNULLや引数マップがNULLなど)</dd>
//     * </dl>
//     *
//     * @param sql
//     *            検索SQL(NamedParameter)
//     * @param map
//     *            検索引数
//     * @param error
//     *            検索処理中にエラーが発生した場合のFaultCode
//     * @return 取得結果(List<Integer>)(結果行0の場合は空のリスト)
//     * @throws WebApiCommandException
//     *             実行時に発生した例外
//     */
//    protected List<Integer> queryForIntegerList(final String sql, final MapSqlParameterSource map,
//	    final FaultCode error) throws WebApiCommandException {
//	return queryForList(sql, map, TypeHRowMappers.INTEGER_ROW_MAPPER, error);
//    }
//
//    /**
//     * SpringJDBCの名前つきJDBCTemplateの取得. 処理内容の隠蔽のために直接.
//     * {@link NamedParameterJdbcDaoSupport#getNamedParameterJdbcTemplate()}
//     * を呼ばずにこのメソッドを経由してください
//     *
//     * @return {@link NamedParameterJdbcTemplate}のインスタンス
//     */
//    private NamedParameterJdbcTemplate findNamedParameterJdbcTemplate() {
//	return getNamedParameterJdbcTemplate();
//    }
//
//    /**
//     * ロック用のSQLスニペットを返す.
//     *
//     * @param isForUpdate
//     *            更新するかどうか
//     * @return SQLスニペット
//     */
//    protected static String makeForUpdateQuery(final boolean isForUpdate) {
//	if (isForUpdate) {
//	    return SQLQUERYPART_FOR_UPDATE;
//	} else {
//	    return EMPTY_STRING;
//	}
//    }
}
