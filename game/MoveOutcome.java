package chess;

import java.io.Serializable;

public enum MoveOutcome implements Serializable{
    DONE{
        @Override
        public boolean isDone() { return true; }
    },
    ILLEGAL_MOVE{
        @Override
        public boolean isDone() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK{
        @Override
        public boolean isDone() {
            return false;
        }
    };

    public abstract boolean isDone();
}
