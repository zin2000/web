package service.impl;

import java.util.List;

import service.BumonService;
import service.DonjonItemService;

import dao.BumonDao;
import dao.DonjonItemDao;
import dto.Bumon;
import dto.DonjonEquItem;

public class DonjonItemServiceImpl implements DonjonItemService {
    private DonjonItemDao itemDao;

    public DonjonItemDao getDonjonItemDao() {
        return itemDao;
    }

    public void setDonjonItemDao(DonjonItemDao itemDao) {
        this.itemDao = itemDao;
    }

	@Override
	public List<DonjonEquItem> queryEquItemVersion(int itemVersionId) {
		return this.itemDao.queryItemVersion(itemVersionId);
	}
}
