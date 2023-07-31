package ru.practicum.shareit.booking.strategies.by.owner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StrategyByOwnerFactory {

    private Map<BookingState, StrategyByOwner> strategies;

    @Autowired
    public StrategyByOwnerFactory(Set<StrategyByOwner> strategyByOwnerSet) {
        createStrategy(strategyByOwnerSet);
    }

    public StrategyByOwner findStrategy(BookingState state) {
        return strategies.get(state);
    }

    private void createStrategy(Set<StrategyByOwner> strategyByOwnerSet) {
        strategies = new HashMap<>();
        strategyByOwnerSet.forEach(strategy -> strategies.put(strategy.getBookingState(), strategy));
    }

}
