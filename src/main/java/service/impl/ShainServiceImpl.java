package service.impl;

import java.util.List;

import service.ShainService;

import dao.ShainDao;
import dto.Shain;

public class ShainServiceImpl implements ShainService {

    private ShainDao shainDao;

    public void registShain(Shain shain) {
        shainDao.insert(shain);
    }

    public Shain getShain(String cdShain) {
        return shainDao.load(cdShain);
    }

    public List<Shain> getAllShain() {
        return shainDao.findAll();
    }

    public void removeShain(String cdShain) {
        shainDao.delete(cdShain);
    }

    public ShainDao getShainDao() {
        return shainDao;
    }

    public void setShainDao(ShainDao shainDao) {
        this.shainDao = shainDao;
    }

}
