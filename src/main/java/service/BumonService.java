package service;

import java.util.List;

import dao.IBumonDao;
import dto.Bumon;

public class BumonService implements IBumonService {
    private IBumonDao bumonDao;

    public void registBumon(Bumon bumon) {
        bumonDao.insert(bumon);
    }

    public Bumon getBumon(String cdBumon) {
        return bumonDao.load(cdBumon);
    }

    public List<Bumon> getAllBumon() {
        return bumonDao.findAll();
    }

    public void removeBumon(String cdBumon) {
        bumonDao.delete(cdBumon);
    }

    public IBumonDao getBumonDao() {
        return bumonDao;
    }

    public void setBumonDao(IBumonDao bumonDao) {
        this.bumonDao = bumonDao;
    }
}
