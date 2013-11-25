package service.impl;

import service.SetupService;
import dao.BumonDao;
import dao.ShainDao;
import dto.Bumon;
import dto.Shain;

public class SetupServiceImpl implements SetupService {

    private BumonDao bumonDao;

    private ShainDao shainDao;

    public void setup(Bumon bumon, Shain shain) {
        bumonDao.insert(bumon);
        shainDao.insert(shain);
    }

    public BumonDao getBumonDao() {
        return bumonDao;
    }

    public void setBumonDao(BumonDao bumonDao) {
        this.bumonDao = bumonDao;
    }

    public ShainDao getShainDao() {
        return shainDao;
    }

    public void setShainDao(ShainDao shainDao) {
        this.shainDao = shainDao;
    }

}
