package service.impl;

import java.util.List;

import service.OldcoinService;

import dao.OldcoinDao;
import dto.OldcoinCharacter;
import dto.OldcoinDetail;
import dto.OldcoinMaster;

public class OldcoinServiceImpl implements OldcoinService {
    private OldcoinDao coinDao;

    public OldcoinDao getOldcoinDao() {
        return coinDao;
    }

    public void setOldcoinDao(OldcoinDao coinDao) {
        this.coinDao = coinDao;
    }

	@Override
	public List<OldcoinDetail> queryAllDetailData() {
		return getOldcoinDao().findAll();
	}

	@Override
	public List<OldcoinDetail> queryPageDetailData(int page){
		return getOldcoinDao().findPage(page);
	}

	@Override
	public int queryCountDetailAll() {
		return getOldcoinDao().findCountAll();
	}

	@Override
	public List<OldcoinMaster> queryMasterData(String c1, String c2, String c3, String c4) {
		return getOldcoinDao().findCoinMaster(c1, c2, c3, c4);
	}

	@Override
	public List<OldcoinCharacter> findCoinCharacter1() {
		return getOldcoinDao().findCoinCharacter1();
	}

	@Override
	public List<OldcoinCharacter> findCoinCharacter2() {
		return getOldcoinDao().findCoinCharacter2();
	}

	@Override
	public List<OldcoinCharacter> findCoinCharacter3() {
		return getOldcoinDao().findCoinCharacter3();
	}

	@Override
	public List<OldcoinCharacter> findCoinCharacter4() {
		return getOldcoinDao().findCoinCharacter4();
	}
}
