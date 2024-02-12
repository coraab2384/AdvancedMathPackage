package amp.algebra;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.function.IntFunction;

import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Range;

public abstract class MathNumbers
        extends Number
        implements Comparable<MathNumbers>, MathObjects  {
    
    
    static byte decimalPrecisionForApprox = 3;

    /**
     * Static constant 0
     */
    //public static final MathNumbers ZERO = new ByteUnsigned(0);

    /**
     * Static constant 1
     */
    //public static final MathNumbers ONE = new ByteSigned(1);

    /**
     * Static constant -1
     */
    //public static final MathNumbers MINUS_ONE = new ByteSigned(-1);
    
    static class Cache<N extends MathNumbers> {
        
        protected static final byte DEFAULT_INITIAL_CACHE_SIZE = 16;
        
        protected static final byte MAX_INITIAL_CACHE = 126;
        
        protected static final int DEFAULT_CACHE_MAGNITUDE = 128;
        
        protected static final int CACHE_MAX = Integer.MAX_VALUE - 1;
        
        protected static final int CACHE_MIN = Integer.MIN_VALUE + 2;
        
        private final Vector<Vector<N>> cache;
        
        Cache(
                @Range(from = 1, to = 4) int dimn,
                @Range(from = 1, to = Byte.MAX_VALUE - 1) byte initialSize,
                @Nullable IntFunction<N> constructor,
                @Nullable N@NonNull[] impVals,
                N@Nullable[] optionalSecondaryArg
        ) {
            Vector<Vector<N>> babyCache = new Vector<>(dimn, dimn);
            
            switch (dimn) {
                default:
                    Vector<N> secondary =
                            new Vector<>( Arrays.asList(optionalSecondaryArg) );
                    babyCache.set(3, secondary);
                case 3:
                    Vector<N> negatives = new Vector<>(initialSize,initialSize);
                    negatives.set(0, impVals[2]);
                    for (int i = 1; i < initialSize; i++) {
                        negatives.set(i, constructor.apply(-i));
                    }
                    babyCache.set(2, negatives);
                    
                case 2:
                    Vector<N> positives = new Vector<>(initialSize, initialSize);
                    positives.set(0, impVals[1]);
                    for (int i = 1; i < initialSize; i++) {
                        positives.set(i, constructor.apply(i));
                    }
                    babyCache.set(1,positives);
                    
                case 1:
                    Vector<N> zero = new Vector<>(1);
                    zero.set(0, impVals[0]);
                    babyCache.set(0, zero);
            }
            cache = babyCache;
        }
        
        @Contract(pure = true)
        protected static int modSignum(int val) {
            return (val < 0) ?
                    2 :
                    Integer.signum(val);
        }
        
        @NonNull
        protected N queryCalcSet(
                @Range(from = Integer.MIN_VALUE + 2, to = Integer.MAX_VALUE - 1) int val,
                @NonNull IntFunction<N> constructor
        ) {
            int magn = Math.abs(val);
            int dimn = modSignum(val);
            Vector<N>thisVector = cache.get(dimn);
            N prospect = thisVector.get(magn);
            if (prospect == null) {
                prospect = constructor.apply(val);
                thisVector.set(magn, prospect);
            }
            return prospect;
        }
        
        @Nullable
        protected N query(
                @Range(from = 0, to = 2) byte dimn,
                int magn
        ) {
            return cache.get(dimn).get(magn);
        }
        
        @NonNull
        protected N getMinMaxZero(
                byte dimn
        ) {
            return cache.get(dimn).getFirst();
        }
        
        protected void set(
                @Range(from = 0, to = 2) byte dimn,
                int magn,
                @NonNull N val
        ) {
            cache.get(dimn).set(magn, val);
        }
        
        /**
         * This increases the cache magnitude, which
         * @param newMagnitude The new magnitude of the Cache.
         *                     Must be explicitly greater than the current magnitude
         * @return true if the cache was increased, and false if the existing cache
         *         already exceeds the proposed expansion.
         */
        
        int cacheResize(
                @Range(from = 1, to = Integer.MAX_VALUE - 1) int newMagn,
                int oldMagn
        ) {
            if ( (newMagn <= oldMagn) || (newMagn == Integer.MAX_VALUE) ) {
                int dimn = cache.size();
                for (int i = 1; i < dimn; i++) {
                    cache.get(i).ensureCapacity(newMagn);
                }
                return newMagn;
            }
            //else
            return -1;
            
        }
        
    }

    /**
     * Check the sign of this value
     * @return the signum if this, which is
     *             -1 if negative, 0 if 0, and 1 if greater than.
     */
    @Contract(pure = true)
    public abstract byte signum();

    /**
     * Check the 'sigmagnum' of this value, an extension of signum.
     * @return the sigmagnum if this, which is -3 if negative and less than -1,
     *             -2 if exactly -1, -1 if negative but greater than -1,
     *             0 if 0, 1 if positive but less than 1,
     *             2 if exactly 1, and 3 if positive and greater than 1
     */
    @Contract(pure = true)
    public abstract byte sigmagnum(); 0

    /**
     * Copies this {@link MathNumbers} but with an always positive sign
     * into another {@link MathNumbers}.
     * @return the {@link MathNumbers} with the magnitude
     */
    @Contract(pure = true) @NotNull
    public abstract MathNumbers abs();

    /**
     * Compare this and some other {@link MathNumbers}
     * and put the result in an {@code int}.
     * This function is for use with {@code boolean} logic,
     * for subtraction, there is the subtract function.
     * @param that the {@link MathNumbers} object to compare to.
     * @return -1 if less than, 0 if equal, and 1 if greater than.
     */
    //public abstract int compareTo(MathNumbers that);

    /**
     * @param that the value against which to test for equality
     * @return true if the two values are equal, false otherwise
     */
    //public abstract boolean equals(MathNumbers that);

    /**
     * Add this and another {@link MathNumbers} into another {@link MathNumbers}.
     * @param that the {@link MathNumbers} to add.
     * @return a {@link MathNumbers} with the sum.
     */
    //@NotNullpublic abstract MathNumbers add(MathNumbers that);

    /**
     * Subtract this by another {@link MathNumbers} into another {@link MathNumbers}.
     * @param that the {@link MathNumbers} to subtract by.
     * @return a {@link MathNumbers} with the difference.
     */
    //@NotNullpublic abstract MathNumbers subtract(MathNumbers that);

    /**
     * Multiply this and another {@link MathNumbers} into another {@link MathNumbers}.
     * @param that the {@link MathNumbers} to multiply by.
     * @return a {@link MathNumbers} with the product.
     */
    //@NotNullpublic abstract MathNumbers multiply(MathNumbers that);

    /**
     * Divide this by another {@link MathNumbers},
     * putting the quotient into another {@link MathNumbers}.
     * Default remainder or float handling is based on the implementing class.
     * @param that the {@link MathNumbers} to divide by.
     * @return a {@link MathNumbers} with the quotient.
     */
    //@NotNullpublic abstract MathNumbers divide(MathNumbers that);

    /**
     * Square this {@link MathNumbers},
     * putting the result into another {@link MathNumbers}.
     * @return a {@link MathNumbers} with the quotient.
     */
    @Contract(pure = true) @NotNull
    public abstract MathNumbers squared();

    /**
     * Raise this to the power of another {@link MathNumbers},
     * putting the result into another {@link MathNumbers}.
     * @param exponent the {@link MathNumbers} to raise to the power of.
     * @return a {@link MathNumbers} with the result.
     */
    //public abstract MathNumbers power(MathNumbers exponent);

    /**
     * Put this value into an {@code byte}.
     * Whether it is signed or unsigned will depend on the type
     * If narrowing occurs, this version will not complain.
     * @return this value as a {@code byte}.
     */
    @Contract(pure = true)
    public abstract byte byteValue();

    /**
     * Put this value into an {@code byte}.
     * Whether it is signed or unsigned will depend on the type
     * If the value does not fit exactly, this will complain.
     * @return this value as a {@code byte}.
     * @throws ArithmeticException if the value cannot be expressed
     *             in a {@code byte}.
     */
    @Contract(pure = true)
    public abstract byte byteValueExact()
            throws ArithmeticException;

    /**
     * Put this value into an {@code short}.
     * Whether it is signed or unsigned will depend on the type
     * If narrowing occurs, this version will not complain.
     * @return this value as a {@code short}.
     */
    @Contract(pure = true)
    public abstract short shortValue();

    /**
     * Put this value into an {@code short}.
     * Whether it is signed or unsigned will depend on the type
     * If the value does not fit exactly, this will complain.
     * @return this value as a {@code short}.
     * @throws ArithmeticException if the value cannot be expressed
     *             in a {@code short}.
     */
    @Contract(pure = true)
    public abstract short shortValueExact()
            throws ArithmeticException;

    /**
     * Put this value into an {@code int}.
     * Whether it is signed or unsigned will depend on the type
     * While narrowing while likely occur, this version will not complain.
     * @return this value as an {@code int}.
     */
    @Contract(pure = true)
    public abstract int intValue();

    /**
     * Put this value into an {@code int}.
     * Whether it is signed or unsigned will depend on the type
     * If the value does not fit exactly, this will complain.
     * @return this value as an {@code int}.
     * @throws ArithmeticException if the value cannot be expressed
     *             in an {@code int}.
     */
    @Contract(pure = true)
    public abstract int intValueExact()
            throws ArithmeticException;

    /**
     * Put this value into an {@code long}.
     * Whether it is signed or unsigned will depend on the type
     * If narrowing occurs, this version will not complain.
     * @return this value as a {@code long}.
     */
    @Contract(pure = true)
    public abstract long longValue();

    /**
     * Put this value into an {@code int}.
     * Whether it is signed or unsigned will depend on the type
     * If the value does not fit exactly, this will complain.
     * @return this value as a {@code long}.
     * @throws ArithmeticException if the value cannot be expressed
     *             in a {@code long}.
     */
    @Contract(pure = true)
    public abstract long longValueExact()
            throws ArithmeticException;

    /**
     * Turns the contained value into a {@code float}.
     * Precision may be lost!
     * @return a {@code float} containing this value
     */
    @Contract(pure = true)
    public abstract float floatValue();

    /**
     * Turns the contained value into a {@code double}.
     * Precision may be lost!
     * @return a {@code double} containing this value
     */
    @Contract(pure = true)
    public abstract double doubleValue();

    /**
     * Put this value into a {@link BigInteger}.
     * @return this value as a {@link BigInteger}.
     */
    @Contract(pure = true) @NotNull
    public abstract BigInteger toBigInteger();

    /**
     * Put this value into an {@link String}.
     * @param radix the {@code int} for the base of the representaion
     * @return the {@link String} representation of the value.
     */
    @Contract(pure = true) @NotNull
    public abstract String toString(
            int radix
    );
}
