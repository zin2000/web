package service.impl;

import java.util.List;
import service.DonjonItemService;

import dao.DonjonItemDao;
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
		return this.itemDao.queryEquItemVersion(itemVersionId);
	}
	
	@Override
	public List<DonjonEquItem> queryEquItemVersionTo(int itemVersionId) {
		return this.itemDao.queryEquItemVersionToVersion(itemVersionId);
	}
	
	@Override
	public List<DonjonEquItem> queryEquItemAll() {
		return this.itemDao.queryEquItemAll();
	}
	
	@Override
	public List<DonjonEquItem> queryItemVersion(int itemVersionId) {
		return this.itemDao.queryItemVersion(itemVersionId);
	}
	
	@Override
	public List<DonjonEquItem> queryItemVersionTo(int itemVersionId) {
		return this.itemDao.queryItemVersionToVersion(itemVersionId);
	}
	
	@Override
	public List<DonjonEquItem> queryItemAll() {
		return this.itemDao.queryItemAll();
	}
}
