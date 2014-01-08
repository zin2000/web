package service.impl;

import java.util.List;

import service.BumonService;
import service.DonjonItemService;
import service.OldcoinService;

import dao.BumonDao;
import dao.DonjonItemDao;
import dao.OldcoinDao;
import dto.Bumon;
import dto.DonjonEquItem;
import dto.OldcoinDetail;

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
}
