package service;

import java.util.List;

import dto.Bumon;

public interface BumonService {
    void registBumon(Bumon bumon);

    Bumon getBumon(String cdBumon);

    List<Bumon> getAllBumon();

    void removeBumon(String cdBumon);
}
