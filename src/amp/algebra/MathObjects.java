package amp.algebra;

import org.jetbrains.annotations.Contract;

public interface MathObjects {
    
    enum NarrowSettings {
        NONE,
        LAZY,
        SIMPLE,
        PRIMITIVE,
        MODERATE,
        STRONG,
        AGGRESSIVE,
        EXTREME
    }
    
    class Context {
    
    }
    
    /*enum NarrowSettings {
        NONE,
        
        STRONG,
        UNSIGNED,
        SIGNED,
        SAME_SIGN_TYPE
    }*/
    
    /**
     * Checks if a number is 0, a function is the constant 0,
     * or more generally for other implementations, that adding by this object changes nothing.
     * @return true if this has a value of 0, and false otherwise.
     */
    @Contract(pure = true)
    boolean isZero();
    
    /**
     * Checks if a number is 1, a function is the constant 1,
     * or more generally for other implementations, that multiplying by this object changes nothing.
     * @return true if this has a value of 1, and false otherwise.
     */
    @Contract(pure = true)
    boolean isOne();
    
    /**
     * Check if this value is negative
     * @return true if it is, false otherwise.
     */
    @Contract(pure = true)
    default boolean isNegative() {
        return false;
    }
    
}
