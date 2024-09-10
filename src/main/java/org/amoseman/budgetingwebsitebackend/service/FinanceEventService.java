package org.amoseman.budgetingwebsitebackend.service;

import org.amoseman.budgetingwebsitebackend.dao.FinanceEventDAO;
import org.amoseman.budgetingwebsitebackend.exception.FinanceEventAlreadyExistsException;
import org.amoseman.budgetingwebsitebackend.exception.FinanceEventDoesNotExistException;
import org.amoseman.budgetingwebsitebackend.exception.InvalidFinanceEventTypeException;
import org.amoseman.budgetingwebsitebackend.exception.NegativeValueException;
import org.amoseman.budgetingwebsitebackend.pojo.*;
import org.amoseman.budgetingwebsitebackend.time.Now;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FinanceEventService<C> {
    private final FinanceEventDAO<C> financeEventDAO;

    public FinanceEventService(FinanceEventDAO<C> financeEventDAO) {
        this.financeEventDAO = financeEventDAO;
    }

    public String addEvent(String user, CreateFinanceEvent create) throws NegativeValueException, InvalidFinanceEventTypeException, FinanceEventAlreadyExistsException, DateTimeException {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime now = Now.get();
        LocalDateTime when = LocalDateTime.of(create.getYear(), create.getMonth(), create.getDay(), 0, 0);
        FinanceEvent event = new FinanceEvent(
                uuid,
                now,
                user,
                create.getAmount(),
                create.getType(),
                when
        );
        financeEventDAO.addEvent(event);
        return uuid;
    }

    public void removeEvent(String user, String id, String type) throws FinanceEventDoesNotExistException {
        financeEventDAO.removeEvent(user, id, type);
    }

    public List<FinanceEvent> getEvents(
            String user, String type,
            String yearStartString, String monthStartString, String dayStartString,
            String yearEndString, String monthEndString, String dayEndString) throws NumberFormatException {

        int yearStart = Integer.parseInt(yearStartString);
        int monthStart = Integer.parseInt(monthStartString);
        int dayStart = Integer.parseInt(dayStartString);
        int yearEnd = Integer.parseInt(yearEndString);
        int monthEnd = Integer.parseInt(monthEndString);
        int dayEnd = Integer.parseInt(dayEndString);

        LocalDateTime start = LocalDateTime.of(yearStart, monthStart, dayStart, 0, 0);
        LocalDateTime end = LocalDateTime.of(yearEnd, monthEnd, dayEnd, 0, 0);
        TimeRange range = new TimeRange(start, end);
        return financeEventDAO.getEvents(user, type, range);
    }

    public List<FinanceEvent> getEvents(String user, String type) {
        return financeEventDAO.getEvents(user, type);
    }
}
