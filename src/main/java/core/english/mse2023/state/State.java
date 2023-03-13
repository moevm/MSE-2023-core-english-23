package core.english.mse2023.state;

import core.english.mse2023.cache.CacheData;

/**
 * Implementation of pattern State. Defines main methods for each state.
 */
public interface State {

    /**
     * Go forward in the state machine
     * @param data Instance of CacheData with access to state object
     */
    void next(CacheData data);

    /**
     * Go backwards in the state machine
     * @param data Instance of CacheData with access to state object
     */
    void prev(CacheData data);

    /**
     * Returns if this state is final
     * @return Does this state is final
     */
    boolean hasNext();

    /**
     * Returns index of this state. <p>
     * Next index must be bigger on 1 than this one. <p>
     * Prev. index must be smaller on 1 than this one. <p> <p> <p>
     * Used for identification in small spaces
     * @return Index of current state
     */
    int getStateIndex();
}
