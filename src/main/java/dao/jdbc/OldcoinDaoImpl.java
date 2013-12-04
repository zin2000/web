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
import dao.OldcoinDao;
import dto.Bumon;
import dto.DonjonEquItem;
import dto.OldcoinDetail;

@SuppressWarnings("deprecation")
public class OldcoinDaoImpl extends SimpleJdbcDaoSupport implements OldcoinDao {

    /** INSERT */
    public static final String INSERT = 
"INSERT INTO BUMON (CD_BUMON, NM_BUMON) VALUE (?, ?)";

    /** SELECT */
    public static final String SELECT = 
"SELECT * FROM BUMON WHERE CD_BUMON = ?";

    /** SELECT */
    public static final String SELECT_ITEM = 
"SELECT od.oldcoin_detail_id, od.add_date, od.name, od.front_img_url, od.back_img_url, of.name as font_name, om.name as material_name, od.start_year, od.end_year FROM OLDCOIN_DETAIL od left outer join OLDCOIN_FONT of ON(od.font_id=of.oldcoin_font_id) left outer join OLDCOIN_MATERIAL om ON(od.material_id=om.oldcoin_material_id)";
    
    /** SELECT */
    public static final String SELECT_COIN = "SELECT od.oldcoin_detail_id, od.add_date, od.name, od.front_img_url, od.back_img_url, of.name as font_name, om.name as material_name, od.start_year, od.end_year FROM OLDCOIN_DETAIL od left outer join OLDCOIN_FONT of ON(od.font_id=of.oldcoin_font_id) left outer join OLDCOIN_MATERIAL om ON(od.material_id=om.oldcoin_material_id) ";

    /** LIMIT_PAGE */
    public static final String LIMIT_PAGE = "LIMIT ?, 10";
    
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

	public void delete(String cdBumon) {
        getSimpleJdbcTemplate().update(DELETE, cdBumon);
    }

	public List<OldcoinDetail> findAll() {
    	return getSimpleJdbcTemplate().query(SELECT_COIN,
                new ParameterizedBumonRowMapper());
    }
    
	public List<OldcoinDetail> findPage(int page) {
    	return getSimpleJdbcTemplate().query(SELECT_COIN+LIMIT_PAGE,
                new ParameterizedBumonRowMapper(),
                page);
    }
    
	public int findCountAll() {
    	return getSimpleJdbcTemplate().queryForInt("SELECT COUNT(*) FROM OLDCOIN_DETAIL");
    }

    static class ParameterizedBumonRowMapper 
implements ParameterizedRowMapper<OldcoinDetail> {
        public OldcoinDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
        	OldcoinDetail bean = new OldcoinDetail();
        	bean.setDetailId(rs.getInt("oldcoin_detail_id"));
        	bean.setAddDate(rs.getDate("add_date"));
        	bean.setName(rs.getString("name"));
        	
        	bean.setFrontImgUrl(rs.getString("front_img_url"));
        	bean.setBackImgUrl(rs.getString("back_img_url"));
        	bean.setFontName(rs.getString("font_name"));
        	bean.setMaterialName(rs.getString("material_name"));
        	bean.setStartYear(rs.getInt("start_year"));
        	bean.setEndtYear(rs.getInt("end_year"));
            return bean;
        }
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
	public void insert(OldcoinDetail oldcoin) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public OldcoinDetail queryDetailId(int itemDetailId) {
		// TODO Auto-generated method stub
		return null;
	}
}
