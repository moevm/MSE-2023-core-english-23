package core.english.mse2023.state.subcription;

import core.english.mse2023.cache.CacheData;
import core.english.mse2023.state.State;

public class CreatedState implements State {
    @Override
    public void next(CacheData data) {
        throw new RuntimeException("Cannot set next state since it's the end of SubscriptionCreationState machine.");
    }

    @Override
    public void prev(CacheData data) {
        data.setState(new PartiallyCreatedState());
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public int getStateIndex() {
        return 2;
    }
}
