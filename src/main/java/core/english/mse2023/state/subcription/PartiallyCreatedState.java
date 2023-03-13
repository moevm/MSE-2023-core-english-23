package core.english.mse2023.state.subcription;

import core.english.mse2023.cache.CacheData;
import core.english.mse2023.state.State;

public class PartiallyCreatedState implements State {
    @Override
    public void next(CacheData data) {
        data.setState(new CreatedState());
    }

    @Override
    public void prev(CacheData data) {
        data.setState(new InitializedState());
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public int getStateIndex() {
        return 1;
    }
}
