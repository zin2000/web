package dao;

import java.util.List;

import dto.Bumon;

public interface BumonDao {
    void insert(Bumon bumon);

    Bumon load(String cdBumon);

    void update(Bumon bumon);

    void delete(String cdBumon);

    List<Bumon> findAll();

}
