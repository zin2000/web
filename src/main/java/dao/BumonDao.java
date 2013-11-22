package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

import dto.Bumon;

@SuppressWarnings("deprecation")
public class BumonDao extends SimpleJdbcDaoSupport implements IBumonDao {

    /** INSERT */
    public static final String INSERT = 
"INSERT INTO BUMON (CD_BUMON, NM_BUMON) VALUE (?, ?)";

    /** SELECT */
    public static final String SELECT = 
"SELECT * FROM BUMON WHERE CD_BUMON = ?";

    /** UPDATE */
    public static final String UPDATE = 
"UPDATE BUMON SET NM_BUMON = ? WHERE CD_BUMON = ?";

    /** DELETE */
    public static final String DELETE = 
"DELETE FROM BUMON WHERE CD_BUMON = ?";

    /** FIND ALL */
    public static final String FIND_ALL = "SELECT * FROM BUMON";

    @SuppressWarnings("deprecation")
	public void insert(Bumon bumon) {
        getSimpleJdbcTemplate().update(INSERT, bumon.getCdBumon(),
                bumon.getNmBumon());
    }

    @SuppressWarnings("deprecation")
	public Bumon load(String cdBumon) {
        return getSimpleJdbcTemplate().queryForObject(SELECT,
                new ParameterizedBumonRowMapper(), cdBumon);
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
	public List<Bumon> findAll() {
    	return getSimpleJdbcTemplate().query(FIND_ALL,
                new ParameterizedBumonRowMapper());
    }

    static class ParameterizedBumonRowMapper 
implements ParameterizedRowMapper<Bumon> {
        public Bumon mapRow(ResultSet rs, int rowNum) throws SQLException {
            Bumon bumon = new Bumon();
            bumon.setCdBumon(rs.getString("CD_BUMON"));
            bumon.setNmBumon(rs.getString("NM_BUMON"));
            return bumon;
        }
    }
}
