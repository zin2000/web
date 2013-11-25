package service.impl;

import java.util.List;

import service.BumonService;

import dao.BumonDao;
import dto.Bumon;

public class BumonServiceImpl implements BumonService {
    private BumonDao bumonDao;

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

    public BumonDao getBumonDao() {
        return bumonDao;
    }

    public void setBumonDao(BumonDao bumonDao) {
        this.bumonDao = bumonDao;
    }
}
