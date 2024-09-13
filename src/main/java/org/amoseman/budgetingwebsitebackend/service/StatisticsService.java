package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.dao.StatisticsDAO;

public class StatisticsService<C> {
    private final StatisticsDAO<C> statisticsDAO;

    public StatisticsService(StatisticsDAO<C> statisticsDAO) {
        this.statisticsDAO = statisticsDAO;
    }

    public long totalFunds(String user) {
        return statisticsDAO.totalFunds(user);
    }
}
