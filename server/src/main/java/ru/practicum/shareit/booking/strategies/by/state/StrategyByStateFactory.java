package ru.practicum.shareit.booking.strategies.by.state;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StrategyByStateFactory {

    private Map<BookingState, StrategyByState> strategies;

    @Autowired
    public StrategyByStateFactory(Set<StrategyByState> strategyByStateSet) {
        createStrategy(strategyByStateSet);
    }

    public StrategyByState findStrategy(BookingState state) {
        return strategies.get(state);
    }

    private void createStrategy(Set<StrategyByState> strategyByStateSet) {
        strategies = new HashMap<>();
        strategyByStateSet.forEach(strategy -> strategies.put(strategy.getBookingState(), strategy));
    }

}