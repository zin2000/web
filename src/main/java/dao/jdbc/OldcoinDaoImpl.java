package dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

import dao.OldcoinDao;
import dto.Bumon;
import dto.DonjonEquItem;
import dto.OldcoinDetail;

@SuppressWarnings("deprecation")
public class OldcoinDaoImpl extends BaseJdbcDaoImpl implements OldcoinDao {
	        
	public void insert(Bumon bumon) {
    	getSimpleJdbcTemplate().update(findSql("INSERT"), bumon.getCdBumon(),
                bumon.getNmBumon());
    }

	public void update(Bumon bumon) {
        getSimpleJdbcTemplate().update(findSql("UPDATE"), bumon.getNmBumon(),
                bumon.getCdBumon());
    }

	public void delete(String cdBumon) {
        getSimpleJdbcTemplate().update(findSql("DELETE"), cdBumon);
    }

	public List<OldcoinDetail> findAll() {
    	return getSimpleJdbcTemplate().query(findSql("SELECT_COIN"),
                new ParameterizedBumonRowMapper());
    }
    
	public List<OldcoinDetail> findPage(int page){
		return getSimpleJdbcTemplate().query(findSql("SELECT_COIN")+findSql("LIMIT_PAGE"),
                new ParameterizedBumonRowMapper(),
                page);
    }
    
	public int findCountAll() {
    	return getSimpleJdbcTemplate().queryForInt(findSql("SELECT_COUNT"));
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
