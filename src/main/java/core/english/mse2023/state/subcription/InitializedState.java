package core.english.mse2023.state.subcription;

import core.english.mse2023.cache.CacheData;
import core.english.mse2023.state.State;

public class InitializedState implements State {
    @Override
    public void next(CacheData data) {
        data.setState(new PartiallyCreatedState());
    }

    @Override
    public void prev(CacheData data) {
        throw new RuntimeException("Cannot set prev state since it's the start of SubscriptionCreationState machine.");
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public int getStateIndex() {
        return 0;
    }
}
