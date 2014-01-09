package dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import dao.BumonDao;
import dao.DonjonItemDao;
import dto.Bumon;
import dto.DonjonEquItem;

@SuppressWarnings("deprecation")
public class DonjonItemDaoImpl extends BaseJdbcDaoImpl implements DonjonItemDao {

    /** INSERT */
    public static final String INSERT = 
"INSERT INTO BUMON (CD_BUMON, NM_BUMON) VALUE (?, ?)";

    /** SELECT */
    public static final String SELECT = 
"SELECT * FROM BUMON WHERE CD_BUMON = ?";

    /** SELECT */
    public static final String SELECT_ITEM = 
"SELECT deid.item_detail_id, deid.item_type_id, deid.item_img_id, deid.item_name, deid.point, deid.skill_id, deid.equ_flag, deid.use_count, deid.item_version, dit.item_type_name, dii.item_img_binary, dii.item_img_mime FROM DONJON_EQU_ITEM_DETAIL deid LEFT OUTER JOIN DONJON_ITEM_TYPE dit ON (deid.item_type_id = dit.item_type_id) LEFT OUTER JOIN DONJON_ITEM_IMG dii ON (deid.item_img_id = dii.item_img_id) WHERE deid.item_version = ?";
    
    /** UPDATE */
    public static final String UPDATE = 
"UPDATE BUMON SET NM_BUMON = ? WHERE CD_BUMON = ?";

    /** DELETE */
    public static final String DELETE = 
"DELETE FROM BUMON WHERE CD_BUMON = ?";

    /** FIND ALL */
    public static final String FIND_ALL = "SELECT * FROM DONJON_EQU_ITEM_DETAIL";

    @SuppressWarnings("deprecation")
	public void insert(Bumon bumon) {
        getSimpleJdbcTemplate().update(INSERT, bumon.getCdBumon(),
                bumon.getNmBumon());
    }

    @SuppressWarnings("deprecation")
	public void update(Bumon bumon) {
        getSimpleJdbcTemplate().update(UPDATE, bumon.getNmBumon(),
                bumon.getCdBumon());
    }

    @SuppressWarnings("deprecation")
	public void delete(String cdBumon) {
        getSimpleJdbcTemplate().update(DELETE, cdBumon);
    }

    @SuppressWarnings("deprecation")
	public List<DonjonEquItem> findAll() {
    	return getSimpleJdbcTemplate().query(findSql("FIND_ALL"),
                new ParameterizedBumonRowMapper());
    }

    static class ParameterizedBumonRowMapper 
implements ParameterizedRowMapper<DonjonEquItem> {
        public DonjonEquItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        	DonjonEquItem item = new DonjonEquItem();
        	item.setItemDetailId(rs.getInt("item_detail_id"));
        	item.setItemTypeId(rs.getInt("item_type_id"));
        	item.setItemImgId(rs.getInt("item_img_id"));
        	item.setItemName(rs.getString("item_name"));
        	item.setPoint(rs.getInt("point"));
        	item.setSkillId(rs.getInt("skill_id"));
        	item.setEquFlag(rs.getBoolean("equ_flag"));
        	item.setUseCount(rs.getInt("use_count"));
        	item.setItemVersion(rs.getInt("item_version"));
        	item.setItemTypeName(rs.getString("item_type_name"));
        	item.setItemImgBinary(rs.getString("item_img_binary"));
        	item.setItemImgMime(rs.getString("item_img_mime"));
            return item;
        }
    }

	@Override
	public void insert(DonjonEquItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DonjonEquItem queryDetailId(int itemDetailId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DonjonEquItem> queryItemAll() {
		// TODO Auto-generated method stub
		return getSimpleJdbcTemplate().query(
				findSql("SELECT_ITEM"),
                new ParameterizedBumonRowMapper()
                );
	}
	
	@Override
	public List<DonjonEquItem> queryItemVersion(int itemVersionId) {
		// TODO Auto-generated method stub
		return getSimpleJdbcTemplate().query(
				findSql("SELECT_ITEM")+findSql("WHERE_VERSION"),
                new ParameterizedBumonRowMapper(),
                itemVersionId);
	}

	@Override
	public List<DonjonEquItem> queryItemVersionToVersion(int itemVersionId) {
		// TODO Auto-generated method stub
		return getSimpleJdbcTemplate().query(
				findSql("SELECT_ITEM")+findSql("WHERE_UPPER_VERSION"),
                new ParameterizedBumonRowMapper(),
                itemVersionId);
	}
	
	@Override
	public List<DonjonEquItem> queryEquItemAll() {
		// TODO Auto-generated method stub
		return getSimpleJdbcTemplate().query(
				findSql("SELECT_EQU_ITEM"),
                new ParameterizedBumonRowMapper()
                );
	}
	
	@Override
	public List<DonjonEquItem> queryEquItemVersion(int itemVersionId) {
		// TODO Auto-generated method stub
		return getSimpleJdbcTemplate().query(
				findSql("SELECT_EQU_ITEM")+findSql("WHERE_VERSION"),
                new ParameterizedBumonRowMapper(),
                itemVersionId);
	}

	@Override
	public List<DonjonEquItem> queryEquItemVersionToVersion(int itemVersionId) {
		// TODO Auto-generated method stub
		return getSimpleJdbcTemplate().query(
				findSql("SELECT_EQU_ITEM")+findSql("WHERE_UPPER_VERSION"),
                new ParameterizedBumonRowMapper(),
                itemVersionId);
	}

	@Override
	public void update(DonjonEquItem item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(int itemDetailId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteEqu(int itemDetailId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteImg(int itemImgId) {
		// TODO Auto-generated method stub
		
	}
}
